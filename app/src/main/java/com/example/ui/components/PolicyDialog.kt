package com.example.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PolicyDialog(
    policyType: String,
    onDismiss: () -> Unit
) {
    val (title, content) = when (policyType) {
        "privacy" -> Pair(
            "Privacy Policy (AdSense Compliant)",
            """Last updated: 2026

US News ('we', 'our', or 'us') is committed to protecting your privacy. This Privacy Policy explains how information is collected, used, and disclosed when you visit our website or use our mobile application.

1. Information We Collect
We collect minimal information necessary to deliver quality news reporting, including device identifiers, language preferences, and anonymized analytics.

2. Third-Party Advertising (Google AdSense & AdMob)
We use third-party advertising companies, including Google LLC, to serve ads when you visit our platforms. These companies may use cookies, web beacons, and device identifiers (such as the Google Advertising ID) to serve personalized ads based on prior visits to our site or other websites.
Users may opt out of personalized advertising by visiting Google Ads Settings (https://www.google.com/settings/ads).

3. GDPR & CCPA Compliance
Under the California Consumer Privacy Act (CCPA) and General Data Protection Regulation (GDPR), users have the right to request access to or deletion of their personal data. We do not sell user personal information to data brokers.

4. Contact Our Data Protection Officer
For any privacy-related questions, contact privacy@usnews-network.com."""
        )
        "terms" -> Pair(
            "Terms of Service",
            """1. Acceptance of Terms
By accessing or using US News digital properties (website, mobile application, RSS feeds), you agree to be bound by these Terms of Service and all applicable federal and state laws.

2. Intellectual Property & Fair Use
All original news reports, investigative reporting, photographs, and logos published on this platform are protected by United States and international copyright law. Content may be shared via official share links for non-commercial discussion. Unauthorized bulk scraping or redistribution is strictly prohibited.

3. Editorial Independence
Our editorial opinions are strictly independent of advertisers. Sponsored content is clearly and transparently demarcated as 'Sponsored' or 'Advertisement'.

4. User Comments & Community Standards
Defamatory, hateful, or harassing user submissions will be removed promptly by editorial moderators."""
        )
        "about" -> Pair(
            "About Our US Newsroom",
            """US News is an independent American digital news and journalism platform headquartered in Washington, D.C. 

Our Mission:
To deliver timely, factual, and balanced news reporting on United States politics, national economy, breakthrough technological developments, science, and cultural affairs.

Editorial Standards:
- Rigorous Multi-Source Fact Checking: Every breaking story undergoes verification with primary records, official press releases, and on-the-record witnesses.
- Non-Partisan Coverage: We provide multiple perspectives on legislative bills, judicial rulings, and economic forecasts without ideological bias.
- Transparent Corrections: Should any factual error occur, a prominent correction notice is appended to the article promptly.

Editorial Board:
- Executive Editor: Eleanor Vance
- Managing Bureau Chief: Marcus Sterling
- Chief Technology Reporter: Sarah Lin"""
        )
        "contact" -> Pair(
            "Contact & Editorial Bureau",
            """Editorial Office:
US News National Press Bureau
1200 Pennsylvania Avenue NW
Washington, D.C. 20004, United States

Email Inquiries:
- News Tips & Press Releases: tips@usnews-network.com
- Editorial Corrections: editor@usnews-network.com
- Advertising & Sponsorship: ads@usnews-network.com
- Legal & Compliance: legal@usnews-network.com

Phone & Media Hotline:
+1 (202) 555-0199 (Mon–Fri, 8 AM – 6 PM EST)"""
        )
        else -> Pair("Information", "")
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 420.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = content,
                    style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 20.sp),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.testTag("close_policy_dialog")
            ) {
                Text("Close", fontWeight = FontWeight.Bold)
            }
        }
    )
}
