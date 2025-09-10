package com.isaakhanimann.journal.ui.tabs.search.custom

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isaakhanimann.journal.data.room.experiences.ExperienceRepository
import com.isaakhanimann.journal.data.room.experiences.entities.CustomSubstance
import com.isaakhanimann.journal.data.room.experiences.entities.custom.CustomRoaInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddCustomSubstanceViewModel @Inject constructor(
    val experienceRepo: ExperienceRepository,
) : ViewModel() {

    var name by mutableStateOf("")
    var units by mutableStateOf("")
    var description by mutableStateOf("")
    val roaInfos = mutableStateListOf<CustomRoaInfo>()

    val isValid get() = name.isNotBlank() && units.isNotBlank()

    fun addCustomSubstance(onDone: (customSubstanceName: String) -> Unit) {
        viewModelScope.launch {
            val customSubstance = CustomSubstance(
                name = name,
                units = units,
                description = description,
                roaInfos = roaInfos.toList()
            )
            experienceRepo.insert(customSubstance)
            onDone(name)
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