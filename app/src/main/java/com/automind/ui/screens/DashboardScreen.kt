package com.automind.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.automind.ui.components.FocusScoreIndicator
import com.automind.ui.components.GlassyButton
import com.automind.ui.components.GradientCard
import com.automind.ui.theme.TextSecondary
import com.automind.ui.viewmodel.AutoMindViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import com.automind.data.model.SuggestionEntity
import com.automind.ui.theme.ElectricBlue

@Composable
fun DashboardScreen(viewModel: AutoMindViewModel) {
    val usageMap by viewModel.todayUsage.observeAsState(initial = emptyMap())
    val suggestions by viewModel.pendingSuggestions.collectAsState()
    val score by viewModel.disciplineScore.collectAsState(initial = 100)
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            Column {
                Text(
                    text = "Good Evening,",
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextSecondary
                )
                Text(
                    text = "Divvyansh",
                    style = MaterialTheme.typography.displayLarge.copy(fontSize = 32.sp)
                )
            }
        }

        item {
            GradientCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Focus Score",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = if (score > 80) "You're doing great!" else "Stay focused!",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    FocusScoreIndicator(score = score)
                }
            }
        }

        if (suggestions.isNotEmpty()) {
            item {
                Text(
                    text = "Smart Suggestions",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                androidx.compose.foundation.lazy.LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(suggestions) { suggestion ->
                        SuggestionCard(
                            suggestion = suggestion,
                            onAccept = { viewModel.acceptSuggestion(it) },
                            onReject = { viewModel.rejectSuggestion(it) }
                        )
                    }
                }
            }
        }

        item {
            Column {
                Text(
                    text = "Quick Actions",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    GlassyButton(
                        text = "Start Focus",
                        icon = Icons.Default.PlayArrow,
                        onClick = {},
                        modifier = Modifier.weight(1f)
                    )
                    GlassyButton(
                        text = "Autopilot",
                        icon = Icons.Default.AutoAwesome,
                        onClick = {},
                        modifier = Modifier.weight(1f),
                        isPrimary = false
                    )
                }
            }
        }

        item {
            Text(
                text = "Today's Usage",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        val maxUsage = usageMap.values.maxOrNull()?.toFloat() ?: 1f
        val usageItems = usageMap.entries.sortedByDescending { it.value }.take(5).map {
            val hours = it.value / (1000 * 60 * 60)
            val minutes = (it.value / (1000 * 60)) % 60
            UsageItem(it.key.split(".").last(), "${hours}h ${minutes}m", (it.value.toFloat() / maxUsage).coerceIn(0f, 1f))
        }

        items(usageItems) { item ->
            UsageRow(item)
        }
    }
}

@Composable
fun SuggestionCard(
    suggestion: SuggestionEntity,
    onAccept: (SuggestionEntity) -> Unit,
    onReject: (SuggestionEntity) -> Unit
) {
    Card(
        modifier = Modifier
            .width(280.dp)
            .shadow(8.dp, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = com.automind.ui.theme.SurfaceDark)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Habit Detected",
                style = MaterialTheme.typography.labelSmall,
                color = ElectricBlue
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = suggestion.message,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 2
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                GlassyButton(
                    text = "Accept",
                    onClick = { onAccept(suggestion) },
                    modifier = Modifier.weight(1f)
                )
                GlassyButton(
                    text = "Ignore",
                    onClick = { onReject(suggestion) },
                    modifier = Modifier.weight(1f),
                    isPrimary = false
                )
            }
        }
    }
}

data class UsageItem(val name: String, val time: String, val percent: Float)

@Composable
fun UsageRow(item: UsageItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = item.name, style = MaterialTheme.typography.bodyLarge)
            Text(text = item.time, style = MaterialTheme.typography.bodyMedium)
        }
        // Simplified progress bar for usage
        androidx.compose.material3.LinearProgressIndicator(
            progress = item.percent,
            modifier = Modifier.width(100.dp),
            trackColor = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.1f)
        )
    }
}
