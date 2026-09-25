package com.example.data

import kotlinx.coroutines.flow.Flow

class NewsRepository(private val newsDao: NewsDao) {
    val allArticles: Flow<List<NewsArticle>> = newsDao.getAllArticles()
    val breakingArticles: Flow<List<NewsArticle>> = newsDao.getBreakingArticles()
    val bookmarkedArticles: Flow<List<NewsArticle>> = newsDao.getBookmarkedArticles()

    fun getArticlesByCategory(category: String): Flow<List<NewsArticle>> {
        return if (category == "Top Stories" || category == "All") {
            newsDao.getAllArticles()
        } else {
            newsDao.getArticlesByCategory(category)
        }
    }

    fun searchArticles(query: String): Flow<List<NewsArticle>> = newsDao.searchArticles(query)

    fun getArticleById(id: Long): Flow<NewsArticle?> = newsDao.getArticleById(id)

    suspend fun insertArticle(article: NewsArticle): Long = newsDao.insertArticle(article)

    suspend fun updateArticle(article: NewsArticle) = newsDao.updateArticle(article)

    suspend fun deleteArticle(article: NewsArticle) = newsDao.deleteArticle(article)

    suspend fun toggleBookmark(id: Long, isBookmarked: Boolean) = newsDao.updateBookmarkStatus(id, isBookmarked)

    suspend fun incrementViewCount(id: Long) = newsDao.incrementViewCount(id)

    suspend fun ensureInitialData() {
        val article1 = newsDao.getArticleByIdDirect(1)
        if (article1 == null) {
            newsDao.insertAll(SampleData.initialArticles)
        } else if (article1.publishedAt <= 1477700000000L) {
            // Upgrade old 2016 timestamps to fresh live timestamps
            SampleData.initialArticles.forEach { sampleArt ->
                val existing = newsDao.getArticleByIdDirect(sampleArt.id)
                if (existing != null && existing.publishedAt <= 1477700000000L) {
                    newsDao.updateArticle(existing.copy(publishedAt = sampleArt.publishedAt))
                }
            }
        }
    }
}
