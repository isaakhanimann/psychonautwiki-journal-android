package com.isaakhanimann.healthassistant.ui.journal.experience

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isaakhanimann.healthassistant.data.room.experiences.ExperienceRepository
import com.isaakhanimann.healthassistant.data.room.experiences.entities.Sentiment
import com.isaakhanimann.healthassistant.data.room.experiences.relations.IngestionWithCompanion
import com.isaakhanimann.healthassistant.data.substances.classes.roa.DoseClass
import com.isaakhanimann.healthassistant.data.substances.classes.roa.RoaDose
import com.isaakhanimann.healthassistant.data.substances.classes.roa.RoaDuration
import com.isaakhanimann.healthassistant.data.substances.repositories.SubstanceRepository
import com.isaakhanimann.healthassistant.ui.addingestion.time.hourLimitToSeparateIngestions
import com.isaakhanimann.healthassistant.ui.main.routers.EXPERIENCE_ID_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class OneExperienceViewModel @Inject constructor(
    private val experienceRepo: ExperienceRepository,
    private val substanceRepo: SubstanceRepository,
    state: SavedStateHandle
) : ViewModel() {

    fun saveSentiment(sentiment: Sentiment?) {
        viewModelScope.launch {
            val experience = experienceWithIngestionsFlow.firstOrNull()?.experience
            if (experience != null) {
                experience.sentiment = sentiment
                experienceRepo.update(experience)
            }
        }
    }

    fun saveIsFavorite(isFavorite: Boolean) {
        viewModelScope.launch {
            val experience = experienceWithIngestionsFlow.firstOrNull()?.experience
            if (experience != null) {
                experience.isFavorite = isFavorite
                experienceRepo.update(experience)
            }
        }
    }

    private val experienceWithIngestionsFlow =
        experienceRepo.getExperienceWithIngestionsAndCompanionsFlow(experienceId = state.get<Int>(EXPERIENCE_ID_KEY)!!)

    private val currentTimeFlow: Flow<Date> = flow {
        while (true) {
            emit(Date())
            delay(timeMillis = 1000 * 10)
        }
    }

    private val isShowingAddIngestionButtonFlow = experienceWithIngestionsFlow.combine(currentTimeFlow) { experienceWithIngestions, currentTime ->
        val ingestionTimes = experienceWithIngestions?.ingestionsWithCompanions?.map { it.ingestion.time }
        val lastIngestionTime = ingestionTimes?.maxOrNull()
        val cal = Calendar.getInstance(TimeZone.getDefault())
        cal.time = currentTime
        cal.add(Calendar.HOUR_OF_DAY, -hourLimitToSeparateIngestions)
        val limitAgo = cal.time
        return@combine limitAgo < lastIngestionTime
    }

    private val sortedIngestionsWithCompanionsFlow = experienceWithIngestionsFlow.map { experience ->
        experience?.ingestionsWithCompanions?.sortedBy { it.ingestion.time } ?: emptyList()
    }

    private data class IngestionWithAssociatedData(
        val ingestionWithCompanion: IngestionWithCompanion,
        val roaDuration: RoaDuration?,
        val roaDose: RoaDose?
    )

    private val ingestionsWithAssociatedDataFlow: Flow<List<IngestionWithAssociatedData>> =
        sortedIngestionsWithCompanionsFlow.map { ingestionsWithComps ->
            ingestionsWithComps.map { oneIngestionWithComp ->
                val ingestion = oneIngestionWithComp.ingestion
                val roa = substanceRepo.getSubstance(oneIngestionWithComp.ingestion.substanceName)
                    ?.getRoa(ingestion.administrationRoute)
                val roaDuration = roa?.roaDuration
                IngestionWithAssociatedData(
                    ingestionWithCompanion = oneIngestionWithComp,
                    roaDuration = roaDuration,
                    roaDose = roa?.roaDose
                )
            }
        }

    private val ingestionElementsFlow = ingestionsWithAssociatedDataFlow.map {
        getIngestionElements(it)
    }

    private val cumulativeDosesFlow = ingestionsWithAssociatedDataFlow.map {
        getCumulativeDoses(it)
    }

    private fun getIngestionElements(sortedIngestionsWith: List<IngestionWithAssociatedData>): List<IngestionElement> {
        return sortedIngestionsWith.mapIndexed { index, ingestionWith ->
            val currentIngestionTime = ingestionWith.ingestionWithCompanion.ingestion.time
            val currentIngestionTimeText = getFullDateText(currentIngestionTime)
            val timeText = if (index == 0) {
                currentIngestionTimeText
            } else {
                val previousIngestionTimeText =
                    getFullDateText(sortedIngestionsWith[index - 1].ingestionWithCompanion.ingestion.time)
                val isCurrentSameAsPrevious = previousIngestionTimeText == currentIngestionTimeText
                if (isCurrentSameAsPrevious) {
                    null
                } else {
                    currentIngestionTimeText
                }
            }
            val doseClass =
                ingestionWith.roaDose?.getDoseClass(
                    ingestionWith.ingestionWithCompanion.ingestion.dose,
                    ingestionUnits = ingestionWith.ingestionWithCompanion.ingestion.units
                )
            IngestionElement(
                dateText = timeText,
                ingestionWithCompanion = ingestionWith.ingestionWithCompanion,
                roaDuration = ingestionWith.roaDuration,
                doseClass = doseClass
            )
        }
    }

    private fun getFullDateText(date: Date): String {
        val formatter = SimpleDateFormat("EEE, dd MMMM yyyy", Locale.getDefault())
        return formatter.format(date)
    }

    fun deleteExperience() {
        viewModelScope.launch {
            experienceWithIngestionsFlow.firstOrNull()?.let {
                experienceRepo.delete(it)
            }
        }
    }

    val oneExperienceScreenModelFlow: StateFlow<OneExperienceScreenModel?> = isShowingAddIngestionButtonFlow.combine(ingestionElementsFlow) { isShowingAddIngestion, ingestionElements ->
        Pair(first = isShowingAddIngestion, second = ingestionElements)
    }.combine(cumulativeDosesFlow) { pair, cumulatives ->
        Pair(first = pair, second = cumulatives)
    }.combine(experienceWithIngestionsFlow) { pair, experienceWithIngestions ->
        val experience = experienceWithIngestions?.experience ?: return@combine null
        return@combine OneExperienceScreenModel(
            isFavorite = experience.isFavorite,
            sentiment = experience.sentiment,
            title = experience.title,
            notes = experience.text,
            isShowingAddIngestionButton = pair.first.first,
            ingestionElements = pair.first.second,
            cumulativeDoses = pair.second
        )
    }.stateIn(
        initialValue = null,
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000)
    )

    private companion object {
        fun getCumulativeDoses(ingestions: List<IngestionWithAssociatedData>): List<CumulativeDose> {
            return ingestions.groupBy { it.ingestionWithCompanion.ingestion.substanceName }
                .mapNotNull { grouped ->
                    val groupedIngestions = grouped.value
                    if (groupedIngestions.size <= 1) return@mapNotNull null
                    if (groupedIngestions.any { it.ingestionWithCompanion.ingestion.dose == null }) return@mapNotNull null
                    val units = groupedIngestions.first().ingestionWithCompanion.ingestion.units
                    if (groupedIngestions.any { it.ingestionWithCompanion.ingestion.units != units }) return@mapNotNull null
                    val isEstimate =
                        groupedIngestions.any { it.ingestionWithCompanion.ingestion.isDoseAnEstimate }
                    val cumulativeDose =
                        groupedIngestions.mapNotNull { it.ingestionWithCompanion.ingestion.dose }
                            .sum()
                    val doseClass = groupedIngestions.first().roaDose?.getDoseClass(
                        ingestionDose = cumulativeDose,
                        ingestionUnits = units
                    )
                    CumulativeDose(
                        substanceName = grouped.key,
                        cumulativeDose = cumulativeDose,
                        units = units,
                        isEstimate = isEstimate,
                        doseClass = doseClass
                    )
                }
        }
    }
}

data class OneExperienceScreenModel(
    val isFavorite: Boolean,
    val sentiment: Sentiment?,
    val title: String,
    val notes: String,
    val isShowingAddIngestionButton: Boolean,
    val ingestionElements: List<IngestionElement>,
    val cumulativeDoses: List<CumulativeDose>
)

data class CumulativeDose(
    val substanceName: String,
    val cumulativeDose: Double,
    val units: String?,
    val isEstimate: Boolean,
    val doseClass: DoseClass?
)

data class IngestionElement(
    val dateText: String?,
    val ingestionWithCompanion: IngestionWithCompanion,
    val roaDuration: RoaDuration?,
    val doseClass: DoseClass?
)