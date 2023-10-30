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
    },
    KHAKI {
        override fun getComposeColor(isDarkTheme: Boolean): Color {
            return if (isDarkTheme) {
                Color(red = 203, green = 183, blue = 137)
            } else {
                Color(red = 195, green = 176, blue = 145)
            }
        }
    },
    CORAL {
        override fun getComposeColor(isDarkTheme: Boolean): Color {
            return if (isDarkTheme) {
                Color(red = 255, green = 131, blue = 121)
            } else {
                Color(red = 248, green = 131, blue = 121)
            }
        }
    },
    FUCHSIA {
        override fun getComposeColor(isDarkTheme: Boolean): Color {
            return if (isDarkTheme) {
                Color(red = 214, green = 68, blue = 146)
            } else {
                Color(red = 200, green = 76, blue = 146)
            }
        }
    },
    LAVENDER {
        override fun getComposeColor(isDarkTheme: Boolean): Color {
            return if (isDarkTheme) {
                Color(red = 173, green = 123, blue = 182)
            } else {
                Color(red = 150, green = 123, blue = 182)
            }
        }
    },
    MAROON {
        override fun getComposeColor(isDarkTheme: Boolean): Color {
            return if (isDarkTheme) {
                Color(red = 128, green = 0, blue = 0)
            } else {
                Color(red = 110, green = 0, blue = 0)
            }
        }
    },
    OLIVE {
        override fun getComposeColor(isDarkTheme: Boolean): Color {
            return if (isDarkTheme) {
                Color(red = 140, green = 140, blue = 0)
            } else {
                Color(red = 128, green = 128, blue = 0)
            }
        }
    };

    abstract fun getComposeColor(isDarkTheme: Boolean): Color
}
