package com.example.healthassistant.ui.search.substance.roa

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.example.healthassistant.data.substances.RoaDuration
import com.example.healthassistant.ui.previewproviders.RoaDurationPreviewProvider

@Preview(showBackground = true)
@Composable
fun DurationView(@PreviewParameter(RoaDurationPreviewProvider::class) roaDuration: RoaDuration) {
    Column {
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Text(
                text = "Duration",
                style = MaterialTheme.typography.h6,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
        Text("total: ${roaDuration.total?.text}")
        Text("after effects: ${roaDuration.afterglow?.text}")
        Row(
            modifier = Modifier.height(80.dp)
        ) {
            val strokeWidth = 6f
            val color = MaterialTheme.colors.secondary
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LineLow(
                    modifier = Modifier.weight(1f),
                    strokeWidth = strokeWidth,
                    color = color
                )
                Text(roaDuration.onset?.text ?: "")
            }
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LineUp(
                    modifier = Modifier.weight(1f),
                    strokeWidth = strokeWidth,
                    color = color
                )
                Text(roaDuration.comeup?.text ?: "")
            }
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LineHigh(
                    modifier = Modifier.weight(1f),
                    strokeWidth = strokeWidth,
                    color = color
                )
                Text(roaDuration.peak?.text ?: "")
            }
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LineDown(
                    modifier = Modifier.weight(1f),
                    strokeWidth = strokeWidth,
                    color = color
                )
                Text(roaDuration.offset?.text ?: "")
            }
        }
    }
}

@Composable
fun LineLow(
    modifier: Modifier = Modifier,
    color: Color,
    strokeWidth: Float
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        drawLine(
            start = Offset(x = 0f, y = canvasHeight),
            end = Offset(x = canvasWidth, y = canvasHeight),
            color = color,
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
    }
}

@Composable
fun LineUp(
    modifier: Modifier = Modifier,
    color: Color,
    strokeWidth: Float
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        drawLine(
            start = Offset(x = 0f, y = canvasHeight),
            end = Offset(x = canvasWidth, y = 0f),
            color = color,
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
    }
}

@Composable
fun LineHigh(
    modifier: Modifier = Modifier,
    color: Color,
    strokeWidth: Float
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val canvasWidth = size.width
        drawLine(
            start = Offset(x = 0f, y = 0f),
            end = Offset(x = canvasWidth, y = 0f),
            color = color,
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
    }
}

@Composable
fun LineDown(
    modifier: Modifier = Modifier,
    color: Color,
    strokeWidth: Float
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        drawLine(
            start = Offset(x = 0f, y = 0f),
            end = Offset(x = canvasWidth, y = canvasHeight),
            color = color,
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
    }
}
