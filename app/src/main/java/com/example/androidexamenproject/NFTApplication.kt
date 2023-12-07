package com.example.androidexamenproject

import android.app.Application
import com.example.androidexamenproject.data.AppContainer
import com.example.androidexamenproject.data.DefaultAppContainer

class NFTApplication: Application(){
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}