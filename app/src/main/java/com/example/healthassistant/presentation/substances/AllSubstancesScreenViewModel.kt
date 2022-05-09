package com.example.healthassistant.presentation.substances

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthassistant.repository.Resource
import com.example.healthassistant.repository.SubstanceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllSubstancesScreenViewModel @Inject constructor(
    private val repository: SubstanceRepository
): ViewModel() {

    var state by mutableStateOf(AllSubstancesState())

    init {
        viewModelScope.launch {
            when(val resource = repository.getSubstances()) {
                is Resource.Success -> {
                    state = state.copy(substances = resource.data ?: emptyList())
                }
                is Resource.Error -> {
                    state = state.copy(error = resource.message)
                }
                else -> Unit
            }
        }
    }
}