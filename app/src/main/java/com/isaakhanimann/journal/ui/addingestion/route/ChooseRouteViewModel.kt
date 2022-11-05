package com.isaakhanimann.journal.ui.addingestion.route

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.isaakhanimann.journal.data.substances.AdministrationRoute
import com.isaakhanimann.journal.data.substances.repositories.SubstanceRepository
import com.isaakhanimann.journal.ui.main.routers.SUBSTANCE_NAME_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChooseRouteViewModel @Inject constructor(
    substanceRepo: SubstanceRepository,
    state: SavedStateHandle
) : ViewModel() {
    val substanceName = state.get<String>(SUBSTANCE_NAME_KEY)!!
    val substance = substanceRepo.getSubstance(substanceName)

    var shouldShowOtherRoutes by mutableStateOf(false)
    val pwRoutes = substance?.roas?.map { it.route } ?: emptyList()
    private val otherRoutes = AdministrationRoute.values().filter { route ->
        !pwRoutes.contains(route)
    }
    val otherRoutesChunked = otherRoutes.chunked(2)

    var isShowingInjectionDialog by mutableStateOf(false)
    var currentRoute by mutableStateOf(AdministrationRoute.INTRAVENOUS)
}