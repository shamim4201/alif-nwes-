package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.NewsArticle
import com.example.ui.AdminTab
import com.example.ui.PublisherFormState
import com.example.ui.theme.NewsNavyPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    currentAdminTab: AdminTab,
    onSelectAdminTab: (AdminTab) -> Unit,
    onExitToUserMode: () -> Unit,
    totalArticles: Int,
    totalViews: Int,
    breakingCount: Int,
    customDomain: String,
    onSaveDomain: (String) -> Unit,
    allArticles: List<NewsArticle>,
    categories: List<String>,
    publisherForm: PublisherFormState,
    onUpdateForm: (
        title: String?,
        subtitle: String?,
        content: String?,
        category: String?,
        author: String?,
        imageUrl: String?,
        isBreaking: Boolean?,
        source: String?
    ) -> Unit,
    onPublish: () -> Unit,
    onClearPublishSuccess: () -> Unit,
    onEditArticle: (NewsArticle) -> Unit,
    onDeleteArticle: (NewsArticle) -> Unit,
    onToggleBreaking: (NewsArticle) -> Unit,
    onArticleClick: (NewsArticle) -> Unit,
    onOpenPolicy: (String) -> Unit,
    firebasePingStatus: String? = null,
    onTestFirebase: () -> Unit = {},
    onSyncAllFirebase: () -> Unit = {},
    onUpdateFullForm: ((PublisherFormState) -> Unit)? = null,
    onSaveDraft: (() -> Unit)? = null
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = NewsNavyPrimary
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Security,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp),
                                    tint = Color.White
                                )
                                Text(
                                    text = "ADMIN",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Black,
                                        letterSpacing = 0.5.sp
                                    ),
                                    color = Color.White
                                )
                            }
                        }

                        Text(
                            text = "Newsroom Studio",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                actions = {
                    Surface(
                        onClick = onExitToUserMode,
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .testTag("btn_exit_admin_mode")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                contentDescription = "Reader Mode",
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "Reader Mode",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 6.dp
            ) {
                NavigationBarItem(
                    selected = currentAdminTab == AdminTab.DASHBOARD,
                    onClick = { onSelectAdminTab(AdminTab.DASHBOARD) },
                    icon = { Icon(Icons.Default.Dashboard, contentDescription = "Dashboard") },
                    label = { Text("Dashboard") },
                    modifier = Modifier.testTag("admin_nav_dashboard")
                )

                NavigationBarItem(
                    selected = currentAdminTab == AdminTab.PUBLISH,
                    onClick = { onSelectAdminTab(AdminTab.PUBLISH) },
                    icon = { Icon(Icons.Default.EditNote, contentDescription = "Post") },
                    label = { Text(if (publisherForm.editingArticleId != null) "Edit Story" else "Post News") },
                    modifier = Modifier.testTag("admin_nav_publish")
                )

                NavigationBarItem(
                    selected = currentAdminTab == AdminTab.MANAGE_POSTS,
                    onClick = { onSelectAdminTab(AdminTab.MANAGE_POSTS) },
                    icon = { Icon(Icons.Default.Article, contentDescription = "Manage") },
                    label = { Text("Manage") },
                    modifier = Modifier.testTag("admin_nav_manage")
                )

                NavigationBarItem(
                    selected = currentAdminTab == AdminTab.DOMAIN_SEO,
                    onClick = { onSelectAdminTab(AdminTab.DOMAIN_SEO) },
                    icon = { Icon(Icons.Default.Dns, contentDescription = "Domain & SEO") },
                    label = { Text("Domain") },
                    modifier = Modifier.testTag("admin_nav_domain")
                )

                NavigationBarItem(
                    selected = currentAdminTab == AdminTab.MONETIZATION,
                    onClick = { onSelectAdminTab(AdminTab.MONETIZATION) },
                    icon = { Icon(Icons.Default.MonetizationOn, contentDescription = "AdSense") },
                    label = { Text("AdSense") },
                    modifier = Modifier.testTag("admin_nav_adsense")
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (currentAdminTab) {
                AdminTab.DASHBOARD -> {
                    AdminDashboardScreen(
                        totalArticles = totalArticles,
                        totalViews = totalViews,
                        breakingCount = breakingCount,
                        customDomain = customDomain,
                        topArticles = allArticles,
                        firebasePingStatus = firebasePingStatus,
                        onTestFirebase = onTestFirebase,
                        onSyncAllFirebase = onSyncAllFirebase,
                        onNavigateAdminTab = onSelectAdminTab,
                        onArticleClick = onArticleClick
                    )
                }

                AdminTab.PUBLISH -> {
                    PublisherScreen(
                        formState = publisherForm,
                        categories = categories,
                        onUpdateForm = onUpdateForm,
                        onSubmit = onPublish,
                        onClearSuccess = onClearPublishSuccess,
                        onUpdateFullForm = onUpdateFullForm,
                        onSaveDraft = onSaveDraft
                    )
                }

                AdminTab.MANAGE_POSTS -> {
                    AdminManagePostsScreen(
                        articles = allArticles,
                        categories = categories,
                        onEditArticle = onEditArticle,
                        onDeleteArticle = onDeleteArticle,
                        onToggleBreaking = onToggleBreaking,
                        onViewArticle = onArticleClick
                    )
                }

                AdminTab.DOMAIN_SEO -> {
                    AdminDomainSeoScreen(
                        currentDomain = customDomain,
                        onSaveDomain = onSaveDomain
                    )
                }

                AdminTab.MONETIZATION -> {
                    AdSenseAuditorScreen(
                        publishedArticlesCount = totalArticles,
                        onOpenPolicy = onOpenPolicy
                    )
                }
            }
        }
    }
}
