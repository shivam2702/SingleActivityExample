package com.shivam.singleactivityexample

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.viewbinding.ViewBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.shivam.singleactivityexample.adapter.HomeViewPagerAdapter
import com.shivam.singleactivityexample.databinding.ActivityMainBinding
import com.shivam.singleactivityexample.fragment.BaseCoreFragment
import com.shivam.singleactivityexample.navigation.BottomNavItems
import com.shivam.singleactivityexample.navigation.HomeNavigation
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseCoreActivity() {
    private val homeNavigation: HomeNavigation by inject()
    private val homeActivityViewModel: MainActivityViewModel by viewModel()
    private var homeItem: ArrayList<BottomNavItems> = ArrayList()
    private var currentTab: TabLayout.Tab? = null

    protected lateinit var viewBinding: ActivityMainBinding

    override fun bindView(): ViewBinding =
        ActivityMainBinding.inflate(layoutInflater).apply { viewBinding = this }

    override fun initOnActivityCreate(savedInstanceState: Bundle?) {
        initHomeNavigation()
        init()
    }

    override fun getFragmentContainerLayoutId() = R.id.upperContainer


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater).apply { viewBinding = this }
        setContentView(viewBinding.root)
    }

    private fun initHomeNavigation() {
        homeNavigation.init(
            supportFragmentManager,
            R.id.upperContainer,
            R.id.fullContainer,
            { viewBinding.upperContainer.childCount },
            { viewBinding.fullContainer.childCount },
            ::onHomePageViewerLifeCycleChange,
            ::onBackStackChange
        )
    }

    private fun onBackStackChange() {
        // Do not allow events below full/upper container when full/upper container contains fragments
        viewBinding.upperContainer.isClickable = viewBinding.upperContainer.childCount > 0
        viewBinding.fullContainer.isClickable = viewBinding.fullContainer.childCount > 0
    }

    private fun onHomePageViewerLifeCycleChange(isResumed: Boolean) {
        if (!isResumed) return
        val selectedTabFragment =
            supportFragmentManager.findFragmentByTag("f" + viewBinding.bottomNavigation.selectedTabPosition)
        selectedTabFragment?.let {
            if (it.isAdded) {
                it.onResume()
            }
        }

    }

    private fun init() {
        homeActivityViewModel.homeBottomNavItems.observe(this, {
            homeItem = it
            initHomeViewPager()
            prepareBottomNavigation()
        })

        homeActivityViewModel.currentBottomNavItem.observe(this, {
            onNavigationSelect(it)
        })
    }

    private fun onNavigationSelect(item: BottomNavItems) {
        homeNavigation.clearBackStack()
        viewBinding.bottomNavigation.selectTab(
            viewBinding.bottomNavigation.getTabAt(
                homeActivityViewModel.getBottomNavItemPosition(item)
            )
        )
    }

    private fun onTabSelect(tab: TabLayout.Tab?): Boolean {
        val bottomNavItems = homeItem[tab?.position!!]
        homeActivityViewModel.currentSelectedTab = bottomNavItems
        return navigateToNonFullScreenTab(tab)
    }

    private fun navigateToNonFullScreenTab(tab: TabLayout.Tab?): Boolean {
        // Return if same tab is reselected, avoid clearing stack
        if (currentTab == tab) return true
        currentTab = tab
        Handler(Looper.getMainLooper()).post { homeNavigation.clearBackStack() }
        return false
    }

    private fun prepareBottomNavigation() {
        viewBinding.apply {
            bottomNavigation.apply {
                addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                    override fun onTabSelected(tab: TabLayout.Tab?) {
                        onTabSelect(tab)
                    }

                    override fun onTabUnselected(tab: TabLayout.Tab?) {}
                    override fun onTabReselected(tab: TabLayout.Tab?) {}
                })

                tabGravity = TabLayout.GRAVITY_FILL
                tabMode = TabLayout.MODE_FIXED

                TabLayoutMediator(
                    viewBinding.bottomNavigation,
                    viewBinding.homeViewPager
                ) { tab, position ->
                    tab.text = homeItem[position].name
                    tab.setIcon(homeItem[position].drawable)
                }.attach()
            }
        }
    }

    private fun initHomeViewPager() {
        viewBinding.homeViewPager.apply {
            offscreenPageLimit = homeItem.size
            adapter = HomeViewPagerAdapter(this@MainActivity, homeItem)
        }
    }

    override fun onBackPressed() {
        if (!isTopFragmentHandledBackPress())
            super.onBackPressed()
    }

    private fun isTopFragmentHandledBackPress(): Boolean {
        homeNavigation.apply {
            val topFragment = getTopFragmentFromStack()
            return topFragment is BaseCoreFragment && topFragment.onBackPressed()
        }
    }
}