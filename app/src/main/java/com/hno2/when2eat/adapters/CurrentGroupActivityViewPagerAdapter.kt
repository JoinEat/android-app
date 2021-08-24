package com.hno2.when2eat.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.hno2.when2eat.fragments.CurrentGroupChatFragment
import com.hno2.when2eat.fragments.CurrentGroupChatFragment2
import com.hno2.when2eat.fragments.CurrentGroupFriendFragment
import com.hno2.when2eat.fragments.CurrentGroupSettingsFragment

class CurrentGroupActivityViewPagerAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> CurrentGroupFriendFragment()
            1 -> CurrentGroupChatFragment2()
            else -> CurrentGroupSettingsFragment()
        }
    }

    override fun getItemCount(): Int = 3
}