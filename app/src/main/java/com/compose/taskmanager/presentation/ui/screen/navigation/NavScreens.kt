package com.compose.taskmanager.presentation.ui.screen.navigation

import androidx.annotation.StringRes
import com.compose.taskmanager.R
import kotlinx.serialization.Serializable

sealed class Destinations {
    @Serializable
    data object BottomNavGraph: Destinations()

    @Serializable
    data object Home: Destinations()

    @Serializable
    data object AddTask: Destinations()

    @Serializable
    data object Tasks: Destinations()

    @Serializable
    data class Details(val taskId: Int) : Destinations()
}

enum class BottomNavScreens(
    val route: Destinations,
    val unselectedIcon: Int,
    val selectedIcon: Int,
    @StringRes val label: Int
) {
    Home(
        route = Destinations.Home,
        unselectedIcon = R.drawable.house_regular,
        selectedIcon = R.drawable.house_solid,
        label = R.string.home
    ),
    AddTask(
        route = Destinations.AddTask,
        unselectedIcon = R.drawable.plus_icon,
        selectedIcon = R.drawable.plus_icon,
        label = R.string.add
    ),
    Tasks(
        route = Destinations.Tasks,
        unselectedIcon = R.drawable.folder_regular,
        selectedIcon = R.drawable.folder_solid,
        label = R.string.tasks
    )
}
