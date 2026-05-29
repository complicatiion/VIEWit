package com.sksdesign.viewit.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sksdesign.viewit.ui.theme.ViewitTextMuted
import com.sksdesign.viewit.ui.theme.ViewitTextPrimary

@Composable
fun SectionHeader(title: String, subtitle: String? = null) {
    Column {
        Text(title, color = ViewitTextPrimary, fontWeight = FontWeight.Bold, fontSize = 20.sp)
        if (!subtitle.isNullOrBlank()) {
            Spacer(Modifier.height(4.dp))
            Text(subtitle, color = ViewitTextMuted, fontSize = 13.sp)
        }
    }
}
