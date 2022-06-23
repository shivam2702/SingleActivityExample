package com.shivam.singleactivityexample.navigation

import android.util.Log
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

class HomeNavigation {
    private lateinit var fragmentManager: FragmentManager
    private var fullScreenContainerChildCount = 0
    private var upperContainerChildCount = 0

    @LayoutRes
    private var upperContainer = -1

    @LayoutRes
    private var fullContainer = -1

    companion object {
        private const val TAG = "HomeNavigation"
    }

    /*Only HomeActivity should call this API once for initialisation*/
    fun init(
        mFragmentManager: FragmentManager,
        upperContainer: Int,
        fullContainer: Int,
        getUpperContainerChildCount: () -> Int,
        getFullScreenContainerChildCount: () -> Int,
        onHomePageViewerLifeCycleChange: (isResume: Boolean) -> Unit,
        onBackStackChange: () -> Unit
    ) {
        logI("HomeNavigation initiated")
        fragmentManager = mFragmentManager
        fullScreenContainerChildCount = getFullScreenContainerChildCount()
        upperContainerChildCount = getUpperContainerChildCount()
        this.upperContainer = upperContainer
        this.fullContainer = fullContainer
        this.fragmentManager.addOnBackStackChangedListener {
            onBackStackChange()
            onBackStackChange(
                getUpperContainerChildCount,
                getFullScreenContainerChildCount,
                onHomePageViewerLifeCycleChange
            )
        }
    }

    private fun onBackStackChange(
        getUpperContainerChildCount: () -> Int, getFullScreenContainerChildCount: () -> Int,
        onHomePageViewerLifeCycleChange: (isResume: Boolean) -> Unit
    ) {
        logI(
            "backStackCount =  ${fragmentManager.backStackEntryCount}  " +
                    "fullContainer child Count = ${getFullScreenContainerChildCount()} " +
                    "upperContainer child Count = ${getUpperContainerChildCount()}  Top Fragment  ${getTopFragmentFromStack()}"
        )

        when {
            fullScreenContainerChildCount == 0 && getFullScreenContainerChildCount() > 0 -> {
                // When switching from Upper container to full screen container,
                // if any fragment exist in the upper container move it to pause state else move viewpager  fragment to pause state
                onEntryToFullScreenMode(
                    getUpperContainerChildCount,
                    onHomePageViewerLifeCycleChange
                )
            }

            fullScreenContainerChildCount > 0 && getFullScreenContainerChildCount() == 0 -> {
                /* Exiting From Full screen stack, Notify the top fragment (Could be any fragment or Home View pager) which is
                present in the upper container about its visibility */
                onExitFromFullScreenMode(
                    getUpperContainerChildCount,
                    onHomePageViewerLifeCycleChange
                )
            }

            upperContainerChildCount == 0 && getUpperContainerChildCount() > 0 -> {
                // Entering to upper container fragment stack, Notify Home View Pager Visibility
                onHomePageViewerLifeCycleChange(false)
                logI("Notify Home View Pager Hidden")
            }

            upperContainerChildCount > 0 && getUpperContainerChildCount() == 0 -> {
                // Exiting From upper container fragment stack, Notify Home View Pager Visibility
                onHomePageViewerLifeCycleChange(true)
                logI("Notify Home View Pager Visibility")
            }
        }

        fullScreenContainerChildCount = getFullScreenContainerChildCount()
        upperContainerChildCount = getUpperContainerChildCount()
    }

    private fun onEntryToFullScreenMode(
        getUpperContainerChildCount: () -> Int,
        onHomePageViewerLifeCycleChange: (isResume: Boolean) -> Unit
    ) {
        if (getUpperContainerChildCount() > 0) {
            logI("Notify upper containers top fragment Hidden")
            getBelowTopFragmentFromStack()?.onPause()
        } else {
            logI("Notify Home View Pager Hidden")
            onHomePageViewerLifeCycleChange(false)
        }
    }

    private fun onExitFromFullScreenMode(
        getUpperContainerChildCount: () -> Int,
        onHomePageViewerLifeCycleChange: (isResume: Boolean) -> Unit
    ) {
        when (getUpperContainerChildCount()) {
            0 -> {
                onHomePageViewerLifeCycleChange(true)
                logI("Notify Home View Pager Visibility")
            }
            else -> {
                logI("Notify upper containers top fragment Visibility")
                getTopFragmentFromStack()?.onResume()
            }
        }
    }

    private fun getBelowTopFragmentFromStack(): Fragment? {
        val fragmentStackSize = fragmentManager.fragments.size
        return if (fragmentManager.fragments.isEmpty() || fragmentStackSize == 1) null
        else fragmentManager.fragments[fragmentStackSize - 2]
    }

    fun getTopFragmentFromStack(): Fragment? {
        logI("back count size: " + fragmentManager.backStackEntryCount)
        logI("size: " + fragmentManager.fragments.size)
        logI(fragmentManager.fragments.toString())
        return if (fragmentManager.fragments.isEmpty()) null else fragmentManager.fragments.last()
    }

    fun openFragment(fragment: Fragment, addToBackStack: Boolean = true) =
        pushFragment(fragment, addToBackStack, upperContainer)

    fun openFragmentInFullScreen(fragment: Fragment) =
        pushFragment(fragment, true, fullContainer)

    fun openFragmentInFullScreen(className: String) {
        val instance = Class.forName(className).newInstance()
        if (instance is Fragment)
            openFragmentInFullScreen(instance)
    }

    private fun pushFragment(
        fragment: Fragment,
        addToBackStack: Boolean = false,
        containerId: Int
    ) {
        val ft = fragmentManager.beginTransaction()
        ft.replace(containerId, fragment, fragment.javaClass.name)
        if (addToBackStack) ft.addToBackStack(fragment.javaClass.name)
        ft.commitAllowingStateLoss()
    }

    fun clearBackStack() = clearBackStack(null)

    fun clearBackStack(backStackClearedListener: (() -> Unit)? = null) {
        if (fragmentManager.backStackEntryCount > 0 && !fragmentManager.isStateSaved) {
            fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
        backStackClearedListener?.let { it() }
    }

    fun isTopFragmentInstanceOf(classname: Class<*>): Boolean =
        getTopFragmentFromStack()?.javaClass?.name == classname.name

    fun isFragmentsExistInStack(listOfClasses: List<Class<*>>): Boolean {
        listOfClasses.forEach {
            if (fragmentManager.findFragmentByTag(it.name) != null)
                return true
        }
        return false
    }

    private fun logI(message: String) = Log.i(TAG, message)
}