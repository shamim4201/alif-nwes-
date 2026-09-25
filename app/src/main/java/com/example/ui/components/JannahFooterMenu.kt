package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.AppRegistration
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.RssFeed
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import com.example.ui.theme.JannahHeaderAccent
import com.example.ui.theme.NewsNavyDark
import com.example.ui.theme.NewsNavyPrimary

/**
 * Jannah Magazine Footer Menu
 * Elegant, rich footer section with quick category links, policy pages,
 * social channels, back to top button, and copyright notices.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun JannahFooterMenu(
    categories: List<String>,
    selectedCategory: String,
    onSelectCategory: (String) -> Unit,
    onOpenPolicy: (String) -> Unit,
    onBackToTop: () -> Unit,
    onSwitchToAdmin: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        NewsNavyPrimary,
                        NewsNavyDark
                    )
                )
            )
            .testTag("jannah_footer_menu")
    ) {
        // 1. Back to Top Bar
        Surface(
            onClick = onBackToTop,
            color = Color.White.copy(alpha = 0.08f),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("footer_back_to_top")
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = "Back to top",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "BACK TO TOP",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    ),
                    color = Color.White
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 24.dp)
        ) {
            // 2. Brand Identity & Tagline
            AlifShenLogo(
                isDarkBackground = true,
                compact = false
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "ALIF SHEN GROUP NEWS is the premier digital journalism network delivering breaking reports, investigative technology coverage, lifestyle, and global analysis.",
                style = MaterialTheme.typography.bodySmall.copy(
                    lineHeight = 19.sp,
                    color = Color.White.copy(alpha = 0.75f)
                )
            )

            Spacer(modifier = Modifier.height(20.dp))
            HorizontalDivider(
                color = Color.White.copy(alpha = 0.12f),
                thickness = 0.8.dp
            )
            Spacer(modifier = Modifier.height(20.dp))

            // 3. Category Links Menu
            FooterSectionHeading(title = "EXPLORE CATEGORIES")
            Spacer(modifier = Modifier.height(10.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                val menuCategories = if (categories.isEmpty()) {
                    listOf("Top Stories", "Life Style", "Travel", "Tech", "Sports", "Foods")
                } else {
                    categories
                }

                menuCategories.forEach { category ->
                    val isSelected = selectedCategory == category
                    Surface(
                        onClick = { onSelectCategory(category) },
                        shape = RoundedCornerShape(4.dp),
                        color = if (isSelected) JannahHeaderAccent else Color.White.copy(alpha = 0.1f),
                        modifier = Modifier.testTag("footer_cat_${category}")
                    ) {
                        Text(
                            text = category,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            ),
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider(
                color = Color.White.copy(alpha = 0.12f),
                thickness = 0.8.dp
            )
            Spacer(modifier = Modifier.height(20.dp))

            // 4. Policy & Company Menu Links
            FooterSectionHeading(title = "ABOUT & POLICIES")
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    FooterLinkItem(
                        icon = Icons.Default.Info,
                        title = "About Our Newsroom",
                        onClick = { onOpenPolicy("about") }
                    )
                    FooterLinkItem(
                        icon = Icons.Default.Email,
                        title = "Contact & News Tips",
                        onClick = { onOpenPolicy("contact") }
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    FooterLinkItem(
                        icon = Icons.Default.Security,
                        title = "Privacy Policy",
                        onClick = { onOpenPolicy("privacy") }
                    )
                    FooterLinkItem(
                        icon = Icons.Default.Policy,
                        title = "Terms of Service",
                        onClick = { onOpenPolicy("terms") }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider(
                color = Color.White.copy(alpha = 0.12f),
                thickness = 0.8.dp
            )
            Spacer(modifier = Modifier.height(20.dp))

            // 5. Social & Editorial Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Social Channels
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    SocialIconBubble(icon = Icons.Default.Public, label = "Global")
                    SocialIconBubble(icon = Icons.Default.Share, label = "Share")
                    SocialIconBubble(icon = Icons.Default.RssFeed, label = "RSS")
                }

                // Sign In button
                OutlinedButton(
                    onClick = onSwitchToAdmin,
                    shape = RoundedCornerShape(6.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.White
                    ),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = Brush.horizontalGradient(
                            listOf(Color.White.copy(alpha = 0.4f), Color.White.copy(alpha = 0.2f))
                        )
                    ),
                    modifier = Modifier.testTag("footer_admin_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Login,
                        contentDescription = "Sign In",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Sign In",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 6. Copyright Notice
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "© 2026 ALIF SHEN GROUP // NEWS. All Rights Reserved.",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                    color = Color.White.copy(alpha = 0.6f)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Engineered with Jannah Magazine Architecture • AdSense Compliant",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    color = Color.White.copy(alpha = 0.4f)
                )
            }
        }
    }
}

@Composable
private fun FooterSectionHeading(title: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .width(3.dp)
                .height(13.dp)
                .background(JannahHeaderAccent, RoundedCornerShape(1.dp))
        )
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp,
                fontSize = 12.sp
            ),
            color = Color.White
        )
    }
}

@Composable
private fun FooterLinkItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = JannahHeaderAccent,
            modifier = Modifier.size(15.dp)
        )
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.5.sp),
            color = Color.White.copy(alpha = 0.85f)
        )
    }
}

@Composable
private fun SocialIconBubble(
    icon: ImageVector,
    label: String
) {
    Surface(
        shape = CircleShape,
        color = Color.White.copy(alpha = 0.1f),
        modifier = Modifier.size(34.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(34.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}
