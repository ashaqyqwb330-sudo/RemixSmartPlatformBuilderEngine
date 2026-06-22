package com.example

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.unit.dp
import com.example.ui.theme.MetallicGold
import com.example.ui.theme.SlateBg
import kotlin.math.sin

/**
 * Animated Wave Background that draws fluid, organic, and slow-moving waves 
 * in the background using sine wave equations.
 */
@Composable
fun WaveBackground(
    modifier: Modifier = Modifier,
    waveColor: Color = MetallicGold
) {
    val infiniteTransition = rememberInfiniteTransition(label = "wave_transition")
    
    // Animate phases for different wave layers to make them look distinct and rich
    val phase1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2f * Math.PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase_1"
    )

    val phase2 by infiniteTransition.animateFloat(
        initialValue = 2f * Math.PI.toFloat(),
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(15000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase_2"
    )

    val phase3 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2f * Math.PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(18000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase_3"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        clipRect {
            val width = size.width
            val height = size.height

            // Base deep background color
            drawRect(color = SlateBg)

            // Layer 1: Bottom Slow Deep Wave
            val path1 = Path().apply {
                moveTo(0f, height)
                for (x in 0..width.toInt() step 10) {
                    val angle = (x.toFloat() / width) * 2f * Math.PI.toFloat() + phase1
                    val y = height - 120.dp.toPx() + sin(angle) * 30.dp.toPx()
                    lineTo(x.toFloat(), y)
                }
                lineTo(width, height)
                close()
            }
            drawPath(
                path = path1,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        waveColor.copy(alpha = 0.0f),
                        waveColor.copy(alpha = 0.05f)
                    ),
                    startY = height - 180.dp.toPx(),
                    endY = height
                )
            )

            // Layer 2: Middle Overlapping Wave
            val path2 = Path().apply {
                moveTo(0f, height)
                for (x in 0..width.toInt() step 10) {
                    val angle = (x.toFloat() / width) * 3f * Math.PI.toFloat() + phase2
                    val y = height - 100.dp.toPx() + sin(angle) * 24.dp.toPx()
                    lineTo(x.toFloat(), y)
                }
                lineTo(width, height)
                close()
            }
            drawPath(
                path = path2,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        waveColor.copy(alpha = 0.0f),
                        waveColor.copy(alpha = 0.08f)
                    ),
                    startY = height - 140.dp.toPx(),
                    endY = height
                )
            )

            // Layer 3: Subtle top wave in corner
            val path3 = Path().apply {
                moveTo(0f, 0f)
                for (x in 0..width.toInt() step 10) {
                    val angle = (x.toFloat() / width) * 1.5f * Math.PI.toFloat() + phase3
                    val y = 60.dp.toPx() + sin(angle) * 15.dp.toPx()
                    lineTo(x.toFloat(), y)
                }
                lineTo(width, 0f)
                close()
            }
            drawPath(
                path = path3,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        waveColor.copy(alpha = 0.03f),
                        waveColor.copy(alpha = 0.0f)
                    ),
                    startY = 0f,
                    endY = 100.dp.toPx()
                )
            )
        }
    }
}

/**
 * Modifier to apply an eye-catching breathing/pulsing golden glow outline.
 * Ideal for premium components and highlight states.
 */
fun Modifier.glowEffect(
    enabled: Boolean = true,
    glowColor: Color = MetallicGold,
    maxAlpha: Float = 0.35f
): Modifier = composed {
    if (!enabled) return@composed this

    val infiniteTransition = rememberInfiniteTransition(label = "glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.05f,
        targetValue = maxAlpha,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_alpha"
    )

    val scaleFactor by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.01f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale_glow"
    )

    this.drawWithContent {
        drawContent()
        // Draw elegant glowing outer halo
        drawCircle(
            color = glowColor.copy(alpha = glowAlpha),
            radius = (size.maxDimension / 2f) * scaleFactor,
            center = Offset(size.width / 2f, size.height / 2f),
            alpha = glowAlpha
        )
    }
}
