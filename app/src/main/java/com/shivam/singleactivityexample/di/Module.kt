package com.shivam.singleactivityexample.di

import android.content.Context
import com.shivam.singleactivityexample.MainActivityViewModel
import com.shivam.singleactivityexample.navigation.HomeNavigation
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

object Module {

    private val homeModules = module {
        single { HomeNavigation() }
        viewModel { MainActivityViewModel() }
    }

    fun init(applicationContext: Context) {
        startKoin {
            androidContext(applicationContext)
            modules(
                listOf(
                    homeModules
                )
            )
        }
    }
}