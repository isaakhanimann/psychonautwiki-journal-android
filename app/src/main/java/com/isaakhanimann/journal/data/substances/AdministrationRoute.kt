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

package com.isaakhanimann.journal.data.substances

import androidx.annotation.Keep
import com.isaakhanimann.journal.data.room.experiences.entities.AdaptiveColor
import kotlinx.serialization.Serializable

@Keep // to fix this issue: https://issuetracker.google.com/issues/358137294. Apparently all enums used as navigation args are getting their metadata removed in minified builds.
@Serializable
enum class AdministrationRoute {
    ORAL {
        override val displayText = "Oral"
        override val description = "Swallowed"
        override val articleText =
            """Oral administration is the most common route of administration for most substance classes. This route allows a substance to be absorbed through blood vessels lining the stomach and intestines. The onset is generally slower than other methods of ingestion as it must undergo first-pass metabolism through the liver (may vary greatly between individual substances). Additionally, the absorption and overall duration are generally longer as well.
This method can also have a greater propensity for nausea and gastrointestinal discomfort."""
        override val isInjectionMethod = false
        override val color = AdaptiveColor.BLUE
    },
    SUBLINGUAL {
        override val displayText = "Sublingual"
        override val description = "Under tongue"
        override val articleText =
            """Sublingual administration refers to absorption under the tongue. It is a common route of administration for lysergamides like LSD.
This route causes the substance to be absorbed through the large lingual artery present underneath the tongue, generally resulting in a faster absorption than oral administration.
It also circumvents first-pass metabolism of certain substances which can be absorbed via sublingual and buccal administration but not oral administration (e.g. 25x-NBOMe, 25x-NBOH).
Caustic compounds, such as the freebase form of amine-containing substance, should not be used sublingually because they can severely burn the inside of one's mouth."""
        override val isInjectionMethod = false
        override val color = AdaptiveColor.INDIGO
    },
    BUCCAL {
        override val displayText = "Buccal"
        override val description = "Between gums and cheek"
        override val articleText =
            """Buccal administration refers to absorption through the cheek and gum.
This route is commonly employed when ingesting potent psychedelics such as 25I-NBOMe, DOM, LSD, and other substances distributed on blotter paper. Potent clandestine manufactured benzodiazepines like alprazolam and etizolam are also sometimes distributed on blotters.
Like sublingual absorption, the substance is largely absorbed through the lingual artery, but is also absorbed through the gum lining. This method is used when chewing plant leaves such as khat, kratom, salvia divinorum, and sometimes tobacco (snus)."""
        override val isInjectionMethod = false
        override val color = AdaptiveColor.CYAN
    },
    INSUFFLATED {
        override val displayText = "Insufflated"
        override val description = "Sniffed"
        override val articleText =
            """Insufflation (also called "inhalation" and "snorting") refers to the introduction of a substance into the sinus via the nostrils, circumventing first pass metabolism.
It is a very common method of use for substances in powder form, specifically so-called "street drugs" like cocaine, heroin, and methamphetamine. Some users find this route to be painful and uncomfortable, although certain substances are easier to insufflate than others.
This method is capable of rapid absorption through mucous membranes and blood vessels in the sinus. Absorption and onset is generally much more rapid than oral and, as a result, a substance feels much more intense and is often shorter acting than if taken orally.
Insufflation is common with substances such as cocaine and ketamine. It is also utilized in yopo rituals, the self-applicator pipe is known as ‘Kuripe’, and the blow pipe is known as a ‘Tepi’ in the Brazilian tradition. Insufflating tobacco in snuff form was a common practice until the early 20th century.
Frequent insufflation of some substances can damage one's mucous membranes, induce bleeding, damage the nostril's cartilage and lining, burn the throat, and cause other trauma to the nasal passage and sinus area. To reduce damage, it is recommended to grind the substance completely before use and alternate nostrils. It can also cause or exacerbate nasal congestion.
Also, sharing snorting equipment (straws, banknotes, bullets, etc) has been linked to the transmission of hepatitis C. In one study, the University of Tennessee Medical Center researches warned that other blood-borne diseases such as HIV, the AIDS-causing virus, could be transmitted as well."""
        override val isInjectionMethod = false
        override val color = AdaptiveColor.PURPLE
    },
    RECTAL {
        override val displayText = "Rectal"
        override val description = "Boofing / Plugging"
        override val articleText =
            """Rectal administration, also commonly referred to as boofing or plugging, is one of the most effective methods of administration for many substances. The absorption rate is very high compared to other methods and the onset is usually very short, generally with a higher intensity and shorter duration.
This is due to a large amount of arteries located in the rectum; thus rectal administration is often superior to other methods despite social stigma. Caustic substances such as 4-FA or phenibut hydrochloride should not be plugged because they can burn the interior rectum resulting in a considerable amount of gastrointestinal distress.
Rectal administration can involve either the insertion of a low-volume solution into the rectum, using a syringe or pipette, or by placing a pill or gelatin capsule containing the active substance. The latter form is known as a suppository, and is common in medicine when the gastrointestinal tract cannot support oral medicine."""
        override val isInjectionMethod = false
        override val color = AdaptiveColor.PINK
    },
    TRANSDERMAL {
        override val displayText = "Transdermal"
        override val description = "Through skin"
        override val articleText =
            """Transdermal is a route of administration where active ingredients are delivered across the skin for systemic distribution. Examples include transdermal patches used for medicine delivery for opioids such as fentanyl and transdermal implants used for medical or anesthetic purposes. This route is typically not observed in non-medical or recreational contexts due to the manufacturing requirements."""
        override val isInjectionMethod = false
        override val color = AdaptiveColor.BROWN
    },
    SUBCUTANEOUS {
        override val displayText = "Subcutaneous"
        override val description = "Injected between skin and muscle"
        override val articleText =
            """Subcutaneous administration (also known as skin popping) refers to a drug being injected into the subcutis, the layer of skin directly below the dermis and epidermis. Subcutaneous administration is relatively uncommon among psychonautics, as many people are not trained how to do it or would rather use a different route of administration which they are more familiar with."""
        override val isInjectionMethod = true
        override val color = AdaptiveColor.TEAL
    },
    INTRAMUSCULAR {
        override val displayText = "Intramuscular"
        override val description = "Injected into muscle tissue"
        override val articleText =
            """Intramuscular administration refers to a drug being injected into the muscle tissue using a hypodermic needle. This method is very similar to the intravenous route, but is often more painful with a decreased onset and absorption. Some drugs (such as ketamine) are commonly administered via this route. Like intravenous administration, intramuscular injection must be taken with precaution, using sterilized unused needles and not leaving any residual air bubbles in the reservoir."""
        override val isInjectionMethod = true
        override val color = AdaptiveColor.MINT
    },
    INTRAVENOUS {
        override val displayText = "Intravenous"
        override val description = "Injected into vein"
        override val articleText =
            """Intravenous administration refers to a drug being directly introduced into the bloodstream using a hypodermic needle. This method has the benefit of a very short onset and eliminates absorption by directly entering the bloodstream. However, much greater care must be taken when compared to other methods of administration.
Sterilized, unused needles and a high purity substance with little to no adulterant are required to avoid damage to the circulatory system. Making sure no air bubbles are present in the reservoir before the plunger is released is also of dire importance as air bubbles in the bloodstream can easily be lethal.
This route is strongly associated with substances such as heroin and cocaine, but can be employed with almost any pure substance."""
        override val isInjectionMethod = true
        override val color = AdaptiveColor.RED
    },
    SMOKED {
        override val displayText = "Smoked"
        override val description = "Inhaling with heat source"
        override val articleText =
            """Smoking substances is a common method of consumption with the most common examples including cannabis and tobacco.
To smoke a substance a direct heat source, most often a flame, is applied directly to the substance with no barrier between the heat source and the substance. The smoking of substances can lead to an almost instantaneous absorption of the substance and passage through the blood brain barrier.
When a substance is smoked, the substance is absorbed through blood vessels found in the bronchi tubes contained within the lungs. Like insufflation, the duration is decreased while its intensity is increased in proportion to oral absorption. Smoking a substance also bypasses the GI tract's tendency to break certain substances down, such as DMT.
Cannabis is commonly consumed via the respiratory tract. The average THC transfer rate for joints, bongs, and vaporizers, is 20-26%, 40%, and 55-83%, respectively. For a proper gas or smoke deposition, one are advised to take a deep initial breath, and then hold it for 10 seconds to allow for the gas or smoke to get fully absorbed in the lungs. Subjects are frequently instructed to follow the "10 seconds rule" in studies. Prolonged breath holding does not substantially enhance the effects of inhaled marijuana smoke.
Bongs that are cleaned regularly eliminates yeast, fungi, bacteria and pathogens that can cause several symptoms that vary from allergy to lung infection.
Heroin is colloquially referred to as "smoked" but is really vaporized, often using tinfoil as a barrier between the substance and the flame source.
However, an overdose caused by chasing the dragon is hard to predict because this technique doesn't deliver a standardized dosage. It's virtually impossible even for skilled users to know how much of the substance that has been evaporated, burned, and inhaled.
These combined factors may create a false sense of security when a given dose seem safe to repeat, but may cause an overdose when all the factors are randomly excluded.
A vaporizer is a safer drug paraphernalia than aluminum foil.

Vaporizing substances is a common method of consumption with the most common examples including cannabis and nicotine, but also heroin and crack-cocaine. Vaporizing a substance allows for more temperature control because the flame or heat source does not come into direct contact with the substance.
Even though many drugs, like heroin and oxycodone pills are colloquially referred to as "smoked" the process used to consume them is vaporization. Vaporizing substances can lead to an almost instantaneous absorption of the substance and passage through the blood brain barrier.
When a substance is vaporized, the substance is absorbed through blood vessels found in the bronchi tubes contained within the lungs. Like insufflation, the duration is decreased while its intensity is increased in proportion to oral absorption. Vaporizing a substance also bypasses the GI tract's tendency to break certain substances down, such as DMT.
Vaporization is commonly associated with the vaporizer pens that have become popular within the past decade, but it is not limited to ingesting the vapors from an electronic heat source. Smoking substances off of tinfoil is a common method of vaporizing substances with a flame heat source.
Due to the higher level of temperature control, vaporization is often a more efficient way to consume a substance. Especially when vaporizing off of tin foil or a oil burning pipe, the heat source can be held at different distances to create the perfect temperature to convert the substance into a vapor that can be inhaled.
Smoking a substance that should be vaporized leads to a blast of heat that may burn off the active ingredient or ignite the substance itself, both of which are wasteful and incorrect.
Ethnobotanist Daniel Siebert cautions that inhaling hot air can be irritating and potentially damaging to the lungs. Vapor produced by a heat gun needs to be cooled by running it through a water pipe or cooling chamber before inhalation."""
        override val isInjectionMethod = false
        override val color = AdaptiveColor.GREEN
    },
    INHALED {
        override val displayText = "Inhaled"
        override val description = "Inhaling without heat source"
        override val articleText =
            """Inhaled administration is used for inhalants gases such as nitrous oxide, volatile liquids such as ether, and volatile viscous compounds such as poppers. It is substantially easier to overdose on alcohol inhalation than drinking alcohol.
Inhalants do not require an external heat source to produce psychoactive vapors that can then be inhaled through various methods depending on the substance used. Inhaled substances are absorbed very rapidly and lead to an almost instantaneous absorption of the substance and passage through the blood brain barrier.
Many substances can be inhaled to achieve an altered state of consciousness, however, some substances used for this purpose produce highly negative physical and neurotoxic effects including solvents like toluene (see toluene toxicity) often found in glue, acetone often found in nail polish, and gasoline., and number of gases intended for household or industrial use including butane gas sold as lighter gas refill."""
        override val isInjectionMethod = false
        override val color = AdaptiveColor.YELLOW
    };

    abstract val displayText: String
    abstract val description: String
    abstract val articleText: String
    abstract val isInjectionMethod: Boolean
    abstract val color: AdaptiveColor

    companion object {
        const val PSYCHONAUT_WIKI_ARTICLE_URL =
            "https://psychonautwiki.org/wiki/Route_of_administration"
        const val SAFER_INJECTION_ARTICLE_URL = "https://psychonautwiki.org/wiki/Safer_injection_guide"
        const val SAFER_PLUGGING_ARTICLE_URL = "https://wiki.tripsit.me/wiki/Quick_Guide_to_Plugging"
    }
}