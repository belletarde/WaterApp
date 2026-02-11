package com.compose.waterapp.ui.screen

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.compose.waterapp.R
import com.compose.waterapp.ui.theme.WaterAppTheme
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.sin

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit
) {
    var waterLevel by remember { mutableFloatStateOf(0f) }
    
    val animatedWaterLevel by animateFloatAsState(
        targetValue = waterLevel,
        animationSpec = tween(
            durationMillis = 2000,
            easing = FastOutSlowInEasing
        ),
        label = "water_fill"
    )
    
    val infiniteTransition = rememberInfiniteTransition(label = "wave")
    val waveOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2 * PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "wave_offset"
    )
    
    LaunchedEffect(Unit) {
        delay(300)
        waterLevel = 1f
        delay(2500)
        onSplashFinished()
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFE3F2FD),
                        Color(0xFFBBDEFB)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Canvas(
                modifier = Modifier
                    .width(200.dp)
                    .height(300.dp)
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
                    style = Stroke(width = 6.dp.toPx())
                )
                
                if (animatedWaterLevel > 0) {
                    clipPath(glassPath) {
                        val waterHeight = size.height * animatedWaterLevel
                        val waterTop = size.height - waterHeight
                        
                        val wavePath = Path().apply {
                            val waveHeight = 12.dp.toPx()
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
                                    Color(0xFF42A5F5).copy(alpha = 0.9f),
                                    Color(0xFF1E88E5)
                                ),
                                startY = waterTop,
                                endY = size.height
                            )
                        )
                        
                        drawPath(
                            path = wavePath,
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color.White.copy(alpha = 0.4f),
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
            
            Spacer(modifier = Modifier.height(40.dp))
            
            Text(
                text = stringResource(R.string.app_name),
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1976D2)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = stringResource(R.string.stay_hydrated),
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF1976D2).copy(alpha = 0.7f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    WaterAppTheme {
        SplashScreen(onSplashFinished = {})
    }
}
