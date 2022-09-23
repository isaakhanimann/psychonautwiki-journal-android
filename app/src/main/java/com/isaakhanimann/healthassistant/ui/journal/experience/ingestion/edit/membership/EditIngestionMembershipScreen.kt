package com.isaakhanimann.healthassistant.ui.journal.experience.ingestion.edit.membership

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.isaakhanimann.healthassistant.data.room.experiences.entities.Experience


@Composable
fun EditIngestionMembershipScreen(
    navigateBack: () -> Unit,
    viewModel: EditIngestionMembershipViewModel = hiltViewModel()
) {
    EditIngestionMembershipScreen(
        onTap = {
            viewModel.onTap(it)
            navigateBack()
        },
        experiences = viewModel.experiences.collectAsState().value
    )
}

@Preview
@Composable
fun EditIngestionMembershipScreenPreview(
    @PreviewParameter(ExperiencesPreviewProvider::class) experiences: List<Experience>,
) {
    EditIngestionMembershipScreen(
        onTap = {},
        experiences = experiences
    )
}

@Composable
fun EditIngestionMembershipScreen(
    onTap: (Int) -> Unit,
    experiences: List<Experience>
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Choose Experience") },
            )
        }
    ) {
        Box(contentAlignment = Alignment.Center) {
            if (experiences.isEmpty()) {
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    Text(text = "There are no experiences yet")
                }
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(experiences.size) { i ->
                    val exp = experiences[i]
                    Text(
                        text = exp.title,
                        style = MaterialTheme.typography.body1.merge(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onTap(exp.id)
                            }
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                    )
                    if (i < experiences.size) {
                        Divider()
                    }
                }
            }
        }
    }

}