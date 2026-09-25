package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Publish
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ui.PublisherFormState
import com.example.ui.theme.NewsBreakingRed
import com.example.ui.theme.NewsGoldAccent
import com.example.ui.theme.NewsNavyPrimary

enum class WordPressEditorTab {
    EDITOR,
    SETTINGS,
    PREVIEW
}

@Composable
fun PublisherScreen(
    formState: PublisherFormState,
    categories: List<String>,
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
    onSubmit: () -> Unit,
    onClearSuccess: () -> Unit,
    onUpdateFullForm: ((PublisherFormState) -> Unit)? = null,
    onSaveDraft: (() -> Unit)? = null
) {
    var selectedTab by remember { mutableStateOf(WordPressEditorTab.EDITOR) }

    val presetImages = listOf(
        Pair("US Capitol", "https://images.unsplash.com/photo-1541872703-74c5e44368f9?w=800&q=80"),
        Pair("Wall Street", "https://images.unsplash.com/photo-1611974789855-9c2a0a7236a3?w=800&q=80"),
        Pair("Silicon Valley", "https://images.unsplash.com/photo-1518770660439-4636190af475?w=800&q=80"),
        Pair("NASA / Space", "https://images.unsplash.com/photo-1446776811953-b23d57bd21aa?w=800&q=80"),
        Pair("Press Conference", "https://images.unsplash.com/photo-1585829365295-ab7cd400c167?w=800&q=80"),
        Pair("Sports Arena", "https://images.unsplash.com/photo-1508098682722-e99c43a406b2?w=800&q=80")
    )

    fun updateState(transform: (PublisherFormState) -> PublisherFormState) {
        val next = transform(formState)
        if (onUpdateFullForm != null) {
            onUpdateFullForm(next)
        } else {
            onUpdateForm(
                next.title,
                next.subtitle,
                next.content,
                next.category,
                next.author,
                next.imageUrl,
                next.isBreaking,
                next.source
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // --- 1. WordPress Top App Bar / Document Controls ---
        Surface(
            tonalElevation = 3.dp,
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left: WP Icon & Post Status
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFF2271B1), // WordPress Classic Blue
                            modifier = Modifier.size(32.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "W",
                                    color = Color.White,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 18.sp
                                )
                            }
                        }

                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = if (formState.editingArticleId != null) "Edit Post" else "Add New Post",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                Surface(
                                    color = if (formState.postStatus == "Published") Color(0xFF16A34A).copy(alpha = 0.15f) else Color(0xFFF58220).copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(
                                        text = formState.postStatus,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = if (formState.postStatus == "Published") Color(0xFF16A34A) else Color(0xFFF58220)
                                        ),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Text(
                                text = "Auto-saving to Firebase Realtime DB",
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Right: Actions (Save Draft & Publish)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                if (onSaveDraft != null) {
                                    onSaveDraft()
                                } else {
                                    updateState { it.copy(postStatus = "Draft", submitSuccessMessage = "Draft saved in WordPress!") }
                                }
                            },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("btn_wp_save_draft")
                        ) {
                            Text(
                                text = "Save Draft",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold)
                            )
                        }

                        Button(
                            onClick = onSubmit,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2271B1)),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("btn_publish_article")
                        ) {
                            Text(
                                text = if (formState.editingArticleId != null) "Update" else "Publish",
                                color = Color.White,
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // View Tabs (Editor | Settings | Preview)
                TabRow(
                    selectedTabIndex = selectedTab.ordinal,
                    containerColor = MaterialTheme.colorScheme.surface
                ) {
                    Tab(
                        selected = selectedTab == WordPressEditorTab.EDITOR,
                        onClick = { selectedTab = WordPressEditorTab.EDITOR },
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(Icons.Default.EditNote, contentDescription = null, modifier = Modifier.size(16.dp))
                                Text("Content", fontWeight = FontWeight.SemiBold)
                            }
                        },
                        modifier = Modifier.testTag("tab_wp_editor")
                    )

                    Tab(
                        selected = selectedTab == WordPressEditorTab.SETTINGS,
                        onClick = { selectedTab = WordPressEditorTab.SETTINGS },
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(Icons.Default.Settings, contentDescription = null, modifier = Modifier.size(16.dp))
                                Text("Post Settings", fontWeight = FontWeight.SemiBold)
                            }
                        },
                        modifier = Modifier.testTag("tab_wp_settings")
                    )

                    Tab(
                        selected = selectedTab == WordPressEditorTab.PREVIEW,
                        onClick = { selectedTab = WordPressEditorTab.PREVIEW },
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(Icons.Default.Visibility, contentDescription = null, modifier = Modifier.size(16.dp))
                                Text("Preview", fontWeight = FontWeight.SemiBold)
                            }
                        },
                        modifier = Modifier.testTag("tab_wp_preview")
                    )
                }
            }
        }

        // --- 2. Main Tab Body ---
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // Success & Error Feedback Alerts
            formState.submitSuccessMessage?.let { msg ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFDCFCE7)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onClearSuccess() }
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF166534))
                        Text(
                            text = msg,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                            color = Color(0xFF166534)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }

            formState.errorMessage?.let { err ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(imageVector = Icons.Default.ErrorOutline, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                        Text(
                            text = err,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }

            when (selectedTab) {
                WordPressEditorTab.EDITOR -> {
                    // ==========================================================
                    // TAB 1: WORDPRESS CONTENT & GUTENBERG STYLE WRITER
                    // ==========================================================

                    // 1. Gutenberg Title
                    OutlinedTextField(
                        value = formState.title,
                        onValueChange = { updateState { s -> s.copy(title = it) } },
                        placeholder = {
                            Text(
                                "Add title",
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                                )
                            )
                        },
                        textStyle = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_article_title"),
                        singleLine = false,
                        maxLines = 3,
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // 2. Permalink / Slug Indicator (Classic WP Slug editor)
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "Permalink:",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "https://official1.online/posts/${formState.effectiveSlug}",
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                color = Color(0xFF2271B1),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // 3. WordPress Rich Formatting Toolbar
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState())
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            // Bold button
                            FormattingBtn(label = "B", isBold = true) {
                                updateState { it.copy(content = it.content + " **bold text** ") }
                            }
                            // Italic button
                            FormattingBtn(label = "I", isItalic = true) {
                                updateState { it.copy(content = it.content + " *italic text* ") }
                            }
                            // H2 button
                            FormattingBtn(label = "H2", isBold = true) {
                                updateState { it.copy(content = it.content + "\n\n## Subheading\n") }
                            }
                            // H3 button
                            FormattingBtn(label = "H3", isBold = true) {
                                updateState { it.copy(content = it.content + "\n\n### Section Title\n") }
                            }
                            // Blockquote button
                            FormattingBtn(label = "“ Quote") {
                                updateState { it.copy(content = it.content + "\n\n> Important quote or source statement\n\n") }
                            }
                            // Bullet list button
                            FormattingBtn(label = "• List") {
                                updateState { it.copy(content = it.content + "\n\n- Key point one\n- Key point two\n") }
                            }
                            // Link button
                            FormattingBtn(label = "🔗 Link") {
                                updateState { it.copy(content = it.content + " [Link Title](https://example.com) ") }
                            }
                            // Horizontal separator
                            FormattingBtn(label = "― HR") {
                                updateState { it.copy(content = it.content + "\n\n---\n\n") }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // 4. Quick Gutenberg Template Blocks
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        QuickBlockChip(label = "+ Lead Paragraph") {
                            updateState {
                                it.copy(content = it.content + "\nWASHINGTON — In a major development today, officials confirmed new details regarding the unfolding situation.")
                            }
                        }
                        QuickBlockChip(label = "+ Key Takeaways Box") {
                            updateState {
                                it.copy(content = it.content + "\n\n### 📌 Key Takeaways:\n- First major update\n- Economic and policy impact\n- What happens next\n\n")
                            }
                        }
                        QuickBlockChip(label = "+ Official Statement") {
                            updateState {
                                it.copy(content = it.content + "\n\n> \"We are committed to delivering transparency and decisive action for the American public,\" a spokesperson stated.\n\n")
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // 5. Main Content Editor
                    OutlinedTextField(
                        value = formState.content,
                        onValueChange = { updateState { s -> s.copy(content = it) } },
                        placeholder = {
                            Text(
                                "Type / to choose a block or write your complete story here. Include background context, source citations, and verified quotes...",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                                )
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .testTag("input_article_content"),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Live Stats Bar (Word count, characters, reading time)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Words: ${formState.wordCount}  |  Characters: ${formState.content.length}",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Reading Time: ~${formState.estimatedReadMinutes} min",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Bottom Quick Next Button to Settings
                    OutlinedButton(
                        onClick = { selectedTab = WordPressEditorTab.SETTINGS },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.Settings, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Configure Categories, Featured Image & SEO ➔")
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }

                WordPressEditorTab.SETTINGS -> {
                    // ==========================================================
                    // TAB 2: WORDPRESS POST SETTINGS / SIDEBAR
                    // ==========================================================

                    // Section 1: Status & Visibility
                    WpSectionHeader(title = "Status & Visibility")
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            // Visibility
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Visibility", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium))
                                Surface(
                                    color = MaterialTheme.colorScheme.surface,
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        "Public",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = Color(0xFF2271B1)),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Post Status choice
                            Text("Post Status", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                FilterChip(
                                    selected = formState.postStatus == "Published",
                                    onClick = { updateState { it.copy(postStatus = "Published") } },
                                    label = { Text("Published") }
                                )
                                FilterChip(
                                    selected = formState.postStatus == "Draft",
                                    onClick = { updateState { it.copy(postStatus = "Draft") } },
                                    label = { Text("Draft") }
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Stick to top / Breaking news
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Stick to the top of the blog",
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Text(
                                        text = "Mark as Breaking Flash and pin at the top of feed",
                                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Switch(
                                    checked = formState.isBreaking,
                                    onCheckedChange = { updateState { s -> s.copy(isBreaking = it) } },
                                    colors = SwitchDefaults.colors(checkedThumbColor = NewsBreakingRed),
                                    modifier = Modifier.testTag("toggle_breaking_switch")
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Author & Source
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedTextField(
                                    value = formState.author,
                                    onValueChange = { updateState { s -> s.copy(author = it) } },
                                    label = { Text("Author / Reporter") },
                                    singleLine = true,
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                OutlinedTextField(
                                    value = formState.source,
                                    onValueChange = { updateState { s -> s.copy(source = it) } },
                                    label = { Text("Editorial Bureau") },
                                    singleLine = true,
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(8.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Section 2: Categories (WordPress Taxonomy)
                    WpSectionHeader(title = "Categories")
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = "Select primary category for this news article:",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                categories.filter { it != "All" }.forEach { cat ->
                                    FilterChip(
                                        selected = formState.category == cat,
                                        onClick = { updateState { it.copy(category = cat) } },
                                        label = { Text(cat) },
                                        modifier = Modifier.testTag("chip_cat_$cat")
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Section 3: Tags (ট্যাগস)
                    WpSectionHeader(title = "Tags")
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            OutlinedTextField(
                                value = formState.tags,
                                onValueChange = { updateState { s -> s.copy(tags = it) } },
                                label = { Text("Add Tags (separate with commas)") },
                                placeholder = { Text("usa, politics, congress, economy") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                formState.tags.split(",").map { it.trim() }.filter { it.isNotBlank() }.forEach { tag ->
                                    Surface(
                                        color = MaterialTheme.colorScheme.surface,
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Text(
                                            text = "#$tag",
                                            style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF2271B1)),
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Section 4: Featured Image (ফিচার্ড ইমেজ)
                    WpSectionHeader(title = "Featured Image")
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            // Thumbnail Preview
                            if (formState.imageUrl.isNotBlank()) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(140.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color.Black)
                                ) {
                                    AsyncImage(
                                        model = formState.imageUrl,
                                        contentDescription = "Featured image preview",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                            }

                            // Quick Unsplash Presets
                            Text("Quick High-Res Presets:", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                presetImages.forEach { (name, url) ->
                                    FilterChip(
                                        selected = formState.imageUrl == url,
                                        onClick = { updateState { it.copy(imageUrl = url) } },
                                        label = { Text(name) }
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = formState.imageUrl,
                                onValueChange = { updateState { s -> s.copy(imageUrl = it) } },
                                label = { Text("Featured Image URL") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("input_article_image_url"),
                                shape = RoundedCornerShape(8.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Section 5: Excerpt (সারাংশ)
                    WpSectionHeader(title = "Excerpt")
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            OutlinedTextField(
                                value = formState.excerpt,
                                onValueChange = { updateState { s -> s.copy(excerpt = it) } },
                                label = { Text("Write an excerpt (optional)") },
                                placeholder = { Text("Brief one-sentence summary for homepage cards and RSS feeds...") },
                                modifier = Modifier.fillMaxWidth(),
                                maxLines = 3,
                                shape = RoundedCornerShape(8.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Section 6: SEO & Google Search Snippet (Yoast/RankMath style)
                    WpSectionHeader(title = "SEO & Google Snippet (Yoast Style)")
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            // SEO Score bar
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("SEO Readiness Score", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                                Text(
                                    text = "${formState.seoScore} / 100",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Black,
                                        color = if (formState.seoScore >= 70) Color(0xFF16A34A) else NewsGoldAccent
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            LinearProgressIndicator(
                                progress = { formState.seoScore / 100f },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp),
                                color = if (formState.seoScore >= 70) Color(0xFF16A34A) else NewsGoldAccent
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            OutlinedTextField(
                                value = formState.focusKeyword,
                                onValueChange = { updateState { s -> s.copy(focusKeyword = it) } },
                                label = { Text("Focus Keyword") },
                                placeholder = { Text("e.g., US Economy 2026") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                shape = RoundedCornerShape(8.dp)
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // Google Search Mobile Preview Box
                            Text(
                                text = "Google Mobile Search Snippet Preview:",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Surface(
                                color = Color.White,
                                shape = RoundedCornerShape(8.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = "https://official1.online › posts › ${formState.effectiveSlug}",
                                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, color = Color(0xFF202124)),
                                        maxLines = 1
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = formState.title.ifBlank { "Article Headline - US News" },
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            color = Color(0xFF1A0DAB),
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 15.sp
                                        ),
                                        maxLines = 2
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = formState.autoMetaDescription.ifBlank { "Comprehensive coverage and live news analysis from US News Network." },
                                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp, color = Color(0xFF4D5156)),
                                        maxLines = 2
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }

                WordPressEditorTab.PREVIEW -> {
                    // ==========================================================
                    // TAB 3: LIVE READER PREVIEW (পাঠক যেভাবে দেখবে)
                    // ==========================================================
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            // Breaking badge
                            if (formState.isBreaking) {
                                Surface(
                                    color = NewsBreakingRed,
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = "⚡ BREAKING NEWS",
                                        color = Color.White,
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Black),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                            }

                            // Category
                            Text(
                                text = formState.category.uppercase(),
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF2271B1)
                                )
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            // Title
                            Text(
                                text = formState.title.ifBlank { "Untitled Article Headline" },
                                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            if (formState.subtitle.isNotBlank() || formState.excerpt.isNotBlank()) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = formState.subtitle.ifBlank { formState.excerpt },
                                    style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Byline
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "By ${formState.author}  •  ${formState.source}",
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Featured Image
                            if (formState.imageUrl.isNotBlank()) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                ) {
                                    AsyncImage(
                                        model = formState.imageUrl,
                                        contentDescription = "Preview Image",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                                Spacer(modifier = Modifier.height(14.dp))
                            }

                            // Content preview
                            Text(
                                text = formState.content.ifBlank { "Article body content will be rendered here..." },
                                style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 24.sp),
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Tags Preview
                            if (formState.tags.isNotBlank()) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .horizontalScroll(rememberScrollState()),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    formState.tags.split(",").map { it.trim() }.filter { it.isNotBlank() }.forEach { tag ->
                                        Surface(
                                            color = MaterialTheme.colorScheme.surfaceVariant,
                                            shape = RoundedCornerShape(12.dp)
                                        ) {
                                            Text(
                                                text = "#$tag",
                                                style = MaterialTheme.typography.labelSmall,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = onSubmit,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2271B1)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.Publish, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Everything looks good — Publish Now!",
                            color = Color.White,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
private fun WpSectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.padding(bottom = 6.dp)
    )
}

@Composable
private fun FormattingBtn(
    label: String,
    isBold: Boolean = false,
    isItalic: Boolean = false,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(4.dp),
        modifier = Modifier.height(32.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(horizontal = 10.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = if (isBold) FontWeight.Black else FontWeight.Medium,
                    fontStyle = if (isItalic) FontStyle.Italic else FontStyle.Normal
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun QuickBlockChip(label: String, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        )
    }
}
