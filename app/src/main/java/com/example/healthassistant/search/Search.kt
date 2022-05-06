package com.example.healthassistant.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.healthassistant.Screen

data class Substance(val name: String, val alternativeNames: List<String>)

val previewSubstances = arrayListOf(
    Substance("MDMA", alternativeNames = listOf("Molly", "Ecstasy")),
    Substance("LSD", alternativeNames = listOf("Lucy", "Acid", "Tabs")),
    Substance("Cocaine", alternativeNames = listOf("Coke", "Crack", "Blow")),
    Substance("Ketamine", alternativeNames = listOf("K", "Ket", "Special K")),
    Substance("Heroin", alternativeNames = listOf("H", "Smack", "Brown")),
    Substance("Cannabis", alternativeNames = listOf("Weed", "Marijuana", "Pot")),
    Substance("2C-B", alternativeNames = listOf("Nexus", "Tusi")),
    Substance("Amphetamine", alternativeNames = listOf("Speed", "Adderall", "Pep")),
    Substance("GHB", alternativeNames = listOf("G")),
    Substance("Caffeine", alternativeNames = listOf()),
    Substance("Mescaline", alternativeNames = listOf()),
    Substance("Nicotine", alternativeNames = listOf())
)

@Preview(showBackground = true)
@Composable
fun SearchPreview() {
    val navController = rememberNavController()
    Search(navController = navController)
}

@Composable
fun Search(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Substances") }
            )
        }
    ) {
        Column {
            val textState = remember { mutableStateOf(TextFieldValue(""))}
            SearchField(state = textState)
            SubstanceList(navController = navController, state = textState)
        }
    }
}

@Composable
fun SubstanceList(navController: NavController, state: MutableState<TextFieldValue>) {
    val substances: ArrayList<Substance> = previewSubstances
    val searchedText = state.value.text
    val filteredSubstances = if (searchedText.isEmpty()) {
        substances
    } else {
        val resultList = ArrayList<Substance>()
        for (substance in substances) {
            if (substance.name.lowercase()
                    .contains(searchedText.lowercase())
            ) {
                resultList.add(substance)
            }
        }
        resultList
    }
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(filteredSubstances) { substance ->
            SubstanceRow(substance = substance, onTap = { substanceName ->
                navController.navigate(Screen.Search.route + "/" + substanceName)
            })
        }
    }
}

@Composable
fun SearchField(state: MutableState<TextFieldValue>) {
    val focusManager = LocalFocusManager.current
    TextField(
        value = state.value,
        onValueChange = { value ->
            state.value = value
        },
        modifier = Modifier
            .fillMaxWidth(),
        textStyle = TextStyle(color = Color.White, fontSize = 18.sp),
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = "",
                modifier = Modifier
                    .padding(15.dp)
                    .size(24.dp)
            )
        },
        trailingIcon = {
            if (state.value != TextFieldValue("")) {
                IconButton(
                    onClick = {
                        state.value =
                            TextFieldValue("")
                    }
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "",
                        modifier = Modifier
                            .padding(15.dp)
                            .size(24.dp)
                    )
                }
            }
        },
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        singleLine = true,
        shape = RectangleShape,
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.White,
            cursorColor = Color.White,
            leadingIconColor = Color.White,
            trailingIconColor = Color.White,
            backgroundColor = MaterialTheme.colors.primary,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        )
    )
}

@Composable
fun SubstanceRow(substance: Substance, onTap: (String) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable {
            onTap(substance.name)
        },
        elevation = 4.dp
    ) {
        Row(modifier = Modifier.padding(horizontal = 6.dp, vertical = 8.dp), verticalAlignment = Alignment.Bottom) {
            Text(text = substance.name, modifier = Modifier.padding(end = 10.dp), style = MaterialTheme.typography.body1)
            val altNamesString = substance.alternativeNames.fold("") { acc, string -> "$acc, $string" }.removePrefix(", ")
            Text(text = altNamesString, style = MaterialTheme.typography.body2)
        }
    }
}