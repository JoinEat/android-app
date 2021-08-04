package com.hno2.when2eat.tools

import android.content.Context
import android.content.SharedPreferences
import com.android.volley.Request
import com.hno2.when2eat.R
import com.hno2.when2eat.adapters.UnitData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class DataSaver {
    private val SHARED_PREF_NAME = "user_token_data"

    fun getToken(c: Context?): String {
        val prefs: SharedPreferences? =
            c?.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        return try {
            prefs?.getString("token", "").toString()
        } catch (e: Exception) {
            ""
        }
    }

    fun getData(c: Context?, key: String): String {
        val prefs: SharedPreferences? =
            c?.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        return prefs?.getString(key, "").toString()
    }

    fun setToken(c: Context?, token: String?) {
        val prefs: SharedPreferences? =
            c?.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = prefs?.edit()
        editor?.putString("token", token)
        editor?.apply()
    }

    fun setData(c: Context?, key: String, value: Any?) {
        val prefs: SharedPreferences? =
                c?.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = prefs?.edit()
        editor?.putString(key,value?.toString())
        editor?.apply()
    }

    fun setData(c: Context?, json: JSONObject) { //TODO: not the whole JSON!!!
        val prefs: SharedPreferences? =
            c?.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = prefs?.edit()
        val attributes = c?.resources?.getStringArray(R.array.attributes)

        if (attributes != null) {
            for (attribute: String in attributes) {
                val value = try {
                    json.getString(attribute)
                } catch (e: JSONException) {
                    ""
                }
                editor?.putString(attribute, value) //TODO: for Boolean type
            }
        }
        editor?.apply()
    }

    fun clearData(c: Context?) {
        val prefs: SharedPreferences? =
            c?.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = prefs?.edit()
        editor?.clear()
        editor?.apply()
    }
}