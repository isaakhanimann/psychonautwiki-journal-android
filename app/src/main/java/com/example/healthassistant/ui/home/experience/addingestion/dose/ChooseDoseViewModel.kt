package com.example.healthassistant.ui.home.experience.addingestion.dose

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.healthassistant.data.substances.AdministrationRoute
import com.example.healthassistant.data.substances.RoaDose
import com.example.healthassistant.data.substances.Substance
import com.example.healthassistant.data.substances.repositories.SubstanceRepository
import com.example.healthassistant.ui.main.routers.ADMINISTRATION_ROUTE_KEY
import com.example.healthassistant.ui.main.routers.EXPERIENCE_ID_KEY
import com.example.healthassistant.ui.main.routers.SUBSTANCE_NAME_KEY
import com.example.healthassistant.ui.search.substance.roa.dose.DoseColor
import com.example.healthassistant.ui.search.substance.roa.toReadableString
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChooseDoseViewModel @Inject constructor(
    repository: SubstanceRepository,
    state: SavedStateHandle
) : ViewModel() {
    val substance: Substance?
    val administrationRoute: AdministrationRoute
    val experienceId = state.get<String>(EXPERIENCE_ID_KEY)?.toIntOrNull()
    val roaDose: RoaDose?
    var isEstimate by mutableStateOf(false)
    var doseText by mutableStateOf("")
    val dose: Double? get() = doseText.toDoubleOrNull()
    val isValidDose: Boolean get() = dose != null
    val currentRoaRangeTextAndColor: Pair<String, DoseColor?>
        get() {
            dose?.let { nonNullDose ->
                if (roaDose?.threshold != null && nonNullDose < roaDose.threshold) {
                    return Pair(
                        "threshold: ${roaDose.threshold.toReadableString()}${roaDose.units ?: ""}",
                        DoseColor.THRESH
                    )
                } else if (roaDose?.light?.isValueInRange(nonNullDose) == true) {
                    return Pair(
                        "light: ${roaDose.light.min?.toReadableString()}-${roaDose.light.max?.toReadableString()}${roaDose.units ?: ""}",
                        DoseColor.LIGHT
                    )
                } else if (roaDose?.common?.isValueInRange(nonNullDose) == true) {
                    return Pair(
                        "common: ${roaDose.common.min?.toReadableString()}-${roaDose.common.max?.toReadableString()}${roaDose.units ?: ""}",
                        DoseColor.COMMON
                    )
                } else if (roaDose?.strong?.isValueInRange(nonNullDose) == true) {
                    return Pair(
                        "strong: ${roaDose.strong.min?.toReadableString()}-${roaDose.strong.max?.toReadableString()}${roaDose.units ?: ""}",
                        DoseColor.STRONG
                    )
                } else if (roaDose?.heavy != null && nonNullDose > roaDose.heavy) {
                    return Pair(
                        "heavy: ${roaDose.heavy.toReadableString()}${roaDose.units ?: ""}-..",
                        DoseColor.HEAVY
                    )
                } else {
                    return Pair("", null)
                }
            } ?: run {
                return Pair("", null)
            }
        }

    fun onDoseTextChange(newDoseText: String) {
        doseText = newDoseText.replace(oldChar = ',', newChar = '.')
    }

    init {
        substance = repository.getSubstance(state.get<String>(SUBSTANCE_NAME_KEY) ?: "LSD")
        val routeString = state.get<String>(ADMINISTRATION_ROUTE_KEY)!!
        administrationRoute = AdministrationRoute.valueOf(routeString)
        assert(substance != null)
        roaDose = substance?.getRoa(administrationRoute)?.roaDose
    }

}
