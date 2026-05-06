package com.automind.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.automind.ui.theme.AccentGradient
import com.automind.ui.theme.ElectricBlue
import com.automind.ui.theme.TextSecondary
import com.automind.ui.viewmodel.AutoMindViewModel

@Composable
fun AutopilotScreen(viewModel: AutoMindViewModel) {
    val isEnabled by viewModel.isAutopilotEnabled.collectAsState(initial = false)
    val score by viewModel.disciplineScore.collectAsState(initial = 100)
    val logs by viewModel.autopilotLogs.collectAsState(initial = emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Autopilot Mode",
            style = MaterialTheme.typography.headlineMedium
        )
        
        Spacer(modifier = Modifier.height(32.dp))

        // Animated Brain / Pulse
        AutopilotPulse(isEnabled)

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = "System Status", style = MaterialTheme.typography.titleLarge)
                Text(
                    text = if (isEnabled) "Active & Learning" else "Manual Control",
                    color = if (isEnabled) ElectricBlue else TextSecondary,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Switch(
                checked = isEnabled,
                onCheckedChange = { viewModel.toggleAutopilot(it) },
                colors = SwitchDefaults.colors(checkedThumbColor = ElectricBlue)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Discipline Score
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(text = "Discipline Score: $score%", style = MaterialTheme.typography.labelLarge)
            LinearProgressIndicator(
                progress = score / 100f,
                modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape),
                color = ElectricBlue,
                trackColor = Color.White.copy(alpha = 0.1f)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Recent Actions",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.align(Alignment.Start).padding(bottom = 12.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxWidth().weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(logs) { log ->
                AutopilotLogItem(log)
            }
        }
    }
}

@Composable
fun AutopilotPulse(enabled: Boolean) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (enabled) 1.2f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Box(
        modifier = Modifier
            .size(160.dp)
            .scale(scale)
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    colors = if (enabled) AccentGradient else listOf(Color.Gray, Color.DarkGray)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (enabled) "ON" else "OFF",
            color = Color.White,
            style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.Black)
        )
    }
}

@Composable
fun AutopilotLogItem(log: com.automind.data.model.AutopilotLogEntity) {
    Surface(
        color = Color.White.copy(alpha = 0.05f),
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = log.action, style = MaterialTheme.typography.labelLarge, color = ElectricBlue)
            Text(text = log.reason, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
        }
    }
}
