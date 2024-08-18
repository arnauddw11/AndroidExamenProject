package com.example.androidexamenproject

import android.app.Application
import com.example.androidexamenproject.data.AppContainer
import com.example.androidexamenproject.data.DefaultAppContainer

/**
 * Custom Application class for the NFT application.
 * Initializes the dependency injection container when the application starts.
 */
class NFTApplication: Application(){
    // Holds the app's dependency container, which will be initialized during app startup.
    lateinit var container: AppContainer
    /**
     * Called when the application is starting, before any other application objects have been created.
     * Initializes the dependency injection container for the application.
     */
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}