/*
 * Copyright (c) 2023. Isaak Hanimann.
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

// the order matters because the ordinal value increases by one for each subsequent case
enum class ShulginRatingOption {
    MINUS {
        override val sign = "-"
        override val rawValue = "minus"
        override val shortDescription = "no effects"
        override val longDescription = "On the quantitative potency scale (-, ±, +, ++, +++), there were no effects observed."
    },
    PLUS_MINUS {
        override val sign = "±"
        override val rawValue = "plusMinus"
        override val shortDescription = "maybe false positive"
        override val longDescription = "The level of effectiveness of a drug that indicates a threshold action. If a higher dosage produces a greater response, then the plus/minus (±) was valid. If a higher dosage produces nothing, then this was a false positive."
    },
    PLUS {
        override val sign = "+"
        override val rawValue = "plus"
        override val shortDescription = "certainly active, nature not yet apparent"
        override val longDescription = "The drug is quite certainly active. The chronology can be determined with some accuracy, but the nature of the drug's effects are not yet apparent."
    },
    TWO_PLUS {
        override val sign = "++"
        override val rawValue = "twoPlus"
        override val shortDescription = "nature apparent, effects may be repressible"
        override val longDescription = "Both the chronology and the nature of the action of a drug are unmistakably apparent. But you still have some choice as to whether you will accept the adventure, or rather just continue with your ordinary day's plans (if you are an experienced researcher, that is). The effects can be allowed a predominant role, or they may be repressible and made secondary to other chosen activities."
    },
    THREE_PLUS {
        override val sign = "+++"
        override val rawValue = "threePlus"
        override val shortDescription = "totally engaged, ignoring no longer an option"
        override val longDescription = "Not only are the chronology and the nature of a drug's action quite clear, but ignoring its action is no longer an option. The subject is totally engaged in the experience, for better or worse."
    },
    FOUR_PLUS {
        override val sign = "++++"
        override val rawValue = "fourPlus"
        override val shortDescription = "rare and precious transcendental state"
        override val longDescription = "A rare and precious transcendental state, which has been called a \"peak experience,\" a \"religious experience,\" \"divine transformation,\" a \"state of Samadhi\" and many other names in other cultures. It is not connected to the +1, +2, and +3 of the measuring of a drug's intensity. It is a state of bliss, a participation mystique, a connectedness with both the interior and exterior universes, which has come about after the ingestion of a psychedelic drug, but which is not necessarily repeatable with a subsequent ingestion of that same drug. If a drug (or technique or process) were ever to be discovered which would consistently produce a plus four experience in all human beings, it is conceivable that it would signal the ultimate evolution, and perhaps the end, of the human experiment."
    };

    abstract val sign: String
    abstract val rawValue: String
    abstract val shortDescription: String
    abstract val longDescription: String
}