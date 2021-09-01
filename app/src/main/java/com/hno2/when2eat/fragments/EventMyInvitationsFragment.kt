package com.hno2.when2eat.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.hno2.when2eat.BuildConfig
import com.hno2.when2eat.R
import com.hno2.when2eat.adapters.OneButtonAdapter
import com.hno2.when2eat.adapters.UnitData
import com.hno2.when2eat.tools.DataSaver
import com.hno2.when2eat.tools.NetworkDataGetter
import com.hno2.when2eat.tools.NetworkProcessor
import kotlinx.coroutines.*

class EventMyInvitationsFragment : Fragment(), OneButtonAdapter.OnItemClickHandler {
    private lateinit var root: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        root = inflater.inflate(R.layout.fragment_event_my_invitations, container, false)
        setRecyclerView()
        return root
    }

    override fun onResume() {
        super.onResume()
        setRecyclerView()
    }

    private fun setRecyclerView() {
        val url = BuildConfig.Base_URL + "/users/myInvitations"

        val coroutineScope = CoroutineScope(Dispatchers.Main)
        coroutineScope.launch {
            val returnedJSON = withContext(Dispatchers.IO) {
                NetworkProcessor().sendRequest(
                        activity,
                        Request.Method.GET,
                        url,
                        null,
                        DataSaver().getToken(activity)
                )
            }


            val data: MutableList<UnitData> = mutableListOf()
            if (returnedJSON.getInt("statusCode") == 200) {
                val eventArray = returnedJSON.getJSONArray("events")
                for (i: Int in (0 until eventArray.length())) {
                    val json = eventArray.getJSONObject(i)
                    data.add(UnitData(json.getJSONObject("eventId").getString("title"),
                            "@" + json.getJSONObject("eventId").getString("creator"), //TODO: change name
                            json.getJSONObject("eventId").getString("_id"),
                            "https://i.imgur.com/0F2Xfhs.png"))
                }
            }

            val recyclerView: RecyclerView = root.findViewById(R.id.event_my_invitations_recycler_view)
            recyclerView.adapter = OneButtonAdapter(
                    data,
                    mutableListOf(
                            getString(R.string.event_attend)
                    ),
                    this@EventMyInvitationsFragment
            )
            recyclerView.layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun onItemClick(id: String, position: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun onBtn1Click(id: String, position: Int): Boolean {
        val url = BuildConfig.Base_URL + "/events/" + id + "/invitations"
        val returnedJSON = withContext(Dispatchers.IO) {
            NetworkProcessor().sendRequest(
                    activity,
                    Request.Method.PUT,
                    url,
                    null,
                    DataSaver().getToken(activity)
            )
        }

        return (returnedJSON.getInt("statusCode") == 200)
    }
}