package com.wscar.sunnyweather

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class MyApplication :Application(){
    companion object{
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        const val TOKEN = "392b72e054aa4623906c189aa3e3acdd"
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}