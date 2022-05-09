package com.example.healthassistant.presentation.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthassistant.model.SubstanceModel
import com.example.healthassistant.repository.SubstanceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: SubstanceRepository
): ViewModel() {

    var searchText by mutableStateOf("")
    var filteredSubstances: MutableList<SubstanceModel>  = mutableListOf()
    private var substances: List<SubstanceModel> = emptyList()

    init {
        viewModelScope.launch {
            substances = repository.getSubstances()
            filterSubstances()
        }
    }

    fun filterSubstances() {
        filteredSubstances = if (searchText.isEmpty()) {
            substances.toMutableList()
        } else {
            val resultList = ArrayList<SubstanceModel>()
            for (substance in substances) {
                if (substance.name.lowercase()
                        .contains(searchText.lowercase())
                ) {
                    resultList.add(substance)
                }
            }
            resultList
        }
    }
}