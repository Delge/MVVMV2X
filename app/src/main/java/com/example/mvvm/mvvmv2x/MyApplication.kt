package com.example.mvvm.mvvmv2x

import android.app.Application
import android.content.Context

class MyApplication :Application() {
    companion object{
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}