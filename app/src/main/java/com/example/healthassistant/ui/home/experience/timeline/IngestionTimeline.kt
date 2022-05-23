package com.example.healthassistant.ui.home.experience.timeline

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.healthassistant.data.experiences.entities.Ingestion
import com.example.healthassistant.data.substances.RoaDuration
import com.example.healthassistant.ui.previewproviders.IngestionTimelineProvider


@Composable
fun IngestionTimeline(
    ingestion: Ingestion,
    modifier: Modifier
) {
    val viewModel: IngestionTimelineViewModel = hiltViewModel()
    IngestionTimelineContent(ingestion = ingestion, roaDuration = viewModel.roaDuration, modifier = modifier)
}

@Preview(showBackground = true)
@Composable
fun IngestionTimelineContentPreview(
    @PreviewParameter(IngestionTimelineProvider::class, limit = 1) ingestionAndDuration: Pair<Ingestion, RoaDuration>,
) {
    IngestionTimelineContent(ingestion = ingestionAndDuration.first,roaDuration = ingestionAndDuration.second, modifier = Modifier
        .width(400.dp)
        .height(200.dp))
}

@Composable
fun IngestionTimelineContent(
    ingestion: Ingestion,
    roaDuration: RoaDuration?,
    modifier: Modifier = Modifier
) {
    Column() {
        Text(text = ingestion.substanceName)
        Text(text = roaDuration?.total?.text ?: "Nothing")
    }
//    val isDarkTheme = isSystemInDarkTheme()
//    Canvas(modifier = modifier.fillMaxSize()) {
//        val canvasWidth = size.width
//        val canvasHeight = size.height
//        val strokePath = Path().apply {
//            val control1X = canvasWidth / 10f
//            val control2X = canvasWidth * 9f / 10f
//            val control2Y = 0f
//            moveTo(0f, canvasHeight)
//            cubicTo(control1X, canvasHeight, control2X, control2Y, canvasWidth, 0f)
//        }
//        val fillPath = android.graphics.Path(strokePath.asAndroidPath())
//            .asComposePath()
//            .apply {
//                lineTo(canvasWidth, canvasHeight)
//                lineTo(0f, canvasHeight)
//                close()
//            }
//        val color = ingestion.color.getComposeColor(isDarkTheme)
//        drawPath(
//            path = fillPath,
//            brush = Brush.verticalGradient(
//                colors = listOf(
//                    color,
//                    Color.Transparent
//                ),
//                endY = canvasHeight
//            )
//        )
//        drawPath(
//            path = strokePath,
//            color = color,
//            style = Stroke(
//                width = 5f,
//                cap = StrokeCap.Round
//            )
//        )
//    }
}