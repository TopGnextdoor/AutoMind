package com.automind.ui.screens

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.automind.ui.components.GlassyButton
import com.automind.ui.theme.ElectricBlue
import com.automind.ui.theme.TextSecondary

@Composable
fun PermissionScreen(onAllGranted: () -> Unit) {
    val context = LocalContext.current
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = ElectricBlue
        )
        Text(
            text = "Permissions Required",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(
            text = "AutoMind needs these permissions to monitor distractions and keep you focused.",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
            modifier = Modifier.padding(top = 8.dp, bottom = 48.dp)
        )

        PermissionCard(
            title = "Usage Access",
            description = "Needed to track which apps are open.",
            icon = Icons.Default.Info,
            onClick = {
                context.startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        PermissionCard(
            title = "Overlay Permission",
            description = "Needed to show focus warnings.",
            icon = Icons.Default.Visibility,
            onClick = {
                context.startActivity(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION).apply {
                    data = Uri.parse("package:${context.packageName}")
                })
            }
        )

        Spacer(modifier = Modifier.height(48.dp))

        GlassyButton(
            text = "I've Granted Them",
            onClick = onAllGranted,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun PermissionCard(title: String, description: String, icon: ImageVector, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        shape = MaterialTheme.shapes.large,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = ElectricBlue)
            Column(modifier = Modifier.padding(start = 16.dp)) {
                Text(text = title, fontWeight = FontWeight.Bold)
                Text(text = description, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
            }
        }
    }
}
