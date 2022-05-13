package com.example.healthassistant.ui.home.experience.addingestion.time

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.healthassistant.data.substances.AdministrationRoute
import com.example.healthassistant.data.substances.Substance
import com.example.healthassistant.data.substances.repositories.SubstanceRepository
import com.example.healthassistant.ui.main.routers.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChooseTimeViewModel @Inject constructor(
    repository: SubstanceRepository,
    state: SavedStateHandle
): ViewModel() {
    val substance: Substance?
    val administrationRoute: AdministrationRoute?
    val dose: Double?
    val units: String?
    val isEstimate: Boolean?

    init {
        substance = repository.getSubstance(state.get<String>(SUBSTANCE_NAME_KEY) ?: "LSD")
        val routeString = state.get<String>(ADMINISTRATION_ROUTE_KEY)
        administrationRoute = try {
            AdministrationRoute.valueOf(routeString ?: "")
        } catch (e: Exception) {
            null
        }
        dose = state.get<Double>(DOSE_KEY)
        units = state.get<String>(UNITS_KEY)
        isEstimate = state.get<Boolean>(IS_ESTIMATE_KEY)
        assert(substance != null)
        assert(administrationRoute != null)
        assert(dose != null)
        assert(units != null)
        assert(isEstimate != null)
    }
}