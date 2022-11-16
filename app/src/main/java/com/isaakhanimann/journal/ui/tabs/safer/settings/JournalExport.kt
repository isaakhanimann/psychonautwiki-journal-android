/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 3.0. If a copy of the MPL was not distributed with this file, You can obtain one at https://www.gnu.org/licenses/gpl-3.0.en.html.
 */

package com.isaakhanimann.journal.ui.tabs.safer.settings

import com.isaakhanimann.journal.data.room.experiences.entities.CustomSubstance
import com.isaakhanimann.journal.data.room.experiences.entities.Experience
import com.isaakhanimann.journal.data.room.experiences.entities.Ingestion
import com.isaakhanimann.journal.data.room.experiences.entities.SubstanceCompanion
import kotlinx.serialization.Serializable

@Serializable
data class JournalExport(
    val ingestions: List<Ingestion>,
    val experiences: List<Experience>,
    val substanceCompanions: List<SubstanceCompanion>,
    val customSubstances: List<CustomSubstance>
)