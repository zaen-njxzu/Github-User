package com.zaen.githubuser.util

import android.content.Context

internal class UserPreference(context: Context) {
    companion object {
        private const val PREFS_NAME = "user_pref"
        private const val ALARM_SETTING = "alarm_setting"
        private const val ONE_TIME_EXECUTION = "one_time_execution"
    }

    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    var isAlarmActive: Boolean
        get() = preferences.getBoolean(ALARM_SETTING, false)
        set(value) {
            val editor = preferences.edit()
            editor.putBoolean(ALARM_SETTING, value)
            editor.commit()
        }

    var isFirstOpenApp: Boolean
        get() = preferences.getBoolean(ONE_TIME_EXECUTION, true)
        set(value) {
            val editor = preferences.edit()
            editor.putBoolean(ONE_TIME_EXECUTION, value)
            editor.commit()
        }
}