package com.shivam.singleactivityexample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseCoreActivity : AppCompatActivity() {
    abstract fun bindView(): ViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = bindView()
        setContentView(binding.root)
        initOnActivityCreate(savedInstanceState)
    }

    abstract fun initOnActivityCreate(savedInstanceState: Bundle?)

    open fun openFragment(
        fragment: Fragment,
        addToBackStack: Boolean = false,
        containerId: Int = getFragmentContainerLayoutId()
    ) {
        val fm = supportFragmentManager.beginTransaction()
        fm.replace(containerId, fragment, fragment.javaClass.name)
        if (addToBackStack) fm.addToBackStack(fragment.javaClass.name)
        fm.commit()
    }

    fun backStackCount() = supportFragmentManager.backStackEntryCount

    abstract fun getFragmentContainerLayoutId(): Int

}