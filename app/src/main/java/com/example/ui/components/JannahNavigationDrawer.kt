package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.AppRegistration
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.Style
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.UserTab
import com.example.ui.theme.JannahBadgeCreative
import com.example.ui.theme.JannahBadgeFoods
import com.example.ui.theme.JannahBadgeLifestyle
import com.example.ui.theme.JannahBadgeSports
import com.example.ui.theme.JannahBadgeTech
import com.example.ui.theme.JannahBadgeTravel
import com.example.ui.theme.JannahHeaderAccent
import com.example.ui.theme.NewsNavyDark
import com.example.ui.theme.NewsNavyPrimary

/**
 * 3-Line (Hamburger) Navigation Drawer Menu
 * Contains all Categories (Lifestyle, Travel, Tech, Sports, Foods, etc.),
 * main navigation tabs, and quick links.
 */
@Composable
fun JannahDrawerContent(
    categories: List<String>,
    selectedCategory: String,
    currentTab: UserTab,
    onSelectCategory: (String) -> Unit,
    onSelectTab: (UserTab) -> Unit,
    onOpenAdmin: () -> Unit,
    onOpenPolicy: (String) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.surface)
            .testTag("jannah_navigation_drawer")
    ) {
        // 1. Drawer Header (Clean Light Editorial Styling with Alif Shen branding & Close Button)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 18.dp, vertical = 20.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AlifShenLogo(
                        isDarkBackground = false,
                        compact = false
                    )

                    IconButton(
                        onClick = onClose,
                        modifier = Modifier
                            .size(32.dp)
                            .testTag("drawer_btn_close")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close Menu",
                            tint = AlifShenNavy
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Global Journalism • Business, Tech, Lifestyle & Sports",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.5.sp),
                    color = AlifShenNavy.copy(alpha = 0.65f)
                )
            }
        }
        HorizontalDivider(color = Color(0xFFE2E8F0))

        // 2. Scrollable Body containing Categories and Navigation
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(horizontal = 12.dp, vertical = 12.dp)
        ) {
            // Main Sections
            DrawerSectionHeader(title = "MAIN FEEDS")
            Spacer(modifier = Modifier.height(4.dp))

            DrawerItem(
                icon = Icons.Default.Home,
                label = "Home / Headlines",
                selected = currentTab == UserTab.HEADLINES && (selectedCategory == "Top Stories" || selectedCategory == "All"),
                onClick = {
                    onSelectCategory("Top Stories")
                    onSelectTab(UserTab.HEADLINES)
                },
                tag = "drawer_nav_home"
            )

            DrawerItem(
                icon = Icons.Default.Explore,
                label = "Explore & Search",
                selected = currentTab == UserTab.EXPLORE,
                onClick = { onSelectTab(UserTab.EXPLORE) },
                tag = "drawer_nav_explore"
            )

            DrawerItem(
                icon = Icons.Default.Bookmark,
                label = "Saved Articles",
                selected = currentTab == UserTab.SAVED,
                onClick = { onSelectTab(UserTab.SAVED) },
                tag = "drawer_nav_saved"
            )

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(14.dp))

            // CATEGORIES Section (Moved from top of HomeScreen)
            DrawerSectionHeader(title = "NEWS CATEGORIES")
            Spacer(modifier = Modifier.height(6.dp))

            val categoryList = if (categories.isEmpty()) {
                listOf("Top Stories", "Life Style", "Travel", "Tech", "Sports", "Foods", "Creative")
            } else {
                categories
            }

            categoryList.forEach { category ->
                val isSelected = selectedCategory.equals(category, ignoreCase = true)
                val badgeColor = getCategoryColor(category)
                val categoryIcon = getCategoryIcon(category)

                Surface(
                    onClick = { onSelectCategory(category) },
                    shape = RoundedCornerShape(8.dp),
                    color = if (isSelected) JannahHeaderAccent.copy(alpha = 0.12f) else Color.Transparent,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp)
                        .testTag("drawer_cat_$category")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .background(badgeColor, CircleShape)
                            )

                            Icon(
                                imageVector = categoryIcon,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                                tint = if (isSelected) JannahHeaderAccent else MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Text(
                                text = category,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                ),
                                color = if (isSelected) JannahHeaderAccent else MaterialTheme.colorScheme.onSurface
                            )
                        }

                        if (isSelected) {
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = JannahHeaderAccent
                            ) {
                                Text(
                                    text = "ACTIVE",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(14.dp))

            // Admin & Quick Links
            DrawerSectionHeader(title = "SETTINGS & POLICIES")
            Spacer(modifier = Modifier.height(4.dp))

            DrawerItem(
                icon = Icons.Default.Login,
                label = "Sign In / Account",
                selected = false,
                onClick = onOpenAdmin,
                tag = "drawer_nav_admin"
            )

            DrawerItem(
                icon = Icons.Default.Security,
                label = "Privacy Policy",
                selected = false,
                onClick = { onOpenPolicy("privacy") },
                tag = "drawer_nav_privacy"
            )

            DrawerItem(
                icon = Icons.Default.Info,
                label = "About Our Newsroom",
                selected = false,
                onClick = { onOpenPolicy("about") },
                tag = "drawer_nav_about"
            )
        }

        // 3. Footer of Drawer
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f))
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = "ALIF SHEN GROUP // NEWS • 2026",
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun DrawerSectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelSmall.copy(
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.8.sp,
            fontSize = 11.sp
        ),
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
    )
}

@Composable
private fun DrawerItem(
    icon: ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    tag: String
) {
    NavigationDrawerItem(
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (selected) JannahHeaderAccent else MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        label = {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                )
            )
        },
        selected = selected,
        onClick = onClick,
        colors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = JannahHeaderAccent.copy(alpha = 0.12f),
            selectedTextColor = JannahHeaderAccent,
            unselectedTextColor = MaterialTheme.colorScheme.onSurface
        ),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
            .testTag(tag)
    )
}

private fun getCategoryColor(category: String): Color {
    return when (category.lowercase()) {
        "lifestyle", "life style" -> JannahBadgeLifestyle
        "travel" -> JannahBadgeTravel
        "tech", "technology" -> JannahBadgeTech
        "sports", "football" -> JannahBadgeSports
        "foods", "food" -> JannahBadgeFoods
        "creative" -> JannahBadgeCreative
        else -> JannahHeaderAccent
    }
}

private fun getCategoryIcon(category: String): ImageVector {
    return when (category.lowercase()) {
        "lifestyle", "life style" -> Icons.Default.Style
        "travel" -> Icons.Default.Flight
        "tech", "technology" -> Icons.Default.Memory
        "sports", "football" -> Icons.Default.SportsSoccer
        "foods", "food" -> Icons.Default.Fastfood
        "creative" -> Icons.Default.Lightbulb
        else -> Icons.Default.Newspaper
    }
}
