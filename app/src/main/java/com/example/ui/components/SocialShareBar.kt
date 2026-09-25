package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.NewsArticle
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

val WhatsAppGreen = Color(0xFF25D366)
val FacebookBlue = Color(0xFF1877F2)
val TwitterXBlack = Color(0xFF0F1419)
val TelegramCyan = Color(0xFF24A1DE)
val LinkedInNavy = Color(0xFF0A66C2)
val ShareNavy = Color(0xFF0F172A)
val CopyGray = Color(0xFF475569)

object SocialShareUtils {
    fun getArticleUrl(article: NewsArticle): String {
        val slugOrId = article.slug.ifBlank { article.id.toString() }
        return "https://alifshennews.com/article/$slugOrId"
    }

    fun getShareText(article: NewsArticle): String {
        return "${article.title}\n\nRead more on Alif Shen News: ${getArticleUrl(article)}"
    }

    fun shareToWhatsApp(context: Context, article: NewsArticle) {
        val text = getShareText(article)
        try {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                setPackage("com.whatsapp")
                putExtra(Intent.EXTRA_TEXT, text)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            val webIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://api.whatsapp.com/send?text=${Uri.encode(text)}")
            )
            context.startActivity(webIntent)
        }
    }

    fun shareToFacebook(context: Context, article: NewsArticle) {
        val url = getArticleUrl(article)
        val title = article.title
        try {
            val fbIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                setPackage("com.facebook.katana")
                putExtra(Intent.EXTRA_TEXT, "$title\n$url")
            }
            context.startActivity(fbIntent)
        } catch (e: Exception) {
            val webIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://www.facebook.com/sharer/sharer.php?u=${Uri.encode(url)}")
            )
            context.startActivity(webIntent)
        }
    }

    fun shareToTwitter(context: Context, article: NewsArticle) {
        val url = getArticleUrl(article)
        val text = article.title
        try {
            val twitterIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                setPackage("com.twitter.android")
                putExtra(Intent.EXTRA_TEXT, "$text\n$url")
            }
            context.startActivity(twitterIntent)
        } catch (e: Exception) {
            val webIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://twitter.com/intent/tweet?text=${Uri.encode(text)}&url=${Uri.encode(url)}")
            )
            context.startActivity(webIntent)
        }
    }

    fun shareToTelegram(context: Context, article: NewsArticle) {
        val url = getArticleUrl(article)
        val text = article.title
        try {
            val tgIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                setPackage("org.telegram.messenger")
                putExtra(Intent.EXTRA_TEXT, "$text\n$url")
            }
            context.startActivity(tgIntent)
        } catch (e: Exception) {
            val webIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://t.me/share/url?url=${Uri.encode(url)}&text=${Uri.encode(text)}")
            )
            context.startActivity(webIntent)
        }
    }

    fun shareToLinkedIn(context: Context, article: NewsArticle) {
        val url = getArticleUrl(article)
        val webIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://www.linkedin.com/sharing/share-offsite/?url=${Uri.encode(url)}")
        )
        context.startActivity(webIntent)
    }

    fun shareGeneral(context: Context, article: NewsArticle) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_SUBJECT, article.title)
            putExtra(Intent.EXTRA_TEXT, getShareText(article))
            type = "text/plain"
        }
        context.startActivity(Intent.createChooser(sendIntent, "Share News Story via"))
    }

    fun copyArticleLink(context: Context, article: NewsArticle) {
        val url = getArticleUrl(article)
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Article Link", url)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, "Link copied to clipboard!", Toast.LENGTH_SHORT).show()
    }
}

/**
 * Compact 2-line social share bar showing all platforms without horizontal scrolling.
 */
@Composable
fun SocialShareRow(
    article: NewsArticle,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var copied by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        // Label
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "SHARE STORY",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Black,
                    letterSpacing = 0.6.sp,
                    fontSize = 10.sp
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "Tap to share",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 10.sp
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
        }

        // Line 1: 4 items (WhatsApp, Facebook, X/Twitter, Telegram)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            CompactSocialPill(
                title = "WhatsApp",
                badge = "WA",
                color = WhatsAppGreen,
                onClick = { SocialShareUtils.shareToWhatsApp(context, article) },
                modifier = Modifier.weight(1f),
                testTag = "share_btn_whatsapp"
            )
            CompactSocialPill(
                title = "Facebook",
                badge = "f",
                color = FacebookBlue,
                onClick = { SocialShareUtils.shareToFacebook(context, article) },
                modifier = Modifier.weight(1f),
                testTag = "share_btn_facebook"
            )
            CompactSocialPill(
                title = "X",
                badge = "𝕏",
                color = TwitterXBlack,
                onClick = { SocialShareUtils.shareToTwitter(context, article) },
                modifier = Modifier.weight(0.85f),
                testTag = "share_btn_twitter"
            )
            CompactSocialPill(
                title = "Telegram",
                badge = "TG",
                color = TelegramCyan,
                onClick = { SocialShareUtils.shareToTelegram(context, article) },
                modifier = Modifier.weight(1.05f),
                testTag = "share_btn_telegram"
            )
        }

        // Line 2: 3 items (LinkedIn, Copy Link, More)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            CompactSocialPill(
                title = "LinkedIn",
                badge = "in",
                color = LinkedInNavy,
                onClick = { SocialShareUtils.shareToLinkedIn(context, article) },
                modifier = Modifier.weight(1f),
                testTag = "share_btn_linkedin"
            )

            // Copy Link
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = if (copied) WhatsAppGreen else MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier
                    .weight(1.15f)
                    .height(30.dp)
                    .clickable {
                        SocialShareUtils.copyArticleLink(context, article)
                        copied = true
                        scope.launch {
                            delay(2000)
                            copied = false
                        }
                    }
                    .testTag("share_btn_copy_link")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = if (copied) Icons.Default.Check else Icons.Default.ContentCopy,
                        contentDescription = "Copy Link",
                        tint = if (copied) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (copied) "Copied!" else "Copy Link",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        ),
                        color = if (copied) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // More / System share sheet
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = ShareNavy,
                modifier = Modifier
                    .weight(1f)
                    .height(30.dp)
                    .clickable { SocialShareUtils.shareGeneral(context, article) }
                    .testTag("share_btn_more")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "More Apps",
                        tint = Color.White,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "More",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        ),
                        color = Color.White,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

/**
 * Editorial footer card with 2-line balanced grid for sharing at the bottom of the article.
 */
@Composable
fun SocialShareCard(
    article: NewsArticle,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var copied by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    shape = CircleShape,
                    color = ShareNavy,
                    modifier = Modifier.size(24.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
                Column {
                    Text(
                        text = "Share this Story",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Spread breaking coverage to your networks",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Line 1: WhatsApp, Facebook, X, Telegram
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                CompactSocialPill(
                    title = "WhatsApp",
                    badge = "WA",
                    color = WhatsAppGreen,
                    onClick = { SocialShareUtils.shareToWhatsApp(context, article) },
                    modifier = Modifier.weight(1f)
                )
                CompactSocialPill(
                    title = "Facebook",
                    badge = "f",
                    color = FacebookBlue,
                    onClick = { SocialShareUtils.shareToFacebook(context, article) },
                    modifier = Modifier.weight(1f)
                )
                CompactSocialPill(
                    title = "X",
                    badge = "𝕏",
                    color = TwitterXBlack,
                    onClick = { SocialShareUtils.shareToTwitter(context, article) },
                    modifier = Modifier.weight(0.85f)
                )
                CompactSocialPill(
                    title = "Telegram",
                    badge = "TG",
                    color = TelegramCyan,
                    onClick = { SocialShareUtils.shareToTelegram(context, article) },
                    modifier = Modifier.weight(1.05f)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Line 2: LinkedIn, Copy Link, More
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                CompactSocialPill(
                    title = "LinkedIn",
                    badge = "in",
                    color = LinkedInNavy,
                    onClick = { SocialShareUtils.shareToLinkedIn(context, article) },
                    modifier = Modifier.weight(1f)
                )

                // Copy Link
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = if (copied) WhatsAppGreen else CopyGray,
                    modifier = Modifier
                        .weight(1.15f)
                        .height(30.dp)
                        .clickable {
                            SocialShareUtils.copyArticleLink(context, article)
                            copied = true
                            scope.launch {
                                delay(2000)
                                copied = false
                            }
                        }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = if (copied) Icons.Default.Check else Icons.Default.ContentCopy,
                            contentDescription = "Copy Link",
                            tint = Color.White,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (copied) "Copied!" else "Copy Link",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            ),
                            color = Color.White,
                            maxLines = 1
                        )
                    }
                }

                // More / System share sheet
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = ShareNavy,
                    modifier = Modifier
                        .weight(1f)
                        .height(30.dp)
                        .clickable { SocialShareUtils.shareGeneral(context, article) }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "More Apps",
                            tint = Color.White,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "More",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            ),
                            color = Color.White,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CompactSocialPill(
    title: String,
    badge: String,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String = ""
) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = color,
        modifier = modifier
            .height(30.dp)
            .clickable(onClick = onClick)
            .then(if (testTag.isNotEmpty()) Modifier.testTag(testTag) else Modifier)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = badge,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Black,
                    fontSize = 10.sp
                ),
                color = Color.White,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.width(3.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp
                ),
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
