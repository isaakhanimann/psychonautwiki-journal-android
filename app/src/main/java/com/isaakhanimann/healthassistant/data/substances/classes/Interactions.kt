package com.isaakhanimann.healthassistant.data.substances.classes

import androidx.compose.ui.graphics.Color

enum class InteractionType {
    DANGEROUS {
        override val color: Color
            get() = Color.Red
    },
    UNSAFE {
        override val color: Color
            get() = Color(0xFFFF9800)
    },
    UNCERTAIN {
        override val color: Color
            get() = Color.Yellow
    };

    abstract val color: Color
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