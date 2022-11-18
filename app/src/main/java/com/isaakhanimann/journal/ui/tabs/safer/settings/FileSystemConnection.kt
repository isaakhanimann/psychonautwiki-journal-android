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

package com.isaakhanimann.journal.ui.tabs.safer.settings

import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class FileSystemConnection @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun getTextFromUri(uri: Uri): String? {
        try {
            val input = context.contentResolver.openInputStream(uri) ?: return null
            val inputAsString = input.bufferedReader().use { it.readText() }
            input.close()
            return inputAsString
        } catch (e: Exception) {
            return null
        }
    }

    fun saveTextInUri(uri: Uri, text: String) {
        try {
            val output = context.contentResolver.openOutputStream(uri) ?: return
            output.bufferedWriter().use { it.write(text) }
            output.close()
        } catch (_: Exception) {
            throw Exception("Failed To Save")
        }

    }


}