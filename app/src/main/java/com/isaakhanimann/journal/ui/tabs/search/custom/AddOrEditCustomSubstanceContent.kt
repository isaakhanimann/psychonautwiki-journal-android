package com.isaakhanimann.journal.ui.tabs.search.custom

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.isaakhanimann.journal.data.room.experiences.entities.custom.CustomDoseInfo
import com.isaakhanimann.journal.data.room.experiences.entities.custom.CustomDurationInfo
import com.isaakhanimann.journal.data.room.experiences.entities.custom.CustomRoaInfo
import com.isaakhanimann.journal.data.room.experiences.entities.custom.SerializableDurationRange
import com.isaakhanimann.journal.data.room.experiences.entities.custom.SerializableDurationUnits
import com.isaakhanimann.journal.data.substances.AdministrationRoute
import com.isaakhanimann.journal.ui.theme.horizontalPadding

@Composable
fun AddOrEditCustomSubstanceContent(
    padding: PaddingValues,
    name: String,
    onNameChange: (String) -> Unit,
    units: String,
    onUnitsChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    roaInfos: List<CustomRoaInfo>,
    onAddRoa: (CustomRoaInfo) -> Unit,
    onRemoveRoa: (CustomRoaInfo) -> Unit,
    onUpdateRoa: (Int, CustomRoaInfo) -> Unit
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .padding(padding)
            .padding(horizontal = horizontalPadding)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            label = { Text("Name") },
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            keyboardOptions = KeyboardOptions.Default.copy(
                capitalization = KeyboardCapitalization.Words
            ),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = units,
            onValueChange = onUnitsChange,
            label = { Text("Default Units") },
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedButton(onClick = { onUnitsChange("µg") }) { Text(text = "µg") }
            OutlinedButton(onClick = { onUnitsChange("mg") }) { Text(text = "mg") }
            OutlinedButton(onClick = { onUnitsChange("g") }) { Text(text = "g") }
            OutlinedButton(onClick = { onUnitsChange("mL") }) { Text(text = "mL") }
        }
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = description,
            onValueChange = onDescriptionChange,
            label = { Text("Description") },
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            keyboardOptions = KeyboardOptions.Default.copy(
                capitalization = KeyboardCapitalization.Sentences
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text("Routes of Administration", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        roaInfos.forEachIndexed { index, roaInfo ->
            RoaInfoEditor(
                roaInfo = roaInfo,
                onUpdate = { onUpdateRoa(index, it) },
                onRemove = { onRemoveRoa(roaInfo) }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(
            onClick = {
                val defaultRoa = AdministrationRoute.entries.firstOrNull { route ->
                    roaInfos.none { it.administrationRoute == route }
                } ?: AdministrationRoute.ORAL
                onAddRoa(
                    CustomRoaInfo(
                        administrationRoute = defaultRoa,
                        doseInfo = CustomDoseInfo(),
                        durationInfo = CustomDurationInfo()
                    )
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Route")
            Spacer(modifier = Modifier.width(4.dp))
            Text("Add Route of Administration")
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun RoaInfoEditor(
    roaInfo: CustomRoaInfo,
    onUpdate: (CustomRoaInfo) -> Unit,
    onRemove: () -> Unit
) {
    var isRoaMenuExpanded by remember { mutableStateOf(false) }

    Card(elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Route", style = MaterialTheme.typography.titleSmall, modifier = Modifier.weight(1f))
                OutlinedButton(onClick = { isRoaMenuExpanded = true }) {
                    Text(roaInfo.administrationRoute.name.lowercase().replaceFirstChar { it.uppercase() })
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                }
                DropdownMenu(
                    expanded = isRoaMenuExpanded,
                    onDismissRequest = { isRoaMenuExpanded = false }
                ) {
                    AdministrationRoute.entries.forEach { route ->
                        DropdownMenuItem(
                            text = { Text(route.name.lowercase().replaceFirstChar { it.uppercase() }) },
                            onClick = {
                                onUpdate(roaInfo.copy(administrationRoute = route))
                                isRoaMenuExpanded = false
                            }
                        )
                    }
                }
                IconButton(onClick = onRemove) {
                    Icon(Icons.Default.Delete, contentDescription = "Remove Route")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text("Dosage", style = MaterialTheme.typography.titleSmall)
            val doseInfo = roaInfo.doseInfo ?: CustomDoseInfo()
            Row {
                DoseTextField("Light", doseInfo.lightMin) {
                    onUpdate(roaInfo.copy(doseInfo = doseInfo.copy(lightMin = it)))
                }
                DoseTextField("Common", doseInfo.commonMin) {
                    onUpdate(roaInfo.copy(doseInfo = doseInfo.copy(commonMin = it)))
                }
            }
            Row {
                DoseTextField("Strong", doseInfo.strongMin) {
                    onUpdate(roaInfo.copy(doseInfo = doseInfo.copy(strongMin = it)))
                }
                DoseTextField("Heavy", doseInfo.heavyMin) {
                    onUpdate(roaInfo.copy(doseInfo = doseInfo.copy(heavyMin = it)))
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text("Duration", style = MaterialTheme.typography.titleSmall)
            val durationInfo = roaInfo.durationInfo ?: CustomDurationInfo()
            DurationRangeEditor("Onset", durationInfo.onset) {
                onUpdate(roaInfo.copy(durationInfo = durationInfo.copy(onset = it)))
            }
            DurationRangeEditor("Comeup", durationInfo.comeup) {
                onUpdate(roaInfo.copy(durationInfo = durationInfo.copy(comeup = it)))
            }
            DurationRangeEditor("Peak", durationInfo.peak) {
                onUpdate(roaInfo.copy(durationInfo = durationInfo.copy(peak = it)))
            }
            DurationRangeEditor("Offset", durationInfo.offset) {
                onUpdate(roaInfo.copy(durationInfo = durationInfo.copy(offset = it)))
            }
            DurationRangeEditor("Total", durationInfo.total) {
                onUpdate(roaInfo.copy(durationInfo = durationInfo.copy(total = it)))
            }
            DurationRangeEditor("Afterglow", durationInfo.afterglow) {
                onUpdate(roaInfo.copy(durationInfo = durationInfo.copy(afterglow = it)))
            }
        }
    }
}

@Composable
private fun RowScope.DoseTextField(label: String, value: Double?, onValueChange: (Double?) -> Unit) {
    OutlinedTextField(
        value = value?.toString() ?: "",
        onValueChange = { onValueChange(it.toDoubleOrNull()) },
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier
            .weight(1f)
            .padding(4.dp),
        singleLine = true
    )
}

@Composable
private fun DurationRangeEditor(label: String, value: SerializableDurationRange?, onValueChange: (SerializableDurationRange?) -> Unit) {
    var isUnitsMenuExpanded by remember { mutableStateOf(false) }
    val currentRange = value ?: SerializableDurationRange(null, null, null)

    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
        Text(label, modifier = Modifier.width(80.dp))
        OutlinedTextField(
            value = currentRange.min?.toString() ?: "",
            onValueChange = { min ->
                onValueChange(currentRange.copy(min = min.toFloatOrNull()))
            },
            label = { Text("Min") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.weight(1f),
            singleLine = true
        )
        Spacer(modifier = Modifier.width(8.dp))
        OutlinedTextField(
            value = currentRange.max?.toString() ?: "",
            onValueChange = { max ->
                onValueChange(currentRange.copy(max = max.toFloatOrNull()))
            },
            label = { Text("Max") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.weight(1f),
            singleLine = true
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            OutlinedButton(onClick = { isUnitsMenuExpanded = true }) {
                Text(currentRange.units?.text ?: "Units")
                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
            }
            DropdownMenu(
                expanded = isUnitsMenuExpanded,
                onDismissRequest = { isUnitsMenuExpanded = false }
            ) {
                SerializableDurationUnits.entries.forEach { unit ->
                    DropdownMenuItem(
                        text = { Text(unit.text) },
                        onClick = {
                            onValueChange(currentRange.copy(units = unit))
                            isUnitsMenuExpanded = false
                        }
                    )
                }
            }
        }
    }
}