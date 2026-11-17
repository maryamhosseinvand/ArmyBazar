package com.example.armybazar

import android.app.Application
import com.example.armybazar.di.myModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApp : Application(){
     override fun onCreate() {
         super.onCreate()

         startKoin {
             androidContext(this@MyApp)
             modules(myModules)
         }

     }
}