package com.shivam.singleactivityexample.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.shivam.singleactivityexample.navigation.BottomNavItems

class HomeViewPagerAdapter(
    activity: FragmentActivity,
    private var items: ArrayList<BottomNavItems>
) : FragmentStateAdapter(activity) {

    override fun getItemCount() = items.size

    override fun createFragment(position: Int): Fragment {
        return items[position].classname.newInstance() as Fragment
    }
}