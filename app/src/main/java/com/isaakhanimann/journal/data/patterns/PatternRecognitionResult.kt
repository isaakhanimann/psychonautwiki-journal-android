package com.isaakhanimann.journal.data.patterns

import java.time.Instant

data class PatternRecognitionResult(
    val patternName: String,
    val timestamp: Instant
)