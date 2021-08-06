package com.hno2.when2eat.tools

import android.content.Context
import com.hno2.when2eat.R
import org.json.JSONException
import org.json.JSONObject
import android.content.res.Resources

class DataParser {
    private fun parseHandle(name: String, realName: String, nickName: String): String {
        return when {
            realName.isEmpty() and nickName.isEmpty() -> name
            realName.isNotEmpty() and nickName.isNotEmpty() -> "$realName($nickName)"
            realName.isNotEmpty() -> realName
            else -> nickName
        }
    }

    fun friendDataParser(c: Context, friendData: JSONObject): String {
        val id = friendData.getString("name")
        val realName = try {
            friendData.getString("realName")
        } catch (e: JSONException) {
            ""
        }
        val nickName = try {
            friendData.getString("nickName")
        } catch (e: JSONException) {
            ""
        }
        val handle = parseHandle(id, realName, nickName)
        val gender = try {
            friendData.getString("gender")
        } catch (e: JSONException) {
            ""
        }
        val school = try {
            friendData.getString("school")
        } catch (e: JSONException) {
            ""
        }
        val department = try {
            friendData.getString("department")
        } catch (e: JSONException) {
            ""
        }

        return c.getString(R.string.user_details,handle,gender,school + department)
    }
}