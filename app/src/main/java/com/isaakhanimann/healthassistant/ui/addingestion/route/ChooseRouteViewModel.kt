package com.isaakhanimann.healthassistant.ui.addingestion.route

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isaakhanimann.healthassistant.data.room.experiences.ExperienceRepository
import com.isaakhanimann.healthassistant.data.substances.AdministrationRoute
import com.isaakhanimann.healthassistant.data.substances.classes.Substance
import com.isaakhanimann.healthassistant.data.substances.repositories.SubstanceRepository
import com.isaakhanimann.healthassistant.ui.main.routers.SUBSTANCE_NAME_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ChooseRouteViewModel @Inject constructor(
    substanceRepo: SubstanceRepository,
    experienceRepo: ExperienceRepository,
    state: SavedStateHandle
) : ViewModel() {
    val substanceName = state.get<String>(SUBSTANCE_NAME_KEY)!!
    val substance: Substance = substanceRepo.getSubstance(substanceName)!!

    data class IngestionSuggestion(
        val dose: Double?,
        val units: String?,
        val administrationRoute: AdministrationRoute,
        val isDoseAnEstimate: Boolean
    )

    val sortedIngestionsFlow: StateFlow<List<IngestionSuggestion>> =
        experienceRepo.getSortedIngestionsFlow(substanceName, limit = 4)
            .map { list ->
                list.map {
                    IngestionSuggestion(
                        dose = it.dose,
                        units = it.units,
                        administrationRoute = it.administrationRoute,
                        isDoseAnEstimate = it.isDoseAnEstimate
                    )
                }.distinct()
            }
            .stateIn(
                initialValue = emptyList(),
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000)
            )
    var shouldShowOtherRoutes by mutableStateOf(false)
    val pwRoutes = substance.roas.map { it.route }
    private val otherRoutes = AdministrationRoute.values().filter { route ->
        !pwRoutes.contains(route)
    }
    val otherRoutesChunked = otherRoutes.chunked(2)

    var isShowingInjectionDialog by mutableStateOf(false)
    var currentRoute by mutableStateOf(AdministrationRoute.INTRAVENOUS)
}