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
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sksdesign.viewit.data.model.AdblockSettings
import com.sksdesign.viewit.data.model.ThemeSettings
import com.sksdesign.viewit.ui.components.CyberBackground
import com.sksdesign.viewit.ui.components.GlassCard
import com.sksdesign.viewit.ui.components.GlowButton
import com.sksdesign.viewit.ui.components.SectionHeader
import com.sksdesign.viewit.ui.components.ViewitOutlinedTextField
import com.sksdesign.viewit.ui.components.ViewitSwitchRow
import com.sksdesign.viewit.ui.theme.ViewitTextMuted

@Composable
fun AdblockSettingsScreen(
    themeSettings: ThemeSettings,
    settings: AdblockSettings,
    onSave: (AdblockSettings) -> Unit,
    onBack: () -> Unit
) {
    var rulesText by remember(settings.userRules) { mutableStateOf(settings.userRules.joinToString("\n")) }
    BackHandler { onBack() }
    CyberBackground() {
        Column(Modifier.fillMaxSize().padding(horizontal = 18.dp, vertical = 16.dp).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                SectionHeader("Adblocking")
                Spacer(Modifier.weight(1f))
                GlowButton("Back", themeSettings, icon = Icons.Outlined.ArrowBack, compact = true, onClick = onBack)
            }
            GlassCard(themeSettings, Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Adblocking is best effort. Some dynamic web apps such as YouTube, Spotify or other streaming services may still show ads.", color = ViewitTextMuted)
                    ViewitSwitchRow("Global Adblock", settings.globalEnabled) { onSave(settings.copy(globalEnabled = it)) }
                    ViewitSwitchRow("Simple Host Block Engine", settings.useSimpleHostBlockEngine) { onSave(settings.copy(useSimpleHostBlockEngine = it)) }
                    ViewitSwitchRow("AdblockPlus Engine Stub", settings.useAdblockPlusEngine) { onSave(settings.copy(useAdblockPlusEngine = it)) }
                    ViewitSwitchRow("Debug Log", settings.debugLogEnabled) { onSave(settings.copy(debugLogEnabled = it)) }
                }
            }
            GlassCard(themeSettings, Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    SectionHeader("Filter Lists")
                    settings.filterLists.forEach { list ->
                        ViewitSwitchRow(list.name, list.enabled) {
                            val updated = settings.filterLists.map { if (it.id == list.id) it.copy(enabled = !it.enabled) else it }
                            onSave(settings.copy(filterLists = updated))
                        }
                    }
                    GlowButton("Mark Updated", themeSettings, icon = Icons.Outlined.Save, modifier = Modifier.fillMaxWidth()) { onSave(settings.copy(lastUpdated = System.currentTimeMillis())) }
                }
            }
            GlassCard(themeSettings, Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    SectionHeader("User Rules", "SimpleHostBlockEngine accepts host fragments and URL fragments, not full ABP syntax.")
                    ViewitOutlinedTextField(value = rulesText, onValueChange = { rulesText = it }, modifier = Modifier.fillMaxWidth(), minLines = 8, label = "Rules")
                    GlowButton("Save Rules", themeSettings, icon = Icons.Outlined.Save, modifier = Modifier.fillMaxWidth()) {
                        onSave(settings.copy(userRules = rulesText.lines().map { it.trim() }.filter { it.isNotBlank() }.distinct()))
                    }
                }
            }
        }
    }
}
