package com.shivam.singleactivityexample.fragment

import android.view.View
import com.shivam.singleactivityexample.R
import com.shivam.singleactivityexample.databinding.FragmentLibraryBinding
import com.shivam.singleactivityexample.navigation.HomeNavigation
import org.koin.android.ext.android.inject

class LibraryFragment : BaseCoreFragment() {
    private val homeNavigation: HomeNavigation by inject()
    private var _viewBinding: FragmentLibraryBinding? = null
    private val viewBinding get() = _viewBinding!!

    companion object {
        fun newInstance() = LibraryFragment()
    }

    override fun bindView(rootView: View) {
        _viewBinding = FragmentLibraryBinding.bind(rootView)
    }

    override fun unbindView() {
        _viewBinding = null
    }

    override fun getLayoutId() = R.layout.fragment_library

    override fun initUiOnViewCreated() {
        viewBinding.upperItem.setOnClickListener {
            homeNavigation.openFragment(UpperFragment.newInstance())
        }

        viewBinding.fullScreenItem.setOnClickListener {
            homeNavigation.openFragmentInFullScreen(UpperFragment.newInstance())
        }
    }
}