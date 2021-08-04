package com.hno2.when2eat.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hno2.when2eat.R
import com.hno2.when2eat.activities.CurrentGroupActivity
import com.hno2.when2eat.adapters.NoButtonAdapter
import com.hno2.when2eat.adapters.UnitData
import com.hno2.when2eat.tools.DataSaver
import com.hno2.when2eat.tools.NetworkDataGetter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EventCurrentFragment : Fragment(), NoButtonAdapter.OnItemClickHandler {
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_event_current, container, false)
        initVariables(root)
        return root
    }

    private fun initVariables(root: View) {

        val coroutineScope = CoroutineScope(Dispatchers.Main)
        coroutineScope.launch {
            val eventData: MutableList<UnitData> = withContext(Dispatchers.IO) {
                    NetworkDataGetter().getEventList(
                            requireActivity(),
                            DataSaver().getData(requireActivity(), "_id")
                    ) }

            val recyclerView: RecyclerView =
                    root.findViewById(R.id.event_current_recycler_view)
            recyclerView.adapter = NoButtonAdapter(
                    eventData,
                    this@EventCurrentFragment
            )
            recyclerView.layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun onItemClick(id: String, position: Int) {
        val coroutineScope = CoroutineScope(Dispatchers.Main)
        coroutineScope.launch {
            val currentGroupIntent = Intent(activity, CurrentGroupActivity::class.java)
            currentGroupIntent.putExtra("eventJSON", NetworkDataGetter().getEventByID(activity, id).toString())
            startActivity(currentGroupIntent)
        }
    }
}