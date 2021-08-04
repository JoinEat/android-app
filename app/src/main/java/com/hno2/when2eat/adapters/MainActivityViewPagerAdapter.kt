package com.hno2.when2eat.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.hno2.when2eat.fragments.EventFragment
import com.hno2.when2eat.fragments.FriendFragment
import com.hno2.when2eat.fragments.NotificationFragment
import com.hno2.when2eat.fragments.SettingsFragment

class MainActivityViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity){
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> EventFragment()
            1 -> FriendFragment()
            2 -> NotificationFragment()
            else -> SettingsFragment()
        }
    }

    override fun getItemCount(): Int {
        return 4
    }
}