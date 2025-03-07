package com.compose.taskmanager.presentation.ui.screen.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.compose.taskmanager.presentation.ui.screen.tasks.TasksScreen
import com.compose.taskmanager.presentation.ui.screen.add.AddTaskScreen
import com.compose.taskmanager.presentation.ui.screen.details.DetailsScreen
import com.compose.taskmanager.presentation.ui.screen.home.HomeScreen

@Composable
fun MainNavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Destinations.BottomNavGraph,
        enterTransition = { fadeIn(animationSpec = tween(300)) },
        exitTransition = { fadeOut(animationSpec = tween(300)) }
    ) {
        bottomNavGraph(navController)

        composable<Destinations.Details> {
            DetailsScreen(navController)
        }
    }
}

fun NavGraphBuilder.bottomNavGraph(
    navController: NavHostController
) {
    navigation<Destinations.BottomNavGraph>(
        startDestination = Destinations.Home,
        enterTransition = { fadeIn(animationSpec = tween(300)) },
        exitTransition = { fadeOut(animationSpec = tween(300)) }
    ) {
        composable<Destinations.Home> {
            HomeScreen(navController)
        }
        composable<Destinations.AddTask> {
            AddTaskScreen(navController)
        }
        composable<Destinations.Tasks> {
            TasksScreen(navController)
        }
    }
}