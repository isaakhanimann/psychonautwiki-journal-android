package com.example.healthassistant.ui.addingestion.route

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthassistant.data.room.experiences.ExperienceRepository
import com.example.healthassistant.data.room.experiences.entities.Ingestion
import com.example.healthassistant.data.substances.AdministrationRoute
import com.example.healthassistant.data.substances.Substance
import com.example.healthassistant.data.substances.repositories.SubstanceRepository
import com.example.healthassistant.ui.main.routers.SUBSTANCE_NAME_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
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
    val sortedIngestionsFlow: StateFlow<List<Ingestion>> =
        experienceRepo.getSortedIngestionsFlow(substanceName, limit = 3)
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