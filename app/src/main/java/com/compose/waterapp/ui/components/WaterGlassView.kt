package com.compose.waterapp.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.sin

@Composable
fun WaterGlassView(
    currentVolume: Int,
    maxVolume: Int,
    @Suppress("UNUSED_PARAMETER") onVolumeChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val targetProgress = (currentVolume.toFloat() / maxVolume).coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "water_progress"
    )
    
    val infiniteTransition = rememberInfiniteTransition(label = "wave")
    val waveOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2 * PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "wave_offset"
    )
    
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .width(160.dp)
                .height(240.dp)
        ) {
            val glassPath = Path().apply {
                val topWidth = size.width * 0.85f
                val bottomWidth = size.width * 0.65f
                val height = size.height
                
                moveTo((size.width - topWidth) / 2, 0f)
                lineTo((size.width + topWidth) / 2, 0f)
                lineTo((size.width + bottomWidth) / 2, height)
                lineTo((size.width - bottomWidth) / 2, height)
                close()
            }
            
            drawPath(
                path = glassPath,
                color = Color(0xFFE0E0E0),
                style = Stroke(width = 4.dp.toPx())
            )
            
            if (animatedProgress > 0) {
                clipPath(glassPath) {
                    val waterHeight = size.height * animatedProgress
                    val waterTop = size.height - waterHeight
                    
                    val wavePath = Path().apply {
                        val waveHeight = 8.dp.toPx()
                        val waveLength = size.width / 2
                        
                        moveTo(0f, waterTop)
                        
                        for (x in 0..size.width.toInt() step 5) {
                            val y = waterTop + sin((x / waveLength * 2 * PI + waveOffset).toFloat()) * waveHeight
                            lineTo(x.toFloat(), y)
                        }
                        
                        lineTo(size.width, size.height)
                        lineTo(0f, size.height)
                        close()
                    }
                    
                    drawPath(
                        path = wavePath,
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF42A5F5).copy(alpha = 0.8f),
                                Color(0xFF1E88E5).copy(alpha = 0.9f)
                            ),
                            startY = waterTop,
                            endY = size.height
                        )
                    )
                    
                    drawPath(
                        path = wavePath,
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.3f),
                                Color.Transparent,
                                Color.Transparent
                            ),
                            startX = 0f,
                            endX = size.width * 0.3f
                        )
                    )
                }
            }
        }
        
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.offset(y = (-30).dp)
        ) {
            Text(
                text = "$currentVolume",
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
                color = if (animatedProgress > 0.5f) Color.White else MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "ml",
                style = MaterialTheme.typography.titleMedium,
                color = if (animatedProgress > 0.5f) Color.White.copy(alpha = 0.9f) else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
