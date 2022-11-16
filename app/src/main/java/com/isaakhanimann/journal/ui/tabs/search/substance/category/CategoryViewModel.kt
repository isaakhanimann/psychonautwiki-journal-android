/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 3.0. If a copy of the MPL was not distributed with this file, You can obtain one at https://www.gnu.org/licenses/gpl-3.0.en.html.
 */

package com.isaakhanimann.journal.ui.tabs.search.substance.category

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.isaakhanimann.journal.data.substances.repositories.SubstanceRepository
import com.isaakhanimann.journal.ui.main.navigation.routers.CATEGORY_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    substanceRepo: SubstanceRepository,
    state: SavedStateHandle
) : ViewModel() {
    val categoryName = state.get<String>(CATEGORY_KEY)!!
    val category = substanceRepo.getCategory(categoryName)
}