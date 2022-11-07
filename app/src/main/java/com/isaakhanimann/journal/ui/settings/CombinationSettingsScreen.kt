package com.isaakhanimann.journal.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.isaakhanimann.journal.ui.theme.horizontalPadding

@Composable
fun CombinationSettingsScreen(viewModel: CombinationSettingsViewModel = hiltViewModel()) {
    CombinationSettingsScreen(
        isSkippingInteractions = viewModel.skipFlow.collectAsState().value,
        toggleIsSkipping = viewModel::toggle
    )
}

@Preview
@Composable
fun CombinationSettingsPreview() {
    CombinationSettingsScreen(
        isSkippingInteractions = false,
        toggleIsSkipping = {}
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CombinationSettingsScreen(
    isSkippingInteractions: Boolean,
    toggleIsSkipping: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Combinations") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(horizontal = horizontalPadding)
        ) {
            Spacer(modifier = Modifier.height(5.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Skip Interaction Check When Logging")
                Switch(checked = isSkippingInteractions, onCheckedChange = { toggleIsSkipping() })
            }
        }
    }
}




