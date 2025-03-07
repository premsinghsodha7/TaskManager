package com.compose.taskmanager.presentation.ui.screen.tasks

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import com.compose.taskmanager.R
import com.compose.taskmanager.presentation.ui.screen.home.TasksListSection
import com.compose.taskmanager.presentation.ui.screen.home.TopBarComponent
import com.compose.taskmanager.presentation.ui.theme.Black
import com.compose.taskmanager.presentation.ui.theme.White
import com.compose.taskmanager.presentation.ui.theme.priegoFont
import com.compose.taskmanager.presentation.util.MultiSelector
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TasksScreen(
    navController: NavController,
    viewModel: TaskViewModel = hiltViewModel()
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(Unit) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            // ViewModel is of type BaseViewModel
            viewModel.onStart()
        }
    }
    val systemUiController = rememberSystemUiController()

    SideEffect {
        systemUiController.setSystemBarsColor(
            Color.Transparent,
            darkIcons = false
        )
    }

    Scaffold(
        containerColor = Black,
        topBar = {
            TopBarComponent()
        }
    ) { paddingValues ->
        TaskContent(paddingValues, navController, viewModel)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TaskContent(
    paddingValues: PaddingValues,
    navController: NavController,
    viewModel: TaskViewModel
) {

    val statusState by viewModel.filterStatusState.collectAsStateWithLifecycle()
    val dateState by viewModel.taskList.collectAsStateWithLifecycle()
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .padding(top = 5.dp)
    ) {

        StatusSelectorSection(statusState, viewModel.statusOptions){
            viewModel.updateFilter(it)
        }

        AnimatedContent(
            modifier = Modifier
                .padding(top = 15.dp),
            targetState = dateState.isNotEmpty(),
            transitionSpec = {
                fadeIn(animationSpec = tween(200)).togetherWith(
                    fadeOut(animationSpec = tween(200))
                )
            },
            label = "",
        ) { targetState ->
            when (targetState) {
                true -> {
                    TasksListSection(dateState, navController)
                }
                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(340.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.nothing),
                            contentDescription = null,
                            modifier = Modifier.size(145.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(9.dp)
                        ) {
                            Text(
                                text = "No Tasks",
                                fontSize = 17.sp,
                                fontFamily = priegoFont,
                                color = White,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "There are no specific tasks tied to this date.",
                                fontSize = 13.sp,
                                fontFamily = priegoFont,
                                color = White,
                                fontWeight = FontWeight.Normal,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}



@Composable
fun StatusSelectorSection(statusState: String, priorityOptions: List<String>, onEvent: (String) -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 17.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Filter By",
            fontSize = 14.sp,
            color =  White,
            fontFamily = priegoFont,
            fontWeight = FontWeight.Normal,
            modifier = Modifier
                .alpha(0.7f)
                .padding(bottom = 9.dp)
        )

        MultiSelector(
            options = priorityOptions,
            selectedOption = statusState,
            onOptionSelect = { option ->
                onEvent(option)
            },
            selectedColor = Black,
            unselectedColor = White,
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
        )
    }
}