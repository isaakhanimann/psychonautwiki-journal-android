package com.isaakhanimann.healthassistant.ui.addingestion.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isaakhanimann.healthassistant.data.room.experiences.ExperienceRepository
import com.isaakhanimann.healthassistant.data.room.experiences.entities.SubstanceColor
import com.isaakhanimann.healthassistant.data.substances.AdministrationRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AddIngestionSearchViewModel @Inject constructor(
    experienceRepo: ExperienceRepository
) : ViewModel() {

    private val last100Ingestions =
        experienceRepo.getSortedIngestionsWithSubstanceCompanionsFlow(limit = 100)

    val modelFlow = last100Ingestions.map { ingestionsWithCompanions ->
        val routeSuggestions = ingestionsWithCompanions.mapNotNull {
            val color = it.substanceCompanion?.color ?: return@mapNotNull null
            return@mapNotNull RouteSuggestionElement(
                color = color,
                substanceName = it.ingestion.substanceName,
                administrationRoute = it.ingestion.administrationRoute
            )
        }.distinct()
        val doseSuggestions = ingestionsWithCompanions.mapNotNull {
            val color = it.substanceCompanion?.color ?: return@mapNotNull null
            val ingestion = it.ingestion
            return@mapNotNull DoseSuggestionElement(
                color = color,
                substanceName = ingestion.substanceName,
                administrationRoute = ingestion.administrationRoute,
                dose = ingestion.dose,
                units = ingestion.units,
                isDoseAnEstimate = ingestion.isDoseAnEstimate
            )
        }.distinct()
        return@map AddIngestionSearchModel(routeSuggestions, doseSuggestions)
    }.stateIn(
        initialValue = AddIngestionSearchModel(
            routeSuggestions = emptyList(),
            doseSuggestions = emptyList()
        ),
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000)
    )
}

data class AddIngestionSearchModel(
    val routeSuggestions: List<RouteSuggestionElement>,
    val doseSuggestions: List<DoseSuggestionElement>
)

data class RouteSuggestionElement(
    val color: SubstanceColor,
    val substanceName: String,
    val administrationRoute: AdministrationRoute,
)

data class DoseSuggestionElement(
    val color: SubstanceColor,
    val substanceName: String,
    val administrationRoute: AdministrationRoute,
    val dose: Double?,
    val units: String?,
    val isDoseAnEstimate: Boolean
)