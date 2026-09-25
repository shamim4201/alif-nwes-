package com.example.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {
    @Query("SELECT * FROM news_articles ORDER BY publishedAt DESC")
    fun getAllArticles(): Flow<List<NewsArticle>>

    @Query("SELECT * FROM news_articles WHERE category = :category ORDER BY publishedAt DESC")
    fun getArticlesByCategory(category: String): Flow<List<NewsArticle>>

    @Query("SELECT * FROM news_articles WHERE isBreaking = 1 ORDER BY publishedAt DESC")
    fun getBreakingArticles(): Flow<List<NewsArticle>>

    @Query("SELECT * FROM news_articles WHERE isBookmarked = 1 ORDER BY publishedAt DESC")
    fun getBookmarkedArticles(): Flow<List<NewsArticle>>

    @Query("SELECT * FROM news_articles WHERE id = :id")
    fun getArticleById(id: Long): Flow<NewsArticle?>

    @Query("SELECT * FROM news_articles WHERE id = :id LIMIT 1")
    suspend fun getArticleByIdDirect(id: Long): NewsArticle?

    @Query("SELECT * FROM news_articles WHERE title LIKE '%' || :query || '%' OR content LIKE '%' || :query || '%' OR category LIKE '%' || :query || '%' ORDER BY publishedAt DESC")
    fun searchArticles(query: String): Flow<List<NewsArticle>>

    @Query("SELECT COUNT(*) FROM news_articles")
    suspend fun getArticleCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(article: NewsArticle): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(articles: List<NewsArticle>)

    @Update
    suspend fun updateArticle(article: NewsArticle)

    @Delete
    suspend fun deleteArticle(article: NewsArticle)

    @Query("UPDATE news_articles SET isBookmarked = :bookmarked WHERE id = :id")
    suspend fun updateBookmarkStatus(id: Long, bookmarked: Boolean)

    @Query("UPDATE news_articles SET viewCount = viewCount + 1 WHERE id = :id")
    suspend fun incrementViewCount(id: Long)
}
