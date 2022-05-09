package com.example.healthassistant.data.substances

import kotlinx.serialization.Serializable

@Serializable
data class SubstanceDecoded(val name: String, val url: String)
