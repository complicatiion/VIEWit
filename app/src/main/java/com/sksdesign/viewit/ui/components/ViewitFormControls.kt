package com.sksdesign.viewit.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.sksdesign.viewit.data.model.ThemeSettings
import com.sksdesign.viewit.ui.theme.ViewitTextMuted
import com.sksdesign.viewit.ui.theme.ViewitTextPrimary
import com.sksdesign.viewit.util.ColorUtils

@Composable
fun ViewitOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    minLines: Int = 1
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        minLines = minLines,
        label = { Text(label, color = ViewitTextMuted) },
        textStyle = androidx.compose.ui.text.TextStyle(color = ViewitTextPrimary),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = ViewitTextPrimary,
            unfocusedTextColor = ViewitTextPrimary,
            focusedLabelColor = ViewitTextPrimary,
            unfocusedLabelColor = ViewitTextMuted,
            cursorColor = ViewitTextPrimary,
            focusedBorderColor = ViewitTextPrimary.copy(alpha = 0.80f),
            unfocusedBorderColor = ViewitTextMuted.copy(alpha = 0.55f),
            focusedContainerColor = Color.White.copy(alpha = 0.04f),
            unfocusedContainerColor = Color.White.copy(alpha = 0.03f)
        )
    )
}

@Composable
fun ViewitSwitchRow(label: String, checked: Boolean, onChanged: (Boolean) -> Unit) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(label, color = ViewitTextPrimary, modifier = Modifier.weight(1f))
        Switch(
            checked = checked,
            onCheckedChange = onChanged,
            colors = SwitchDefaults.colors(
                checkedThumbColor = ViewitTextPrimary,
                checkedTrackColor = ViewitTextPrimary.copy(alpha = 0.35f),
                uncheckedThumbColor = ViewitTextMuted,
                uncheckedTrackColor = Color.White.copy(alpha = 0.12f),
                uncheckedBorderColor = ViewitTextMuted.copy(alpha = 0.60f)
            )
        )
    }
}

@Composable
fun ViewitSliderRow(label: String, value: Float, range: ClosedFloatingPointRange<Float>, onChanged: (Float) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text("$label: ${"%.2f".format(value)}", color = ViewitTextPrimary)
        Slider(
            value = value.coerceIn(range.start, range.endInclusive),
            onValueChange = onChanged,
            valueRange = range,
            colors = SliderDefaults.colors(
                thumbColor = ViewitTextPrimary,
                activeTrackColor = ViewitTextPrimary,
                inactiveTrackColor = ViewitTextMuted.copy(alpha = 0.35f)
            )
        )
    }
}

@Composable
fun ViewitChoiceRow(
    label: String,
    value: String,
    values: List<String>,
    themeSettings: ThemeSettings,
    icon: ImageVector? = null,
    onChanged: (String) -> Unit
) {
    val accent = ColorUtils.parseColor(themeSettings.accentColorHex)
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("$label: ${choiceLabel(value)}", color = ViewitTextPrimary)
        Column(verticalArrangement = Arrangement.spacedBy(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            values.forEach { item ->
                Button(
                    onClick = { onChanged(item) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(themeSettings.cardRadius.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (item == value) accent.copy(alpha = 0.24f) else Color.White.copy(alpha = 0.08f),
                        contentColor = ViewitTextPrimary
                    ),
                    border = BorderStroke(1.dp, if (item == value) accent.copy(alpha = 0.95f) else Color.White.copy(alpha = 0.20f))
                ) {
                    if (icon != null) {
                        androidx.compose.material3.Icon(icon, contentDescription = null, modifier = Modifier.size(17.dp))
                        androidx.compose.foundation.layout.Spacer(Modifier.size(7.dp))
                    }
                    Text(choiceLabel(item), maxLines = 2, overflow = TextOverflow.Ellipsis)
                }
            }
        }
    }
}

private fun choiceLabel(value: String): String = when (value) {
    "MOBILE" -> "Mobile"
    "TV" -> "TV"
    "compact" -> "Compact"
    "comfortable" -> "Comfortable"
    "large" -> "Large"
    "default" -> "Default"
    "no_cache" -> "No Cache"
    "cache_else_network" -> "Cache Else Network"
    "webview_back_first" -> "WebView Back First"
    "always_show_controls" -> "Always Show Controls"
    "return_home" -> "Return Home"
    else -> value.replace('_', ' ').replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
}
