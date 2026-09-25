package com.example.data

import android.content.Context
import android.util.Log
import com.example.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirebaseRealtimeDbManager(private val context: Context) {
    private val tag = "FirebaseRealtimeDb"

    val database: FirebaseDatabase by lazy {
        try {
            val rtdbUrl = context.getString(R.string.firebase_database_url)
            if (rtdbUrl.isNotBlank()) {
                FirebaseDatabase.getInstance(rtdbUrl)
            } else {
                FirebaseDatabase.getInstance()
            }
        } catch (e: Exception) {
            Log.e(tag, "Error obtaining FirebaseDatabase instance", e)
            FirebaseDatabase.getInstance()
        }
    }

    private val articlesRef by lazy { database.getReference("articles") }
    private val breakingRef by lazy { database.getReference("breaking_news") }
    private val viewsRef by lazy { database.getReference("article_views") }

    /**
     * Publishes or updates an article in Firebase Realtime Database
     */
    suspend fun publishArticle(article: NewsArticle) {
        try {
            val idStr = if (article.slug.isNotBlank()) article.slug else "article_${article.id}"
            val map = mapOf(
                "id" to article.id,
                "title" to article.title,
                "subtitle" to article.subtitle,
                "content" to article.content,
                "category" to article.category,
                "author" to article.author,
                "publishedAt" to article.publishedAt,
                "imageUrl" to article.imageUrl,
                "isBreaking" to article.isBreaking,
                "viewCount" to article.viewCount,
                "slug" to article.slug,
                "metaDescription" to article.metaDescription,
                "source" to article.source,
                "readTimeMinutes" to article.readTimeMinutes,
                "updatedAt" to ServerValue.TIMESTAMP
            )
            articlesRef.child(idStr).setValue(map).await()
            if (article.isBreaking) {
                breakingRef.setValue(article.title).await()
            }
            Log.d(tag, "Article synced to Realtime Database: $idStr")
        } catch (e: Exception) {
            Log.e(tag, "Failed to publish to Realtime Database", e)
        }
    }

    /**
     * Sends a test ping to Firebase Realtime Database
     */
    suspend fun sendTestPing(): Result<String> {
        return try {
            val pingRef = database.getReference("connection_test")
            val pingData = mapOf(
                "status" to "connected",
                "app" to "Alif Shen News Network",
                "timestamp" to ServerValue.TIMESTAMP,
                "message" to "Realtime Database is connected & working perfectly!"
            )
            pingRef.setValue(pingData).await()
            Result.success("Success! Ping saved to /connection_test")
        } catch (e: Exception) {
            Log.e(tag, "Test ping failed", e)
            Result.failure(e)
        }
    }

    /**
     * Syncs all existing news articles and metadata to Realtime Database
     */
    suspend fun syncAllArticles(articles: List<NewsArticle>): Result<Int> {
        return try {
            val rootRef = database.reference
            val updates = mutableMapOf<String, Any>()

            // Connection ping & metadata
            updates["connection_test"] = mapOf(
                "status" to "online",
                "app" to "Alif Sheen News Network",
                "last_sync" to ServerValue.TIMESTAMP,
                "total_synced_articles" to articles.size
            )

            // Sync all articles under /articles
            for (article in articles) {
                val key = "articles/${article.id}"
                updates[key] = mapOf(
                    "id" to article.id,
                    "title" to article.title,
                    "subtitle" to article.subtitle,
                    "category" to article.category,
                    "author" to article.author,
                    "content" to article.content,
                    "imageUrl" to article.imageUrl,
                    "isBreaking" to article.isBreaking,
                    "publishedAt" to article.publishedAt,
                    "viewCount" to article.viewCount,
                    "readTimeMinutes" to article.readTimeMinutes,
                    "slug" to article.slug,
                    "source" to article.source
                )
            }

            // Sync breaking news
            val breaking = articles.firstOrNull { it.isBreaking }?.title ?: articles.firstOrNull()?.title
            if (breaking != null) {
                updates["breaking_news"] = breaking
            }

            rootRef.updateChildren(updates).await()
            Log.d(tag, "Successfully synced ${articles.size} articles to Realtime DB")
            Result.success(articles.size)
        } catch (e: Exception) {
            Log.e(tag, "Failed to sync all articles", e)
            Result.failure(e)
        }
    }

    /**
     * Increments view count in Realtime Database
     */
    fun recordArticleView(articleId: Long) {
        try {
            viewsRef.child(articleId.toString()).runTransaction(object : com.google.firebase.database.Transaction.Handler {
                override fun doTransaction(currentData: com.google.firebase.database.MutableData): com.google.firebase.database.Transaction.Result {
                    val count = currentData.getValue(Long::class.java) ?: 0L
                    currentData.value = count + 1
                    return com.google.firebase.database.Transaction.success(currentData)
                }
                override fun onComplete(error: DatabaseError?, committed: Boolean, currentData: DataSnapshot?) {}
            })
        } catch (e: Exception) {
            Log.w(tag, "Error incrementing view in Realtime DB", e)
        }
    }

    /**
     * Flow for real-time live breaking banner updates
     */
    fun observeBreakingNews(): Flow<String?> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.getValue(String::class.java)
                trySend(value)
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w(tag, "Realtime DB observe cancelled: ${error.message}")
            }
        }
        breakingRef.addValueEventListener(listener)
        awaitClose { breakingRef.removeEventListener(listener) }
    }
}
