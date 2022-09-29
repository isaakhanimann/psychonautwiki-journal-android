package com.isaakhanimann.healthassistant.ui.search.substance.category

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.isaakhanimann.healthassistant.data.substances.classes.Category
import com.isaakhanimann.healthassistant.ui.stats.EmptyScreenDisclaimer
import com.isaakhanimann.healthassistant.ui.utils.JournalTopAppBar

@Composable
fun CategoryScreen(
    viewModel: CategoryViewModel = hiltViewModel()
) {
    CategoryScreen(category = viewModel.category)
}


@Composable
fun CategoryScreen(category: Category?) {
    if (category == null) {
        EmptyScreenDisclaimer(
            title = "Category Not Found",
            description = "An error happened, please navigate back."
        )
    } else {
        Scaffold(topBar = { JournalTopAppBar(title = category.name) }) {

        }
    }
}