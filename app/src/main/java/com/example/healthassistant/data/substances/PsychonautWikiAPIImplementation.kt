package com.example.healthassistant.data.substances

import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PsychonautWikiAPIImplementation @Inject constructor(
    private val psychonautWikiAPI: PsychonautWikiAPI
) {
    suspend fun getStringFromAPI(): String {
        val paramObject = JSONObject()
        paramObject.put(
            "query", """
            query AllSubstances {
                      substances(limit: 9999) {
                        name
                        url
                        effects {
                          name
                          url
                        }
                        class {
                          chemical
                          psychoactive
                        }
                        tolerance {
                          full
                          half
                          zero
                        }
                        roas {
                          name
                          dose {
                            units
                            threshold
                            light {
                              min
                              max
                            }
                            common {
                              min
                              max
                            }
                            strong {
                              min
                              max
                            }
                            heavy
                          }
                          duration {
                            onset {
                              min
                              max
                              units
                            }
                            comeup {
                              min
                              max
                              units
                            }
                            peak {
                              min
                              max
                              units
                            }
                            offset {
                              min
                              max
                              units
                            }
                            total {
                              min
                              max
                              units
                            }
                            afterglow {
                              min
                              max
                              units
                            }
                          }
                          bioavailability {
                            min
                            max
                          }
                        }
                        summary
                        addictionPotential
                        toxicity
                        crossTolerances
                        uncertainInteractions {
                          name
                        }
                        unsafeInteractions {
                          name
                        }
                        dangerousInteractions {
                          name
                        }
                      }
                    }
        """.trimIndent()
        )
        val response = psychonautWikiAPI.postDynamicQuery(paramObject.toString())
        return response.body().toString()
    }
}