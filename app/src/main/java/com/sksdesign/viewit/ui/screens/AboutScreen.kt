package com.sksdesign.viewit.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.OpenInBrowser
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.sksdesign.viewit.data.model.ThemeSettings
import com.sksdesign.viewit.ui.components.CyberBackground
import com.sksdesign.viewit.ui.components.GlassCard
import com.sksdesign.viewit.ui.components.GlowButton
import com.sksdesign.viewit.ui.components.SectionHeader
import com.sksdesign.viewit.ui.components.ViewitLogo
import com.sksdesign.viewit.ui.theme.ViewitTextMuted
import com.sksdesign.viewit.ui.theme.ViewitTextPrimary
import com.sksdesign.viewit.util.GitHubUtils
import com.sksdesign.viewit.util.IntentUtils

@Composable
fun AboutScreen(themeSettings: ThemeSettings, onBack: () -> Unit) {
    val context = LocalContext.current
    BackHandler { onBack() }
    CyberBackground() {
        Column(Modifier.fillMaxSize().padding(horizontal = 18.dp, vertical = 16.dp).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                ViewitLogo(40.dp, onClick = onBack)
                Spacer(Modifier.padding(6.dp))
                SectionHeader("About")
                Spacer(Modifier.weight(1f))
                GlowButton("Back", themeSettings, icon = Icons.Outlined.ArrowBack, compact = true, onClick = onBack)
            }
            GlassCard(themeSettings, Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    ViewitLogo(120.dp)
                    Text("VIEWit", color = ViewitTextPrimary)
                    Text("Version: V1.1.2", color = ViewitTextMuted)
                    Text("Package: com.sksdesign.viewit", color = ViewitTextMuted)
                    Text("Author: complicatiion aka sksdesign", color = ViewitTextMuted)
                }
            }
            GlassCard(themeSettings, Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    SectionHeader("Project")
                    GlowButton("Open GitHub", themeSettings, icon = Icons.Outlined.OpenInBrowser, modifier = Modifier.fillMaxWidth()) { IntentUtils.openUrl(context, GitHubUtils.RepositoryUrl) }
                }
            }
            GlassCard(themeSettings, Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    SectionHeader("Notices")
                    Text("Adblocking is best effort. Some dynamic web apps such as YouTube, Spotify or other streaming services may still show ads.", color = ViewitTextMuted)
                    Text("DNSforge can be configured system-wide in Android Private DNS settings. VIEWit only displays these values and does not change system DNS settings automatically.", color = ViewitTextMuted)
                }
            }
            GlassCard(themeSettings, Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    SectionHeader("Third-Party Components")
                    Text("VIEWit uses Kotlin, Android platform APIs, Android WebView, AndroidX Core, Activity Compose, Lifecycle Runtime, Jetpack Compose, Material 3 and Material Icons Extended.", color = ViewitTextMuted)
                    Text("These components are distributed under their respective open-source licenses, mainly Apache License 2.0 where applicable.", color = ViewitTextMuted)
                    Text("No AdblockPlus binary is bundled. The AdblockPlus engine is only a local integration stub in this version.", color = ViewitTextMuted)
                }
            }
            GlassCard(themeSettings, Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    SectionHeader("License")
                    Text("Project license and third-party notices are included in the repository root and docs folder.", color = ViewitTextMuted)
                    Text("Author: complicatiion aka sksdesign", color = ViewitTextMuted)
                }
            }
        }
    }
}
