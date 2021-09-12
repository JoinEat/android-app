package com.hno2.when2eat.tools

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.android.volley.Request
import com.hno2.when2eat.R
import com.hno2.when2eat.adapters.UnitData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import java.lang.NullPointerException
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.reflect.typeOf

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

    fun getID(c: Context?): String {
        val prefs: SharedPreferences? =
                c?.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)

        return prefs?.getString("_id","").toString()
    }

    fun getData(c: Context?, key: String): Any? {
        val prefs: SharedPreferences? =
            c?.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)

        if (prefs?.contains(key) == true) {
            return if (prefs.getString(key,"")?.isNotEmpty() == true)
                prefs.getString(key,"").toString()
            else prefs.getBoolean(key,false).toString()
        } else {
            return null
        }
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
            for (attribute: String in attributes){
                val value = try {
                    json.get(attribute)
                } catch (e: JSONException) {
                    0
                }

                if (value is String) {
                    editor?.putString(attribute, value) //TODO: for Boolean type
                } else if (value is Boolean) {
                    editor?.putBoolean(attribute, value)
                }
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