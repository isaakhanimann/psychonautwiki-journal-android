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

package com.isaakhanimann.journal.ui.main.navigation.routers

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.isaakhanimann.journal.data.substances.AdministrationRoute
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

// argument keys
const val EXPERIENCE_ID_KEY = "experienceId"
const val RATING_ID_KEY = "ratingId"
const val INGESTION_ID_KEY = "ingestionId"
const val SUBSTANCE_NAME_KEY = "substanceName"
const val URL_KEY = "url"
const val ADMINISTRATION_ROUTE_KEY = "administrationRoute"
const val DOSE_KEY = "dose"
const val IS_ESTIMATE_KEY = "isEstimate"
const val UNITS_KEY = "units"
const val CATEGORY_KEY = "category"

// route starts
private const val ROUTE_START_EXPERIENCES = "experiences/"
private const val ROUTE_START_INGESTIONS = "ingestions/"
private const val ROUTE_START_EDIT_EXPERIENCE = "editExperience/"
private const val ROUTE_START_ADD_RATING = "addRating/"
private const val ROUTE_START_EDIT_RATING = "editRating/"
private const val ROUTE_START_EDIT_CUSTOM = "editCustom/"
private const val ROUTE_START_CHOOSE_ROUTE_CUSTOM = "chooseRouteCustom/"
private const val ROUTE_START_CHOOSE_DOSE_CUSTOM = "chooseDoseCustom/"
private const val ROUTE_START_SUBSTANCES = "substances/"
private const val ROUTE_START_CHECK_INTERACTIONS_SKIP_NOTHING = "checkInteractionsSkipNothing/"
private const val ROUTE_START_CHOOSE_ROUTE = "chooseRoute/"
private const val ROUTE_START_CHOOSE_DOSE = "chooseDose/"
private const val ROUTE_START_CHOOSE_TIME = "chooseTime/"
private const val ROUTE_START_SUBSTANCE_COMPANION = "substancesCompanion/"
private const val ROUTE_START_CATEGORY = "category/"
private const val ROUTE_START_URL = "url/"
private const val ROUTE_START_JOURNAL_TAB_URL = "journalTabUrl/"
private const val ROUTE_START_SAFER_TAB_URL = "saferTabUrl/"


sealed class ArgumentRouter(val route: String, val args: List<NamedNavArgument>) {
    object ExperienceRouter : ArgumentRouter(
        route = "$ROUTE_START_EXPERIENCES{$EXPERIENCE_ID_KEY}",
        args = listOf(navArgument(EXPERIENCE_ID_KEY) { type = NavType.IntType })
    )

    object IngestionRouter : ArgumentRouter(
        route = "$ROUTE_START_INGESTIONS{$INGESTION_ID_KEY}",
        args = listOf(navArgument(INGESTION_ID_KEY) { type = NavType.IntType })
    )

    object EditExperienceRouter : ArgumentRouter(
        route = "$ROUTE_START_EDIT_EXPERIENCE{$EXPERIENCE_ID_KEY}",
        args = listOf(navArgument(EXPERIENCE_ID_KEY) { type = NavType.IntType })
    )

    object AddRatingRouter : ArgumentRouter(
        route = "$ROUTE_START_ADD_RATING{$EXPERIENCE_ID_KEY}",
        args = listOf(navArgument(EXPERIENCE_ID_KEY) { type = NavType.IntType })
    )

    object EditRatingRouter : ArgumentRouter(
        route = "$ROUTE_START_EDIT_RATING{$RATING_ID_KEY}",
        args = listOf(navArgument(RATING_ID_KEY) { type = NavType.IntType })
    )

    object EditCustomRouter : ArgumentRouter(
        route = "$ROUTE_START_EDIT_CUSTOM{$SUBSTANCE_NAME_KEY}",
        args = listOf(navArgument(SUBSTANCE_NAME_KEY) { type = NavType.StringType })
    )

    object URLRouterOnSearchTab : ArgumentRouter(
        route = "$ROUTE_START_URL{$URL_KEY}",
        args = listOf(navArgument(URL_KEY) { type = NavType.StringType })
    )

    object JournalTabURLRouter : ArgumentRouter(
        route = "$ROUTE_START_JOURNAL_TAB_URL{$URL_KEY}",
        args = listOf(navArgument(URL_KEY) { type = NavType.StringType })
    )

    object URLRouterOnSaferTab : ArgumentRouter(
        route = "$ROUTE_START_SAFER_TAB_URL{$URL_KEY}",
        args = listOf(navArgument(URL_KEY) { type = NavType.StringType })
    )

    object CategoryRouter : ArgumentRouter(
        route = "$ROUTE_START_CATEGORY{$CATEGORY_KEY}",
        args = listOf(navArgument(CATEGORY_KEY) { type = NavType.StringType })
    )

    object CustomChooseRouteRouter : ArgumentRouter(
        route = "$ROUTE_START_CHOOSE_ROUTE_CUSTOM{$SUBSTANCE_NAME_KEY}",
        args = listOf(navArgument(SUBSTANCE_NAME_KEY) { type = NavType.StringType })
    )

    object CustomChooseDoseRouter : ArgumentRouter(
        route = "$ROUTE_START_CHOOSE_DOSE_CUSTOM{$SUBSTANCE_NAME_KEY}/{$ADMINISTRATION_ROUTE_KEY}",
        args = listOf(
            navArgument(SUBSTANCE_NAME_KEY) { type = NavType.StringType },
            navArgument(ADMINISTRATION_ROUTE_KEY) { type = NavType.StringType }
        )
    )

    object SubstanceRouter : ArgumentRouter(
        route = "$ROUTE_START_SUBSTANCES{$SUBSTANCE_NAME_KEY}",
        args = listOf(
            navArgument(SUBSTANCE_NAME_KEY) { type = NavType.StringType },
        )
    )

    object SubstanceCompanionRouter : ArgumentRouter(
        route = "$ROUTE_START_SUBSTANCE_COMPANION{$SUBSTANCE_NAME_KEY}",
        args = listOf(
            navArgument(SUBSTANCE_NAME_KEY) { type = NavType.StringType },
        )
    )

    object CheckInteractionsRouterSkipNothing : ArgumentRouter(
        route = "$ROUTE_START_CHECK_INTERACTIONS_SKIP_NOTHING{$SUBSTANCE_NAME_KEY}",
        args = listOf(
            navArgument(SUBSTANCE_NAME_KEY) { type = NavType.StringType },
        )
    )

    object ChooseRouteRouter : ArgumentRouter(
        route = "$ROUTE_START_CHOOSE_ROUTE{$SUBSTANCE_NAME_KEY}",
        args = listOf(
            navArgument(SUBSTANCE_NAME_KEY) { type = NavType.StringType },
        )
    )

    object ChooseDoseRouter : ArgumentRouter(
        route = "$ROUTE_START_CHOOSE_DOSE{$SUBSTANCE_NAME_KEY}/{$ADMINISTRATION_ROUTE_KEY}",
        args = listOf(
            navArgument(SUBSTANCE_NAME_KEY) { type = NavType.StringType },
            navArgument(ADMINISTRATION_ROUTE_KEY) { type = NavType.StringType }
        )
    )

    object ChooseTimeRouter : ArgumentRouter(
        route = "$ROUTE_START_CHOOSE_TIME{$SUBSTANCE_NAME_KEY}/{$ADMINISTRATION_ROUTE_KEY}/{$IS_ESTIMATE_KEY}/?$UNITS_KEY={$UNITS_KEY}/?$DOSE_KEY={$DOSE_KEY}",
        args = listOf(
            navArgument(SUBSTANCE_NAME_KEY) { type = NavType.StringType },
            navArgument(ADMINISTRATION_ROUTE_KEY) { type = NavType.StringType },
            navArgument(IS_ESTIMATE_KEY) { type = NavType.BoolType },
            navArgument(UNITS_KEY) { nullable = true },
            navArgument(DOSE_KEY) { nullable = true },
        )
    )
}

fun NavController.navigateToExperience(experienceId: Int) {
    navigate(ROUTE_START_EXPERIENCES + experienceId)
}

fun NavController.navigateToIngestion(ingestionId: Int) {
    navigate(ROUTE_START_INGESTIONS + ingestionId)
}

fun NavController.navigateToEditExperience(experienceId: Int) {
    navigate(ROUTE_START_EDIT_EXPERIENCE + experienceId)
}

fun NavController.navigateToAddRating(experienceId: Int) {
    navigate(ROUTE_START_ADD_RATING + experienceId)
}

fun NavController.navigateToEditRating(ratingId: Int) {
    navigate(ROUTE_START_EDIT_RATING + ratingId)
}

fun NavController.navigateToSubstanceScreen(substanceName: String) {
    navigate(ROUTE_START_SUBSTANCES + substanceName)
}

fun NavController.navigateToURLScreenOnSearchTab(url: String) {
    val encodedUrl = URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
    navigate(ROUTE_START_URL + encodedUrl)
}

fun NavController.navigateToURLInJournalTab(url: String) {
    val encodedUrl = URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
    navigate(ROUTE_START_JOURNAL_TAB_URL + encodedUrl)
}

fun NavController.navigateToURLInSaferTab(url: String) {
    val encodedUrl = URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
    navigate(ROUTE_START_SAFER_TAB_URL + encodedUrl)
}

fun NavController.navigateToEditCustomSubstance(substanceName: String) {
    navigate(ROUTE_START_EDIT_CUSTOM + substanceName)
}

fun NavController.navigateToChooseCustomRoute(substanceName: String) {
    navigate(ROUTE_START_CHOOSE_ROUTE_CUSTOM + substanceName)
}

fun NavController.navigateToSubstanceCompanionScreen(substanceName: String) {
    navigate(ROUTE_START_SUBSTANCE_COMPANION + substanceName)
}

fun NavController.navigateToCategoryScreen(categoryName: String) {
    navigate(ROUTE_START_CATEGORY + categoryName)
}

fun NavController.navigateToCheckInteractionsSkipNothing(substanceName: String) {
    navigate("$ROUTE_START_CHECK_INTERACTIONS_SKIP_NOTHING$substanceName")
}

fun NavController.navigateToChooseRoute(substanceName: String) {
    navigate("$ROUTE_START_CHOOSE_ROUTE$substanceName")
}

fun NavController.navigateToChooseDose(
    substanceName: String,
    administrationRoute: AdministrationRoute
) {
    navigate("$ROUTE_START_CHOOSE_DOSE$substanceName/${administrationRoute.name}")
}

fun NavController.navigateToChooseDoseCustom(
    substanceName: String,
    administrationRoute: AdministrationRoute
) {
    navigate("$ROUTE_START_CHOOSE_DOSE_CUSTOM$substanceName/${administrationRoute.name}")
}

fun NavController.navigateToChooseTimeAndMaybeColor(
    substanceName: String,
    administrationRoute: AdministrationRoute,
    units: String?,
    isEstimate: Boolean,
    dose: Double?,
) {
    navigate("$ROUTE_START_CHOOSE_TIME$substanceName/${administrationRoute.name}/$isEstimate/?$UNITS_KEY=$units/?$DOSE_KEY=$dose")
}