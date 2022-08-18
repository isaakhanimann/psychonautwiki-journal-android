package com.isaakhanimann.healthassistant.ui.search.substance

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.isaakhanimann.healthassistant.data.substances.repositories.SubstanceRepository
import com.isaakhanimann.healthassistant.ui.main.routers.SUBSTANCE_NAME_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SubstanceViewModel @Inject constructor(
    substanceRepo: SubstanceRepository,
    state: SavedStateHandle
) : ViewModel() {
    val substanceName = state.get<String>(SUBSTANCE_NAME_KEY)!!
    val substanceWithCategories = substanceRepo.getSubstanceWithCategories(substanceName)!!
}