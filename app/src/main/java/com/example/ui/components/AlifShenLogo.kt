package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R

// Brand Colors from the Alif Shen Group logo
val AlifShenNavy = Color(0xFF031B4D)
val AlifShenBlue = Color(0xFF0073E6)
val AlifShenLightBlue = Color(0xFF1EA7FD)
val AlifShenRed = Color(0xFFE50914)
val AlifShenDarkRed = Color(0xFFB8050D)

/**
 * High-fidelity logo for "ALIF SHEN GROUP // NEWS"
 * Supports dark mode, top bar placement, drawer header, and footer.
 */
@Composable
fun AlifShenLogo(
    modifier: Modifier = Modifier,
    isDarkBackground: Boolean = true,
    compact: Boolean = false
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        modifier = modifier.testTag("alif_shen_logo")
    ) {
        // 1. Monogram AS Emblem with Globe & Orbit
        Box(
            modifier = Modifier
                .size(if (compact) 32.dp else 38.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(
                    if (isDarkBackground) Color.White.copy(alpha = 0.12f) else Color(0xFFF1F5F9)
                )
                .border(
                    width = if (isDarkBackground) 0.dp else 0.8.dp,
                    color = if (isDarkBackground) Color.Transparent else Color(0xFFE2E8F0),
                    shape = RoundedCornerShape(8.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            // Stylized AS Emblem: Dual Ring + Globe
            Box(
                modifier = Modifier
                    .size(if (compact) 26.dp else 32.dp),
                contentAlignment = Alignment.Center
            ) {
                // Outer orbital red arc
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .border(
                            width = 2.dp,
                            brush = Brush.sweepGradient(
                                listOf(
                                    AlifShenBlue,
                                    AlifShenRed,
                                    AlifShenBlue
                                )
                            ),
                            shape = CircleShape
                        )
                )

                // Globe icon at center
                Icon(
                    imageVector = Icons.Default.Public,
                    contentDescription = null,
                    tint = if (isDarkBackground) AlifShenLightBlue else AlifShenBlue,
                    modifier = Modifier.size(if (compact) 14.dp else 18.dp)
                )

                // Mini red dot / satellite
                Box(
                    modifier = Modifier
                        .size(5.dp)
                        .align(Alignment.TopEnd)
                        .background(AlifShenRed, CircleShape)
                )
            }
        }

        // 2. Typography: ALIF SHEN GROUP & Slanted NEWS Badge
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "ALIF SHEN",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Black,
                        fontSize = if (compact) 13.sp else 15.sp,
                        letterSpacing = 0.5.sp,
                        fontFamily = FontFamily.SansSerif
                    ),
                    color = if (isDarkBackground) Color.White else AlifShenNavy
                )

                Text(
                    text = "GROUP",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = if (compact) 11.5.sp else 13.5.sp,
                        letterSpacing = 0.5.sp,
                        fontFamily = FontFamily.SansSerif
                    ),
                    color = if (isDarkBackground) AlifShenLightBlue else AlifShenBlue
                )

                // Slanted NEWS Badge
                Surface(
                    shape = RoundedCornerShape(3.dp),
                    color = AlifShenRed,
                    modifier = Modifier
                        .graphicsLayer {
                            rotationZ = -4f
                        }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                    ) {
                        Text(
                            text = "// NEWS",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Black,
                                fontSize = if (compact) 8.5.sp else 9.5.sp,
                                letterSpacing = 0.3.sp
                            ),
                            color = Color.White
                        )
                    }
                }
            }

            // Accent gradient line underneath
            Box(
                modifier = Modifier
                    .width(if (compact) 110.dp else 135.dp)
                    .height(1.8.dp)
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                AlifShenLightBlue,
                                AlifShenBlue,
                                Color.Transparent
                            )
                        )
                    )
            )
        }
    }
}

/**
 * Large Banner Logo with image asset or vector presentation
 */
@Composable
fun AlifShenHeroLogo(
    modifier: Modifier = Modifier,
    isDarkBackground: Boolean = false
) {
    Surface(
        color = if (isDarkBackground) Color.White.copy(alpha = 0.06f) else Color.White,
        shape = RoundedCornerShape(12.dp),
        shadowElevation = if (isDarkBackground) 0.dp else 2.dp,
        modifier = modifier.testTag("alif_shen_hero_logo")
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AlifShenLogo(
                isDarkBackground = isDarkBackground,
                compact = false
            )
        }
    }
}
