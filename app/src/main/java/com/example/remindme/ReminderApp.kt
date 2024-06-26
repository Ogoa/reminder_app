package com.example.remindme

import android.app.Application
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.remindme.reminder.data.NewReminderViewModel
import com.example.remindme.reminder.presentation.CreateReminderScreen
import com.example.remindme.reminder.presentation.SavedRemindersScreen
import com.example.remindme.ui.theme.RemindMeTheme


/**
 * Defines destination strings for routes to display different screens
 */
enum class RemindMeScreens(@StringRes val title: Int) {
    HomeScreen(title = R.string.app_name),
    Create(title = R.string.create_reminder)
}


/**
 * Parent composable of the app
 */
@Composable
fun RemindMeApp() {
    val navController: NavHostController = rememberNavController()

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = RemindMeScreens.valueOf(
        backStackEntry?.destination?.route ?: RemindMeScreens.HomeScreen.name
    )

    val context = LocalContext.current.applicationContext
    val newReminderViewModel = remember {
        NewReminderViewModel(context as Application)
    }

    Scaffold(
        topBar = { RemindMeAppTopBar(currentScreen) },
        floatingActionButton = {
            if(currentScreen == RemindMeScreens.HomeScreen) {
                FloatingActionButton(
                    onClick = { navController.navigate(RemindMeScreens.Create.name) }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = null
                    )
                }
            }
        }
    ) {
        // NavHost to control navigation between different screens
        NavHost(
            navController = navController,
            startDestination = RemindMeScreens.HomeScreen.name,
            modifier = Modifier.padding(it)
        ) {
            // Define routes to different screens
            composable(route = RemindMeScreens.HomeScreen.name) {
                SavedRemindersScreen(viewModel = newReminderViewModel)
            }

            composable(route = RemindMeScreens.Create.name) {
                CreateReminderScreen(
                    navController = navController,
                    newReminderViewModel = newReminderViewModel
                )
            }
        }
    }
}


/**
 * Displays the contents of the top bar
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemindMeAppTopBar(
    currentScreen: RemindMeScreens,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(id = currentScreen.title),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        },
        modifier = modifier
    )
}


@Preview(showBackground = true)
@Composable
fun ReminderAppPreview() {
    RemindMeTheme(darkTheme = true) {
        RemindMeApp()
    }
}


