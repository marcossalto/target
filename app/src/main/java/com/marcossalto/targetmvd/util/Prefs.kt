package com.marcossalto.targetmvd.util

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.google.gson.Gson

class Prefs(context: Context) {

    val prefs: SharedPreferences = PreferenceManager
        .getDefaultSharedPreferences(context)
    private val gson: Gson = Gson()

    fun clear() = prefs.edit().clear().apply()
}