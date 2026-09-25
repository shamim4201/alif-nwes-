package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.AlifShenLogo
import com.example.ui.components.AlifShenNavy
import com.example.ui.components.JannahDrawerContent
import com.example.ui.components.PolicyDialog
import kotlinx.coroutines.launch
import com.example.ui.AuthMode
import com.example.ui.screens.AdminScreen
import com.example.ui.screens.ArticleDetailScreen
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.SavedScreen
import com.example.ui.screens.SearchScreen
import com.example.ui.screens.UserMoreScreen
import com.example.ui.theme.JannahHeaderAccent
import com.example.ui.theme.JannahHeaderDark
import com.example.ui.theme.NewsGoldAccent
import com.example.ui.theme.NewsNavyPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScreen(
    viewModel: NewsViewModel = viewModel()
) {
    val appMode by viewModel.appMode.collectAsStateWithLifecycle()
    val userTab by viewModel.userTab.collectAsStateWithLifecycle()
    val adminTab by viewModel.adminTab.collectAsStateWithLifecycle()

    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val displayArticles by viewModel.displayArticles.collectAsStateWithLifecycle()
    val allArticles by viewModel.allArticles.collectAsStateWithLifecycle()
    val breakingNews by viewModel.breakingNews.collectAsStateWithLifecycle()
    val bookmarkedNews by viewModel.bookmarkedNews.collectAsStateWithLifecycle()
    val totalCount by viewModel.totalArticleCount.collectAsStateWithLifecycle()
    val totalViews by viewModel.totalViewsCount.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedArticle by viewModel.selectedArticle.collectAsStateWithLifecycle()
    val publisherForm by viewModel.publisherForm.collectAsStateWithLifecycle()
    val customDomain by viewModel.customDomain.collectAsStateWithLifecycle()
    val activePolicyDialog by viewModel.activePolicyDialog.collectAsStateWithLifecycle()
    val firebasePingStatus by viewModel.firebasePingStatus.collectAsStateWithLifecycle()

    // Full Article Detail Overlay
    selectedArticle?.let { article ->
        BackHandler { viewModel.closeArticle() }
        ArticleDetailScreen(
            article = article,
            allArticles = allArticles,
            categories = viewModel.categories,
            onBack = { viewModel.closeArticle() },
            onSelectArticle = { nextArticle -> viewModel.openArticle(nextArticle) },
            onBookmarkToggle = { viewModel.toggleBookmark(article) },
            onDelete = { viewModel.deleteArticle(article) },
            onOpenPolicy = { policy -> viewModel.showPolicyDialog(policy) },
            onSelectCategory = { cat ->
                viewModel.selectCategory(cat)
                viewModel.closeArticle()
            },
            onSwitchToAdmin = {
                viewModel.closeArticle()
                viewModel.switchToAdmin()
            }
        )

        // AdSense / Reusable Policy Dialog when viewing article
        activePolicyDialog?.let { policyType ->
            PolicyDialog(
                policyType = policyType,
                onDismiss = { viewModel.dismissPolicyDialog() }
            )
        }
        return
    }

    // AdSense Policy Dialog
    activePolicyDialog?.let { policyType ->
        PolicyDialog(
            policyType = policyType,
            onDismiss = { viewModel.dismissPolicyDialog() }
        )
    }

    // Mode 1: ADMIN PANEL
    if (appMode == AppMode.ADMIN) {
        BackHandler { viewModel.switchToUser() }
        AdminScreen(
            currentAdminTab = adminTab,
            onSelectAdminTab = { viewModel.setAdminTab(it) },
            onExitToUserMode = { viewModel.switchToUser() },
            totalArticles = totalCount,
            totalViews = totalViews,
            breakingCount = breakingNews.size,
            customDomain = customDomain,
            onSaveDomain = { viewModel.updateCustomDomain(it) },
            allArticles = allArticles,
            categories = viewModel.categories,
            publisherForm = publisherForm,
            onUpdateForm = { title, subtitle, content, category, author, imageUrl, isBreaking, source ->
                viewModel.updatePublisherForm(
                    title = title,
                    subtitle = subtitle,
                    content = content,
                    category = category,
                    author = author,
                    imageUrl = imageUrl,
                    isBreaking = isBreaking,
                    source = source
                )
            },
            onPublish = { viewModel.publishArticle() },
            onClearPublishSuccess = { viewModel.clearSuccessMessage() },
            onEditArticle = { viewModel.startEditArticle(it) },
            onDeleteArticle = { viewModel.deleteArticle(it) },
            onToggleBreaking = { viewModel.toggleArticleBreaking(it) },
            onArticleClick = { viewModel.openArticle(it) },
            onOpenPolicy = { viewModel.showPolicyDialog(it) },
            firebasePingStatus = firebasePingStatus,
            onTestFirebase = { viewModel.testFirebaseConnection() },
            onSyncAllFirebase = { viewModel.syncAllToRealtimeDb() },
            onUpdateFullForm = { viewModel.setPublisherForm(it) },
            onSaveDraft = { viewModel.saveDraft() }
        )
        return
    }

    val authMode by viewModel.authMode.collectAsStateWithLifecycle()

    // Mode 2: AUTHENTICATION (LOGIN & REGISTRATION)
    if (appMode == AppMode.AUTH) {
        BackHandler { viewModel.switchToUser() }
        AuthScreen(
            currentAuthMode = authMode,
            onSelectAuthMode = { viewModel.setAuthMode(it) },
            onLogin = { email, role, name ->
                viewModel.loginUser(email = email, role = role, name = name)
            },
            onRegister = { fullName, email, phone, role ->
                viewModel.registerUser(fullName = fullName, email = email, phone = phone, role = role)
            },
            onExit = { viewModel.switchToUser() }
        )
        return
    }

    // Mode 3: USER READER EXPERIENCE
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = MaterialTheme.colorScheme.surface,
                modifier = Modifier.width(310.dp)
            ) {
                JannahDrawerContent(
                    categories = viewModel.categories,
                    selectedCategory = selectedCategory,
                    currentTab = userTab,
                    onSelectCategory = { cat ->
                        viewModel.selectCategory(cat)
                        viewModel.setUserTab(UserTab.HEADLINES)
                        coroutineScope.launch { drawerState.close() }
                    },
                    onSelectTab = { tab ->
                        viewModel.setUserTab(tab)
                        coroutineScope.launch { drawerState.close() }
                    },
                    onOpenAdmin = {
                        viewModel.switchToAuth(AuthMode.LOGIN)
                        coroutineScope.launch { drawerState.close() }
                    },
                    onOpenPolicy = { policy ->
                        viewModel.showPolicyDialog(policy)
                        coroutineScope.launch { drawerState.close() }
                    },
                    onClose = {
                        coroutineScope.launch { drawerState.close() }
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                coroutineScope.launch { drawerState.open() }
                            },
                            modifier = Modifier.testTag("btn_top_menu")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu",
                                tint = AlifShenNavy
                            )
                        }
                    },
                    title = {
                        AlifShenLogo(
                            isDarkBackground = false,
                            compact = true
                        )
                    },
                    actions = {
                        IconButton(
                            onClick = { viewModel.setUserTab(UserTab.EXPLORE) },
                            modifier = Modifier.testTag("btn_top_search")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search Stories",
                                tint = AlifShenNavy
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.White,
                        titleContentColor = AlifShenNavy,
                        navigationIconContentColor = AlifShenNavy,
                        actionIconContentColor = AlifShenNavy
                    ),
                    modifier = Modifier.drawBehind {
                        drawLine(
                            color = Color(0xFFE2E8F0),
                            start = Offset(0f, size.height),
                            end = Offset(size.width, size.height),
                            strokeWidth = 1.dp.toPx()
                        )
                    }
                )
            },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 6.dp
            ) {
                NavigationBarItem(
                    selected = userTab == UserTab.HEADLINES,
                    onClick = { viewModel.setUserTab(UserTab.HEADLINES) },
                    icon = { Icon(Icons.Default.Newspaper, contentDescription = "Headlines") },
                    label = { Text("Headlines") },
                    modifier = Modifier.testTag("user_nav_headlines")
                )

                NavigationBarItem(
                    selected = userTab == UserTab.EXPLORE,
                    onClick = { viewModel.setUserTab(UserTab.EXPLORE) },
                    icon = { Icon(Icons.Default.Search, contentDescription = "Explore") },
                    label = { Text("Explore") },
                    modifier = Modifier.testTag("user_nav_explore")
                )

                NavigationBarItem(
                    selected = userTab == UserTab.SAVED,
                    onClick = { viewModel.setUserTab(UserTab.SAVED) },
                    icon = {
                        if (bookmarkedNews.isNotEmpty()) {
                            BadgedBox(
                                badge = {
                                    Badge(
                                        containerColor = NewsGoldAccent,
                                        contentColor = Color.Black
                                    ) {
                                        Text("${bookmarkedNews.size}")
                                    }
                                }
                            ) {
                                Icon(Icons.Default.Bookmark, contentDescription = "Saved")
                            }
                        } else {
                            Icon(Icons.Default.Bookmark, contentDescription = "Saved")
                        }
                    },
                    label = { Text("Saved") },
                    modifier = Modifier.testTag("user_nav_saved")
                )

                NavigationBarItem(
                    selected = userTab == UserTab.MORE,
                    onClick = { viewModel.setUserTab(UserTab.MORE) },
                    icon = { Icon(Icons.Default.Info, contentDescription = "More") },
                    label = { Text("About & Info") },
                    modifier = Modifier.testTag("user_nav_more")
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (userTab) {
                UserTab.HEADLINES -> {
                    HomeScreen(
                        articles = displayArticles,
                        breakingArticles = breakingNews,
                        categories = viewModel.categories,
                        selectedCategory = selectedCategory,
                        onSelectCategory = { viewModel.selectCategory(it) },
                        onArticleClick = { viewModel.openArticle(it) },
                        onBookmarkToggle = { viewModel.toggleBookmark(it) },
                        onOpenPolicy = { viewModel.showPolicyDialog(it) },
                        onSwitchToAdmin = { viewModel.switchToAuth(AuthMode.LOGIN) }
                    )
                }

                UserTab.EXPLORE -> {
                    SearchScreen(
                        searchQuery = searchQuery,
                        onQueryChange = { viewModel.updateSearchQuery(it) },
                        searchResults = displayArticles,
                        onArticleClick = { viewModel.openArticle(it) },
                        onBookmarkToggle = { viewModel.toggleBookmark(it) }
                    )
                }

                UserTab.SAVED -> {
                    SavedScreen(
                        savedArticles = bookmarkedNews,
                        onArticleClick = { viewModel.openArticle(it) },
                        onBookmarkToggle = { viewModel.toggleBookmark(it) }
                    )
                }

                UserTab.MORE -> {
                    UserMoreScreen(
                        onOpenPolicy = { viewModel.showPolicyDialog(it) },
                        onSwitchToAdmin = { viewModel.switchToAuth(AuthMode.LOGIN) }
                    )
                }
            }
        }
    }
}
}
