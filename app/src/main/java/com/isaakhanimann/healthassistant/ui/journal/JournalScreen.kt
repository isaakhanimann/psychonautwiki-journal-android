package com.isaakhanimann.healthassistant.ui.journal

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.hilt.navigation.compose.hiltViewModel
import com.isaakhanimann.healthassistant.data.room.experiences.relations.ExperienceWithIngestionsAndCompanions
import com.isaakhanimann.healthassistant.ui.stats.EmptyScreenDisclaimer

@Composable
fun JournalScreen(
    navigateToExperiencePopNothing: (experienceId: Int) -> Unit,
    navigateToAddIngestion: () -> Unit,
    viewModel: JournalViewModel = hiltViewModel()
) {
    JournalScreen(
        navigateToExperiencePopNothing = navigateToExperiencePopNothing,
        navigateToAddIngestion = navigateToAddIngestion,
        groupedExperiences = viewModel.experiencesGrouped.collectAsState().value,
        isFavoriteEnabled = viewModel.isFavoriteEnabledFlow.collectAsState().value,
        onChangeIsFavorite = viewModel::onChangeFavorite,
        searchText = viewModel.searchTextFlow.collectAsState().value,
        onChangeSearchText = viewModel::search,
        isSearchEnabled = viewModel.isSearchEnabled.value,
        onChangeIsSearchEnabled = viewModel::onChangeOfIsSearchEnabled
    )
}

@Preview
@Composable
fun ExperiencesScreenPreview(
    @PreviewParameter(
        JournalScreenPreviewProvider::class,
    ) groupedExperiences: Map<String, List<ExperienceWithIngestionsAndCompanions>>,
) {
    JournalScreen(
        navigateToExperiencePopNothing = {},
        navigateToAddIngestion = {},
        groupedExperiences = groupedExperiences,
        isFavoriteEnabled = false,
        onChangeIsFavorite = {},
        searchText = "",
        onChangeSearchText = {},
        isSearchEnabled = true,
        onChangeIsSearchEnabled = {}
    )
}

@Composable
fun JournalScreen(
    navigateToExperiencePopNothing: (experienceId: Int) -> Unit,
    navigateToAddIngestion: () -> Unit,
    groupedExperiences: Map<String, List<ExperienceWithIngestionsAndCompanions>>,
    isFavoriteEnabled: Boolean,
    onChangeIsFavorite: (Boolean) -> Unit,
    searchText: String,
    onChangeSearchText: (String) -> Unit,
    isSearchEnabled: Boolean,
    onChangeIsSearchEnabled: (Boolean) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                     Text(text = "Experiences")
                },
                actions = {
                    IconToggleButton(checked = isFavoriteEnabled, onCheckedChange = onChangeIsFavorite) {
                        if (isFavoriteEnabled) {
                            Icon(Icons.Filled.Star, contentDescription = "Is Favorite")
                        } else {
                            Icon(Icons.Outlined.StarOutline, contentDescription = "Is not Favorite")
                        }
                    }
                    IconToggleButton(
                        checked = isSearchEnabled,
                        onCheckedChange = onChangeIsSearchEnabled
                    ) {
                        if (isSearchEnabled) {
                            Icon(Icons.Outlined.SearchOff, contentDescription = "Search Off")
                        } else {
                            Icon(Icons.Filled.Search, contentDescription = "Search")
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = navigateToAddIngestion,
                icon = {
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = "Add"
                    )
                },
                text = { Text("Ingestion") },
            )
        }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Column {
                if (isSearchEnabled) {
                    val focusManager = LocalFocusManager.current
                    TextField(
                        value = searchText,
                        onValueChange = onChangeSearchText,
                        leadingIcon = {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = "Search",
                            )
                        },
                        trailingIcon = {
                            if (searchText != "") {
                                IconButton(
                                    onClick = {
                                        onChangeSearchText("")
                                    }
                                ) {
                                    Icon(
                                        Icons.Default.Close,
                                        contentDescription = "Close",
                                    )
                                }
                            }
                        },
                        label = { Text(text = "Search by title") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        singleLine = true
                    )
                }
                ExperiencesList(
                    groupedExperiences = groupedExperiences,
                    navigateToExperiencePopNothing = navigateToExperiencePopNothing,
                )
            }
            if (groupedExperiences.isEmpty()) {
                if (isSearchEnabled && searchText.isNotEmpty()) {
                    if (isFavoriteEnabled) {
                        EmptyScreenDisclaimer(
                            title = "No Results",
                            description = "No favorite experience titles match your search."
                        )
                    } else {
                        EmptyScreenDisclaimer(
                            title = "No Results",
                            description = "No experience titles match your search."
                        )
                    }
                } else {
                    if (isFavoriteEnabled) {
                        EmptyScreenDisclaimer(
                            title = "No Favorites",
                            description = "Mark experiences as favorites to find them quickly."
                        )
                    } else {
                        EmptyScreenDisclaimer(
                            title = "No Experiences Yet",
                            description = "Add your first ingestion."
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ExperiencesList(
    groupedExperiences: Map<String, List<ExperienceWithIngestionsAndCompanions>>,
    navigateToExperiencePopNothing: (experienceId: Int) -> Unit,
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        groupedExperiences.forEach { (year, experiencesInYear) ->
            item {
                SectionTitle(title = year)
            }
            items(experiencesInYear.size) { i ->
                val experienceWithIngestions = experiencesInYear[i]
                ExperienceRow(
                    experienceWithIngestions,
                    navigateToExperienceScreen = {
                        navigateToExperiencePopNothing(experienceWithIngestions.experience.id)
                    },
                )
                if (i < experiencesInYear.size) {
                    Divider()
                }
            }
        }
    }
}