package com.example.aurameditation

import android.content.Context
import android.content.SharedPreferences

object GooglePlayBillingPreferences {
    private lateinit var prefs : SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private const val PREFS_NAME = "demo"
    private const val IS_PREMIUM = "premium"
    private const val IS_ADDREMOVE = "addremove"

    fun init(context: Context){
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        editor = prefs.edit()
        editor.commit()
    }

    fun savePremiumToPref(b : Boolean){
        editor.putBoolean(IS_PREMIUM, b)
        editor.commit()
    }

    fun saveRemoveAdd(b: Boolean){
        editor.putBoolean(IS_ADDREMOVE, b)
        editor.commit()
    }

    fun isPremium() : Boolean{
        return prefs.getBoolean(IS_PREMIUM, false)
    }

    fun isRemoveAdd(): Boolean{
        return prefs.getBoolean(IS_ADDREMOVE, false)
    }
}