/*
 * Copyright (c) 2022-2023. Isaak Hanimann.
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

package com.isaakhanimann.journal.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.isaakhanimann.journal.R

val DOSE_DISCLAIMER
    @Composable get() = stringResource(R.string.dose_disclaimer)

const val VOLUMETRIC_DOSE_ARTICLE_URL = "https://psychonautwiki.org/wiki/Volumetric_liquid_dosing"

const val VERSION_NAME = "11.11"

val FULL_STOMACH_DISCLAIMER
    @Composable get() = stringResource(R.string.full_stomach_disclaimer)

const val YOU = "You"