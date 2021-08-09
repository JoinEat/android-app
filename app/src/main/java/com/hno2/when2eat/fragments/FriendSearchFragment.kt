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
import com.hno2.when2eat.tools.*
import kotlinx.coroutines.*
import org.json.JSONObject

class FriendSearchFragment : Fragment(), SearchView.OnQueryTextListener, OneButtonAdapter.OnItemClickHandler {
    lateinit var root: View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        root = inflater.inflate(R.layout.fragment_friend_search, container, false)
        initVariables(root)
        return root
    }

    private fun initVariables(root: View) {
        val searchView: SearchView = root.findViewById(R.id.friend_search_search_view)
        searchView.setOnQueryTextListener(this)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        val url = BuildConfig.Base_URL + "/users?nickNameContain=$query&excludeFriends=true"
        val coroutineScope = CoroutineScope(Dispatchers.Main)
        coroutineScope.launch {
            val returnedJSON = NetworkProcessor().sendRequest(
                    activity,
                    Request.Method.GET,
                    url,
                    null,
                    DataSaver().getToken(activity)
            )

            val data: MutableList<UnitData> = mutableListOf()
            if (returnedJSON.getInt("statusCode") == 200) {
                val friendArray = returnedJSON.getJSONArray("users")
                for (i: Int in (0 until friendArray.length())) {
                    val json = friendArray.getJSONObject(i)
                    data.add(UnitData(json.getString("name"),
                            "@" + json.getString("name"), //TODO: change name
                            json.getString("_id"),
                            "https://i.imgur.com/0F2Xfhs.png"))
                }
            }

            val recyclerView: RecyclerView = root.findViewById(R.id.friend_search_recycler_view)
            recyclerView.adapter = OneButtonAdapter(
                    data,
                    mutableListOf(
                            getString(R.string.friend_add_friend)
                    ),
                    this@FriendSearchFragment
            )
            recyclerView.layoutManager = LinearLayoutManager(activity)
        }

        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return false
    }

    override fun onItemClick(id: String, position: Int) {
        val coroutineScope = CoroutineScope(Dispatchers.Main)
        coroutineScope.launch {
            val friendData = NetworkDataGetter().getUserByID(activity, id)
            DialogMaker(requireActivity(), friendData).show(requireActivity().supportFragmentManager, "details")
        }
    }

    override suspend fun onBtn1Click(id: String, position: Int): Boolean {
        val url = BuildConfig.Base_URL + "/friends"

        val body = JSONObject()
        body.put("friendID", id)


        val returnedJSON = withContext(Dispatchers.IO) {
            NetworkProcessor().sendRequest(
                    activity,
                    Request.Method.POST, url, body, DataSaver().getToken(activity)
            )
        }
        ToastMaker().toaster(activity, returnedJSON)

        return when (returnedJSON.getInt("statusCode")) {
            200 -> true
            else -> false
        }
    }
}