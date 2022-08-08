package com.isaakhanimann.healthassistant.ui.search.substancerow

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.isaakhanimann.healthassistant.ui.search.CategoryModel
import com.isaakhanimann.healthassistant.ui.search.SubstanceModel

@Preview(showBackground = true)
@Composable
fun SubstanceRowPreview(
    @PreviewParameter(SubstanceModelPreviewProvider::class) substanceModel: SubstanceModel
) {
    SubstanceRow(substanceModel = substanceModel, onTap = {})
}

@Composable
fun SubstanceRow(
    substanceModel: SubstanceModel,
    onTap: (substanceName: String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onTap(substanceModel.name)
            }
            .padding(horizontal = 6.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = substanceModel.name,
                style = MaterialTheme.typography.body1,
            )
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Column(horizontalAlignment = Alignment.Start) {
                    substanceModel.commonNames.forEach { commonName ->
                        Text(text = commonName, style = MaterialTheme.typography.caption)
                    }
                }
            }
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(3.dp),
            horizontalAlignment = Alignment.End
        ) {
            substanceModel.categories.forEach {
                CategoryChipStatic(categoryModel = it)
            }
        }
    }
}

@Composable
fun CategoryChipStatic(categoryModel: CategoryModel) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(shape = CircleShape)
            .background(color = categoryModel.color.copy(alpha = 0.2f))
            .padding(vertical = 2.dp, horizontal = 8.dp)

    ) {
        Text(text = categoryModel.name, style = MaterialTheme.typography.caption)
    }
}