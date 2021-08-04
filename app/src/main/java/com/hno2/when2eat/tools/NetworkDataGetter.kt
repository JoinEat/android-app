package com.hno2.when2eat.tools

import android.content.Context
import com.android.volley.Request
import com.hno2.when2eat.BuildConfig
import com.hno2.when2eat.adapters.UnitData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject

class NetworkDataGetter {
    suspend fun getEventByID(c: Context?, id: String): JSONObject {
        val url = BuildConfig.Base_URL + "/events/$id"

        val returnedJSON = withContext(Dispatchers.IO) {
            NetworkProcessor().sendRequest(
                    c,
                    Request.Method.GET,
                    url,
                    JSONObject(),
                    DataSaver().getToken(c)
            )
        }
        return if (returnedJSON.getInt("statusCode") == 200) {
            returnedJSON.getJSONObject("event")
        } else {
            JSONObject()
        }
    }

    suspend fun getUserByID(c: Context?, id: String): JSONObject {
        val url = BuildConfig.Base_URL + "/users/$id"

        val returnedJSON = withContext(Dispatchers.IO) {
            NetworkProcessor().sendRequest(
                    c,
                    Request.Method.GET,
                    url,
                    JSONObject(),
                    DataSaver().getToken(c)
            )
        }

        return if (returnedJSON.getInt("statusCode") == 200) {
            returnedJSON.getJSONObject("user")
        } else {
            JSONObject()
        }
    }

    suspend fun getFriend(c: Context?, status: String, id: String): MutableList<UnitData> {
        val url = BuildConfig.Base_URL + "/friends?state=$status&userID=$id"

        val returnedJSON = withContext(Dispatchers.IO) {
            NetworkProcessor().sendRequest(
                    c,
                    Request.Method.GET,
                    url,
                    JSONObject(),
                    DataSaver().getToken(c)
            )
        }

        if (returnedJSON.getInt("statusCode") == 200) {
            val returnList: MutableList<UnitData> = mutableListOf()
            val friendArray = returnedJSON.getJSONArray("friends")
            for (i: Int in (0 until friendArray.length())) {
                val userJSON = friendArray.getJSONObject(i).getJSONObject("friendId")
                val avatarUrl = try {
                    returnedJSON.getString("avatarUrl")
                } catch (e: JSONException) {
                    "https://i.imgur.com/0F2Xfhs.png"
                }

                returnList.add(UnitData(
                        userJSON.getString("name"),
                        "@" + userJSON.getString("name"), //TODO: change name
                        userJSON.getString("_id"),
                        avatarUrl
                )
                )
            }
            return returnList
        } else {
            return mutableListOf()
        }

    }

    suspend fun getEventList(c: Context?, id: String): MutableList<UnitData> {
        val url = BuildConfig.Base_URL + "/users/$id"

        val returnedJSON = withContext(Dispatchers.IO) {
            NetworkProcessor().sendRequest(
                    c,
                    Request.Method.GET,
                    url,
                    null,
                    DataSaver().getToken(c)
            )
        }
        if (returnedJSON.getInt("statusCode") == 200) {
            val returnList: MutableList<UnitData> = mutableListOf()
            val eventArray = returnedJSON.getJSONObject("user").getJSONArray("currentEvent")
            for (i: Int in (0 until eventArray.length())) {
                val event = eventArray.getJSONObject(i)
                val avatarUrl = try {
                    returnedJSON.getString("avatarUrl")
                } catch (e: JSONException) {
                    "https://i.imgur.com/0F2Xfhs.png"
                }

                returnList.add(UnitData(
                        event.getString("title"),
                        "@${event.getString("_id")}", //TODO: change name
                        event.getString("_id"),
                        avatarUrl
                )
                )
            }
            return returnList
        } else {
            return mutableListOf()
        }
    }
}