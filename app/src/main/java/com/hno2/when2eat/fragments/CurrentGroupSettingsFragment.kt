package com.hno2.when2eat.fragments

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.android.volley.Request
import com.hno2.when2eat.BuildConfig
import com.hno2.when2eat.R
import com.hno2.when2eat.activities.CurrentGroupActivity
import com.hno2.when2eat.tools.DataSaver
import com.hno2.when2eat.tools.NetworkProcessor
import com.hno2.when2eat.tools.ToastMaker
import kotlinx.coroutines.*
import org.json.JSONException
import org.json.JSONObject

class CurrentGroupSettingsFragment : PreferenceFragmentCompat() {
    lateinit var preferenceChangeListener: SharedPreferences.OnSharedPreferenceChangeListener

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences_event, rootKey)
        setDefaultValues()
        setListeners()
    }

    private fun setDefaultValues() {
        val keys = resources.getStringArray(R.array.group_preferences)
        for (key in keys) {
            val pref: Preference = findPreference<Preference>(key) as Preference

            val coroutineScope = CoroutineScope(Dispatchers.Main)
            coroutineScope.launch {
                val url = BuildConfig.Base_URL + "/events/" + (activity as CurrentGroupActivity).eventID
                val eventJSON = withContext(Dispatchers.IO) {
                    NetworkProcessor().sendRequest(
                        requireActivity(),
                        Request.Method.GET,
                        url,
                        null,
                        DataSaver().getToken(activity)
                    ) }

                val defaultValue = try {
                    eventJSON.getString(key)
                } catch (e: JSONException) {
                    ""
                }
                pref.summary = defaultValue
                pref.setDefaultValue(defaultValue)
            }
        }
    }

    private fun setListeners() {
        preferenceChangeListener =
                SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
                    onChangeSettings(key, sharedPreferences.getString(key, ""))
                }
    }

    private fun onChangeSettings(key: String, changedName: String?) {
        val url = BuildConfig.Base_URL + "/events/" + (activity as CurrentGroupActivity).eventID

        val body = JSONObject()
        body.put(key, changedName)

        val coroutineScope = CoroutineScope(Dispatchers.Main)
        coroutineScope.launch {
            val returnedJSON = withContext(Dispatchers.IO) {
                    NetworkProcessor().sendRequest(
                            requireActivity(),
                            Request.Method.PUT,
                            url,
                            body,
                            DataSaver().getToken(activity)
                    ) }

            ToastMaker().toaster(requireActivity(), returnedJSON)
            if (returnedJSON.getInt("statusCode") == 200) {
                val pref: Preference = findPreference<Preference>(key) as Preference
                pref.summary = changedName
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