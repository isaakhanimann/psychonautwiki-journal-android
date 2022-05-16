package com.example.healthassistant.data.experiences.entities

import androidx.compose.ui.graphics.Color

enum class IngestionColor {
    RED {
        override fun getComposeColor(isDarkTheme: Boolean): Color {
            return if (isDarkTheme) {
                Color(red = 255, green = 69, blue = 58)
            } else {
                Color(red = 255, green = 59, blue = 48)
            }
        }
    },
    ORANGE {
        override fun getComposeColor(isDarkTheme: Boolean): Color {
            return if (isDarkTheme) {
                Color(red = 255, green = 159, blue = 10)
            } else {
                Color(red = 255, green = 149, blue = 0)
            }
        }
    },
    YELLOW {
        override fun getComposeColor(isDarkTheme: Boolean): Color {
            return if (isDarkTheme) {
                Color(red = 255, green = 214, blue = 10)
            } else {
                Color(red = 255, green = 204, blue = 0)
            }
        }
    },
    GREEN {
        override fun getComposeColor(isDarkTheme: Boolean): Color {
            return if (isDarkTheme) {
                Color(red = 48, green = 209, blue = 88)
            } else {
                Color(red = 52, green = 199, blue = 89)
            }
        }
    },
    MINT {
        override fun getComposeColor(isDarkTheme: Boolean): Color {
            return if (isDarkTheme) {
                Color(red = 102, green = 212, blue = 207)
            } else {
                Color(red = 0, green = 199, blue = 190)
            }
        }
    },
    TEAL {
        override fun getComposeColor(isDarkTheme: Boolean): Color {
            return if (isDarkTheme) {
                Color(red = 64, green = 200, blue = 224)
            } else {
                Color(red = 48, green = 176, blue = 199)
            }
        }
    },
    CYAN {
        override fun getComposeColor(isDarkTheme: Boolean): Color {
            return if (isDarkTheme) {
                Color(red = 100, green = 210, blue = 255)
            } else {
                Color(red = 50, green = 173, blue = 230)
            }
        }
    },
    BLUE {
        override fun getComposeColor(isDarkTheme: Boolean): Color {
            return if (isDarkTheme) {
                Color(red = 10, green = 132, blue = 255)
            } else {
                Color(red = 0, green = 122, blue = 255)
            }
        }
    },
    INDIGO {
        override fun getComposeColor(isDarkTheme: Boolean): Color {
            return if (isDarkTheme) {
                Color(red = 94, green = 92, blue = 230)
            } else {
                Color(red = 88, green = 86, blue = 214)
            }
        }
    },
    PURPLE {
        override fun getComposeColor(isDarkTheme: Boolean): Color {
            return if (isDarkTheme) {
                Color(red = 191, green = 90, blue = 242)
            } else {
                Color(red = 175, green = 82, blue = 222)
            }
        }
    },
    PINK {
        override fun getComposeColor(isDarkTheme: Boolean): Color {
            return if (isDarkTheme) {
                Color(red = 255, green = 55, blue = 95)
            } else {
                Color(red = 255, green = 45, blue = 85)
            }
        }
    },
    BROWN {
        override fun getComposeColor(isDarkTheme: Boolean): Color {
            return if (isDarkTheme) {
                Color(red = 172, green = 142, blue = 104)
            } else {
                Color(red = 162, green = 132, blue = 94)
            }
        }
    };

    abstract fun getComposeColor(isDarkTheme: Boolean): Color
}