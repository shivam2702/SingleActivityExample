package com.shivam.singleactivityexample.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

abstract class BaseCoreFragment : Fragment() {
    private lateinit var rootView: View

    open fun onBackPressed() = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayoutId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        rootView = view
        super.onViewCreated(view, savedInstanceState)
        bindView(view)
        initUiOnViewCreated()
    }

    abstract fun bindView(rootView: View)
    abstract fun unbindView()

    abstract fun getLayoutId(): Int
    abstract fun initUiOnViewCreated()

}