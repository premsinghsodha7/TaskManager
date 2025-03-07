package com.compose.taskmanager.presentation.ui.screen.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compose.taskmanager.data.local.Task
import com.compose.taskmanager.data.repositories.TaskRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: TaskRepositoryImpl): ViewModel() {

    private val selectedDate = MutableStateFlow(LocalDate.now())

    private val listTasks =
        selectedDate.flatMapLatest { date ->
            repository.getTasksByDate(date)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private var _dateUiState = MutableStateFlow(DateUiState())

    /**
     * A [StateFlow] representing the UI state related to the selected date and associated tasks.
     *
     * This flow combines three sources:
     *   - [_dateUiState]: An internal [MutableStateFlow] holding the base [DateUiState].
     *   - [listTasks]: A [Flow] emitting the list of tasks.
     *   - [selectedDate]: A [Flow] emitting the currently selected date.
     *
     * The combined state is derived by:
     *   - Copying the current [_dateUiState]
     *   - Updating the `selectedDate` property with the latest emitted value from [selectedDate].
     *   - Updating the `listTasks` property with the latest emitted list from [listTasks].
     *   - Calculating `hasTasks`: a boolean indicating whether any task in the `listTasks` has a due date matching the `selectedDate`.
     *
     * This [StateFlow] is collected within the [viewModelScope] and will share its emissions among multiple collectors that
     * subscribe while the scope is active. It uses [SharingStarted.WhileSubscribed] with a 5-second replay cache timeout,
     * meaning that new collectors will receive the last emitted value if it was emitted within the last 5 seconds.
     * If no collectors are active for more than 5 seconds, the upstream flows will be unsubscribed. When a new collector
     * subscribes after more than 5 seconds of inactivity, it receives the default [DateUiState] first and then the current
     * state.
     *
     * @see DateUiState
     */
    val dateUiState = combine(_dateUiState, listTasks, selectedDate) { dateUiState, listTasks, selectedDate ->
        dateUiState.copy(
            selectedDate = selectedDate,
            listTasks = listTasks,
            hasTasks = listTasks.any { it.dueDate == selectedDate }
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DateUiState())

    fun insertTask(task: Task) {
        viewModelScope.launch {
            repository.insertTask(task)
        }
    }

    /**
     * Sets the currently selected date.
     *
     * This function updates the `selectedDate` MutableState with the provided date.
     *
     * @param date The LocalDate to set as the selected date.
     */
    fun setSelectedDate(date: LocalDate) {
        selectedDate.value = date
    }
}