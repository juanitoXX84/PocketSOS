package com.pocketsos.app.presentation.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object ExitMode : Screen("exit_mode")
    object MyLists : Screen("my_lists")
    object ListDetail : Screen("list_detail/{listId}") {
        fun createRoute(listId: String): String = "list_detail/$listId"
    }
    object Reminders : Screen("reminders")
    object History : Screen("history")
}
