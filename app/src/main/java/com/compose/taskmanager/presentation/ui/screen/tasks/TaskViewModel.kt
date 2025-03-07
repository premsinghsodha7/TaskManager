package com.compose.taskmanager.presentation.ui.screen.tasks

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compose.taskmanager.data.local.Task
import com.compose.taskmanager.data.repositories.TaskRepositoryImpl
import com.compose.taskmanager.presentation.ui.screen.add.TaskFormState
import com.compose.taskmanager.presentation.ui.screen.add.TaskStatus
import com.compose.taskmanager.presentation.ui.screen.home.DateUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class TaskViewModel @Inject constructor(
    private val repository: TaskRepositoryImpl
): ViewModel() {


    val statusOptions = listOf("All","InProgress", "Completed")

    private val _taskList = MutableStateFlow(listOf<Task>())
    val taskList = _taskList.asStateFlow()

    private var _filterStatusState = MutableStateFlow(statusOptions.first())
    val filterStatusState = _filterStatusState.asStateFlow()

    init {
        getTaskByStatus()
    }

    /**
     * Retrieves tasks from the repository based on the provided status.
     *
     * This function fetches tasks from the repository and updates the `_taskList` LiveData.
     * It supports filtering tasks by "InProgress" or "Completed" status.
     * If no status or any other status is provided, it fetches all tasks.
     *
     * @param status The status of the tasks to retrieve. Can be "InProgress", "Completed", or null (to get all tasks).
     *               Defaults to null, which fetches all tasks.
     *
     * Usage examples:
     *
     *      // Get all tasks:
     *      getTaskByStatus()
     *
     *      // Get tasks with "InProgress" status:
     *      getTaskByStatus("InProgress")
     *
     *      // Get tasks with "Completed" status:
     *      getTaskByStatus("Completed")
     *
     *      // Getting all tasks when passing a different status
     *      getTaskByStatus("NotStarted") // will retrieve all tasks
     *
     *      // Get all tasks when passing null
     *      getTaskByStatus(null) // will retrieve all tasks
     */
    private fun getTaskByStatus(status: String? = null){
        viewModelScope.launch {
            _taskList.value = when(status){
                "InProgress", "Completed" -> repository.getTasksByStatus(status)
                else -> repository.getAllTasks()
            }
        }
    }

    /**
     * Updates the filter status and retrieves tasks based on the new status.
     *
     * This function updates the internal state (`_filterStatusState`) with the provided `status`
     * and then calls `getTaskByStatus` to fetch tasks that match the newly applied filter.
     *
     * @param status The new status to filter tasks by. This could be values like "Pending", "Completed", "All", etc.
     *               The exact valid values depend on the implementation of `getTaskByStatus`.
     * @throws IllegalArgumentException If the provided status is invalid or not supported. (This is implicit based on the code and should be documented if there are invalid values).
     * @see getTaskByStatus
     * @see _filterStatusState
     */
    fun updateFilter(status: String){
        _filterStatusState.value = status
        getTaskByStatus(status)
    }

    fun onStart() {
        getTaskByStatus()
    }
}