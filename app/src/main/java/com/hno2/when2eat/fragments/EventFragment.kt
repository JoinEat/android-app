package com.hno2.when2eat.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.hno2.when2eat.R
import com.hno2.when2eat.adapters.EventFragmentViewPagerAdapter

class EventFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_event, container, false)

        val tabLayout = root.findViewById<TabLayout>(R.id.event_tabs)
        val viewPager2 = root.findViewById<ViewPager2>(R.id.event_view_pager)
        viewPager2.adapter = EventFragmentViewPagerAdapter(this)

        TabLayoutMediator(tabLayout, viewPager2) { tab: TabLayout.Tab, i: Int ->
            val array = resources.getStringArray(R.array.event_fragment_tab_titles)
            tab.text = array[i]
        }.attach()

        return root
    }
}