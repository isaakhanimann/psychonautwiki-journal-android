package com.isaakhanimann.journal.ui.settings.combinations

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SubstanceViewModelInteraction(
    private val scope: CoroutineScope,
    private val booleanInteraction: BooleanInteraction
) {
    val name = booleanInteraction.name
    val stateFlow = booleanInteraction.flow.stateIn(
        initialValue = false,
        scope = scope,
        started = SharingStarted.WhileSubscribed(5000)
    )

    fun toggle() {
        scope.launch {
            booleanInteraction.toggle()
        }
    }
}