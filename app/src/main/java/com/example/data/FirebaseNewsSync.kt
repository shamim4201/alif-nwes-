package com.example.data

import android.content.Context
import android.util.Log
import com.example.R
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

class FirebaseNewsSync(private val context: Context) {
    private val tag = "FirebaseNewsSync"

    private val firestore: FirebaseFirestore? by lazy {
        try {
            val dbId = context.getString(R.string.firestore_database_id)
            if (dbId.isNotBlank() && dbId != "(default)") {
                FirebaseFirestore.getInstance(FirebaseApp.getInstance(), dbId)
            } else {
                FirebaseFirestore.getInstance()
            }
        } catch (e: Exception) {
            Log.e(tag, "Failed to initialize Firestore instance", e)
            null
        }
    }

    suspend fun syncArticleToCloud(article: NewsArticle) {
        val db = firestore ?: return
        try {
            val docId = if (article.slug.isNotBlank()) article.slug else "article_${article.id}"
            val map = hashMapOf(
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
                "readTimeMinutes" to article.readTimeMinutes
            )
            db.collection("articles").document(docId).set(map, SetOptions.merge()).await()
            Log.d(tag, "Article synced to Firestore successfully: $docId")
        } catch (e: Exception) {
            Log.w(tag, "Error syncing article to Firestore", e)
        }
    }

    suspend fun syncAllArticles(articles: List<NewsArticle>) {
        articles.forEach { syncArticleToCloud(it) }
    }
}
