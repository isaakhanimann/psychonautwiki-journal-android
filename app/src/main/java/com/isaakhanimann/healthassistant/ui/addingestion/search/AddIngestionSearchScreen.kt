package com.isaakhanimann.healthassistant.ui.addingestion.search

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.isaakhanimann.healthassistant.data.room.experiences.entities.SubstanceColor
import com.isaakhanimann.healthassistant.ui.journal.SectionTitle
import com.isaakhanimann.healthassistant.ui.search.SearchScreen
import com.isaakhanimann.healthassistant.ui.search.substance.roa.toReadableString

@Composable
fun AddIngestionSearchScreen(
    navigateToCheckInteractions: (substanceName: String) -> Unit,
    viewModel: AddIngestionSearchViewModel = hiltViewModel()
) {
    AddIngestionSearchScreen(
        navigateToCheckInteractions = navigateToCheckInteractions,
        searchModel = viewModel.modelFlow.collectAsState().value
    )
}

@Composable
fun AddIngestionSearchScreen(
    navigateToCheckInteractions: (substanceName: String) -> Unit,
    searchModel: AddIngestionSearchModel
) {
    Column {
        LinearProgressIndicator(progress = 0.17f, modifier = Modifier.fillMaxWidth())
        SearchScreen(
            onSubstanceTap = {
                navigateToCheckInteractions(it)
            },
            modifier = Modifier.weight(1f),
            isShowingSettings = false,
            navigateToSettings = {}
        )
        SuggestionsSection(
            searchModel = searchModel,
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview
@Composable
fun SuggestionSectionPreview(@PreviewParameter(AddIngestionSearchScreenProvider::class) addIngestionSearchModel: AddIngestionSearchModel) {
    Scaffold {
        Column(modifier = Modifier.fillMaxSize()) {
            Surface(
                color = Color.Blue, modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                Text(text = "Search Screen")
            }
            SuggestionsSection(
                searchModel = addIngestionSearchModel,
                modifier = Modifier.weight(1f)
            )
        }
    }

}

@Composable
fun SuggestionsSection(searchModel: AddIngestionSearchModel, modifier: Modifier) {
    LazyColumn(modifier = modifier) {
        item {
            SectionTitle(title = "Quick Logging")
        }
        items(searchModel.routeSuggestions) {
            RouteSuggestion(routeSuggestionElement = it)
            Divider()
        }
        items(searchModel.doseSuggestions) {
            DoseSuggestion(doseSuggestionElement = it)
            Divider()
        }
    }
}

@Composable
fun RouteSuggestion(routeSuggestionElement: RouteSuggestionElement) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
        ColorCircle(substanceColor = routeSuggestionElement.color)
        Text(
            text = routeSuggestionElement.substanceName + " " + routeSuggestionElement.administrationRoute.displayText,
        )
    }
}

@Composable
fun DoseSuggestion(doseSuggestionElement: DoseSuggestionElement) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
        ColorCircle(substanceColor = doseSuggestionElement.color)
        if (doseSuggestionElement.dose != null) {
            val isEstimateText = if (doseSuggestionElement.isDoseAnEstimate) "~" else ""
            val doseText = doseSuggestionElement.dose.toReadableString()
            Text(
                text = "${doseSuggestionElement.substanceName} ${doseSuggestionElement.administrationRoute.displayText} $isEstimateText$doseText ${doseSuggestionElement.units}",
            )
        } else {
            Text(
                text = "${doseSuggestionElement.substanceName} ${doseSuggestionElement.administrationRoute.displayText} Unknown Dose",
            )
        }
    }
}

@Composable
fun ColorCircle(substanceColor: SubstanceColor) {
    val isDarkTheme = isSystemInDarkTheme()
    Surface(
        shape = CircleShape,
        color = substanceColor.getComposeColor(isDarkTheme),
        modifier = Modifier
            .size(25.dp)
    ) {}
}