/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 3.0. If a copy of the MPL was not distributed with this file, You can obtain one at https://www.gnu.org/licenses/gpl-3.0.en.html.
 */

package com.isaakhanimann.journal.ui.settings

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