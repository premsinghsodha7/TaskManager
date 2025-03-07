package com.compose.taskmanager.presentation.ui.screen.home

import com.compose.taskmanager.data.local.Task
import java.time.LocalDate

data class DateUiState(
    var selectedDate: LocalDate = LocalDate.now(),
    var listTasks: List<Task> = emptyList(),
    var hasTasks: Boolean = true
)