package com.example.splitmoney.header

import androidx.compose.animation.core.FastOutSlowInEasing
//import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
//import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
//import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
//import androidx.compose.ui.geometry.Offset
//import androidx.compose.ui.geometry.Size
//import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.Path
//import androidx.compose.ui.graphics.StrokeCap
//import androidx.compose.ui.graphics.StrokeJoin
//import androidx.compose.ui.graphics.drawscope.Stroke
//import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

//import kotlin.math.cos
//import kotlin.math.sin

@Composable
fun SplitMoneyLogoAnimation() {
    val infiniteTransition = rememberInfiniteTransition(label = "logo animation")
    val offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 18f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

//    Box(
//        modifier = Modifier
//            .size(20.dp, 36.dp)
//            .offset(x = (-offset).dp)
//            .clip(RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp))
//            .background(Color(0xFF4CAF50))
//    )
//    Box(
//        modifier = Modifier
//            .size(20.dp, 36.dp)
//            .offset(x = offset.dp)
//            .clip(RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp))
//            .background(Color(0xFF2196F3))
//    )

    MoneyNote(
        modifier = Modifier
            .size(40.dp, 24.dp)
            .offset(x = (-offset).dp)
            .rotate(-10f),
        currencySymbol = "₹",
        color = Color(0xFF4CAF50)
    )

    MoneyNote(
        modifier = Modifier
            .size(40.dp, 24.dp)
            .offset(x = offset.dp)
            .rotate(10f),
        currencySymbol = "$",
        color = Color(0xFF2196F3)
    )
}


@Composable
fun MoneyNote(modifier: Modifier = Modifier, currencySymbol: String, color: Color) {
    Box(
        modifier = modifier
            .background(color.copy(alpha = 0.9f))
            .border(1.dp, color.copy(alpha = 0.7f))
    ) {
        Text(
            text = currencySymbol,
            color = Color.White,
            modifier = Modifier
                .align(Alignment.Center),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }
}

//@Composable
//fun AnimatedSplitMoneyLogo() {
//    val infiniteTransition = rememberInfiniteTransition(label = "logo animation")
//    val rotationAngle by infiniteTransition.animateFloat(
//        initialValue = 0f,
//        targetValue = 360f,
//        label = "logo rotation",
//        animationSpec = infiniteRepeatable(
//            animation = tween(durationMillis = 3000, easing = LinearEasing),
//            repeatMode = RepeatMode.Restart
//        ),
//    )
//
//
//    val scale by infiniteTransition.animateFloat(
//        initialValue = 1f,
//        targetValue = 1.1f,
//        animationSpec = infiniteRepeatable(
//            animation = tween(durationMillis = 1500, easing = LinearEasing),
//            repeatMode = RepeatMode.Reverse
//        ),
//        label = "scale"
//    )
//    Canvas(modifier = Modifier.size(100.dp)) {
//        val width = size.width * scale
//        val height = size.height * scale
//        val centerX = size.width / 2
//        val centerY = size.height / 2
//
//        rotate(rotationAngle, Offset(centerX, centerY)) {
//            val greenGradient = Brush.verticalGradient(listOf(Color(0xFF4cAF50), Color(0xFF81C784)))
//            val blueGradient = Brush.verticalGradient(listOf(Color(0xFF2196F3), Color(0xFF64B5F6)))
//
//
//            val path = Path().apply {
//                moveTo(centerX, height * 0.1f)
//                lineTo(centerX + width * 0.3f, height * 0.4f)
//                lineTo(centerX - width * 0.3f, height * 0.4f)
//                close()
//                moveTo(centerX - width * 0.25f, height * 0.45f)
//                lineTo(centerX + width * 0.25f, height * 0.45f)
//
//                moveTo(centerX - width * 0.25f, height * 0.55f)
//                lineTo(centerX + width * 0.25f, height * 0.55f)
//
//            }
//            drawPath(path = path, brush = greenGradient)
//            drawPath(path = path, brush = blueGradient)
//
//            drawArc(
//                brush = greenGradient,
//                startAngle = 180f,
//                sweepAngle = 180f,
//                useCenter = true,
//                size= Size(width/2, height/2),
//                topLeft = Offset(0f, (size.height * scale)/4)
//            )
//            drawArc(
//                brush = blueGradient,
//                startAngle = 0f,
//                sweepAngle = 180f,
//                useCenter = true,
//                size = Size(width/2, height/2),
//                topLeft = Offset(size.width/2, (size.height * scale)/4)
//            )
//
//        }
//    }
//}


//@Preview
//@Composable
//fun AnimatedScreen(){
//    AnimatedSplitMoneyLogo()
//}