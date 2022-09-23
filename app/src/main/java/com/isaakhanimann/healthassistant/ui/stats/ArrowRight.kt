package com.isaakhanimann.healthassistant.ui.stats

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun ArrowRight(
    modifier: Modifier = Modifier,
    strokeWidth: Float = 3f,
    fractionWhenHeadStarts: Float = 19.2f / 20f,
) {
    val color = MaterialTheme.colors.onBackground
    Canvas(
        modifier = modifier.fillMaxWidth()
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val halfHeight = canvasHeight / 2
        drawLine(
            start = Offset(x = 0f, y = halfHeight),
            end = Offset(x = canvasWidth, y = halfHeight),
            strokeWidth = strokeWidth,
            color = color,
            cap = StrokeCap.Round,
        )
        drawLine(
            start = Offset(x = canvasWidth * fractionWhenHeadStarts, y = 0f),
            end = Offset(x = canvasWidth, y = halfHeight),
            strokeWidth = strokeWidth,
            color = color,
            cap = StrokeCap.Round,
        )
        drawLine(
            start = Offset(x = canvasWidth * fractionWhenHeadStarts, y = canvasHeight),
            end = Offset(x = canvasWidth, y = halfHeight),
            strokeWidth = strokeWidth,
            color = color,
            cap = StrokeCap.Round,
        )
    }

}