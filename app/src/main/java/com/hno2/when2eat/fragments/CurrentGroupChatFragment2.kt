package com.hno2.when2eat.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.hno2.when2eat.BuildConfig
import com.hno2.when2eat.R
import com.hno2.when2eat.activities.CurrentGroupActivity
import com.hno2.when2eat.tools.DataSaver
import com.hno2.when2eat.tools.NetworkProcessor
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class CurrentGroupChatFragment2 : Fragment() {
    private lateinit var eventID: String
    private val mSocket: Socket = IO.socket(BuildConfig.Base_URL)
    private var lastMessageID = "0"

    private suspend fun updateMessage() {
        val url = BuildConfig.Base_URL + "/events/" + eventID + "/messages?nextKey=" + lastMessageID
        val returnedJSON = withContext(Dispatchers.IO) {
            NetworkProcessor().sendRequest(
                    requireActivity(),
                    Request.Method.GET,
                    url,
                    null,
                    DataSaver().getToken(activity)
            )
        }

        val returnedJSONObject = returnedJSON.getJSONArray("messages")
        //TODO: Process messages
        lastMessageID = returnedJSONObject.getJSONObject(returnedJSONObject.length() - 1).getString("_id")

        Log.e("returnedJSON", returnedJSON.toString())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        eventID = (activity as CurrentGroupActivity).eventID
        mSocket.connect()

        val joinRoomBody = JSONObject()
        joinRoomBody.put("eventId", eventID)
        mSocket.emit("message_join_room", joinRoomBody)

        val coroutineScope = CoroutineScope(Dispatchers.Main)
        coroutineScope.launch { updateMessage() }

        setSocketListeners()
        return inflater.inflate(R.layout.fragment_current_group_chat2, container, false)
    }

    private fun setSocketListeners() {
        mSocket.on("message_update") {
            val coroutineScope = CoroutineScope(Dispatchers.Main)
            coroutineScope.launch { updateMessage() }
        }
    }

    private fun destroySocketListeners() {
        mSocket.off("message_update")
    }


    override fun onDestroy() {
        super.onDestroy()
        mSocket.disconnect()
        destroySocketListeners()
    }
}