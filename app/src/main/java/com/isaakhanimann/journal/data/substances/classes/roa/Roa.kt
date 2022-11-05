package com.isaakhanimann.journal.data.substances.classes.roa

import com.isaakhanimann.journal.data.substances.AdministrationRoute

data class Roa(
    val route: AdministrationRoute,
    val roaDose: RoaDose?,
    val roaDuration: RoaDuration?,
    val bioavailability: Bioavailability?
)