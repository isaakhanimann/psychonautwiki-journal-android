/*
 * Copyright (c) 2023. Isaak Hanimann.
 * This file is part of PsychonautWiki Journal.
 *
 * PsychonautWiki Journal is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 *
 * PsychonautWiki Journal is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with PsychonautWiki Journal.  If not, see https://www.gnu.org/licenses/gpl-3.0.en.html.
 */

package com.isaakhanimann.journal.ui.tabs.journal.experience.timednote.add

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isaakhanimann.journal.data.room.experiences.ExperienceRepository
import com.isaakhanimann.journal.data.room.experiences.entities.AdaptiveColor
import com.isaakhanimann.journal.data.room.experiences.entities.TimedNote
import com.isaakhanimann.journal.ui.main.navigation.routers.EXPERIENCE_ID_KEY
import com.isaakhanimann.journal.ui.utils.getInstant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class AddTimedNoteViewModel @Inject constructor(
    private val experienceRepo: ExperienceRepository,
    state: SavedStateHandle
) : ViewModel() {
    var note by mutableStateOf("")
    var color by mutableStateOf(AdaptiveColor.BLUE)
    var isPartOfTimeline by mutableStateOf(true)
    val experienceId = state.get<Int>(EXPERIENCE_ID_KEY)!!
    var localDateTimeFlow = MutableStateFlow(LocalDateTime.now())
    var alreadyUsedColors by mutableStateOf(emptyList<AdaptiveColor>())
    var otherColors by mutableStateOf(emptyList<AdaptiveColor>())

    init {
        viewModelScope.launch {
            val ingestionsWithCompanions = experienceRepo.getIngestionsWithCompanions(experienceId)
            val substanceColors = ingestionsWithCompanions.mapNotNull { it.substanceCompanion?.color }.distinct()
            val notes = experienceRepo.getTimedNotes(experienceId)
            val noteColors = notes.map { it.color }.distinct()
            alreadyUsedColors = (substanceColors + noteColors).distinct()
            otherColors = AdaptiveColor.values().filter {
                !alreadyUsedColors.contains(it)
            }
            color = otherColors.randomOrNull() ?: AdaptiveColor.BLUE
            val lastIngestionTime = ingestionsWithCompanions.maxOfOrNull { it.ingestion.time }
            if (lastIngestionTime != null) {
                if (Duration.between(lastIngestionTime, Instant.now()).toHours() > 12) {
                    isPartOfTimeline = false
                }
            }
        }
    }

    fun onChangeTime(newLocalDateTime: LocalDateTime) {
        viewModelScope.launch {
            localDateTimeFlow.emit(newLocalDateTime)
        }
    }

    fun onChangeIsPartOfTimeline(newIsPartOfTimeline: Boolean) {
        isPartOfTimeline = newIsPartOfTimeline
    }

    fun onChangeNote(newNote: String) {
        note = newNote
    }

    fun onChangeColor(newColor: AdaptiveColor) {
        color = newColor
    }

    fun onDoneTap() {
        if (note.isNotBlank()) {
            viewModelScope.launch {
                val newTimedNote = TimedNote(
                    time = localDateTimeFlow.firstOrNull()?.getInstant() ?: Instant.now(),
                    creationDate = Instant.now(),
                    note = note,
                    color = color,
                    experienceId = experienceId,
                    isPartOfTimeline = isPartOfTimeline
                )
                experienceRepo.insert(newTimedNote)
            }
        }
    }
}