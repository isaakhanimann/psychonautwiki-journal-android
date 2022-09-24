package com.isaakhanimann.healthassistant.data.substances.classes

import androidx.compose.ui.graphics.Color

enum class InteractionType {
    DANGEROUS {
        override val color = Color.Red
        override val dangerCount = 3
    },
    UNSAFE {
        override val color = Color(0xFFFF9800)
        override val dangerCount = 2
    },
    UNCERTAIN {
        override val color = Color.Yellow
        override val dangerCount = 1
    };

    abstract val color: Color
    abstract val dangerCount: Int
}

data class Interactions(
    val dangerous: List<String>,
    val unsafe: List<String>,
    val uncertain: List<String>
) {
    fun getInteractions(interactionType: InteractionType): List<String> {
        return when (interactionType) {
            InteractionType.DANGEROUS -> dangerous
            InteractionType.UNSAFE -> unsafe
            InteractionType.UNCERTAIN -> uncertain
        }
    }
}