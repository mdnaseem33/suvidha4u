package com.martvalley.suvidha_u.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPref(internal var context: Context) {
    private val sharedPref: SharedPreferences =
        context.getSharedPreferences("UserFile", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPref.edit()


    private val inAppPreferences: SharedPreferences =
        context.getSharedPreferences("appPreferences", Context.MODE_PRIVATE)
    private val inAppEditor: SharedPreferences.Editor = inAppPreferences.edit()


    var isFirstTimeLaunch: Boolean
        get() = inAppPreferences.getBoolean(Constants.IS_FIRST_LAUNCH, true)
        set(value) {
            inAppEditor.putBoolean(Constants.IS_FIRST_LAUNCH, value)
            inAppEditor.commit()
        }

    fun save(KEY_NAME: String, text: String) {
        sharedPref.let {
            val editor: SharedPreferences.Editor = it.edit()
            editor.putString(KEY_NAME, text)
            editor.apply()
            editor.commit()
        }
    }

    fun save(KEY_NAME: String, value: Int) {
        sharedPref.let {
            val editor: SharedPreferences.Editor = it.edit()
            editor.putInt(KEY_NAME, value)
            editor.apply()
            editor.commit()
        }
    }

    fun save(KEY_NAME: String, status: Boolean) {
        sharedPref.let {
            val editor: SharedPreferences.Editor = it.edit()
            editor.putBoolean(KEY_NAME, status)
            editor.apply()
            editor.commit()
        }
    }

    fun getValueString(KEY_NAME: String): String? {
        return sharedPref.getString(KEY_NAME, null)
    }

    fun getValueInt(KEY_NAME: String): Int? {
        return sharedPref.getInt(KEY_NAME, 0)
    }

    fun getValueBoolean(KEY_NAME: String, defaultValue: Boolean): Boolean? {
        return sharedPref?.getBoolean(KEY_NAME, defaultValue)
    }

    fun clearSharedPreference() {
        sharedPref.let {
            val editor: SharedPreferences.Editor = it.edit()
            editor.clear()
            editor.apply()
            editor.commit()
        }
    }


    fun removeValue(KEY_NAME: String) {
        sharedPref?.let {
            val editor: SharedPreferences.Editor = it.edit()
            editor.remove(KEY_NAME)
            editor.apply()
            editor.commit()
        }
    }


}