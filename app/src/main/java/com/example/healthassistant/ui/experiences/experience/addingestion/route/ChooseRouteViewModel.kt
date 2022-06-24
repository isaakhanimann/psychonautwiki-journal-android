package com.example.healthassistant.ui.experiences.experience.addingestion.route

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.healthassistant.data.substances.AdministrationRoute
import com.example.healthassistant.data.substances.Substance
import com.example.healthassistant.data.substances.repositories.SubstanceRepository
import com.example.healthassistant.ui.main.routers.SUBSTANCE_NAME_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChooseRouteViewModel @Inject constructor(
    repository: SubstanceRepository,
    state: SavedStateHandle
): ViewModel() {
    val substance: Substance = repository.getSubstance(state.get<String>(SUBSTANCE_NAME_KEY)!!)!!

    var shouldShowOtherRoutes by mutableStateOf(false)
    val pwRoutes = substance.roas.map { it.route }
    val otherRoutes = AdministrationRoute.values().filter { route ->
        !pwRoutes.contains(route)
    }
    val otherRoutesChunked = otherRoutes.chunked(2)

}