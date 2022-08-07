package com.isaakhanimann.healthassistant.data.substances.classes.roa

import androidx.compose.ui.graphics.Color

enum class DoseClass {
    THRESHOLD {
        override val numDots: Int
            get() = 1
        override val description: String
            get() = "A threshold dose is the dose at which the mental and physical alterations produced by the substance first become apparent.\n" +
                    "These effects are distinctly beyond that of placebo but may still be ignored with some effort by directing one's focus towards the external environment.\n" +
                    "Subjects may perceive a vague sense of \"something\" or anticipatory energy building up in the background at this level."

        override fun getComposeColor(isDarkTheme: Boolean): Color {
            return if (isDarkTheme) {
                Color(red = 100, green = 210, blue = 255)
            } else {
                Color(red = 50, green = 173, blue = 230)
            }
        }
    },
    LIGHT {
        override val numDots: Int
            get() = 2
        override val description: String
            get() = "A light dose produces a state which is somewhat distinct from sobriety but does not threaten to override the subject's ordinary awareness.\n" +
                    "The effects can be ignored by increasing the focus one directs towards the external environment and performing complex tasks.\n" +
                    "The subject may have to pay particular attention for the substance's effects to be perceptible, or they may be slightly noticeable but will not insist upon the subject's attention."

        override fun getComposeColor(isDarkTheme: Boolean): Color {
            return if (isDarkTheme) {
                Color(red = 48, green = 209, blue = 88)
            } else {
                Color(red = 52, green = 199, blue = 89)
            }
        }
    },
    COMMON {
        override val numDots: Int
            get() = 3
        override val description: String
            get() = "A common dose is the dose at which the effects and nature of the substance is quite clear and distinct; the subject's ordinary awareness slips and ignoring its action becomes difficult.\n" +
                    "The subject will generally be able to partake in regular behaviors and remain functional and able to communicate, although this can depend on the individual.\n" +
                    "The effects can be allowed to occupy a predominant role or they may be suppressed and made secondary to other chosen activities with sufficient effort or in case of an emergency."

        override fun getComposeColor(isDarkTheme: Boolean): Color {
            return if (isDarkTheme) {
                Color(red = 255, green = 214, blue = 10)
            } else {
                Color(red = 255, green = 204, blue = 0)
            }
        }
    },
    STRONG {
        override val numDots: Int
            get() = 4
        override val description: String
            get() = "A strong dose renders its subject mostly incapable of functioning, interacting normally, or thinking in a straightforward manner.\n" +
                    "The effects of the substance are clear and can no longer be ignored or suppressed, leaving the subject entirely engaged in the experience regardless of their desire or volition. Negative effects become more common at this level.\n" +
                    "As subjects are not able to alter the trajectory of their behavior at strong doses, it is vital that they have prepared their environment and activities in advance as well as taken any precautionary measures."

        override fun getComposeColor(isDarkTheme: Boolean): Color {
            return if (isDarkTheme) {
                Color(red = 255, green = 159, blue = 10)
            } else {
                Color(red = 255, green = 149, blue = 0)
            }
        }
    },
    HEAVY {
        override val numDots: Int
            get() = 5
        override val description: String
            get() = "A heavy dose is the upper limit of what a substance is capable of producing in terms of psychoactive effects; doses past this range are associated with rapidly increasing side effects and marginal desirable effects.\n" +
                    "Depending on the substance consumed, the user may be rendered incapable of functioning and communicating in addition to experiencing extremely uncomfortable side effects that overshadow the positive effects experienced at lower doses.\n" +
                    "It is absolutely vital to employ harm reduction measures with heavy doses as the user will likely be unable to properly take care of themselves in the event of an emergency. Trip sitters are strongly advised.\n" +
                    "Users should also be aware that the line between a heavy dose and overdose is often very blurry and they are placing themselves at a significantly higher risk of injury, hospitalization, and death whenever they choose to take a heavy dose.\n" +
                    "The desire or compulsion to regularly take heavy doses (\"chronic use\") may also be an indicator of tolerance, addiction or other mental health problems."

        override fun getComposeColor(isDarkTheme: Boolean): Color {
            return if (isDarkTheme) {
                Color(red = 255, green = 69, blue = 58)
            } else {
                Color(red = 255, green = 59, blue = 48)
            }
        }
    };

    abstract val numDots: Int
    abstract val description: String
    abstract fun getComposeColor(isDarkTheme: Boolean): Color
}