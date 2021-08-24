package com.hno2.when2eat.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.hno2.when2eat.BuildConfig
import com.hno2.when2eat.R
import io.socket.client.Ack
import io.socket.client.IO
import io.socket.client.Socket

class CurrentGroupFriendInvitationFragment : Fragment() {

//    val mSocket :Socket = IO.socket(BuildConfig.Base_URL)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        mSocket.on("pong") { args ->
//            Log.e("effwfew1111","qfqfqw11111")
//        }
//        mSocket.connect()
//        mSocket.emit("ping",null)
        return inflater.inflate(R.layout.fragment_current_group_friend_invitation, container, false)
    }

//    override fun onDestroy() {
//        super.onDestroy()
//        mSocket.disconnect()
//        mSocket.off("pong")
//    }
}