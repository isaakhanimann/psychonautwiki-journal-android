package com.isaakhanimann.journal.ui.tabs.journal.addingestion.dose

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.isaakhanimann.journal.ui.tabs.search.substance.roa.toPreservedString
import com.isaakhanimann.journal.ui.tabs.search.substance.roa.toReadableString

@Composable
fun StandardDeviationExplanation(
    modifier: Modifier = Modifier,
    mean: Double,
    standardDeviation: Double,
    unit: String
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.Start) {
        Text("${mean.toPreservedString()}±${standardDeviation.toPreservedString()} $unit means:")
        Text(
            getOneStandardDeviationText(
                mean = mean,
                standardDeviation = standardDeviation,
                unit = unit
            )
        )
        Text(
            getTwoStandardDeviationText(
                mean = mean,
                standardDeviation = standardDeviation,
                unit = unit
            )
        )
    }
}

@Preview
@Composable
private fun StandardDeviationExplanationPreview() {
    StandardDeviationExplanation(mean = 20.0, standardDeviation = 3.0, unit = "mg")
}


// https://online.stat.psu.edu/stat200/lesson/2/2.2/2.2.7#:~:text=The%2095%25%20Rule%20states%20that,mean%20on%20a%20normal%20distribution
private fun getOneStandardDeviationText(
    mean: Double,
    standardDeviation: Double,
    unit: String
): String {
    val lowerRange = mean - standardDeviation
    val higherRange = mean + standardDeviation
    return "${lowerRange.toReadableString()}-${higherRange.toReadableString()} $unit in 68% of cases"
}

private fun getTwoStandardDeviationText(
    mean: Double,
    standardDeviation: Double,
    unit: String
): String {
    val twoStandardDeviations = 2 * standardDeviation
    val lowerRange = mean - twoStandardDeviations
    val higherRange = mean + twoStandardDeviations
    return "${lowerRange.toReadableString()}-${higherRange.toReadableString()} $unit in 95% of cases"
}
