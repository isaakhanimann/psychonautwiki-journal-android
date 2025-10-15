package com.isaakhanimann.journal.ui.main.navigation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.isaakhanimann.journal.ui.gamification.GamificationScreen
import com.isaakhanimann.journal.ui.main.navigation.GamificationTopLevelRoute
import com.isaakhanimann.journal.ui.main.navigation.composableWithTransitions

fun NavGraphBuilder.gamificationGraph(navController: NavHostController) {
    composableWithTransitions<GamificationTopLevelRoute> {
        GamificationScreen()
    }
}