package com.hno2.when2eat.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.hno2.when2eat.R
import com.hno2.when2eat.adapters.CurrentGroupActivityViewPagerAdapter
import org.json.JSONObject

class CurrentGroupActivity : AppCompatActivity() {
    lateinit var eventJSON: JSONObject

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_current_group)

        val extras: Bundle? = intent.extras
        eventJSON = if (extras?.getString("eventJSON") == null) {
            JSONObject()
        } else {
            JSONObject(extras.getString("eventJSON"))
        }

        val tabLayout = findViewById<TabLayout>(R.id.event_tabs)
        val viewPager2 = findViewById<ViewPager2>(R.id.event_view_pager)
        viewPager2.adapter = CurrentGroupActivityViewPagerAdapter(this)

        TabLayoutMediator(tabLayout,viewPager2) { tab: TabLayout.Tab, i: Int ->
            val array = resources.getStringArray(R.array.current_group_activity_tab_titles)
            tab.text = array[i]
        }.attach()
    }
}