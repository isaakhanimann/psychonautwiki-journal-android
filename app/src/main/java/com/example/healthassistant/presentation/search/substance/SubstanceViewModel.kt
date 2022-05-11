package com.example.healthassistant.presentation.search.substance

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.healthassistant.SUBSTANCE_NAME
import com.example.healthassistant.data.substances.Substance
import com.example.healthassistant.data.substances.repositories.SubstanceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SubstanceViewModel @Inject constructor(
    repository: SubstanceRepository,
    state: SavedStateHandle
): ViewModel() {
    val substance: Substance? = repository.getSubstance(state.get<String>(SUBSTANCE_NAME) ?: "LSD")
}