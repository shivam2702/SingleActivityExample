package com.shivam.singleactivityexample

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shivam.singleactivityexample.navigation.BottomNavItems
import com.shivam.singleactivityexample.navigation.all_tabs

class MainActivityViewModel : ViewModel() {
    val homeBottomNavItems: MutableLiveData<ArrayList<BottomNavItems>> = MutableLiveData()
    val currentBottomNavItem: MutableLiveData<BottomNavItems> = MutableLiveData()
    var currentSelectedTab: BottomNavItems = BottomNavItems.LIBRARY

    init {
        homeBottomNavItems.value = all_tabs
    }

    fun getBottomNavItemPosition(items: BottomNavItems) = homeBottomNavItems.value!!.indexOf(items)
}