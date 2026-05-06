package com.automind.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.automind.ui.components.FocusBarChart
import com.automind.ui.components.FocusDonutChart
import com.automind.ui.components.GradientCard
import com.automind.ui.theme.AccentGradient
import com.automind.ui.theme.ElectricBlue
import com.automind.ui.theme.TextSecondary
import com.automind.ui.viewmodel.AutoMindViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun InsightsScreen(viewModel: AutoMindViewModel) {
    val dangerZones by viewModel.dangerZones.collectAsState()
    val hourlyUsage by viewModel.usageByHour.collectAsState()
    val topDistractions by viewModel.topDistractions.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            Text(
                text = "Performance Insights",
                style = MaterialTheme.typography.headlineMedium
            )
        }

        item {
            GradientCard {
                Text(
                    text = "Usage Patterns",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                FocusBarChart(data = hourlyUsage)
                Text(
                    text = "Activity distribution across the day",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }

        if (topDistractions.isNotEmpty()) {
            item {
                GradientCard {
                    Text(
                        text = "Top Distractions",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FocusDonutChart(data = topDistractions, modifier = Modifier.weight(1f))
                        Column(modifier = Modifier.weight(1f).padding(start = 16.dp)) {
                            topDistractions.forEach { (pkg, duration) ->
                                Text(
                                    text = pkg.split(".").last(),
                                    style = MaterialTheme.typography.labelMedium,
                                    maxLines = 1
                                )
                                Text(
                                    text = "${duration / 60000} min",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                            }
                        }
                    }
                }
            }
        }

        if (dangerZones.isNotEmpty()) {
            item {
                GradientCard {
                    Text(
                        text = "Detected Danger Zones",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "You are most likely to get distracted during these hours:",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        dangerZones.forEach { hour ->
                            val displayHour = if (hour == 0) "12 AM" else if (hour < 12) "$hour AM" else if (hour == 12) "12 PM" else "${hour - 12} PM"
                            Text(
                                text = displayHour,
                                color = ElectricBlue,
                                style = MaterialTheme.typography.labelLarge,
                                modifier = Modifier
                                    .padding(4.dp)
                                    .background(ElectricBlue.copy(alpha = 0.1f))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }

        item {
            GradientCard {
                Text(
                    text = "Productivity Trend",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                Text(
                    text = "Your focus time increased by 15% this week. Keep it up!",
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextSecondary
                )
            }
        }
    }
}

@Composable
fun SimpleBarChart(data: List<Float>, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val barWidth = size.width / (data.size * 1.5f)
        val maxBarHeight = size.height
        val cornerRadius = 8.dp.toPx()

        data.forEachIndexed { index, value ->
            val barHeight = value * maxBarHeight
            val x = index * (barWidth * 1.5f) + barWidth / 4
            val y = size.height - barHeight

            drawRoundRect(
                brush = Brush.verticalGradient(AccentGradient),
                topLeft = Offset(x, y),
                size = Size(barWidth, barHeight),
                cornerRadius = CornerRadius(cornerRadius, cornerRadius)
            )
        }
    }
}

@Composable
fun InsightMetricCard(title: String, value: String, modifier: Modifier = Modifier) {
    GradientCard(modifier = modifier) {
        Column(horizontalAlignment = Alignment.Start) {
            Text(text = title, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp),
                color = ElectricBlue
            )
        }
    }
}
