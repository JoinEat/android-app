package com.hno2.when2eat.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.hno2.when2eat.R
import com.hno2.when2eat.adapters.MainActivityViewPagerAdapter


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tabLayout = findViewById<TabLayout>(R.id.main_tabs)
        val viewPager2 = findViewById<ViewPager2>(R.id.main_view_pager)
        viewPager2.adapter = MainActivityViewPagerAdapter(this)

        TabLayoutMediator(tabLayout,viewPager2) { tab: TabLayout.Tab, i: Int ->
            val array = resources.getStringArray(R.array.main_activity_tab_titles)
            tab.text = array[i]
        }.attach()
    }
}