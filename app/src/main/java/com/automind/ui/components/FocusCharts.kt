package com.automind.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.automind.ui.theme.AccentGradient
import com.automind.ui.theme.ElectricBlue

@Composable
fun FocusBarChart(data: Map<Int, Long>, modifier: Modifier = Modifier) {
    val animatedProgress = remember { Animatable(0f) }
    LaunchedEffect(data) {
        animatedProgress.animateTo(1f, animationSpec = tween(1000))
    }

    val maxVal = (data.values.maxOrNull() ?: 1L).coerceAtLeast(1L)
    
    Canvas(modifier = modifier.height(200.dp).fillMaxWidth()) {
        val width = size.width
        val height = size.height
        val barWidth = width / 24f
        val spacing = 4f

        for (hour in 0..23) {
            val duration = data[hour] ?: 0L
            val barHeight = (duration.toFloat() / maxVal) * height * animatedProgress.value
            
            drawRoundRect(
                brush = Brush.verticalGradient(AccentGradient),
                topLeft = Offset(hour * barWidth + spacing, height - barHeight),
                size = Size(barWidth - spacing * 2, barHeight),
                cornerRadius = CornerRadius(4.dp.toPx())
            )
        }
    }
}

@Composable
fun FocusDonutChart(data: List<Pair<String, Long>>, modifier: Modifier = Modifier) {
    val animatedProgress = remember { Animatable(0f) }
    LaunchedEffect(data) {
        animatedProgress.animateTo(1f, animationSpec = tween(1200))
    }

    Canvas(modifier = modifier.size(200.dp)) {
        val total = data.sumOf { it.second }.coerceAtLeast(1L)
        var startAngle = -90f
        val colors = listOf(ElectricBlue, Color(0xFF9D50BB), Color(0xFF6E48AA), Color(0xFF240b36))

        data.forEachIndexed { index, item ->
            val sweepAngle = (item.second.toFloat() / total) * 360f * animatedProgress.value
            drawArc(
                color = colors.getOrElse(index) { Color.Gray },
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 40f)
            )
            startAngle += sweepAngle
        }
    }
}
