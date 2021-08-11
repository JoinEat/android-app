package com.hno2.when2eat.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.hno2.when2eat.BuildConfig
import com.hno2.when2eat.R
import com.hno2.when2eat.activities.CurrentGroupActivity
import com.hno2.when2eat.adapters.NoButtonAdapter
import com.hno2.when2eat.adapters.UnitData
import com.hno2.when2eat.tools.DataSaver
import com.hno2.when2eat.tools.DialogMaker
import com.hno2.when2eat.tools.NetworkDataGetter
import com.hno2.when2eat.tools.NetworkProcessor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CurrentGroupFriendMemberFragment : Fragment(), NoButtonAdapter.OnItemClickHandler {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_current_group_friend_member, container, false)
        initVariables(root)
        return root
    }

    private fun initVariables(root: View) {
        val url = BuildConfig.Base_URL + "/events/" + (activity as CurrentGroupActivity).eventJSON.getString("_id") + "/members"
        val coroutineScope = CoroutineScope(Dispatchers.Main)
        coroutineScope.launch{
            val returnedJSON = NetworkProcessor().sendRequest(
                    activity,
                    Request.Method.GET,
                    url,
                    null,
                    DataSaver().getToken(activity)
            )

            val data: MutableList<UnitData> = mutableListOf()
            if (returnedJSON.getInt("statusCode") == 200) {
                val friendArray = returnedJSON.getJSONArray("members")
                for (i: Int in (0 until friendArray.length())) {
                    val json = friendArray.getJSONObject(i).getJSONObject("memberId")
                    data.add(UnitData(json.getString("name"),
                            "@" + json.getString("name"), //TODO: change name
                            json.getString("_id"),
                            "https://i.imgur.com/0F2Xfhs.png"))
                }
            }

            val recyclerView: RecyclerView =
                    root.findViewById(R.id.current_group_friend_member_recycler_view)
            recyclerView.adapter = NoButtonAdapter(
                    data,
                    this@CurrentGroupFriendMemberFragment
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
}