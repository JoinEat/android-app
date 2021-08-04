package com.hno2.when2eat.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.hno2.when2eat.fragments.FriendRequestFragment
import com.hno2.when2eat.fragments.FriendSearchFragment
import com.hno2.when2eat.fragments.FriendSuccessFragment

class FriendFragmentViewPagerAdapter(fragment: Fragment):FragmentStateAdapter(fragment) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FriendSuccessFragment()
            1 -> FriendRequestFragment()
            else -> FriendSearchFragment()
        }
    }

    override fun getItemCount() = 3
}