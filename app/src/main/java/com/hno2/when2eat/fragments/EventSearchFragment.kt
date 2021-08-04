package com.hno2.when2eat.fragments

import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.hno2.when2eat.BuildConfig
import com.hno2.when2eat.R
import com.hno2.when2eat.adapters.OneButtonAdapter
import com.hno2.when2eat.adapters.UnitData
import com.hno2.when2eat.tools.DataSaver
import com.hno2.when2eat.tools.NetworkProcessor
import com.hno2.when2eat.tools.ToastMaker
import kotlinx.coroutines.*

class EventSearchFragment : Fragment(), SearchView.OnQueryTextListener, OneButtonAdapter.OnItemClickHandler {
    lateinit var root: View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        root = inflater.inflate(R.layout.fragment_event_search, container, false)
        initVariables(root)
        return root
    }

    private fun initVariables(root: View) {
        val searchView: SearchView = root.findViewById(R.id.event_search_search_view)
        searchView.setOnQueryTextListener(this)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        val url = BuildConfig.Base_URL + "/events"

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
                    data.add(UnitData(json.getString("title"),
                            "@" + json.getString("creator"), //TODO: change name
                            json.getString("_id"),
                            "https://i.imgur.com/0F2Xfhs.png"))
                }
            }

            val recyclerView: RecyclerView = root.findViewById(R.id.event_search_recycler_view)
            recyclerView.adapter = OneButtonAdapter(
                    data,
                    mutableListOf(
                            getString(R.string.event_attend)
                    ),
                    this@EventSearchFragment
            )
            recyclerView.layoutManager = LinearLayoutManager(activity)
        }

        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return false
    }

    override fun onItemClick(id: String, position: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun onBtn1Click(id: String, position: Int):Boolean {
        val url = BuildConfig.Base_URL + "/events/$id/requests"

            val returnedJSON = withContext(Dispatchers.IO) {
                NetworkProcessor().sendRequest(
                        activity,
                        Request.Method.POST, url, null, DataSaver().getToken(activity)
                )
            }

            ToastMaker().toaster(activity, returnedJSON)
        return when (returnedJSON.getInt("statusCode")) {
            200 -> true
            else -> false
        }
    }
}