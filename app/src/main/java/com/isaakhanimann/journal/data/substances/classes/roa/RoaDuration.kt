package com.isaakhanimann.journal.data.substances.classes.roa

data class RoaDuration(
    val onset: DurationRange?,
    val comeup: DurationRange?,
    val peak: DurationRange?,
    val offset: DurationRange?,
    val total: DurationRange?,
    val afterglow: DurationRange?
)