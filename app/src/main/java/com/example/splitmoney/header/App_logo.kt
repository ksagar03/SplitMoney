package com.example.splitmoney.header

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun AnimatedSplitMoneyLogo() {
    val infiniteTransition = rememberInfiniteTransition(label = "logo animation")
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        label = "logo rotation",
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
    )


    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )
    Canvas(modifier = Modifier.size(100.dp)) {
        val width = size.width * scale
        val height = size.height * scale
        val centerX = size.width / 2
        val centerY = size.height / 2

        rotate(rotationAngle, Offset(centerX, centerY)) {
            val greenGradient = Brush.verticalGradient(listOf(Color(0xFF4cAF50), Color(0xFF81C784)))
            val blueGradient = Brush.verticalGradient(listOf(Color(0xFF2196F3), Color(0xFF64B5F6)))


            val path = Path().apply {
                moveTo(centerX, height * 0.1f)
                lineTo(centerX + width * 0.3f, height * 0.4f)
                lineTo(centerX - width * 0.3f, height * 0.4f)
                close()
                moveTo(centerX - width * 0.25f, height * 0.45f)
                lineTo(centerX + width * 0.25f, height * 0.45f)

                moveTo(centerX - width * 0.25f, height * 0.55f)
                lineTo(centerX + width * 0.25f, height * 0.55f)

            }
            drawPath(path = path, brush = greenGradient)
            drawPath(path = path, brush = blueGradient)

            drawArc(
                brush = greenGradient,
                startAngle = 180f,
                sweepAngle = 180f,
                useCenter = true,
                size= Size(width/2, height/2),
                topLeft = Offset(0f, (size.height * scale)/4)
            )
            drawArc(
                brush = blueGradient,
                startAngle = 0f,
                sweepAngle = 180f,
                useCenter = true,
                size = Size(width/2, height/2),
                topLeft = Offset(size.width/2, (size.height * scale)/4)
            )

        }
    }
}

//@Preview
//@Composable
//fun AnimatedScreen(){
//    AnimatedSplitMoneyLogo()
//}