package com.pocketsos.app.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.pocketsos.app.presentation.navigation.Screen
import com.pocketsos.app.presentation.screens.exit.ExitModeScreen
import com.pocketsos.app.presentation.screens.history.HistoryScreen
import com.pocketsos.app.presentation.screens.home.HomeScreen
import com.pocketsos.app.presentation.screens.lists.ListDetailScreen
import com.pocketsos.app.presentation.screens.lists.MyListsScreen
import com.pocketsos.app.presentation.screens.reminders.RemindersScreen
import com.pocketsos.app.presentation.theme.PocketSosTheme
import com.pocketsos.app.presentation.viewmodel.PocketSosViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: PocketSosViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PocketSosApp(viewModel = viewModel)
        }
    }
}

@Composable
fun PocketSosApp(viewModel: PocketSosViewModel) {
    PocketSosTheme {
        val navController = rememberSwipeDismissableNavController()

        SwipeDismissableNavHost(
            navController = navController,
            startDestination = Screen.Home.route
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    viewModel = viewModel,
                    onNavigateToExitMode = { navController.navigate(Screen.ExitMode.route) },
                    onNavigateToMyLists = { navController.navigate(Screen.MyLists.route) },
                    onNavigateToReminders = { navController.navigate(Screen.Reminders.route) },
                    onNavigateToHistory = { navController.navigate(Screen.History.route) }
                )
            }

            composable(Screen.ExitMode.route) {
                ExitModeScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToHistory = {
                        navController.navigate(Screen.History.route) {
                            popUpTo(Screen.Home.route)
                        }
                    }
                )
            }

            composable(Screen.MyLists.route) {
                MyListsScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToListDetail = { listId ->
                        navController.navigate(Screen.ListDetail.createRoute(listId))
                    }
                )
            }

            composable(Screen.ListDetail.route) { backStackEntry ->
                val listId = backStackEntry.arguments?.getString("listId").orEmpty()
                ListDetailScreen(
                    listId = listId,
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToExitMode = {
                        navController.navigate(Screen.ExitMode.route) {
                            popUpTo(Screen.Home.route)
                        }
                    }
                )
            }

            composable(Screen.Reminders.route) {
                RemindersScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(Screen.History.route) {
                HistoryScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}
