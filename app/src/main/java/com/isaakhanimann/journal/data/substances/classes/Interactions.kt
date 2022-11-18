/*
 * Copyright (c) 2022. Free Software Foundation, Inc.
 * This file is part of PsychonautWiki Journal.
 *
 * PsychonautWiki Journal is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 *
 * PsychonautWiki Journal is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with PsychonautWiki Journal.  If not, see https://www.gnu.org/licenses/gpl-3.0.en.html.
 */

package com.isaakhanimann.journal.data.substances.classes

import androidx.compose.ui.graphics.Color

enum class InteractionType {
    DANGEROUS {
        override val color = Color(0xffFF7B66)
        override val dangerCount = 3
    },
    UNSAFE {
        override val color = Color(0xFFFFC466)
        override val dangerCount = 2
    },
    UNCERTAIN {
        override val color = Color(0xffFFF966)
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