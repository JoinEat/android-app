package com.hno2.when2eat.tools

import android.content.Context
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject
import java.nio.charset.Charset
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class NetworkProcessor {
    class RequestWithHeaders(
        c: Context?,
        method: Int,
        url: String,
        body: JSONObject?,
        var token: String,
        cont: Continuation<JSONObject>
    ) :
        JsonObjectRequest(
            method, url, body,
            { json ->
                json.put("statusCode", 200)
                cont.resume(json)
            },
            { error ->
                val networkResponse = error.networkResponse
                val json = try {
                    JSONObject(
                        String(
                            networkResponse?.data ?: ByteArray(0),
                            Charset.forName(HttpHeaderParser.parseCharset(networkResponse?.headers))
                        )
                    )
                } catch (e: JSONException) {
                    JSONObject().put("statusCode", 503)
                }

                cont.resume(json)
            }) {
        override fun getHeaders(): Map<String, String> {
            val params = HashMap<String, String>(super.getHeaders())
            if (token.isNotEmpty()) params["Authorization"] = "Bearer $token"
            return params
        }
    }

    suspend fun sendRequest(
        c: Context?,
        method: Int,
        url: String,
        body: JSONObject?,
        token: String
    ) = suspendCoroutine<JSONObject> { cont ->
        RequestWithHeaders(c, method, url, body, token, cont).also {
            val queu = Volley.newRequestQueue(c)
            it.setShouldCache(false)
            queu.add(it)
        }
    }
}
