package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.FirebaseNewsSync
import com.example.data.FirebaseRealtimeDbManager
import com.example.data.NewsArticle
import com.example.data.NewsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppMode {
    USER,
    AUTH,
    ADMIN
}

enum class AuthMode {
    LOGIN,
    REGISTER
}

data class UserAccount(
    val fullName: String,
    val email: String,
    val role: String = "Reader",
    val phone: String = ""
)

enum class UserTab {
    HEADLINES,
    EXPLORE,
    SAVED,
    MORE
}

enum class AdminTab {
    DASHBOARD,
    PUBLISH,
    MANAGE_POSTS,
    DOMAIN_SEO,
    MONETIZATION
}

data class PublisherFormState(
    val editingArticleId: Long? = null,
    val title: String = "",
    val subtitle: String = "",
    val content: String = "",
    val category: String = "Top Stories",
    val author: String = "US News Desk",
    val imageUrl: String = "https://images.unsplash.com/photo-1585829365295-ab7cd400c167?w=800&q=80",
    val isBreaking: Boolean = false,
    val source: String = "US News Network",
    val slug: String = "",
    val tags: String = "US News, Politics, Breaking",
    val postStatus: String = "Published", // "Published", "Draft", "Scheduled"
    val focusKeyword: String = "",
    val excerpt: String = "",
    val isSubmitting: Boolean = false,
    val submitSuccessMessage: String? = null,
    val errorMessage: String? = null
) {
    val wordCount: Int
        get() = if (content.isBlank()) 0 else content.trim().split("\\s+".toRegex()).size

    val estimatedReadMinutes: Int
        get() = maxOf(1, wordCount / 180)

    val autoSlug: String
        get() = title.lowercase()
            .replace("[^a-z0-9\\s-]".toRegex(), "")
            .trim()
            .replace("\\s+".toRegex(), "-")
            .take(60)

    val effectiveSlug: String
        get() = if (slug.isNotBlank()) slug.trim() else autoSlug

    val autoMetaDescription: String
        get() = if (excerpt.isNotBlank()) excerpt.take(160) else if (subtitle.isNotBlank()) subtitle.take(160) else content.take(150)

    val seoScore: Int
        get() {
            var score = 0
            if (title.trim().length in 25..80) score += 20
            if (wordCount >= 150) score += 25
            if (imageUrl.isNotBlank()) score += 15
            if (subtitle.isNotBlank() || autoMetaDescription.isNotBlank()) score += 15
            if (category.isNotBlank()) score += 10
            if (focusKeyword.isNotBlank()) score += 15
            return score.coerceAtMost(100)
        }
}

class NewsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: NewsRepository
    private val firebaseNewsSync = FirebaseNewsSync(application)
    private val realtimeDbManager = FirebaseRealtimeDbManager(application)

    val categories = listOf(
        "Top Stories",
        "Life Style",
        "Travel",
        "Technology",
        "Sports",
        "Football",
        "Creative",
        "Foods"
    )

    private val _appMode = MutableStateFlow(AppMode.USER)
    val appMode: StateFlow<AppMode> = _appMode.asStateFlow()

    private val _authMode = MutableStateFlow(AuthMode.REGISTER)
    val authMode: StateFlow<AuthMode> = _authMode.asStateFlow()

    private val _currentUser = MutableStateFlow<UserAccount?>(null)
    val currentUser: StateFlow<UserAccount?> = _currentUser.asStateFlow()

    private val _userTab = MutableStateFlow(UserTab.HEADLINES)
    val userTab: StateFlow<UserTab> = _userTab.asStateFlow()

    private val _adminTab = MutableStateFlow(AdminTab.DASHBOARD)
    val adminTab: StateFlow<AdminTab> = _adminTab.asStateFlow()

    private val _selectedCategory = MutableStateFlow("Top Stories")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedArticle = MutableStateFlow<NewsArticle?>(null)
    val selectedArticle: StateFlow<NewsArticle?> = _selectedArticle.asStateFlow()

    private val _publisherForm = MutableStateFlow(PublisherFormState())
    val publisherForm: StateFlow<PublisherFormState> = _publisherForm.asStateFlow()

    private val _customDomain = MutableStateFlow("official1.online")
    val customDomain: StateFlow<String> = _customDomain.asStateFlow()

    private val _adMobEnabled = MutableStateFlow(true)
    val adMobEnabled: StateFlow<Boolean> = _adMobEnabled.asStateFlow()

    private val _activePolicyDialog = MutableStateFlow<String?>(null)
    val activePolicyDialog: StateFlow<String?> = _activePolicyDialog.asStateFlow()

    private val _firebasePingStatus = MutableStateFlow<String?>(null)
    val firebasePingStatus: StateFlow<String?> = _firebasePingStatus.asStateFlow()

    val allArticles: StateFlow<List<NewsArticle>>
    val breakingNews: StateFlow<List<NewsArticle>>
    val bookmarkedNews: StateFlow<List<NewsArticle>>
    val displayArticles: StateFlow<List<NewsArticle>>
    val totalArticleCount: StateFlow<Int>
    val totalViewsCount: StateFlow<Int>

    init {
        val database = AppDatabase.getDatabase(application, viewModelScope)
        repository = NewsRepository(database.newsDao())

        viewModelScope.launch {
            repository.ensureInitialData()
            try {
                val list = repository.allArticles.first { it.isNotEmpty() }
                realtimeDbManager.syncAllArticles(list)
                firebaseNewsSync.syncAllArticles(list)
            } catch (e: Exception) {
                // Ignore initial sync error if offline
            }
        }

        allArticles = repository.allArticles.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        breakingNews = repository.breakingArticles.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        bookmarkedNews = repository.bookmarkedArticles.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

        totalArticleCount = allArticles.map { it.size }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            0
        )

        totalViewsCount = allArticles.map { list ->
            list.sumOf { it.viewCount }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

        displayArticles = combine(
            allArticles,
            _selectedCategory,
            _searchQuery
        ) { all, category, query ->
            var filtered = all
            if (query.isNotBlank()) {
                filtered = filtered.filter {
                    it.title.contains(query, ignoreCase = true) ||
                    it.content.contains(query, ignoreCase = true) ||
                    it.category.contains(query, ignoreCase = true)
                }
            } else if (category != "Top Stories") {
                filtered = filtered.filter { it.category.equals(category, ignoreCase = true) }
            }
            filtered
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    }

    fun switchToAdmin() {
        _appMode.value = AppMode.ADMIN
    }

    fun switchToAuth(mode: AuthMode = AuthMode.REGISTER) {
        _authMode.value = mode
        _appMode.value = AppMode.AUTH
    }

    fun setAuthMode(mode: AuthMode) {
        _authMode.value = mode
    }

    fun loginUser(email: String, role: String = "Reader", name: String = "") {
        val isAdmin = email.trim().equals("alifsheenshopping@gmail.com", ignoreCase = true) || role.equals("Admin", ignoreCase = true)
        val finalRole = if (isAdmin) "Admin" else "Reader"
        val userName = if (name.isNotBlank()) name else if (isAdmin) "Alif Shen Admin" else email.substringBefore("@").replaceFirstChar { it.uppercase() }
        _currentUser.value = UserAccount(fullName = userName, email = email, role = finalRole)
        if (isAdmin) {
            _appMode.value = AppMode.ADMIN
        } else {
            _appMode.value = AppMode.USER
        }
    }

    fun registerUser(fullName: String, email: String, phone: String, role: String) {
        _currentUser.value = UserAccount(fullName = fullName, email = email, role = role, phone = phone)
        if (role.equals("Admin", ignoreCase = true)) {
            _appMode.value = AppMode.ADMIN
        } else {
            _appMode.value = AppMode.USER
        }
    }

    fun logoutUser() {
        _currentUser.value = null
        _appMode.value = AppMode.USER
    }

    fun switchToUser() {
        _appMode.value = AppMode.USER
    }

    fun setUserTab(tab: UserTab) {
        _userTab.value = tab
    }

    fun setAdminTab(tab: AdminTab) {
        _adminTab.value = tab
    }

    fun selectCategory(category: String) {
        _selectedCategory.value = category
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun openArticle(article: NewsArticle) {
        _selectedArticle.value = article
        viewModelScope.launch {
            repository.incrementViewCount(article.id)
            realtimeDbManager.recordArticleView(article.id)
        }
    }

    fun closeArticle() {
        _selectedArticle.value = null
    }

    fun toggleBookmark(article: NewsArticle) {
        viewModelScope.launch {
            val newStatus = !article.isBookmarked
            repository.toggleBookmark(article.id, newStatus)
            if (_selectedArticle.value?.id == article.id) {
                _selectedArticle.value = _selectedArticle.value?.copy(isBookmarked = newStatus)
            }
        }
    }

    fun deleteArticle(article: NewsArticle) {
        viewModelScope.launch {
            repository.deleteArticle(article)
            if (_selectedArticle.value?.id == article.id) {
                _selectedArticle.value = null
            }
        }
    }

    fun toggleArticleBreaking(article: NewsArticle) {
        viewModelScope.launch {
            repository.updateArticle(article.copy(isBreaking = !article.isBreaking))
        }
    }

    fun startEditArticle(article: NewsArticle) {
        _publisherForm.value = PublisherFormState(
            editingArticleId = article.id,
            title = article.title,
            subtitle = article.subtitle,
            content = article.content,
            category = article.category,
            author = article.author,
            imageUrl = article.imageUrl,
            isBreaking = article.isBreaking,
            source = article.source,
            slug = article.slug,
            excerpt = article.metaDescription,
            postStatus = "Published"
        )
        _adminTab.value = AdminTab.PUBLISH
    }

    fun setPublisherForm(form: PublisherFormState) {
        _publisherForm.value = form.copy(errorMessage = null)
    }

    fun saveDraft() {
        val current = _publisherForm.value
        if (current.title.isBlank()) {
            _publisherForm.value = current.copy(errorMessage = "Please enter an article title to save draft.")
            return
        }
        _publisherForm.value = current.copy(
            postStatus = "Draft",
            submitSuccessMessage = "Draft saved in WordPress Studio! (Status: Draft)"
        )
    }

    fun cancelEdit() {
        _publisherForm.value = PublisherFormState()
    }

    fun updateCustomDomain(domain: String) {
        _customDomain.value = domain.trim()
    }

    fun toggleAdMob(enabled: Boolean) {
        _adMobEnabled.value = enabled
    }

    fun updatePublisherForm(
        title: String? = null,
        subtitle: String? = null,
        content: String? = null,
        category: String? = null,
        author: String? = null,
        imageUrl: String? = null,
        isBreaking: Boolean? = null,
        source: String? = null,
        slug: String? = null,
        tags: String? = null,
        postStatus: String? = null,
        focusKeyword: String? = null,
        excerpt: String? = null
    ) {
        _publisherForm.value = _publisherForm.value.copy(
            title = title ?: _publisherForm.value.title,
            subtitle = subtitle ?: _publisherForm.value.subtitle,
            content = content ?: _publisherForm.value.content,
            category = category ?: _publisherForm.value.category,
            author = author ?: _publisherForm.value.author,
            imageUrl = imageUrl ?: _publisherForm.value.imageUrl,
            isBreaking = isBreaking ?: _publisherForm.value.isBreaking,
            source = source ?: _publisherForm.value.source,
            slug = slug ?: _publisherForm.value.slug,
            tags = tags ?: _publisherForm.value.tags,
            postStatus = postStatus ?: _publisherForm.value.postStatus,
            focusKeyword = focusKeyword ?: _publisherForm.value.focusKeyword,
            excerpt = excerpt ?: _publisherForm.value.excerpt,
            errorMessage = null
        )
    }

    fun publishArticle() {
        val current = _publisherForm.value
        if (current.title.isBlank()) {
            _publisherForm.value = current.copy(errorMessage = "Please enter an article headline.")
            return
        }
        if (current.content.isBlank()) {
            _publisherForm.value = current.copy(errorMessage = "Please write the news article body.")
            return
        }

        viewModelScope.launch {
            val finalSlug = current.effectiveSlug
            val finalMeta = current.autoMetaDescription

            if (current.editingArticleId != null) {
                // Update existing
                val updated = NewsArticle(
                    id = current.editingArticleId,
                    title = current.title.trim(),
                    subtitle = current.subtitle.trim(),
                    content = current.content.trim(),
                    category = current.category,
                    author = current.author.ifBlank { "US News Desk" },
                    publishedAt = System.currentTimeMillis(),
                    imageUrl = current.imageUrl.ifBlank { "https://images.unsplash.com/photo-1585829365295-ab7cd400c167?w=800&q=80" },
                    isBreaking = current.isBreaking,
                    slug = finalSlug,
                    metaDescription = finalMeta,
                    source = current.source.ifBlank { "US News Network" },
                    readTimeMinutes = current.estimatedReadMinutes
                )
                repository.updateArticle(updated)
                firebaseNewsSync.syncArticleToCloud(updated)
                realtimeDbManager.publishArticle(updated)
                _publisherForm.value = PublisherFormState(
                    submitSuccessMessage = "Article updated successfully and synced with Realtime Database!"
                )
            } else {
                // Insert new
                val newArticle = NewsArticle(
                    title = current.title.trim(),
                    subtitle = current.subtitle.trim(),
                    content = current.content.trim(),
                    category = current.category,
                    author = current.author.ifBlank { "US News Desk" },
                    publishedAt = System.currentTimeMillis(),
                    imageUrl = current.imageUrl.ifBlank { "https://images.unsplash.com/photo-1585829365295-ab7cd400c167?w=800&q=80" },
                    isBreaking = current.isBreaking,
                    isBookmarked = false,
                    slug = finalSlug,
                    metaDescription = finalMeta,
                    source = current.source.ifBlank { "US News Network" },
                    readTimeMinutes = current.estimatedReadMinutes
                )
                val id = repository.insertArticle(newArticle)
                val articleWithId = newArticle.copy(id = id)
                firebaseNewsSync.syncArticleToCloud(articleWithId)
                realtimeDbManager.publishArticle(articleWithId)
                _publisherForm.value = PublisherFormState(
                    submitSuccessMessage = "Article successfully published and live on Realtime Database!"
                )
            }
        }
    }

    fun clearSuccessMessage() {
        _publisherForm.value = _publisherForm.value.copy(submitSuccessMessage = null)
    }

    fun showPolicyDialog(policyType: String) {
        _activePolicyDialog.value = policyType
    }

    fun dismissPolicyDialog() {
        _activePolicyDialog.value = null
    }

    fun testFirebaseConnection() {
        viewModelScope.launch {
            _firebasePingStatus.value = "Sending test ping..."
            val result = realtimeDbManager.sendTestPing()
            _firebasePingStatus.value = if (result.isSuccess) {
                result.getOrNull()
            } else {
                "Error: ${result.exceptionOrNull()?.message ?: "Check Realtime DB Rules"}"
            }
        }
    }

    fun syncAllToRealtimeDb() {
        viewModelScope.launch {
            _firebasePingStatus.value = "Syncing all articles to Firebase Realtime Database..."
            val list = allArticles.value
            val result = realtimeDbManager.syncAllArticles(list)
            firebaseNewsSync.syncAllArticles(list)
            _firebasePingStatus.value = if (result.isSuccess) {
                "Success! ${result.getOrNull()} articles live on Firebase."
            } else {
                "Error: ${result.exceptionOrNull()?.message ?: "Check Realtime DB Rules"}"
            }
        }
    }
}
