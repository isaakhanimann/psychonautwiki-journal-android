/*
 * Copyright (c) 2023. Isaak Hanimann.
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

package com.isaakhanimann.journal.ui.tabs.journal.experience.components

enum class SavedTimeDisplayOption {
    AUTO {
        override val text = "Auto"
    }, RELATIVE_TO_NOW {
        override val text = "Time relative to now"
    }, RELATIVE_TO_START {
        override val text = "Time relative to start"
    }, TIME_BETWEEN {
        override val text = "Time between"
    }, REGULAR {
        override val text = "Regular time"
    };

    abstract val text: String
}


enum class TimeDisplayOption {
    RELATIVE_TO_NOW, RELATIVE_TO_START, TIME_BETWEEN, REGULAR;
}