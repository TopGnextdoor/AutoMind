package com.automind.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.automind.ui.theme.TextSecondary

@Composable
fun SettingsScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(
                text = "Settings",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        item { SettingHeader("Preferences") }
        item { SettingItem("Notifications", Icons.Default.Notifications) }
        item { SettingItem("Dark Mode", Icons.Default.DarkMode) }
        item { SettingItem("Language", Icons.Default.Language) }

        item { Spacer(modifier = Modifier.height(16.dp)) }

        item { SettingHeader("Security") }
        item { SettingItem("Privacy Lock", Icons.Default.Lock) }
        item { SettingItem("Data Export", Icons.Default.Storage) }

        item { Spacer(modifier = Modifier.height(16.dp)) }

        item { SettingHeader("About") }
        item { SettingItem("AutoMind Version", Icons.Default.Info, "v1.0.0") }
        item { SettingItem("Support", Icons.Default.SupportAgent) }
    }
}

@Composable
fun SettingHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(vertical = 12.dp)
    )
}

@Composable
fun SettingItem(title: String, icon: ImageVector, subtitle: String? = null) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = TextSecondary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = title, style = MaterialTheme.typography.bodyLarge)
                if (subtitle != null) {
                    Text(text = subtitle, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                }
            }
        }
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = TextSecondary
        )
    }
}
