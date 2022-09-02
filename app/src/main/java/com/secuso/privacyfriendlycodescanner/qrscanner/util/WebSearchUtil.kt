package com.secuso.privacyfriendlycodescanner.qrscanner.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.preference.PreferenceManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.secuso.privacyfriendlycodescanner.qrscanner.R

object WebSearchUtil {

    @JvmStatic
    fun openWebSearchDialog(context: Context, content: String) {
        val searchEngineURI = getSearchEngineURI(context)
        MaterialAlertDialogBuilder(context)
            .setMessage(
                context.resources.getString(
                    R.string.fragment_result_text_dialog_message,
                    getSearchEngineName(context)
                )
            )
            .setIcon(R.drawable.ic_warning)
            .setTitle(R.string.fragment_result_text_dialog_title)
            .setPositiveButton(
                R.string.fragment_result_text_dialog_positive_button
            ) { _, _ ->
                val uri =
                    Uri.parse(String.format(searchEngineURI, content))
                val search = Intent(Intent.ACTION_VIEW, uri)
                val caption: String =
                    context.resources.getStringArray(R.array.text_array).get(0)
                context.startActivity(Intent.createChooser(search, caption))
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }

    private fun getPrefSearchEngineIndex(context: Context): Int {
        val searchEngines: Array<String> =
            context.resources.getStringArray(R.array.pref_search_engine_values)
        val pref = PreferenceManager.getDefaultSharedPreferences(context)
        val activeSearchEngine = pref.getString("pref_search_engine", searchEngines[0])
        var i = 0
        while (i < searchEngines.size) {
            if (searchEngines[i] == activeSearchEngine) {
                break
            }
            i++
        }
        return if (i < searchEngines.size) {
            i
        } else 0
    }

    private fun getSearchEngineURI(context: Context): String {
        val searchEngineIndex = getPrefSearchEngineIndex(context)
        val searchEngineUris: Array<String> =
            context.resources.getStringArray(R.array.pref_search_engine_uris)
        return searchEngineUris[searchEngineIndex]
    }

    private fun getSearchEngineName(context: Context): String {
        val searchEngineIndex = getPrefSearchEngineIndex(context)
        val searchEnginesEntries: Array<String> =
            context.resources.getStringArray(R.array.pref_search_engine_entries)
        return searchEnginesEntries[searchEngineIndex]
    }
}