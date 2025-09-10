package com.isaakhanimann.journal.ui.tabs.search.custom

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.isaakhanimann.journal.data.room.experiences.ExperienceRepository
import com.isaakhanimann.journal.data.room.experiences.entities.CustomSubstance
import com.isaakhanimann.journal.data.room.experiences.entities.custom.CustomRoaInfo
import com.isaakhanimann.journal.ui.main.navigation.graphs.EditCustomSubstanceRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditCustomSubstanceViewModel @Inject constructor(
    val experienceRepo: ExperienceRepository,
    state: SavedStateHandle
) : ViewModel() {

    private var id: Int = 0
    var name by mutableStateOf("")
    var units by mutableStateOf("")
    var description by mutableStateOf("")
    val roaInfos = mutableStateListOf<CustomRoaInfo>()

    val isValid get() = name.isNotBlank() && units.isNotBlank()

    init {
        val editCustomSubstanceRoute = state.toRoute<EditCustomSubstanceRoute>()
        val customSubstanceId = editCustomSubstanceRoute.customSubstanceId
        id = customSubstanceId
        viewModelScope.launch {
            val customSubstance =
                experienceRepo.getCustomSubstanceFlow(customSubstanceId).firstOrNull() ?: return@launch
            name = customSubstance.name
            units = customSubstance.units
            description = customSubstance.description
            roaInfos.addAll(customSubstance.roaInfos)
        }
    }

    fun onDoneTap() {
        viewModelScope.launch {
            val customSubstance = CustomSubstance(
                id,
                name,
                units,
                description,
                roaInfos.toList()
            )
            experienceRepo.update(customSubstance)
        }
    }

    fun deleteCustomSubstance() {
        viewModelScope.launch {
            experienceRepo.delete(CustomSubstance(id, name, units, description, roaInfos.toList()))
        }
    }

    fun addRoa(roaInfo: CustomRoaInfo) {
        roaInfos.add(roaInfo)
    }

    fun removeRoa(roaInfo: CustomRoaInfo) {
        roaInfos.remove(roaInfo)
    }

    fun updateRoa(index: Int, roaInfo: CustomRoaInfo) {
        if (index in 0 until roaInfos.size) {
            roaInfos[index] = roaInfo
        }
    }
}