package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
 * Reusable Footer component in Compose that includes essential links
 * like About Us, Contact, Privacy Policy, Terms of Service, Category links,
 * Back to Top button, and Copyright notice.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AppFooter(
    categories: List<String> = emptyList(),
    selectedCategory: String = "",
    onSelectCategory: (String) -> Unit = {},
    onOpenPolicy: (String) -> Unit = {},
    onBackToTop: () -> Unit = {},
    onSwitchToAdmin: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(NewsNavyPrimary, NewsNavyDark)
                )
            )
            .testTag("reusable_app_footer")
    ) {
        // 1. Back to Top Bar
        Surface(
            onClick = onBackToTop,
            color = Color.White.copy(alpha = 0.08f),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("footer_back_to_top_button")
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
                text = "ALIF SHEN NEWS is your trusted digital journalism network providing non-partisan reporting, breaking national and world news, investigative journalism, lifestyle, and business insights.",
                style = MaterialTheme.typography.bodySmall.copy(
                    lineHeight = 18.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )
            )

            Spacer(modifier = Modifier.height(20.dp))
            HorizontalDivider(color = Color.White.copy(alpha = 0.12f), thickness = 0.8.dp)
            Spacer(modifier = Modifier.height(20.dp))

            // 3. Category navigation if provided
            if (categories.isNotEmpty()) {
                Text(
                    text = "CATEGORIES",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp,
                        color = JannahHeaderAccent
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    categories.forEach { category ->
                        val isSelected = selectedCategory == category
                        Surface(
                            onClick = { onSelectCategory(category) },
                            shape = RoundedCornerShape(4.dp),
                            color = if (isSelected) JannahHeaderAccent else Color.White.copy(alpha = 0.12f),
                            modifier = Modifier.testTag("app_footer_cat_$category")
                        ) {
                            Text(
                                text = category,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                ),
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
                HorizontalDivider(color = Color.White.copy(alpha = 0.12f), thickness = 0.8.dp)
                Spacer(modifier = Modifier.height(20.dp))
            }

            // 4. Essential Links (About Us, Contact, Privacy Policy, Terms of Service)
            Text(
                text = "ESSENTIAL LINKS & POLICIES",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp,
                    color = JannahHeaderAccent
                )
            )
            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    AppFooterLink(
                        icon = Icons.Default.Info,
                        title = "About Us",
                        tag = "footer_link_about",
                        onClick = { onOpenPolicy("about") }
                    )
                    AppFooterLink(
                        icon = Icons.Default.Email,
                        title = "Contact Us",
                        tag = "footer_link_contact",
                        onClick = { onOpenPolicy("contact") }
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    AppFooterLink(
                        icon = Icons.Default.Security,
                        title = "Privacy Policy",
                        tag = "footer_link_privacy",
                        onClick = { onOpenPolicy("privacy") }
                    )
                    AppFooterLink(
                        icon = Icons.Default.Policy,
                        title = "Terms of Service",
                        tag = "footer_link_terms",
                        onClick = { onOpenPolicy("terms") }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider(color = Color.White.copy(alpha = 0.12f), thickness = 0.8.dp)
            Spacer(modifier = Modifier.height(18.dp))

            // 5. Copyright notice and disclaimer
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "© 2026 Alif Shen Group News Network. All Rights Reserved.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    ),
                    color = Color.White.copy(alpha = 0.7f)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Published with Google AdSense and Google News compliance standards.",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                    color = Color.White.copy(alpha = 0.5f)
                )
            }
        }
    }
}

@Composable
private fun AppFooterLink(
    icon: ImageVector,
    title: String,
    tag: String,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable(onClick = onClick)
            .testTag(tag)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White.copy(alpha = 0.85f),
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
        )
    }
}
