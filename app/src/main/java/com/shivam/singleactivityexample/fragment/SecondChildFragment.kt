package com.shivam.singleactivityexample.fragment

import android.view.View
import com.shivam.singleactivityexample.R
import com.shivam.singleactivityexample.databinding.FragmentSecondChildBinding
import com.shivam.singleactivityexample.navigation.HomeNavigation
import org.koin.android.ext.android.inject

class SecondChildFragment : BaseCoreFragment() {
    private val homeNavigation: HomeNavigation by inject()
    private var _viewBinding: FragmentSecondChildBinding? = null
    private val viewBinding get() = _viewBinding!!

    companion object {
        fun newInstance() = SecondChildFragment()
    }

    override fun bindView(rootView: View) {
        _viewBinding = FragmentSecondChildBinding.bind(rootView)
    }

    override fun unbindView() {
        _viewBinding = null
    }

    override fun getLayoutId() = R.layout.fragment_second_child

    override fun initUiOnViewCreated() {
    }
}