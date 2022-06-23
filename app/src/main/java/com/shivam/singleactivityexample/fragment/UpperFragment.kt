package com.shivam.singleactivityexample.fragment

import android.view.View
import com.shivam.singleactivityexample.R
import com.shivam.singleactivityexample.databinding.FragmentUpperBinding
import com.shivam.singleactivityexample.navigation.HomeNavigation
import org.koin.android.ext.android.inject

class UpperFragment : BaseCoreFragment() {
    private val homeNavigation: HomeNavigation by inject()
    private var _viewBinding: FragmentUpperBinding? = null
    private val viewBinding get() = _viewBinding!!

    companion object {
        fun newInstance() = UpperFragment()
    }

    override fun bindView(rootView: View) {
        _viewBinding = FragmentUpperBinding.bind(rootView)
    }

    override fun unbindView() {
        _viewBinding = null
    }

    override fun getLayoutId() = R.layout.fragment_upper

    override fun initUiOnViewCreated() {
        viewBinding.upperItem.setOnClickListener {
            homeNavigation.openFragment(SecondChildFragment.newInstance())
        }

        viewBinding.fullScreenItem.setOnClickListener {
            homeNavigation.openFragmentInFullScreen(SecondChildFragment.newInstance())
        }
    }
}