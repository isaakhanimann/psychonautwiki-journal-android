package com.example.healthassistant.ui.ingestions.ingestion.edit.membership

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.healthassistant.data.room.experiences.entities.Experience
import com.example.healthassistant.ui.previewproviders.ExperiencesPreviewProvider


@Composable
fun EditIngestionMembershipScreen(
    navigateBack: () -> Unit,
    viewModel: EditIngestionMembershipViewModel = hiltViewModel()
) {
    EditIngestionMembershipScreen(
        onDoneTap = {
            viewModel.onDoneTap()
            navigateBack()
        },
        selectedExperienceId = viewModel.selectedExperienceId,
        onIdChange = { viewModel.selectedExperienceId = it },
        experiences = viewModel.experiences.collectAsState().value
    )
}

@Preview
@Composable
fun EditIngestionMembershipScreenPreview(
    @PreviewParameter(ExperiencesPreviewProvider::class) experiences: List<Experience>,
) {
    EditIngestionMembershipScreen(
        onDoneTap = {},
        selectedExperienceId = null,
        onIdChange = {},
        experiences = experiences
    )
}

@Composable
fun EditIngestionMembershipScreen(
    onDoneTap: () -> Unit,
    selectedExperienceId: Int?,
    onIdChange: (Int?) -> Unit,
    experiences: List<Experience>
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Choose Experience") },
                actions = {
                    if (selectedExperienceId != null) {
                        TextButton(onClick = { onIdChange(null) }) {
                            Text(text = "Clear", color = MaterialTheme.colors.onPrimary)
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onDoneTap,
                icon = {
                    Icon(
                        Icons.Filled.Done,
                        contentDescription = "Done Icon"
                    )
                },
                text = { Text("Done") }
            )
        },
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
                    .padding(horizontal = 10.dp)
            ) {
                items(experiences.size) { i ->
                    val exp = experiences[i]
                    val isSelected = exp.id == selectedExperienceId
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = isSelected,
                                onClick = { onIdChange(exp.id) },
                                role = Role.RadioButton
                            )
                            .padding(vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = isSelected,
                            onClick = null // null recommended for accessibility with screen readers
                        )
                        Text(
                            text = exp.title,
                            style = MaterialTheme.typography.body1.merge(),
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                    if (i < experiences.size) {
                        Divider()
                    }
                }
            }
        }
    }

}