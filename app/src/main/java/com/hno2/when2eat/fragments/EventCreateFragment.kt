package com.hno2.when2eat.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.hno2.when2eat.BuildConfig
import com.hno2.when2eat.R
import com.hno2.when2eat.tools.DataSaver
import com.hno2.when2eat.tools.NetworkProcessor
import com.hno2.when2eat.tools.ToastMaker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class EventCreateFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_event_create, container, false)
        initViews(root)
        setListeners(root)
        return root
    }

    lateinit var eventName: EditText
    lateinit var createButton: Button

    private fun initViews(root: View) {
        eventName = root.findViewById(R.id.event_name)
        createButton = root.findViewById(R.id.create_button)
    }

    private fun setListeners(root: View) {
        createButton.setOnClickListener {
            val url = BuildConfig.Base_URL + "/events"

            val body = JSONObject()
            body.put("title", eventName.text)

            val coroutineScope = CoroutineScope(Dispatchers.Main)
            coroutineScope.launch {


                val returnedJSON = withContext(Dispatchers.IO) {
                    NetworkProcessor().sendRequest(
                            activity,
                            Request.Method.POST,
                            url,
                            body,
                            DataSaver().getToken(activity)
                    )
                }

                ToastMaker().toaster(activity, returnedJSON)
            }
        }
    }
}