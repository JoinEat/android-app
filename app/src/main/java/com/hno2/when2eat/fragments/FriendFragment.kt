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
import com.hno2.when2eat.adapters.FriendFragmentViewPagerAdapter

class FriendFragment : Fragment() {
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_friend, container, false)

        val tabLayout = root.findViewById<TabLayout>(R.id.friend_tabs)
        val viewPager2 = root.findViewById<ViewPager2>(R.id.friend_view_pager)
        viewPager2.adapter = FriendFragmentViewPagerAdapter(this)

        TabLayoutMediator(tabLayout, viewPager2) { tab: TabLayout.Tab, i: Int ->
            val array = resources.getStringArray(R.array.friend_fragment_tab_titles)
            tab.text = array[i]
        }.attach()
        return root
    }
}