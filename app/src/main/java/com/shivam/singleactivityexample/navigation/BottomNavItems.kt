package com.shivam.singleactivityexample.navigation

import com.shivam.singleactivityexample.R
import com.shivam.singleactivityexample.fragment.FaqFragment
import com.shivam.singleactivityexample.fragment.LibraryFragment
import com.shivam.singleactivityexample.fragment.ProfileFragment

enum class BottomNavItems(
    val drawable: Int,
    val classname: Class<*>,
) {
    LIBRARY(R.drawable.ic_library, LibraryFragment::class.java),
    FAQ(R.drawable.ic_faq, FaqFragment::class.java),
    PROFILE(R.drawable.ic_profile, ProfileFragment::class.java),
}

val all_tabs = arrayListOf(
    BottomNavItems.LIBRARY,
    BottomNavItems.FAQ,
    BottomNavItems.PROFILE,
)