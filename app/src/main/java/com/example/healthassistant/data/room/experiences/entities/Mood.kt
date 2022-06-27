package com.example.healthassistant.data.room.experiences.entities

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector


enum class Mood {
    VERY_DISSATISFIED {
        override val icon: ImageVector
            get() = Icons.Default.SentimentVeryDissatisfied
    },
    DISSATISFIED {
        override val icon: ImageVector
            get() = Icons.Default.SentimentDissatisfied
    },
    NEUTRAL {
        override val icon: ImageVector
            get() = Icons.Default.SentimentNeutral
    },
    SATISFIED {
        override val icon: ImageVector
            get() = Icons.Default.SentimentSatisfied
    },
    VERY_SATISFIED {
        override val icon: ImageVector
            get() = Icons.Default.SentimentVerySatisfied

    };

    abstract val icon: ImageVector
}