package com.hno2.when2eat.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.hno2.when2eat.fragments.CurrentGroupFriendInvitationFragment
import com.hno2.when2eat.fragments.CurrentGroupFriendMemberFragment

class CurrentGroupMemberViewPagerAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> CurrentGroupFriendMemberFragment()
            else -> CurrentGroupFriendInvitationFragment()
        }
    }

    override fun getItemCount(): Int = 2
}