package com.isaakhanimann.healthassistant.ui.addingestion.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isaakhanimann.healthassistant.data.room.experiences.ExperienceRepository
import com.isaakhanimann.healthassistant.data.room.experiences.entities.CustomSubstance
import com.isaakhanimann.healthassistant.data.room.experiences.entities.SubstanceColor
import com.isaakhanimann.healthassistant.data.room.experiences.relations.IngestionWithCompanion
import com.isaakhanimann.healthassistant.data.substances.AdministrationRoute
import com.isaakhanimann.healthassistant.data.substances.repositories.SubstanceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AddIngestionSearchViewModel @Inject constructor(
    experienceRepo: ExperienceRepository,
    val substanceRepo: SubstanceRepository
) : ViewModel() {

    private val last100Ingestions =
        experienceRepo.getSortedIngestionsWithSubstanceCompanionsFlow(limit = 100)

    private val customSubstancesFlow = experienceRepo.getCustomSubstancesFlow()

    val previousSubstanceRows: StateFlow<List<PreviousSubstance>> =
        last100Ingestions.combine(customSubstancesFlow) { ingestions, customSubstances ->
            return@combine getPreviousSubstances(ingestions, customSubstances)
        }.stateIn(
            initialValue = emptyList(),
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000)
        )

    private fun getPreviousSubstances(
        ingestions: List<IngestionWithCompanion>,
        customSubstances: List<CustomSubstance>
    ): List<PreviousSubstance> {
        val grouped = ingestions.groupBy { it.ingestion.substanceName }
        return grouped.mapNotNull { entry ->
            val substanceName = entry.key
            val groupedIngestions = entry.value
            val color =
                groupedIngestions.firstOrNull()?.substanceCompanion?.color ?: return@mapNotNull null
            val isPredefinedSubstance = substanceRepo.getSubstance(substanceName) != null
            val isCustomSubstance = customSubstances.any { it.name == substanceName }
            if (!isPredefinedSubstance && !isCustomSubstance) {
                return@mapNotNull null
            } else {
                return@mapNotNull PreviousSubstance(
                    color = color,
                    substanceName = substanceName,
                    isCustom = isCustomSubstance,
                    routesWithDoses = groupedIngestions.groupBy { it.ingestion.administrationRoute }
                        .map { routeEntry ->
                            RouteWithDoses(
                                route = routeEntry.key,
                                doses = routeEntry.value.map { ingestionWithCompanion ->
                                    PreviousDose(
                                        dose = ingestionWithCompanion.ingestion.dose,
                                        unit = ingestionWithCompanion.ingestion.units,
                                        isEstimate = ingestionWithCompanion.ingestion.isDoseAnEstimate
                                    )
                                }.distinct().take(4)
                            )
                        }
                )
            }
        }
    }
}

data class PreviousSubstance(
    val color: SubstanceColor,
    val substanceName: String,
    val isCustom: Boolean,
    val routesWithDoses: List<RouteWithDoses>
)

data class RouteWithDoses(
    val route: AdministrationRoute,
    val doses: List<PreviousDose>
)

data class PreviousDose(
    val dose: Double?,
    val unit: String?,
    val isEstimate: Boolean
)