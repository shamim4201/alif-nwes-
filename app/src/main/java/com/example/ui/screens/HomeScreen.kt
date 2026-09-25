package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.NewsArticle
import com.example.ui.components.AlifShenNavy
import com.example.ui.components.CategoryBadge
import com.example.ui.components.JannahCompactItem
import com.example.ui.components.JannahDarkSportsSection
import com.example.ui.components.JannahFeaturedCard
import com.example.ui.components.JannahGridCard
import com.example.ui.components.JannahHeroGrid
import com.example.ui.components.JannahSectionHeader
import com.example.ui.components.NewsArticleCard
import com.example.ui.theme.JannahBadgeCreative
import com.example.ui.theme.JannahBadgeSports
import com.example.ui.theme.JannahBadgeTech
import com.example.ui.theme.JannahHeaderAccent
import com.example.ui.theme.NewsBreakingRed

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import com.example.ui.components.JannahFooterMenu

@Composable
fun HomeScreen(
    articles: List<NewsArticle>,
    breakingArticles: List<NewsArticle>,
    categories: List<String>,
    selectedCategory: String,
    onSelectCategory: (String) -> Unit,
    onArticleClick: (NewsArticle) -> Unit,
    onBookmarkToggle: (NewsArticle) -> Unit,
    onOpenPolicy: (String) -> Unit = {},
    onSwitchToAdmin: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val isAllCategory = selectedCategory == "Top Stories" || selectedCategory == "All"
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val showScrollToTop by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0 || listState.firstVisibleItemScrollOffset > 250
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .testTag("home_news_feed"),
            contentPadding = PaddingValues(bottom = 96.dp)
        ) {
        // 1. Trending / Breaking News ticker bar (Auto-sliding one after another)
        if (breakingArticles.isNotEmpty()) {
            item {
                var tickerIndex by remember { mutableIntStateOf(0) }
                LaunchedEffect(breakingArticles.size) {
                    if (breakingArticles.size > 1) {
                        while (true) {
                            delay(3500)
                            tickerIndex = (tickerIndex + 1) % breakingArticles.size
                        }
                    }
                }
                val currentBreaking = breakingArticles.getOrNull(tickerIndex) ?: breakingArticles.first()

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onArticleClick(currentBreaking) }
                        .testTag("breaking_news_banner"),
                    color = NewsBreakingRed
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(3.dp),
                            color = Color.White.copy(alpha = 0.25f),
                            modifier = Modifier.size(24.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.FlashOn,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                        Text(
                            text = "TRENDING:",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Black,
                                letterSpacing = 0.8.sp
                            ),
                            color = Color.White
                        )

                        AnimatedContent(
                            targetState = currentBreaking,
                            transitionSpec = {
                                (slideInVertically { height -> height } + fadeIn()) togetherWith
                                (slideOutVertically { height -> -height } + fadeOut())
                            },
                            modifier = Modifier.weight(1f),
                            label = "ticker_anim"
                        ) { article ->
                            Text(
                                text = article.title,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 12.5.sp
                                ),
                                color = Color.White,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
        }

        // When viewing all / Top Stories, display the magazine layout from the screenshot:
        if (isAllCategory && articles.isNotEmpty()) {
            val boyArticle = articles.find { 
                it.id == 1L || 
                it.title.contains("One man with courage", ignoreCase = true) 
            } ?: articles.first()
            val remainingArticles = articles.filter { it.id != boyArticle.id }
            val sliderArticlesList = listOf(boyArticle) + remainingArticles.take(4)
            val subArticlesList = remainingArticles.drop(4).take(2).ifEmpty { remainingArticles.take(2) }

            // 3. Featured Auto-Sliding Hero Grid
            item {
                JannahHeroGrid(
                    sliderArticles = sliderArticlesList,
                    subArticles = subArticlesList,
                    onArticleClick = onArticleClick
                )
            }

            // 4. TRENDING NEWS Section (2-Column Grid)
            item {
                Spacer(modifier = Modifier.height(10.dp))
                JannahSectionHeader(
                    title = "Trending News",
                    accentColor = JannahHeaderAccent
                )
            }

            val trendingArticles = articles.filter { it.id != boyArticle.id }.take(4)
            items(trendingArticles.chunked(2)) { rowArticles ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 5.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    rowArticles.forEach { article ->
                        Box(modifier = Modifier.weight(1f)) {
                            JannahGridCard(
                                article = article,
                                onClick = { onArticleClick(article) }
                            )
                        }
                    }
                    if (rowArticles.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }

            // 5. Dark Theme SPORTS Section
            val sportsList = articles.filter { 
                it.category.equals("Sports", ignoreCase = true) || 
                it.category.equals("Football", ignoreCase = true) 
            }.ifEmpty {
                articles.takeLast(4)
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                JannahDarkSportsSection(
                    sportsArticles = sportsList,
                    onArticleClick = onArticleClick
                )
            }

            // 6. WHAT'S NEW (2-Column Grid Section)
            item {
                Spacer(modifier = Modifier.height(12.dp))
                JannahSectionHeader(
                    title = "What's New",
                    accentColor = JannahBadgeCreative
                )
            }

            val whatsNewArticles = articles.takeLast(4)
            items(whatsNewArticles.chunked(2)) { rowArticles ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 5.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    rowArticles.forEach { gridArticle ->
                        Box(modifier = Modifier.weight(1f)) {
                            JannahGridCard(
                                article = gridArticle,
                                onClick = { onArticleClick(gridArticle) }
                            )
                        }
                    }
                    if (rowArticles.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }

            // 7. Latest Articles Feed (2-Column Grid)
            item {
                Spacer(modifier = Modifier.height(16.dp))
                JannahSectionHeader(
                    title = "Latest Stories",
                    accentColor = JannahBadgeTech
                )
            }

            items(articles.chunked(2)) { rowArticles ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 5.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    rowArticles.forEach { article ->
                        Box(modifier = Modifier.weight(1f)) {
                            JannahGridCard(
                                article = article,
                                onClick = { onArticleClick(article) }
                            )
                        }
                    }
                    if (rowArticles.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }

        } else if (articles.isEmpty()) {
            // Empty State
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(40.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Newspaper,
                        contentDescription = null,
                        modifier = Modifier.size(54.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No articles in $selectedCategory",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Use the Admin Panel to publish the first story in this category!",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            // Category Filtered View (2-Column Grid)
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = selectedCategory,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = "${articles.size} Stories",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            items(articles.chunked(2)) { rowArticles ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 5.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    rowArticles.forEach { article ->
                        Box(modifier = Modifier.weight(1f)) {
                            JannahGridCard(
                                article = article,
                                onClick = { onArticleClick(article) }
                            )
                        }
                    }
                    if (rowArticles.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }

        // Jannah Footer Menu
        item {
            Spacer(modifier = Modifier.height(28.dp))
            JannahFooterMenu(
                categories = categories,
                selectedCategory = selectedCategory,
                onSelectCategory = { cat ->
                    onSelectCategory(cat)
                    coroutineScope.launch { listState.animateScrollToItem(0) }
                },
                onOpenPolicy = onOpenPolicy,
                onBackToTop = {
                    coroutineScope.launch { listState.animateScrollToItem(0) }
                },
                onSwitchToAdmin = onSwitchToAdmin
            )
        }
    }

    // Floating Quick Navigation Action Buttons (Scroll Down to Posts / Scroll Up to Top)
    Box(
        modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(bottom = 16.dp, end = 16.dp)
    ) {
        if (showScrollToTop) {
            FloatingActionButton(
                onClick = {
                    coroutineScope.launch {
                        listState.animateScrollToItem(0)
                    }
                },
                containerColor = AlifShenNavy,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier
                    .size(48.dp)
                    .testTag("btn_scroll_to_top")
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = "Scroll to top",
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                )
            }
        } else {
            // When at top, show Quick Jump button to jump down to news posts
            Surface(
                onClick = {
                    coroutineScope.launch {
                        listState.animateScrollToItem(3)
                    }
                },
                shape = RoundedCornerShape(20.dp),
                color = AlifShenNavy,
                shadowElevation = 6.dp,
                modifier = Modifier.testTag("btn_scroll_down_posts")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 9.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "More Stories",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Scroll down to news",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
}
