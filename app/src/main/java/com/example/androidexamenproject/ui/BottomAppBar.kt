package com.example.androidexamenproject.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.androidexamenproject.ui.viewModel.AlchemyViewModel

/**
 * A composable function that displays a bottom app bar with navigation icons.
 * The app bar allows users to navigate to the home (collections) screen and the account screen.
 *
 * @param navController The NavController used to navigate between different screens.
 * @param alchemyViewModel The AlchemyViewModel that provides user details such as the avatar URL.
 */
@Composable
fun BottomAppBar(navController: NavController, alchemyViewModel: AlchemyViewModel) {
    // The Material3 BottomAppBar component with customized colors
    androidx.compose.material3.BottomAppBar(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
    ) {
        // Row that arranges the icons evenly across the app bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            // Home icon that navigates to the collections screen when clicked
            Icon(
                Icons.Default.Home,
                contentDescription = "Home",
                modifier = Modifier
                    .padding(8.dp)
                    .size(50.dp)
                    .clickable {
                        navController.navigate("collections")
                    },
            )

            // Check if the user's avatar URL is available
            val avatar = alchemyViewModel.ethDetails.collectAsState().value?.avatar
            if (avatar != null && avatar.toString() != "") {
                // Display the user's avatar if available
                AsyncImage(
                    model = avatar.toString(),
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .padding(8.dp)
                        .size(50.dp)
                        .clickable {
                            navController.navigate("account")
                        }
                )
            } else {
                // Default account icon if no avatar is available
                Icon(
                    Icons.Default.AccountCircle,
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .padding(8.dp)
                        .size(50.dp)
                        .clickable {
                            navController.navigate("account")
                        },
                )
            }
        }
    }
}