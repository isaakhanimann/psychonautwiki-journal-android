package com.example.healthassistant.ui.home.experience.timeline.ingestion

import androidx.lifecycle.ViewModel
import com.example.healthassistant.data.experiences.entities.Ingestion
import com.example.healthassistant.data.substances.RoaDuration
import com.example.healthassistant.data.substances.repositories.SubstanceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class IngestionTimelineViewModel @Inject constructor(
    ingestion: Ingestion,
    repo: SubstanceRepository,
) : ViewModel() {

    val roaDuration: RoaDuration?

    init {
        val substance = repo.getSubstance(ingestion.substanceName)!!
        roaDuration = substance.roas.firstOrNull { it.route == ingestion.administrationRoute }?.roaDuration
    }
}
