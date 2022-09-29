package com.isaakhanimann.healthassistant.ui.safer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.isaakhanimann.healthassistant.ui.search.substance.BulletPoints
import com.isaakhanimann.healthassistant.ui.utils.JournalTopAppBar

@Preview
@Composable
fun SaferHallucinogensScreen() {
    Scaffold(
        topBar = {
            JournalTopAppBar(
                title = "Safer Hallucinogens",
            )
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "The information below is exclusively tailored for the use and experimentation with hallucinogens such as psychedelics, dissociatives, and deliriants.")
            Spacer(modifier = Modifier.height(5.dp))
            Divider()
            Text(text = "Setting", style = MaterialTheme.typography.h6)
            Text(text = "Choosing a suitable place to experience the effects of a hallucinogen is extremely important and plays a major role in determining the outcome of the experience. The ideal place for an inexperienced user is a familiar, safe, indoor environment over which they have full control and is devoid of factors that can negatively influence one's mental state. In order to prepare a proper setting for hallucinogens, it is advised to take the following steps:")
            BulletPoints(
                points = listOf(
                    "Ensure that one is completely free of responsibilities for the duration of the experience, and ideally the day after. This is because even the simplest of tasks can become incredibly difficult and potentially stressful to perform while under the influence of hallucinogens. The user should be prepared to fully relax and not perform chores or everyday routines. This includes driving and operating heavy machinery.",
                    "Avoid people who are not directly participating in the experience. This includes relatives who may be sleeping in the same house and friends that are anything but extremely trustworthy, understanding, and informed about the effects of hallucinogens. The mere vicinity of unaware people can prompt anxiety and paranoia as well as prevent full immersion in the experience.",
                    "Avoid unfamiliar, loud, cluttered, and/or public environments. The user should select an environment over which they have a considerable degree of control. This can be as simple as having the ability to adjust the air conditioning settings or freely enter and exit a restroom. One should be able to sit, lie down, and walk around as they please for the full duration of the experience. The chosen setting should ideally be equipped with privacy, relaxing music, comfortable seating, and readily available food and water. Examples of such settings include a safe, comfortable room at home or a friend's house.",
                    "Avoid sources of anything that can generate \"bad vibes.\" The user should not expose themselves to unpleasant or disturbing stimuli such as scary films or dark music. If bad vibes are encompassing the experience, they can be escaped by quickly changing the immediate environment the user is in. For example, if one is sitting down with the lights off, stand up and turn the lights on, change the music, or move to a different room in the house."
                )
            )
            Text(text = "Once the user has become intimately familiar with their substance of choice, it is up to them as an individual whether they would be comfortable tripping in a less controlled environment such as out in nature, social gatherings, parties, raves, etc. However, it should be noted that tripping in these settings entails considerably more physical and legal risk.")
            Spacer(modifier = Modifier.height(5.dp))
            Divider()
            Text(text = "Set (State of Mind)", style = MaterialTheme.typography.h6)
            Text(
                text = """
                The user's set or state of mind in plays a major role in determining the outcome of a trip. Hallucinogens amplify one's current state of mind, mood and outlook: a positive mindset will likely become more positive and a negative one will become even more negative. As a result, hallucinogens should generally be avoided during acutely stressful or negative periods of life. Users should be fully aware of the ways in which hallucinogens, particularly psychedelics, are able to force one to face their internal problems that they may not be psychologically prepared to handle at that time.
                Those with preexisting mental conditions (especially individuals with psychotic illnesses like schizophrenia) should avoid hallucinogens due to the way they can strongly amplify one's underlying mental and emotional state as well as promote delusions and hallucinations. Those who wish to take hallucinogens with such conditions should seek the advice of a qualified medical practitioner.
                A common piece of advice while tripping is to "let go" and allow the effects of the substance to take charge. One should take the metaphorical passenger seat and forgo trying to control or suppress any part of the experience. It is extremely important that the user simply relaxes and take things as they come, as any resistance will only serve to amplify what is trying to be avoided.
                Additionally, the user must understand that the experience of tripping is often ungraspable, meaning that one should accept being unable to understand or express the full scope of what is happening during the experience. The user should embrace the fact that their thought processes, although potentially more lucid in some ways, will be unavoidably impaired along with fine motor control, conversational skills, and situational awareness. The user should be sure to frequently remind themselves that these effects are normal and, most importantly, temporary.
            """.trimIndent()
            )
            Spacer(modifier = Modifier.height(5.dp))
            Divider()
            Text(text = "Bodily State", style = MaterialTheme.typography.h6)
            Text(
                text = """
                The user's current bodily condition is just as important as one's mood and mindset when going into a trip. If one feels tired, sick or injured, these sensations will manifest as amplified versions of the same conditions which, when combined with possible body load, may easily detract from or ruin the experience.
                Instead of tripping while stressed, tired, sick or injured, one should wait for a more suitable opportunity. This will drastically lower the chances of having a negative or unfulfilling experience.
                """.trimIndent()
            )
            Spacer(modifier = Modifier.height(5.dp))
            Divider()
            Text(text = "Trip Sitters", style = MaterialTheme.typography.h6)
            Text(
                text = """
                When using hallucinogens, a trip sitter is strongly recommended to be present, particularly if one is inexperienced with the substance. It is the trip sitter's responsibility to assist the individual or group by maintaining a calm and grounded frame of mind. This can be accomplished by simply watching over the trippers and calmly reassuring them if they experience any anxiety or stress, while also preventing them from coming to any harm. There is an obvious correlation between the name "trip sitter" and "baby sitter": this is because trip sitting often feels like babysitting and it is a responsibility that must be taken every bit as seriously.
                A good trip sitter must fulfill a number of requirements. In addition to being a generally responsible adult, they should ideally be sober and able to relate to the group members' situation from either personal experiences or researched knowledge. Trip sitters should understand that when an individual is tripping, they may not be able to communicate or interact as they usually do. Also, their balance and spatial judgment may be impaired so assistance in performing tasks such as staying hydrated or navigating through an area can greatly reduce anxiety and confusion. The trip sitter can participate in the conversation, but should also remember to give the trippers space to explore the experience without too much external influence.
                Once the user becomes familiar with a substance, it becomes a personal choice as to whether or not they feel comfortable enough to trip without a sitter. It is also advised to use trip sitters when taking high doses or a dose one has never taken before. It should be remembered that having friends around while tripping is the best way to avoid potential psychological, medical, or legal consequences.
                """.trimIndent()
            )
            Divider()
            Spacer(modifier = Modifier.height(5.dp))
            Text(text = "Anchors", style = MaterialTheme.typography.h6)
            Text(text = "In the context of hallucinogen use, an anchor is an activity or physical object which keeps the user grounded during the heavy distortion of a person's sense of time, space, memory, and sense of self. At higher dosages, this can result in extreme disorientation and confusion. Anchors are often used to counteract this and maintain one's concept of the current situation as it is within reality.")
            Text(text = "Examples of anchors include:")
            BulletPoints(
                points = listOf(
                    "Familiar and uplifting music. An example of this includes our community good vibes portal. However, users are encouraged to create their own playlist that is composed of music one personally associates with being happy and relaxed.",
                    "An extremely personal and ingrained image or object.",
                    "Continuous repetition of a meaningful word or motto as a mantra.",
                    "Writing an easily readable reminder onto a large piece of paper and placing it close within one's visual field throughout the experience. Common reminders include the name of the substance along with its dosage and phrases such as \"You are tripping on LSD.\" The same principle can be used to write reminders on one's hand or other visible body parts.",
                    "An item of clothing or an accessory that is only worn during and therefore associated with the act of tripping."
                )
            )
            Spacer(modifier = Modifier.height(5.dp))
            Divider()
            Text(text = "Aborting Trips", style = MaterialTheme.typography.h6)
            Text(
                text = """
                Hallucinogens have the potential to become overwhelming and push the user into a paranoid or dreadful mood, particularly if they are inexperienced or in an inappropriate set and setting.
                If one decides to terminate the trip, benzodiazepines and other sedatives such as some antipsychotics can be considered analogous to an "eject button" of a downhill-headed or extensively long trip. These substances tend to be very effective tools in preventing panic attacks, paranoia, and possible traumatic experiences.
                However, experienced users generally advise trying to wait out difficult parts of a trip if possible. Challenging moments are often temporary and can turn out to be catalysts for the greatest learning experiences.
            """.trimIndent()
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}