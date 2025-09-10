package com.isaakhanimann.journal.ui.tabs.settings.customsubstances

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.isaakhanimann.journal.R

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CustomSubstanceManagementScreen(
    navigateBack: () -> Unit,
    navigateToAddCustomSubstance: () -> Unit,
    navigateToEditCustomSubstance: (Int) -> Unit,
    viewModel: CustomSubstanceManagementViewModel = hiltViewModel()
) {
    val substances by viewModel.allSubstances.collectAsState()
    val selectionMode by viewModel.selectionMode.collectAsState()
    val selectedIds by viewModel.selectedSubstances.collectAsState()
    val context = LocalContext.current

    val exportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json"),
        onResult = { uri ->
            if (uri != null) {
                viewModel.exportSelection(context, uri) { }
            }
        }
    )

    val importLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            if (uri != null) {
                viewModel.importSubstances(context, uri) { }
            }
        }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (selectionMode == SelectionMode.None) "Custom Substances" else "${selectedIds.size} selected") },
                navigationIcon = {
                    if (selectionMode != SelectionMode.None) {
                        IconButton(onClick = viewModel::clearSelection) {
                            Icon(Icons.Default.Close, contentDescription = "Clear Selection")
                        }
                    } else {
                        IconButton(onClick = navigateBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                },
                actions = {
                    if (selectionMode != SelectionMode.None) {
                        IconButton(onClick = viewModel::selectAll) {
                            Icon(Icons.Default.CheckCircle, contentDescription = "Select All")
                        }
                        IconButton(
                            onClick = { exportLauncher.launch("custom_substances.json") },
                            enabled = selectedIds.isNotEmpty()
                        ) {
                            Icon(Icons.Default.Save, contentDescription = "Export")
                        }
                    } else {
                        var menuExpanded by remember { mutableStateOf(false) }
                        IconButton(onClick = { menuExpanded = true }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "More")
                        }
                        DropdownMenu(expanded = menuExpanded, onDismissRequest = { menuExpanded = false }) {
                            DropdownMenuItem(
                                text = { Text("Import from file") },
                                onClick = {
                                    importLauncher.launch("application/json")
                                    menuExpanded = false
                                }
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            if (selectionMode == SelectionMode.None) {
                FloatingActionButton(onClick = navigateToAddCustomSubstance) {
                    Icon(Icons.Default.Add, contentDescription = "Add Custom Substance")
                }
            }
        }
    ) { padding ->
        if (substances.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("No custom substances found.")
            }
        } else {
            LazyColumn(modifier = Modifier.padding(padding)) {
                items(substances, key = { it.id }) { substance ->
                    val isSelected = selectedIds.contains(substance.id)
                    ListItem(
                        headlineContent = { Text(substance.name) },
                        supportingContent = { if (substance.description.isNotBlank()) Text(substance.description) },
                        modifier = Modifier.combinedClickable(
                            onClick = {
                                if (selectionMode != SelectionMode.None) {
                                    viewModel.toggleSelection(substance.id)
                                } else {
                                    navigateToEditCustomSubstance(substance.id)
                                }
                            },
                            onLongClick = {
                                viewModel.toggleSelection(substance.id)
                            }
                        ),
                        leadingContent = {
                            if (selectionMode != SelectionMode.None) {
                                Checkbox(
                                    checked = isSelected,
                                    onCheckedChange = { viewModel.toggleSelection(substance.id) }
                                )
                            }
                        },
                        trailingContent = {
                            if (selectionMode == SelectionMode.None) {
                                Icon(Icons.AutoMirrored.Filled.ArrowRight, contentDescription = "Arrow Right")
                            }
                        }
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}