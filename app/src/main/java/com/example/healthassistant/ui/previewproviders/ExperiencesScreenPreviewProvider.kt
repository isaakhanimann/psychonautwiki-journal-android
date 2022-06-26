package com.example.healthassistant.ui.previewproviders

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.healthassistant.data.room.experiences.entities.Experience
import com.example.healthassistant.data.room.experiences.entities.ExperienceWithIngestions
import com.example.healthassistant.data.room.experiences.entities.Ingestion
import com.example.healthassistant.data.substances.AdministrationRoute

class ExperiencesScreenPreviewProvider : PreviewParameterProvider<Map<String, List<ExperienceWithIngestions>>> {
    override val values: Sequence<Map<String, List<ExperienceWithIngestions>>> = sequenceOf(
        mapOf(
            // todo: add color
            Pair(
                first = "2022", second = listOf(
                    ExperienceWithIngestions(
                        experience = Experience(
                            id = 0,
                            title = "Festival",
                            text = "Some notes"
                        ),
                        ingestions = listOf(
                            Ingestion(
                                substanceName = "MDMA",
                                time = getDate(year = 2022, month = 7, day = 5, hourOfDay = 14, minute = 20)!!,
                                administrationRoute = AdministrationRoute.ORAL,
                                dose = 90.0,
                                isDoseAnEstimate = false,
                                units = "mg",
                                experienceId = 0,
                                notes = null
                            ),
                            Ingestion(
                                substanceName = "Cocaine",
                                time = getDate(year = 2022, month = 7, day = 5, hourOfDay = 14, minute = 20)!!,
                                administrationRoute = AdministrationRoute.INSUFFLATED,
                                dose = 30.0,
                                isDoseAnEstimate = false,
                                units = "mg",
                                experienceId = 0,
                                notes = null
                            ),
                            Ingestion(
                                substanceName = "Ketamine",
                                time = getDate(year = 2022, month = 7, day = 5, hourOfDay = 14, minute = 20)!!,
                                administrationRoute = AdministrationRoute.INSUFFLATED,
                                dose = 50.0,
                                isDoseAnEstimate = false,
                                units = "mg",
                                experienceId = 0,
                                notes = null
                            )
                        )
                    ),
                    ExperienceWithIngestions(
                        experience = Experience(
                            id = 0,
                            title = "Bachelor Party",
                            text = "Some notes"
                        ),
                        ingestions = listOf(
                            Ingestion(
                                substanceName = "MDMA",
                                time = getDate(year = 2022, month = 6, day = 21, hourOfDay = 12, minute = 20)!!,
                                administrationRoute = AdministrationRoute.ORAL,
                                dose = 90.0,
                                isDoseAnEstimate = false,
                                units = "mg",
                                experienceId = 0,
                                notes = null
                            )
                        )
                    )

                )
            ),
            Pair(
                first = "2021", second = listOf(
                    ExperienceWithIngestions(
                        experience = Experience(
                            id = 0,
                            title = "Liam's Birthday",
                            text = "Some notes"
                        ),
                        ingestions = listOf(
                            Ingestion(
                                substanceName = "Cocaine",
                                time = getDate(year = 2021, month = 9, day = 2, hourOfDay = 18, minute = 13)!!,
                                administrationRoute = AdministrationRoute.INSUFFLATED,
                                dose = 30.0,
                                isDoseAnEstimate = false,
                                units = "mg",
                                experienceId = 0,
                                notes = null
                            ),
                            Ingestion(
                                substanceName = "Ketamine",
                                time = getDate(year = 2021, month = 9, day = 2, hourOfDay = 18, minute = 13)!!,
                                administrationRoute = AdministrationRoute.INSUFFLATED,
                                dose = 20.0,
                                isDoseAnEstimate = false,
                                units = "mg",
                                experienceId = 0,
                                notes = null
                            )
                        )
                    ),
                    ExperienceWithIngestions(
                        experience = Experience(
                            id = 0,
                            title = "Last day in Stockholm",
                            text = "Some notes"
                        ),
                        ingestions = listOf(
                            Ingestion(
                                substanceName = "LSD",
                                time = getDate(year = 2021, month = 7, day = 22, hourOfDay = 18, minute = 13)!!,
                                administrationRoute = AdministrationRoute.ORAL,
                                dose = 90.0,
                                isDoseAnEstimate = false,
                                units = "µg",
                                experienceId = 0,
                                notes = null
                            ),
                            Ingestion(
                                substanceName = "Cocaine",
                                time = getDate(year = 2021, month = 7, day = 22, hourOfDay = 18, minute = 13)!!,
                                administrationRoute = AdministrationRoute.INSUFFLATED,
                                dose = 20.0,
                                isDoseAnEstimate = false,
                                units = "mg",
                                experienceId = 0,
                                notes = null
                            )
                        )
                    )
                )
            )
        ),
        mapOf()
    )
}