package com.example.healthassistant.data.room.experiences.entities

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector


enum class Sentiment {
    VERY_DISSATISFIED {
        override val icon: ImageVector
            get() = Icons.Default.SentimentVeryDissatisfied
        override val description: String
            get() = "Very Bad"
    },
    DISSATISFIED {
        override val icon: ImageVector
            get() = Icons.Default.SentimentDissatisfied
        override val description: String
            get() = "Bad"
    },
    NEUTRAL {
        override val icon: ImageVector
            get() = Icons.Default.SentimentNeutral
        override val description: String
            get() = "Neutral"
    },
    SATISFIED {
        override val icon: ImageVector
            get() = Icons.Default.SentimentSatisfied
        override val description: String
            get() = "Good"
    },
    VERY_SATISFIED {
        override val icon: ImageVector
            get() = Icons.Default.SentimentVerySatisfied
        override val description: String
            get() = "Very Good"

    };

    abstract val icon: ImageVector
    abstract val description: String
}