package com.example.healthassistant.presentation.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthassistant.data.substances.Substance
import com.example.healthassistant.data.substances.repositories.SubstanceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: SubstanceRepository
): ViewModel() {

    var searchText by mutableStateOf("")
    var substancesToShow: List<Substance> = repository.getAllSubstances()

    fun filterSubstances() {
        viewModelScope.launch {
            substancesToShow = repository.getSubstances(searchText = searchText)
        }
    }
}