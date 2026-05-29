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
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.OpenInNew
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.sksdesign.viewit.data.model.PrivacyDnsProfile
import com.sksdesign.viewit.data.model.ThemeSettings
import com.sksdesign.viewit.ui.components.CyberBackground
import com.sksdesign.viewit.ui.components.GlassCard
import com.sksdesign.viewit.ui.components.GlowButton
import com.sksdesign.viewit.ui.components.SectionHeader
import com.sksdesign.viewit.ui.components.ViewitOutlinedTextField
import com.sksdesign.viewit.ui.theme.ViewitTextMuted
import com.sksdesign.viewit.ui.theme.ViewitTextPrimary
import com.sksdesign.viewit.util.ClipboardUtils
import com.sksdesign.viewit.util.IntentUtils

@Composable
fun PrivacyDnsScreen(
    themeSettings: ThemeSettings,
    profiles: List<PrivacyDnsProfile>,
    selectedProfileId: String,
    customDnsValues: String,
    onSelectProfile: (String) -> Unit,
    onSaveCustomDns: (String) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var customDns by remember(customDnsValues) { mutableStateOf(customDnsValues) }
    BackHandler { onBack() }
    CyberBackground() {
        Column(Modifier.fillMaxSize().padding(horizontal = 18.dp, vertical = 16.dp).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                SectionHeader("Privacy DNS")
                Spacer(Modifier.weight(1f))
                GlowButton("Back", themeSettings, icon = Icons.Outlined.ArrowBack, compact = true, onClick = onBack)
            }
            GlassCard(themeSettings, Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("DNSforge can be configured system-wide in Android Private DNS settings. VIEWit only displays these values and does not change system DNS settings automatically.", color = ViewitTextMuted)
                    GlowButton("Android DNS Settings", themeSettings, icon = Icons.Outlined.OpenInNew, modifier = Modifier.fillMaxWidth()) { IntentUtils.openPrivateDnsSettings(context) }
                }
            }
            profiles.forEach { profile ->
                GlassCard(themeSettings, Modifier.fillMaxWidth()) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = selectedProfileId == profile.id,
                                onClick = { onSelectProfile(profile.id) },
                                colors = RadioButtonDefaults.colors(selectedColor = ViewitTextPrimary, unselectedColor = ViewitTextMuted)
                            )
                            SectionHeader(profile.name, profile.description)
                        }
                        if (profile.ipv4.isNotEmpty()) Text("IPv4: ${profile.ipv4.joinToString(", ")}", color = ViewitTextPrimary)
                        if (profile.ipv6.isNotEmpty()) Text("IPv6: ${profile.ipv6.joinToString(", ")}", color = ViewitTextPrimary)
                        if (profile.dot.isNotBlank()) Text("DoT: ${profile.dot}", color = ViewitTextPrimary)
                        if (profile.doh.isNotBlank()) Text("DoH: ${profile.doh}", color = ViewitTextPrimary)
                        GlowButton("Copy Values", themeSettings, icon = Icons.Outlined.ContentCopy, modifier = Modifier.fillMaxWidth()) { ClipboardUtils.copy(context, profile.name, profile.copyText()) }
                    }
                }
            }
            GlassCard(themeSettings, Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    SectionHeader("Custom DNS Servers", "Store your own DNS values as notes. VIEWit does not apply them automatically.")
                    ViewitOutlinedTextField(value = customDns, onValueChange = { customDns = it }, modifier = Modifier.fillMaxWidth(), minLines = 6, label = "Custom DNS Values")
                    GlowButton("Save Custom DNS", themeSettings, icon = Icons.Outlined.Save, modifier = Modifier.fillMaxWidth()) { onSaveCustomDns(customDns) }
                    GlowButton("Copy Custom DNS", themeSettings, icon = Icons.Outlined.ContentCopy, modifier = Modifier.fillMaxWidth()) { ClipboardUtils.copy(context, "VIEWit Custom DNS", customDns) }
                }
            }
        }
    }
}
