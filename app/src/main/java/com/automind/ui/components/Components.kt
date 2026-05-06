package com.automind.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.automind.ui.theme.AccentGradient
import com.automind.ui.theme.SurfaceDark
import com.automind.ui.theme.TextPrimary
import com.automind.ui.theme.TextSecondary

@Composable
fun GradientCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(16.dp, RoundedCornerShape(24.dp), ambientColor = Color.Black, spotColor = Color.Black),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark)
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
        ) {
            content()
        }
    }
}

@Composable
fun GlassyButton(
    text: String,
    icon: ImageVector? = null,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isPrimary: Boolean = true
) {
    val gradientBrush = Brush.linearGradient(colors = AccentGradient)
    
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .then(
                if (isPrimary) Modifier.background(gradientBrush)
                else Modifier
                    .background(Color.White.copy(alpha = 0.05f))
                    .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(16.dp))
            )
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = TextPrimary,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text,
                color = TextPrimary,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            )
        }
    }
}

@Composable
fun FocusScoreIndicator(score: Int) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(120.dp)) {
        CircularProgressIndicator(
            progress = score / 100f,
            modifier = Modifier.fillMaxSize(),
            strokeWidth = 8.dp,
            color = AccentGradient[0], // Use one of the gradient colors or a brush if possible in newer Compose
            trackColor = Color.White.copy(alpha = 0.1f)
        )
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "$score",
                style = MaterialTheme.typography.displayLarge.copy(fontSize = 36.sp),
                color = TextPrimary
            )
            Text(
                text = "Score",
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary
            )
        }
    }
}
