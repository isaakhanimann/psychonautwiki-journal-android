/*
 * Copyright (c) 2022. Isaak Hanimann.
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

package com.isaakhanimann.journal.data.room.experiences.entities

import androidx.compose.ui.graphics.Color

enum class AdaptiveColor {
    RED {
        override fun getComposeColor(isDarkTheme: Boolean): Color {
            return if (isDarkTheme) {
                Color(red = 255, green = 69, blue = 58)
            } else {
                Color(red = 255, green = 59, blue = 48)
            }
        }

        override val isPreferred = true
    },
    ORANGE {
        override fun getComposeColor(isDarkTheme: Boolean): Color {
            return if (isDarkTheme) {
                Color(red = 255, green = 159, blue = 10)
            } else {
                Color(red = 255, green = 149, blue = 0)
            }
        }

        override val isPreferred = true
    },
    YELLOW {
        override fun getComposeColor(isDarkTheme: Boolean): Color {
            return if (isDarkTheme) {
                Color(red = 255, green = 214, blue = 10)
            } else {
                Color(red = 255, green = 204, blue = 0)
            }
        }

        override val isPreferred = true
    },
    GREEN {
        override fun getComposeColor(isDarkTheme: Boolean): Color {
            return if (isDarkTheme) {
                Color(red = 48, green = 209, blue = 88)
            } else {
                Color(red = 52, green = 199, blue = 89)
            }
        }

        override val isPreferred = true
    },
    MINT {
        override fun getComposeColor(isDarkTheme: Boolean): Color {
            return if (isDarkTheme) {
                Color(red = 102, green = 212, blue = 207)
            } else {
                Color(red = 0, green = 199, blue = 190)
            }
        }

        override val isPreferred = true
    },
    TEAL {
        override fun getComposeColor(isDarkTheme: Boolean): Color {
            return if (isDarkTheme) {
                Color(red = 64, green = 200, blue = 224)
            } else {
                Color(red = 48, green = 176, blue = 199)
            }
        }

        override val isPreferred = true
    },
    CYAN {
        override fun getComposeColor(isDarkTheme: Boolean): Color {
            return if (isDarkTheme) {
                Color(red = 100, green = 210, blue = 255)
            } else {
                Color(red = 50, green = 173, blue = 230)
            }
        }

        override val isPreferred = true
    },
    BLUE {
        override fun getComposeColor(isDarkTheme: Boolean): Color {
            return if (isDarkTheme) {
                Color(red = 10, green = 132, blue = 255)
            } else {
                Color(red = 0, green = 122, blue = 255)
            }
        }

        override val isPreferred = true
    },
    INDIGO {
        override fun getComposeColor(isDarkTheme: Boolean): Color {
            return if (isDarkTheme) {
                Color(red = 94, green = 92, blue = 230)
            } else {
                Color(red = 88, green = 86, blue = 214)
            }
        }

        override val isPreferred = true
    },
    PURPLE {
        override fun getComposeColor(isDarkTheme: Boolean): Color {
            return if (isDarkTheme) {
                Color(red = 191, green = 90, blue = 242)
            } else {
                Color(red = 175, green = 82, blue = 222)
            }
        }

        override val isPreferred = true
    },
    PINK {
        override fun getComposeColor(isDarkTheme: Boolean): Color {
            return if (isDarkTheme) {
                Color(red = 255, green = 55, blue = 95)
            } else {
                Color(red = 255, green = 45, blue = 85)
            }
        }

        override val isPreferred = true
    },
    BROWN {
        override fun getComposeColor(isDarkTheme: Boolean): Color {
            return if (isDarkTheme) {
                Color(red = 172, green = 142, blue = 104)
            } else {
                Color(red = 162, green = 132, blue = 94)
            }
        }

        override val isPreferred = true
    },
    FIRE_ENGINE_RED {
        override fun getComposeColor(isDarkTheme: Boolean): Color {
            return if (isDarkTheme) {
                Color(red = 237, green = 43, blue = 42)
            } else {
                Color(red = 237, green = 14, blue = 6)
            }
        }

        override val isPreferred = false
    },
    CORAL {
        override fun getComposeColor(isDarkTheme: Boolean): Color {
            return if (isDarkTheme) {
                Color(red = 255, green = 131, blue = 121)
            } else {
                Color(red = 180, green = 92, blue = 85)
            }
        }

        override val isPreferred = false
    },
    TOMATO {
        override fun getComposeColor(isDarkTheme: Boolean): Color {
            return if (isDarkTheme) {
                Color(red = 255, green = 99, blue = 71)
            } else {
                Color(red = 180, green = 69, blue = 50)
            }
        }

        override val isPreferred = false
    },
    CINNABAR {
        override fun getComposeColor(isDarkTheme: Boolean): Color {
            return if (isDarkTheme) {
                Color(red = 227, green = 36, blue = 0)
            } else {
                Color(red = 227, green = 36, blue = 0)
            }
        }

        override val isPreferred = false
    },
    RUST {
        override fun getComposeColor(isDarkTheme: Boolean): Color {
            return if (isDarkTheme) {
                Color(red = 199, green = 81, blue = 58)
            } else {
                Color(red = 199, green = 81, blue = 58)
            }
        }

        override val isPreferred = false
    },
    ORANGE_RED {
        override fun getComposeColor(isDarkTheme: Boolean): Color {
            return if (isDarkTheme) {
                Color(red = 255, green = 69, blue = 0)
            } else {
                Color(red = 205, green = 55, blue = 0)
            }
        }

        override val isPreferred = false
    },
    AUBURN {
        override fun getComposeColor(isDarkTheme: Boolean): Color {
            return if (isDarkTheme) {
                Color(red = 217, green = 80, blue = 0)
            } else {
                Color(red = 173, green = 62, blue = 0)
            }
        }

        override val isPreferred = false
    },
    SADDLE_BROWN {
        override fun getComposeColor(isDarkTheme: Boolean): Color {
            return if (isDarkTheme) {
                Color(red = 191, green = 95, blue = 25)
            } else {
                Color(red = 139, green = 69, blue = 19)
            }
        }

        override val isPreferred = false
    },
    DARK_ORANGE {
        override fun getComposeColor(isDarkTheme: Boolean): Color {
            return if (isDarkTheme) {
                Color(red = 255, green = 140, blue = 0)
            } else {
                Color(red = 155, green = 84, blue = 0)
            }
        }

        override val isPreferred = false
    },
    DARK_GOLD {
        override fun getComposeColor(isDarkTheme: Boolean): Color {
            return if (isDarkTheme) {
                Color(red = 169, green = 104, blue = 0)
            } else {
                Color(red = 169, green = 104, blue = 0)
            }
        }

        override val isPreferred = false
    },
    KHAKI {
        override fun getComposeColor(isDarkTheme: Boolean): Color {
            return if (isDarkTheme) {
                Color(red = 203, green = 183, blue = 137)
            } else {
                Color(red = 128, green = 114, blue = 86)
            }
        }

        override val isPreferred = false
    },
    BRONZE {
        override fun getComposeColor(isDarkTheme: Boolean): Color {
            return if (isDarkTheme) {
                Color(red = 167, green = 123, blue = 0)
            } else {
                Color(red = 120, green = 87, blue = 0)
            }
        }

        override val isPreferred = false
    },
    GOLD {
        override fun getComposeColor(isDarkTheme: Boolean): Color {
            return if (isDarkTheme) {
                Color(red = 255, green = 215, blue = 0)
            } else {
                Color(red = 130, green = 109, blue = 0)
            }
        }

        override val isPreferred = false
    },
    OLIVE {
        override fun getComposeColor(isDarkTheme: Boolean): Color {
            return if (isDarkTheme) {
                Color(red = 141, green = 134, blue = 0)
            } else {
                Color(red = 102, green = 97, blue = 0)
            }
        }

        override val isPreferred = false
    },
    OLIVE_DRAB {
        override fun getComposeColor(isDarkTheme: Boolean): Color {
            return if (isDarkTheme) {
                Color(red = 154, green = 166, blue = 14)
            } else {
                Color(red = 111, green = 118, blue = 8)
            }
        }

        override val isPreferred = false
    },
    DARK_OLIVE_GREEN {
        override fun getComposeColor(isDarkTheme: Boolean): Color {
            return if (isDarkTheme) {
                Color(red = 105, green = 133, blue = 58)
            } else {
                Color(red = 85, green = 107, blue = 47)
            }
        }

        override val isPreferred = false
    },
    MOSS_GREEN {
        override fun getComposeColor(isDarkTheme: Boolean): Color {
            return if (isDarkTheme) {
                Color(red = 102, green = 156, blue = 53)
            } else {
                Color(red = 79, green = 122, blue = 40)
            }
        }

        override val isPreferred = false
    },
    LIME_GREEN {
        override fun getComposeColor(isDarkTheme: Boolean): Color {
            return if (isDarkTheme) {
                Color(red = 0, green = 255, blue = 0)
            } else {
                Color(red = 0, green = 130, blue = 0)
            }
        }

        override val isPreferred = false
    },
    LIME {
        override fun getComposeColor(isDarkTheme: Boolean): Color {
            return if (isDarkTheme) {
                Color(red = 50, green = 205, blue = 50)
            } else {
                Color(red = 32, green = 130, blue = 32)
            }
        }

        override val isPreferred = false
    },
    FOREST_GREEN {
        override fun getComposeColor(isDarkTheme: Boolean): Color {
            return if (isDarkTheme) {
                Color(red = 34, green = 139, blue = 34)
            } else {
                Color(red = 28, green = 114, blue = 28)
            }
        }

        override val isPreferred = false
    },
    SEA_GREEN {
        override fun getComposeColor(isDarkTheme: Boolean): Color {
            return if (isDarkTheme) {
                Color(red = 46, green = 139, blue = 87)
            } else {
                Color(red = 38, green = 114, blue = 71)
            }
        }

        override val isPreferred = false
    },
    JUNGLE_GREEN {
        override fun getComposeColor(isDarkTheme: Boolean): Color {
            return if (isDarkTheme) {
                Color(red = 3, green = 136, blue = 88)
            } else {
                Color(red = 3, green = 136, blue = 88)
            }
        }

        override val isPreferred = false
    },
    LIGHT_SEA_GREEN {
        override fun getComposeColor(isDarkTheme: Boolean): Color {
            return if (isDarkTheme) {
                Color(red = 32, green = 178, blue = 170)
            } else {
                Color(red = 22, green = 128, blue = 122)
            }
        }

        override val isPreferred = false
    },
    DARK_TURQUOISE {
        override fun getComposeColor(isDarkTheme: Boolean): Color {
            return if (isDarkTheme) {
                Color(red = 0, green = 206, blue = 209)
            } else {
                Color(red = 0, green = 131, blue = 134)
            }
        }

        override val isPreferred = false
    },
    DODGER_BLUE {
        override fun getComposeColor(isDarkTheme: Boolean): Color {
            return if (isDarkTheme) {
                Color(red = 30, green = 144, blue = 255)
            } else {
                Color(red = 24, green = 116, blue = 205)
            }
        }

        override val isPreferred = false
    },
    ROYAL_BLUE {
        override fun getComposeColor(isDarkTheme: Boolean): Color {
            return if (isDarkTheme) {
                Color(red = 72, green = 117, blue = 251)
            } else {
                Color(red = 65, green = 105, blue = 225)
            }
        }

        override val isPreferred = false
    },
    DEEP_LAVENDER {
        override fun getComposeColor(isDarkTheme: Boolean): Color {
            return if (isDarkTheme) {
                Color(red = 135, green = 78, blue = 254)
            } else {
                Color(red = 135, green = 78, blue = 254)
            }
        }

        override val isPreferred = false
    },
    BLUE_VIOLET {
        override fun getComposeColor(isDarkTheme: Boolean): Color {
            return if (isDarkTheme) {
                Color(red = 166, green = 73, blue = 252)
            } else {
                Color(red = 138, green = 43, blue = 226)
            }
        }

        override val isPreferred = false
    },
    DARK_VIOLET {
        override fun getComposeColor(isDarkTheme: Boolean): Color {
            return if (isDarkTheme) {
                Color(red = 162, green = 76, blue = 210)
            } else {
                Color(red = 148, green = 0, blue = 211)
            }
        }

        override val isPreferred = false
    },
    HELIOTROPE {
        override fun getComposeColor(isDarkTheme: Boolean): Color {
            return if (isDarkTheme) {
                Color(red = 151, green = 93, blue = 175)
            } else {
                Color(red = 151, green = 93, blue = 175)
            }
        }

        override val isPreferred = false
    },
    BYZANTIUM {
        override fun getComposeColor(isDarkTheme: Boolean): Color {
            return if (isDarkTheme) {
                Color(red = 190, green = 56, blue = 243)
            } else {
                Color(red = 153, green = 41, blue = 189)
            }
        }

        override val isPreferred = false
    },
    MAGENTA {
        override fun getComposeColor(isDarkTheme: Boolean): Color {
            return if (isDarkTheme) {
                Color(red = 255, green = 0, blue = 255)
            } else {
                Color(red = 205, green = 0, blue = 205)
            }
        }

        override val isPreferred = false
    },
    DARK_MAGENTA {
        override fun getComposeColor(isDarkTheme: Boolean): Color {
            return if (isDarkTheme) {
                Color(red = 217, green = 0, blue = 217)
            } else {
                Color(red = 139, green = 0, blue = 139)
            }
        }

        override val isPreferred = false
    },
    FUCHSIA {
        override fun getComposeColor(isDarkTheme: Boolean): Color {
            return if (isDarkTheme) {
                Color(red = 214, green = 68, blue = 146)
            } else {
                Color(red = 189, green = 60, blue = 129)
            }
        }

        override val isPreferred = false
    },
    DEEP_PINK {
        override fun getComposeColor(isDarkTheme: Boolean): Color {
            return if (isDarkTheme) {
                Color(red = 255, green = 20, blue = 147)
            } else {
                Color(red = 205, green = 16, blue = 117)
            }
        }

        override val isPreferred = false
    },
    GRAYISH_MAGENTA {
        override fun getComposeColor(isDarkTheme: Boolean): Color {
            return if (isDarkTheme) {
                Color(red = 161, green = 96, blue = 128)
            } else {
                Color(red = 161, green = 96, blue = 128)
            }
        }

        override val isPreferred = false
    },
    HOT_PINK {
        override fun getComposeColor(isDarkTheme: Boolean): Color {
            return if (isDarkTheme) {
                Color(red = 255, green = 105, blue = 180)
            } else {
                Color(red = 180, green = 74, blue = 126)
            }
        }

        override val isPreferred = false
    },
    JAZZBERRY_JAM {
        override fun getComposeColor(isDarkTheme: Boolean): Color {
            return if (isDarkTheme) {
                Color(red = 230, green = 59, blue = 122)
            } else {
                Color(red = 185, green = 45, blue = 93)
            }
        }

        override val isPreferred = false
    },
    MAROON {
        override fun getComposeColor(isDarkTheme: Boolean): Color {
            return if (isDarkTheme) {
                Color(red = 187, green = 82, blue = 99)
            } else {
                Color(red = 190, green = 49, blue = 68)
            }
        }

        override val isPreferred = false
    };

    abstract fun getComposeColor(isDarkTheme: Boolean): Color
    abstract val isPreferred: Boolean
}
