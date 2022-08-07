package com.isaakhanimann.healthassistant.data.substances.classes.roa

import com.isaakhanimann.healthassistant.data.substances.AdministrationRoute

data class Roa(
    val route: AdministrationRoute,
    val roaDose: RoaDose?,
    val roaDuration: RoaDuration?,
    val bioavailability: Bioavailability?
)