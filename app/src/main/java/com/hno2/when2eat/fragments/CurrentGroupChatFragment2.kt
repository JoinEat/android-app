package com.hno2.when2eat.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.hno2.when2eat.BuildConfig
import com.hno2.when2eat.R
import com.hno2.when2eat.activities.CurrentGroupActivity
import com.hno2.when2eat.adapters.ChatAdapter
import com.hno2.when2eat.adapters.ChatUnit
import com.hno2.when2eat.adapters.NoButtonAdapter
import com.hno2.when2eat.adapters.UnitData
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
    private lateinit var recyclerViewAdapter: ChatAdapter
    private lateinit var root: View
    private lateinit var recyclerView: RecyclerView

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

        if (returnedJSON.getInt("statusCode") != 200) return

        val returnedMessageList = returnedJSON.getJSONArray("messages")
        val data: MutableList<ChatUnit> = mutableListOf()
        for (i: Int in (0 until returnedMessageList.length())) {
            val json = returnedMessageList.getJSONObject(i)
            data.add(ChatUnit(json.getJSONObject("author").getString("name")
                    ,json.getString("text")
                    ,json.getJSONObject("author").getString("_id")))
        }

        recyclerViewAdapter.appendMessages(data)
        lastMessageID = if (returnedMessageList.length() == 0) "0"
        else returnedMessageList.getJSONObject(returnedMessageList.length() - 1).getString("_id")

        recyclerView.scrollToPosition(recyclerViewAdapter.itemCount-1)
    }

    private suspend fun sendMessage() {
        val messageTextArea = root.findViewById<EditText>(R.id.messageEdit)
        val message = messageTextArea.text.toString()

        val body = JSONObject()
        body.put("text",message)

        val url = BuildConfig.Base_URL + "/events/" + eventID + "/messages"
        val returnedJSON = withContext(Dispatchers.IO) {
            NetworkProcessor().sendRequest(
                    requireActivity(),
                    Request.Method.POST,
                    url,
                    body,
                    DataSaver().getToken(activity)
            )
        }

        if (returnedJSON.getInt("statusCode") != 200) {
            Log.e("connection_error","error")
            return
        }

        messageTextArea.setText("")
        val sentMessageBody = JSONObject()
        sentMessageBody.put("eventId", eventID)
        mSocket.emit("message_send",sentMessageBody)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        root = inflater.inflate(R.layout.fragment_current_group_chat2, container, false)

        initVariables()
        eventID = (activity as CurrentGroupActivity).eventID

        mSocket.connect()
        val joinRoomBody = JSONObject()
        joinRoomBody.put("eventId", eventID)
        mSocket.emit("message_join_room", joinRoomBody)

        val coroutineScope = CoroutineScope(Dispatchers.Main)
        coroutineScope.launch { updateMessage() }

        setSocketListeners()
        return root
    }

    private fun initVariables() {
        recyclerView = root.findViewById(R.id.messageRecyclerView)
        recyclerViewAdapter = ChatAdapter(DataSaver().getData(activity,"_id"))
        recyclerView.adapter = recyclerViewAdapter
        recyclerView.layoutManager = LinearLayoutManager(activity)

        val sendButton = root.findViewById<Button>(R.id.sendButton)
        sendButton.setOnClickListener {
            val coroutineScope = CoroutineScope(Dispatchers.Main)
            coroutineScope.launch { sendMessage() }
        }
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