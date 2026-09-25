package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "news_articles")
data class NewsArticle(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val subtitle: String = "",
    val content: String,
    val category: String, // Top Stories, Politics, Business, Tech, World, Sports, Entertainment
    val author: String = "US News Desk",
    val publishedAt: Long = System.currentTimeMillis(),
    val imageUrl: String = "",
    val isBreaking: Boolean = false,
    val isBookmarked: Boolean = false,
    val viewCount: Int = 0,
    val slug: String = "",
    val metaDescription: String = "",
    val source: String = "US News Network",
    val readTimeMinutes: Int = 3
)
