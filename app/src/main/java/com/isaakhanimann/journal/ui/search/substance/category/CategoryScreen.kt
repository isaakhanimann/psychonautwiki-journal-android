package com.isaakhanimann.journal.ui.search.substance.category

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.isaakhanimann.journal.data.substances.classes.Category
import com.isaakhanimann.journal.ui.stats.EmptyScreenDisclaimer
import com.isaakhanimann.journal.ui.theme.horizontalPadding

@Composable
fun CategoryScreen(
    viewModel: CategoryViewModel = hiltViewModel()
) {
    CategoryScreen(category = viewModel.category)
}

@Preview
@Composable
fun CategoryPreview() {
    CategoryScreen(
        category = Category(
            name = "psychedelic",
            description = "Psychedelics are drugs which alter the perception, causing a number of mental effects which manifest in many forms including altered states of consciousness, visual or tactile effects.",
            url = "https://psychonautwiki.org/wiki/Psychedelics",
            color = Color.Red
        )
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(category: Category?) {
    if (category == null) {
        EmptyScreenDisclaimer(
            title = "Category Not Found",
            description = "An error happened, please navigate back."
        )
    } else {
        val uriHandler = LocalUriHandler.current
        Scaffold(
            topBar = { TopAppBar(title = { Text(category.name.replaceFirstChar { it.uppercase() }) }) },
            floatingActionButton = {
                if (category.url != null) {
                    ExtendedFloatingActionButton(
                        onClick = { uriHandler.openUri(category.url) },
                        icon = {
                            Icon(
                                Icons.Default.OpenInBrowser,
                                contentDescription = "Open Link"
                            )
                        },
                        text = { Text("More Info") },
                    )
                }
            }
        ) { padding ->
            Text(
                text = category.description,
                textAlign = TextAlign.Justify,
                modifier = Modifier
                    .padding(padding)
                    .padding(horizontal = horizontalPadding, vertical = 10.dp)
            )
        }
    }
}