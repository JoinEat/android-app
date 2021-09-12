package com.hno2.when2eat.fragments

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.android.volley.Request
import com.hno2.when2eat.BuildConfig
import com.hno2.when2eat.R
import com.hno2.when2eat.activities.WelcomePageActivity
import com.hno2.when2eat.tools.DataSaver
import com.hno2.when2eat.tools.NetworkProcessor
import com.hno2.when2eat.tools.ToastMaker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.lang.ClassCastException

class SettingsFragment : PreferenceFragmentCompat() {
    lateinit var preferenceChangeListener: SharedPreferences.OnSharedPreferenceChangeListener

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences_user, rootKey)
        setDefaultValues()
        setListeners()
    }

    private fun setDefaultValues() {
        val keys = resources.getStringArray(R.array.personal_preferences)
        for (key in keys) {
            val pref: Preference = findPreference<Preference>(key) as Preference
            val defaultValue = DataSaver().getData(requireActivity(), key)
           // Log.e(key,defaultValue)
            if (defaultValue is String) pref.summary = defaultValue
            pref.setDefaultValue(defaultValue)
        }
    }

    private fun setListeners() {
        preferenceChangeListener =
                SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
                    onChangeSettings(key, try{
                        sharedPreferences.getString(key,"")
                    }catch(e: ClassCastException){
                        sharedPreferences.getBoolean(key, false)
                    })
                }

        val logoutButton: Preference = findPreference<Preference>("logout") as Preference
        logoutButton.onPreferenceClickListener = Preference.OnPreferenceClickListener(
                fun(_: Preference): Boolean {
                    DataSaver().clearData(activity)
                    val logoutIntent = Intent(activity, WelcomePageActivity::class.java)
                    startActivity(logoutIntent)
                    return true
                }
        )
    }

    private fun onChangeSettings(key: String, changedName: Any?) {
        val url = BuildConfig.Base_URL + "/users"

        val body = JSONObject()
        body.put(key, changedName)

        val coroutineScope = CoroutineScope(Dispatchers.Main)
        coroutineScope.launch {
            val returnedJSON = NetworkProcessor().sendRequest(
                    requireActivity(),
                    Request.Method.PUT,
                    url,
                    body,
                    DataSaver().getToken(activity)
            )
            ToastMaker().toaster(requireActivity(), returnedJSON)
            if (returnedJSON.getInt("statusCode") == 200) {
                DataSaver().setData(activity, key, changedName)

                val pref: Preference = findPreference<Preference>(key) as Preference
                pref.summary = if (changedName is String?) {
                    changedName
                } else { "" }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(
                preferenceChangeListener
        )
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(
                preferenceChangeListener
        )
    }
}