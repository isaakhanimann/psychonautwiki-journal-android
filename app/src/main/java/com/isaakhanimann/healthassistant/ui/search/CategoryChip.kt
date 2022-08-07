package com.isaakhanimann.healthassistant.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun CategoryChipPreview(
    @PreviewParameter(CategoryChipPreviewProvider::class) categoryChipModel: CategoryChipModel
) {
    CategoryChip(
        categoryChipModel = categoryChipModel,
    )
}

@Composable
fun CategoryChip(
    categoryChipModel: CategoryChipModel,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(shape = CircleShape)
            .background(color = categoryChipModel.color.copy(alpha = 0.2f))
            .then(modifier)
            .padding(vertical = 2.dp, horizontal = 8.dp)

    ) {
        if (categoryChipModel.isActive) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "CheckMark",
                Modifier.size(ButtonDefaults.IconSize)
            )
            Spacer(Modifier.width(3.dp))
        }
        Text(text = categoryChipModel.chipName)
    }
}