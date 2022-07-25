package com.isaakhanimann.healthassistant.ui.ingestions


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isaakhanimann.healthassistant.data.room.experiences.ExperienceRepository
import com.isaakhanimann.healthassistant.data.room.experiences.relations.IngestionWithCompanion
import com.isaakhanimann.healthassistant.data.substances.DoseClass
import com.isaakhanimann.healthassistant.data.substances.repositories.SubstanceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import java.util.*
import javax.inject.Inject


@HiltViewModel
class IngestionsViewModel @Inject constructor(
    experienceRepo: ExperienceRepository,
    substanceRepo: SubstanceRepository,
) : ViewModel() {

    private val sortedIngestionsWithCompanionsFlow: Flow<List<IngestionWithCompanion>> =
        experienceRepo.getSortedIngestionsWithSubstanceCompanionsFlow()

    data class IngestionElement(
        val ingestionWithCompanion: IngestionWithCompanion,
        val doseClass: DoseClass?
    )

    private val ingestionElementsFlow: Flow<List<IngestionElement>> =
        sortedIngestionsWithCompanionsFlow.map { ingestionsWithCompanions ->
            ingestionsWithCompanions.map { ingestionWithCompanion ->
                val ingestion = ingestionWithCompanion.ingestion
                val substance = substanceRepo.getSubstance(ingestion.substanceName)
                val roaDose = substance?.getRoa(route = ingestion.administrationRoute)?.roaDose
                val doseClass = roaDose?.getDoseClass(
                    ingestionDose = ingestion.dose,
                    ingestionUnits = ingestion.units
                )
                IngestionElement(
                    ingestionWithCompanion,
                    doseClass
                )
            }
        }

    val ingestionsGrouped: StateFlow<Map<String, List<IngestionElement>>> =
        ingestionElementsFlow.map { elements ->
            groupIngestionsByYear(ingestions = elements)
        }.stateIn(
            initialValue = emptyMap(),
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000)
        )

    companion object {
        fun groupIngestionsByYear(ingestions: List<IngestionElement>): Map<String, List<IngestionElement>> {
            val cal = Calendar.getInstance(TimeZone.getDefault())
            return ingestions.groupBy {
                cal.time = it.ingestionWithCompanion.ingestion.time
                cal.get(Calendar.YEAR).toString()
            }
        }
    }

}