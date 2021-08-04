package com.hno2.when2eat.tools

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.hno2.when2eat.R
import org.json.JSONObject

class ToastMaker {
    @RequiresApi(Build.VERSION_CODES.P)
    fun toaster(c: Context?, jsonObject: JSONObject?) {
        c?.mainExecutor?.execute {
            when (jsonObject?.getInt("statusCode")) {
                200 -> Toast.makeText(
                    c,
                    c.getString(R.string.operation_successful),
                    Toast.LENGTH_LONG
                ).show()
                503 -> Toast.makeText(
                    c,
                    c.getString(R.string.no_internet_access_or_internal_error),
                    Toast.LENGTH_LONG
                ).show()
                else -> Toast.makeText(
                    c,
                    c.getString(R.string.error_message, jsonObject?.get("message")),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}