package com.compose.taskmanager.presentation.ui.screen.details

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.compose.taskmanager.presentation.ui.screen.add.TaskStatus
import com.compose.taskmanager.presentation.ui.screen.tasks.TaskViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.compose.taskmanager.presentation.ui.theme.Blue
import es.dmoral.toasty.Toasty

@Composable
fun DetailsScreen(
    navController: NavController,
    viewModel: DetailsViewModel = hiltViewModel()
) {
    val taskDetails by viewModel.taskDetails.collectAsStateWithLifecycle()

    val systemUiController = rememberSystemUiController()

    SideEffect {
        systemUiController.setStatusBarColor(
            color = Transparent,
            darkIcons = true
        )
    }
    val context = LocalContext.current

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding(),
        containerColor = Blue,
        topBar = {
            TopBarComponent(navController, taskDetails){
                Toasty.success(
                    context,
                    "Task is deleted successfully.",
                    Toast.LENGTH_SHORT,
                    true
                ).show()

                viewModel.deleteTask(taskDetails.id)
                navController.popBackStack()
            }
        }
    ) { paddingValues ->
        DetailsContent(paddingValues, taskDetails){

            Toasty.success(
                context,
                "Task is updated successfully.",
                Toast.LENGTH_SHORT,
                true
            ).show()

            taskDetails.apply {
                status = TaskStatus.Completed.name
            }
            viewModel.changeStatusOfTask(taskDetails)
        }
    }
}

