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

// argument keys
const val EXPERIENCE_ID_KEY = "experienceId"
const val CONSUMER_NAME_KEY = "consumerName"
const val RATING_ID_KEY = "ratingId"
const val TIMED_NOTE_ID_KEY = "timedNoteId"
const val INGESTION_ID_KEY = "ingestionId"
const val SUBSTANCE_NAME_KEY = "substanceName"
const val CUSTOM_SUBSTANCE_ID_KEY = "customSubstanceId"
const val ADMINISTRATION_ROUTE_KEY = "administrationRoute"
const val DOSE_KEY = "dose"
const val ESTIMATED_DOSE_STANDARD_DEVIATION_KEY = "estimatedDoseStandardDeviation"
const val CUSTOM_UNIT_ID_KEY = "customUnitId"
const val IS_ESTIMATE_KEY = "isEstimate"
const val UNITS_KEY = "units"
const val CATEGORY_KEY = "category"

// route starts
private const val ROUTE_START_EXPERIENCES = "experiences/"
private const val ROUTE_START_TIMELINE_SCREEN = "timeline/"
private const val ROUTE_START_INGESTIONS = "ingestions/"
private const val ROUTE_START_EDIT_CUSTOM_UNIT = "editCustomUnit/"
private const val ROUTE_START_CHOOSE_DOSE_CUSTOM_UNIT = "chooseDoseCustomUnit/"
private const val ROUTE_START_EDIT_EXPERIENCE = "editExperience/"
private const val ROUTE_START_ADD_RATING = "addRating/"
private const val ROUTE_START_ADD_TIMED_NOTE = "addTimedNote/"
private const val ROUTE_START_EDIT_RATING = "editRating/"
private const val ROUTE_START_EDIT_TIMED_NOTE = "editTimedNote/"
private const val ROUTE_START_EDIT_CUSTOM = "editCustom/"
private const val ROUTE_START_CHOOSE_ROUTE_CUSTOM = "chooseRouteCustom/"
private const val ROUTE_START_CHOOSE_DOSE_CUSTOM = "chooseDoseCustom/"
private const val ROUTE_START_SUBSTANCES = "substances/"
private const val ROUTE_START_CHECK_INTERACTIONS = "checkInteractionsSkipNothing/"
private const val ROUTE_START_CHECK_SAFER_USE = "checkSaferUse/"
private const val ROUTE_START_CHOOSE_ROUTE_OF_ADD_INGESTION = "chooseRouteOfAddIngestion/"
private const val ROUTE_START_OF_ADD_CUSTOM_UNIT = "chooseRouteOfAddCustomUnit/"
private const val ROUTE_START_CHOOSE_DOSE = "chooseDose/"
private const val ROUTE_START_CHOOSE_TIME = "chooseTime/"
private const val ROUTE_START_FINISH_ADD_CUSTOM_UNIT = "finishAddCustomUnit/"
private const val ROUTE_START_SUBSTANCE_COMPANION = "substancesCompanion/"
private const val ROUTE_START_CATEGORY = "category/"

sealed class ArgumentRouter(val route: String, val args: List<NamedNavArgument>) {
    data object ExperienceRouter : ArgumentRouter(
        route = "$ROUTE_START_EXPERIENCES{$EXPERIENCE_ID_KEY}",
        args = listOf(navArgument(EXPERIENCE_ID_KEY) { type = NavType.IntType })
    )

    data object IngestionRouter : ArgumentRouter(
        route = "$ROUTE_START_INGESTIONS{$INGESTION_ID_KEY}",
        args = listOf(navArgument(INGESTION_ID_KEY) { type = NavType.IntType })
    )

    data object EditCustomUnitRouter : ArgumentRouter(
        route = "$ROUTE_START_EDIT_CUSTOM_UNIT{$CUSTOM_UNIT_ID_KEY}",
        args = listOf(navArgument(CUSTOM_UNIT_ID_KEY) { type = NavType.IntType })
    )

    data object ChooseDoseCustomUnitRouter : ArgumentRouter(
        route = "$ROUTE_START_CHOOSE_DOSE_CUSTOM_UNIT{$CUSTOM_UNIT_ID_KEY}",
        args = listOf(navArgument(CUSTOM_UNIT_ID_KEY) { type = NavType.IntType })
    )

    data object EditExperienceRouter : ArgumentRouter(
        route = "$ROUTE_START_EDIT_EXPERIENCE{$EXPERIENCE_ID_KEY}",
        args = listOf(navArgument(EXPERIENCE_ID_KEY) { type = NavType.IntType })
    )

    data object AddRatingRouter : ArgumentRouter(
        route = "$ROUTE_START_ADD_RATING{$EXPERIENCE_ID_KEY}",
        args = listOf(navArgument(EXPERIENCE_ID_KEY) { type = NavType.IntType })
    )

    data object AddTimedNoteRouter : ArgumentRouter(
        route = "$ROUTE_START_ADD_TIMED_NOTE{$EXPERIENCE_ID_KEY}",
        args = listOf(navArgument(EXPERIENCE_ID_KEY) { type = NavType.IntType })
    )

    data object EditRatingRouter : ArgumentRouter(
        route = "$ROUTE_START_EDIT_RATING{$RATING_ID_KEY}",
        args = listOf(navArgument(RATING_ID_KEY) { type = NavType.IntType })
    )

    data object EditTimedNoteRouter : ArgumentRouter(
        route = "$ROUTE_START_EDIT_TIMED_NOTE{$TIMED_NOTE_ID_KEY}/{$EXPERIENCE_ID_KEY}",
        args = listOf(
            navArgument(TIMED_NOTE_ID_KEY) { type = NavType.IntType },
            navArgument(EXPERIENCE_ID_KEY) { type = NavType.IntType }
        )
    )

    data object TimelineScreenRouter : ArgumentRouter(
        route = "$ROUTE_START_TIMELINE_SCREEN{$CONSUMER_NAME_KEY}/{$EXPERIENCE_ID_KEY}",
        args = listOf(
            navArgument(CONSUMER_NAME_KEY) { type = NavType.StringType },
            navArgument(EXPERIENCE_ID_KEY) { type = NavType.IntType }
        )
    )

    data object EditCustomRouter : ArgumentRouter(
        route = "$ROUTE_START_EDIT_CUSTOM{$CUSTOM_SUBSTANCE_ID_KEY}",
        args = listOf(navArgument(CUSTOM_SUBSTANCE_ID_KEY) { type = NavType.IntType })
    )

    data object CategoryRouter : ArgumentRouter(
        route = "$ROUTE_START_CATEGORY{$CATEGORY_KEY}",
        args = listOf(navArgument(CATEGORY_KEY) { type = NavType.StringType })
    )

    data object CustomChooseRouteRouter : ArgumentRouter(
        route = "$ROUTE_START_CHOOSE_ROUTE_CUSTOM{$CUSTOM_SUBSTANCE_ID_KEY}",
        args = listOf(navArgument(CUSTOM_SUBSTANCE_ID_KEY) { type = NavType.IntType })
    )

    data object CustomChooseDoseRouter : ArgumentRouter(
        route = "$ROUTE_START_CHOOSE_DOSE_CUSTOM{$CUSTOM_SUBSTANCE_ID_KEY}/{$ADMINISTRATION_ROUTE_KEY}",
        args = listOf(
            navArgument(CUSTOM_SUBSTANCE_ID_KEY) { type = NavType.IntType },
            navArgument(ADMINISTRATION_ROUTE_KEY) { type = NavType.StringType }
        )
    )

    data object SubstanceRouter : ArgumentRouter(
        route = "$ROUTE_START_SUBSTANCES{$SUBSTANCE_NAME_KEY}",
        args = listOf(
            navArgument(SUBSTANCE_NAME_KEY) { type = NavType.StringType },
        )
    )

    data object SubstanceCompanionRouter : ArgumentRouter(
        route = "$ROUTE_START_SUBSTANCE_COMPANION{$SUBSTANCE_NAME_KEY}/?$CONSUMER_NAME_KEY={$CONSUMER_NAME_KEY}",
        args = listOf(
            navArgument(SUBSTANCE_NAME_KEY) { type = NavType.StringType },
            navArgument(CONSUMER_NAME_KEY) { nullable = true },
        )
    )

    data object CheckInteractionsRouter : ArgumentRouter(
        route = "$ROUTE_START_CHECK_INTERACTIONS{$SUBSTANCE_NAME_KEY}",
        args = listOf(
            navArgument(SUBSTANCE_NAME_KEY) { type = NavType.StringType },
        )
    )

    data object CheckSaferUseRouter : ArgumentRouter(
        route = "$ROUTE_START_CHECK_SAFER_USE{$SUBSTANCE_NAME_KEY}",
        args = listOf(
            navArgument(SUBSTANCE_NAME_KEY) { type = NavType.StringType },
        )
    )

    data object ChooseRouteOfAddIngestionRouter : ArgumentRouter(
        route = "$ROUTE_START_CHOOSE_ROUTE_OF_ADD_INGESTION{$SUBSTANCE_NAME_KEY}",
        args = listOf(
            navArgument(SUBSTANCE_NAME_KEY) { type = NavType.StringType },
        )
    )

    data object ChooseRouteOfAddCustomUnitRouter : ArgumentRouter(
        route = "$ROUTE_START_OF_ADD_CUSTOM_UNIT{$SUBSTANCE_NAME_KEY}",
        args = listOf(
            navArgument(SUBSTANCE_NAME_KEY) { type = NavType.StringType },
        )
    )

    data object ChooseDoseRouter : ArgumentRouter(
        route = "$ROUTE_START_CHOOSE_DOSE{$SUBSTANCE_NAME_KEY}/{$ADMINISTRATION_ROUTE_KEY}",
        args = listOf(
            navArgument(SUBSTANCE_NAME_KEY) { type = NavType.StringType },
            navArgument(ADMINISTRATION_ROUTE_KEY) { type = NavType.StringType }
        )
    )

    data object ChooseTimeRouter : ArgumentRouter(
        route = "$ROUTE_START_CHOOSE_TIME{$ADMINISTRATION_ROUTE_KEY}/{$IS_ESTIMATE_KEY}/?$UNITS_KEY={$UNITS_KEY}/?$DOSE_KEY={$DOSE_KEY}/?$ESTIMATED_DOSE_STANDARD_DEVIATION_KEY={$ESTIMATED_DOSE_STANDARD_DEVIATION_KEY}/?$SUBSTANCE_NAME_KEY={$SUBSTANCE_NAME_KEY}/?$CUSTOM_UNIT_ID_KEY={$CUSTOM_UNIT_ID_KEY}/?$CUSTOM_SUBSTANCE_ID_KEY={$CUSTOM_SUBSTANCE_ID_KEY}",
        args = listOf(
            navArgument(ADMINISTRATION_ROUTE_KEY) { type = NavType.StringType },
            navArgument(IS_ESTIMATE_KEY) { type = NavType.BoolType },
            navArgument(UNITS_KEY) { nullable = true },
            navArgument(DOSE_KEY) { nullable = true },
            navArgument(ESTIMATED_DOSE_STANDARD_DEVIATION_KEY) { nullable = true },
            navArgument(SUBSTANCE_NAME_KEY) { nullable = true },
            navArgument(CUSTOM_UNIT_ID_KEY) { nullable = true },
            navArgument(CUSTOM_SUBSTANCE_ID_KEY) { nullable = true },
        )
    )

    data object FinishAddCustomUnitRouter : ArgumentRouter(
        route = "$ROUTE_START_FINISH_ADD_CUSTOM_UNIT{$SUBSTANCE_NAME_KEY}/{$ADMINISTRATION_ROUTE_KEY}",
        args = listOf(
            navArgument(SUBSTANCE_NAME_KEY) { type = NavType.StringType },
            navArgument(ADMINISTRATION_ROUTE_KEY) { type = NavType.StringType }
        )
    )
}

fun NavController.navigateToExperience(experienceId: Int) {
    navigate(ROUTE_START_EXPERIENCES + experienceId)
}

fun NavController.navigateToIngestion(ingestionId: Int) {
    navigate(ROUTE_START_INGESTIONS + ingestionId)
}

fun NavController.navigateToEditCustomUnit(customUnitId: Int) {
    navigate(ROUTE_START_EDIT_CUSTOM_UNIT + customUnitId)
}

fun NavController.navigateToChooseDoseCustomUnit(customUnitId: Int) {
    navigate(ROUTE_START_CHOOSE_DOSE_CUSTOM_UNIT + customUnitId)
}

fun NavController.navigateToEditExperience(experienceId: Int) {
    navigate(ROUTE_START_EDIT_EXPERIENCE + experienceId)
}

fun NavController.navigateToAddRating(experienceId: Int) {
    navigate(ROUTE_START_ADD_RATING + experienceId)
}

fun NavController.navigateToAddTimedNote(experienceId: Int) {
    navigate(ROUTE_START_ADD_TIMED_NOTE + experienceId)
}

fun NavController.navigateToEditRating(ratingId: Int) {
    navigate(ROUTE_START_EDIT_RATING + ratingId)
}

fun NavController.navigateToEditTimedNote(timedNoteId: Int, experienceId: Int) {
    navigate("$ROUTE_START_EDIT_TIMED_NOTE$timedNoteId/$experienceId")
}

fun NavController.navigateToTimelineScreen(consumerName: String, experienceId: Int) {
    navigate("$ROUTE_START_TIMELINE_SCREEN$consumerName/$experienceId")
}

fun NavController.navigateToSubstanceScreen(substanceName: String) {
    navigate(ROUTE_START_SUBSTANCES + substanceName)
}

fun NavController.navigateToEditCustomSubstance(customSubstanceId: Int) {
    navigate(ROUTE_START_EDIT_CUSTOM + customSubstanceId)
}

fun NavController.navigateToChooseCustomRoute(customSubstanceId: Int) {
    navigate(ROUTE_START_CHOOSE_ROUTE_CUSTOM + customSubstanceId)
}

fun NavController.navigateToSubstanceCompanionScreen(substanceName: String, consumerName: String?) {
    navigate("$ROUTE_START_SUBSTANCE_COMPANION$substanceName/?$CONSUMER_NAME_KEY=$consumerName")
}

fun NavController.navigateToCategoryScreen(categoryName: String) {
    navigate(ROUTE_START_CATEGORY + categoryName)
}

fun NavController.navigateToCheckInteractions(substanceName: String) {
    navigate("$ROUTE_START_CHECK_INTERACTIONS$substanceName")
}

fun NavController.navigateToCheckSaferUse(substanceName: String) {
    navigate("$ROUTE_START_CHECK_SAFER_USE$substanceName")
}

fun NavController.navigateToChooseRouteOfAddIngestion(substanceName: String) {
    navigate("$ROUTE_START_CHOOSE_ROUTE_OF_ADD_INGESTION$substanceName")
}

fun NavController.navigateToChooseRouteOfAddCustomUnit(substanceName: String) {
    navigate("$ROUTE_START_OF_ADD_CUSTOM_UNIT$substanceName")
}


fun NavController.navigateToChooseDose(
    substanceName: String,
    administrationRoute: AdministrationRoute
) {
    navigate("$ROUTE_START_CHOOSE_DOSE$substanceName/${administrationRoute.name}")
}

fun NavController.navigateToChooseDoseCustom(
    customSubstanceId: Int,
    administrationRoute: AdministrationRoute
) {
    navigate("$ROUTE_START_CHOOSE_DOSE_CUSTOM$customSubstanceId/${administrationRoute.name}")
}

fun NavController.navigateToChooseTimeAndMaybeColor(
    administrationRoute: AdministrationRoute,
    units: String?,
    isEstimate: Boolean,
    dose: Double?,
    estimatedDoseStandardDeviation: Double?,
    substanceName: String?,
    customUnitId: Int?,
    customSubstanceId: Int?,
) {
    navigate("$ROUTE_START_CHOOSE_TIME${administrationRoute.name}/$isEstimate/?$UNITS_KEY=$units/?$DOSE_KEY=$dose/?$ESTIMATED_DOSE_STANDARD_DEVIATION_KEY=$estimatedDoseStandardDeviation/?$SUBSTANCE_NAME_KEY=$substanceName/?$CUSTOM_UNIT_ID_KEY=$customUnitId/?$CUSTOM_SUBSTANCE_ID_KEY=$customSubstanceId")
}

fun NavController.navigateToFinishAddCustomUnit(
    substanceName: String,
    administrationRoute: AdministrationRoute
) {
    navigate("$ROUTE_START_FINISH_ADD_CUSTOM_UNIT$substanceName/${administrationRoute.name}")
}