package com.example.healthassistant.ui.settings

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor() : ViewModel() {

    val text = mutableStateOf("")

    init {
        val retrofit = GraphQLInstance.graphQLService
        val paramObject = JSONObject()
        paramObject.put("query", """
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
        """.trimIndent())
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = retrofit.postDynamicQuery(paramObject.toString())
                text.value = response.body().toString()
            } catch (e: java.lang.Exception) {
                text.value = e.toString()
            }
        }
    }
}


interface GraphQLService {

    @Headers("Content-Type: application/json")
    @POST("/")
    suspend fun postDynamicQuery(@Body body: String): Response<String>
}

object GraphQLInstance {

    private const val BASE_URL: String = "https://api.psychonautwiki.org/"

    val graphQLService: GraphQLService by lazy {
        Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build().create(GraphQLService::class.java)
    }
}