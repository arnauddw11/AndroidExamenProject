package com.example.androidexamenproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.androidexamenproject.ui.NFTApp
import com.example.androidexamenproject.ui.theme.AppTheme

/**
 * MainActivity serves as the entry point of the application.
 * It initializes the UI and sets the content view using Jetpack Compose.
 */
class MainActivity : ComponentActivity() {

    /**
     * Called when the activity is starting. This is where most initialization should happen.
     * The UI content is set using Jetpack Compose, with a theme and a surface container.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     * this Bundle contains the data it most recently supplied in [onSaveInstanceState].
     * Note: Otherwise it is null.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NFTApp()
                }
            }
        }
    }
}

