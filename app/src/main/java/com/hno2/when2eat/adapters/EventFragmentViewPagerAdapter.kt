package com.hno2.when2eat.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.hno2.when2eat.fragments.EventCreateFragment
import com.hno2.when2eat.fragments.EventCurrentFragment
import com.hno2.when2eat.fragments.EventMyInvitationsFragment
import com.hno2.when2eat.fragments.EventSearchFragment

class EventFragmentViewPagerAdapter(fragment: Fragment):FragmentStateAdapter(fragment) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> EventCurrentFragment()
            1 -> EventCreateFragment()
            2 -> EventMyInvitationsFragment()
            else -> EventSearchFragment()
        }
    }

    override fun getItemCount() = 4
}