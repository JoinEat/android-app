package com.hno2.when2eat.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.hno2.when2eat.BuildConfig
import com.hno2.when2eat.R
import com.hno2.when2eat.adapters.TwoButtonAdapter
import com.hno2.when2eat.adapters.UnitData
import com.hno2.when2eat.tools.*
import kotlinx.coroutines.*


class FriendRequestFragment : Fragment(), TwoButtonAdapter.OnItemClickHandler {
    private lateinit var root:View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        root = inflater.inflate(R.layout.fragment_friend_request, container, false)
        setRecyclerView()
        return root
    }

    override fun onResume() {
        super.onResume()
        setRecyclerView()
    }

    private fun setRecyclerView() {
        val coroutineScope = CoroutineScope(Dispatchers.Main)
        coroutineScope.launch {
            val friendData: MutableList<UnitData> =
                    NetworkDataGetter().getFriend(
                            requireActivity(),
                            "requested", DataSaver().getID(activity)
                    )

            val recyclerView: RecyclerView =
                    root.findViewById(R.id.friend_request_recycler_view)
            recyclerView.adapter = TwoButtonAdapter(
                    friendData,
                    mutableListOf(
                            getString(R.string.friend_accept),
                            getString(R.string.friend_decline)
                    ),
                    this@FriendRequestFragment
            )
            recyclerView.layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun onItemClick(id: String, position: Int) {
        val coroutineScope = CoroutineScope(Dispatchers.Main)
        coroutineScope.launch {
            val friendData = NetworkDataGetter().getUserByID(activity, id)
            DialogMaker(requireActivity(), friendData).show(requireActivity().supportFragmentManager, "details")
        }
    }

    override suspend fun onBtn1Click(id: String, position: Int):Boolean {
        val url = BuildConfig.Base_URL + "/friends/$id"



            val returnedJSON =
                    withContext(Dispatchers.IO) {
                        NetworkProcessor()
                                .sendRequest(activity, Request.Method.PUT, url, null, DataSaver().getToken(activity))
                    }
            ToastMaker().toaster(activity, returnedJSON)
        return when (returnedJSON.getInt("statusCode")) {
            200 -> true
            else -> false
        }
    }

    override suspend fun onBtn2Click(id: String, position: Int):Boolean {
        val url = BuildConfig.Base_URL + "/friends/$id"

            val returnedJSON = withContext(Dispatchers.IO) {
                NetworkProcessor()
                        .sendRequest(activity, Request.Method.DELETE, url, null, DataSaver().getToken(activity))
            }
            ToastMaker().toaster(activity, returnedJSON)

        return when (returnedJSON.getInt("statusCode")) {
            200 -> true
            else -> false
        }
    }
}