/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 3.0. If a copy of the MPL was not distributed with this file, You can obtain one at https://www.gnu.org/licenses/gpl-3.0.en.html.
 */

package com.isaakhanimann.journal.ui.tabs.journal.experience.edit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isaakhanimann.journal.data.room.experiences.ExperienceRepository
import com.isaakhanimann.journal.data.room.experiences.entities.Experience
import com.isaakhanimann.journal.ui.main.navigation.routers.EXPERIENCE_ID_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class EditExperienceViewModel @Inject constructor(
    private val repository: ExperienceRepository,
    state: SavedStateHandle
) :
    ViewModel() {

    var experience: Experience? = null
    var enteredTitle by mutableStateOf("")
    val isEnteredTitleOk get() = enteredTitle.isNotEmpty()
    var enteredText by mutableStateOf("")

    init {
        val id = state.get<Int>(EXPERIENCE_ID_KEY)!!
        viewModelScope.launch {
            experience = repository.getExperience(id = id)!!
            enteredTitle = experience!!.title
            enteredText = experience!!.text
        }
    }

    fun onDoneTap() {
        if (enteredTitle.isNotEmpty()) {
            viewModelScope.launch {
                experience!!.title = enteredTitle
                experience!!.text = enteredText
                repository.update(experience = experience!!)
            }
        }
    }

}