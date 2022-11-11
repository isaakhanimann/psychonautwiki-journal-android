/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 3.0. If a copy of the MPL was not distributed with this file, You can obtain one at https://www.gnu.org/licenses/gpl-3.0.en.html.
 */

package com.isaakhanimann.journal.data.substances.classes.roa

enum class DurationUnits(val text: String) {
    SECONDS("seconds") {
        override val inSecondsMultiplier = 1
        override val shortText = "s"
    },
    MINUTES("minutes") {
        override val inSecondsMultiplier = 60
        override val shortText = "m"
    },
    HOURS("hours") {
        override val inSecondsMultiplier = 3600
        override val shortText = "h"
    },
    DAYS("days") {
        override val inSecondsMultiplier = 86400
        override val shortText = "d"
    };

    abstract val inSecondsMultiplier: Int
    abstract val shortText: String
}