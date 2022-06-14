package com.example.healthassistant.ui.settings

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor() : ViewModel() {

    val text = mutableStateOf("")

    init {
//        println("**********************1111**********************")
//
//        viewModelScope.launch(Dispatchers.IO) {
//            val postBody = "{\"query\":\"query AllSubstances {\\n          substances(limit: 9999) {\\n            name\\n            url\\n            effects {\\n              name\\n              url\\n            }\\n            class {\\n              chemical\\n              psychoactive\\n            }\\n            tolerance {\\n              full\\n              half\\n              zero\\n            }\\n            roas {\\n              name\\n              dose {\\n                units\\n                threshold\\n                light {\\n                  min\\n                  max\\n                }\\n                common {\\n                  min\\n                  max\\n                }\\n                strong {\\n                  min\\n                  max\\n                }\\n                heavy\\n              }\\n              duration {\\n                onset {\\n                  min\\n                  max\\n                  units\\n                }\\n                comeup {\\n                  min\\n                  max\\n                  units\\n                }\\n                peak {\\n                  min\\n                  max\\n                  units\\n                }\\n                offset {\\n                  min\\n                  max\\n                  units\\n                }\\n                total {\\n                  min\\n                  max\\n                  units\\n                }\\n                afterglow {\\n                  min\\n                  max\\n                  units\\n                }\\n              }\\n              bioavailability {\\n                min\\n                max\\n              }\\n            }\\n            summary\\n            addictionPotential\\n            toxicity\\n            crossTolerances\\n            uncertainInteractions {\\n              name\\n            }\\n            unsafeInteractions {\\n              name\\n            }\\n            dangerousInteractions {\\n              name\\n            }\\n          }\\n        }\\n\",\"variables\":{}}"
//            val client = OkHttpClient()
//            val request = Request.Builder()
//                .url("https://api.psychonautwiki.org")
//                .post(postBody.toRequestBody(MEDIA_TYPE_JSON))
//                .build()
//            println("**********************2222**********************")
//
//            client.newCall(request).execute().use { response ->
//                println("**********************3333**********************")
//                if (!response.isSuccessful) throw IOException("Unexpected code $response")
//                text.value = response.body!!.string()
//            }
//        }
    }

//    companion object {
//        val MEDIA_TYPE_JSON = "application/json".toMediaType()
//    }
}