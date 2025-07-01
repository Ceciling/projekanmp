package com.ubaya.projekanmp_uas.session

import android.content.Context

class SessionManager(context: Context) {
    private val pref = context.getSharedPreferences("session", Context.MODE_PRIVATE)

    fun save(username: String) {
        pref.edit().putString("USER", username).apply()
    }

    fun get(): String? = pref.getString("USER", null)
    fun clear() = pref.edit().remove("USER").apply()
}
