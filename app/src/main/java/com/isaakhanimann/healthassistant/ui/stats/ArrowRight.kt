package com.isaakhanimann.healthassistant.ui.stats

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun ArrowRight(modifier: Modifier = Modifier.width(200.dp).height(10.dp)) {
    val color = MaterialTheme.colors.onBackground
    val strokeWidth = 3f
    val fraction = 19f/20f
    Canvas(
        modifier = modifier.fillMaxWidth()
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val halfHeight = canvasHeight/2
        drawLine(
            start = Offset(x = 0f, y = halfHeight),
            end = Offset(x = canvasWidth, y = halfHeight),
            strokeWidth = strokeWidth,
            color = color,
            cap = StrokeCap.Round,
        )
        drawLine(
            start = Offset(x = canvasWidth * fraction, y = 0f),
            end = Offset(x = canvasWidth, y = halfHeight),
            strokeWidth = strokeWidth,
            color = color,
            cap = StrokeCap.Round,
        )
        drawLine(
            start = Offset(x = canvasWidth * fraction, y = canvasHeight),
            end = Offset(x = canvasWidth, y = halfHeight),
            strokeWidth = strokeWidth,
            color = color,
            cap = StrokeCap.Round,
        )
    }

}