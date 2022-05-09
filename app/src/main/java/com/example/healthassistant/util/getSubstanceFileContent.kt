package com.example.healthassistant.util

import android.content.Context

fun getSubstanceFileContent(context: Context): String {
    return context.assets.open("Substances-Date8May2022-Time15h12m.json").bufferedReader().use { it.readText() }
}