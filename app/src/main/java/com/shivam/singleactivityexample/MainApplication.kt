package com.shivam.singleactivityexample

import android.app.Application
import com.shivam.singleactivityexample.di.Module

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Module.init(this)
    }
}