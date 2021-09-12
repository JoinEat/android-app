package com.hno2.when2eat.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hno2.when2eat.R
import com.hno2.when2eat.adapters.OneButtonAdapter
import com.hno2.when2eat.adapters.UnitData
import com.hno2.when2eat.tools.DataParser
import com.hno2.when2eat.tools.DataSaver
import com.hno2.when2eat.tools.DialogMaker
import com.hno2.when2eat.tools.NetworkDataGetter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FriendSuccessFragment : Fragment(), OneButtonAdapter.OnItemClickHandler {
    private lateinit var root:View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        root = inflater.inflate(R.layout.fragment_friend_success, container, false)
        setRecyclerView()
        return root
    }

    override fun onResume() {
        super.onResume()
        setRecyclerView()
    }

    private fun setRecyclerView() {
        val coroutineScope = CoroutineScope(Dispatchers.Main)
        coroutineScope.launch{
            val friendData: MutableList<UnitData> =
                    NetworkDataGetter().getFriend(
                            requireActivity(),
                            "success",
                            DataSaver().getID(activity)
                    )

                val recyclerView: RecyclerView =
                        root.findViewById(R.id.friend_success_recycler_view)
                recyclerView.adapter = OneButtonAdapter(
                        friendData,
                        mutableListOf(
                                getString(R.string.friend_delete)
                        ),
                        this@FriendSuccessFragment
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

    override suspend fun onBtn1Click(id: String, position: Int): Boolean {
        return false
    }
}