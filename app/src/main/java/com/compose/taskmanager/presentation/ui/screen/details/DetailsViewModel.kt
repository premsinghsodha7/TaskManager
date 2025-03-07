package com.compose.taskmanager.presentation.ui.screen.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compose.taskmanager.data.local.Task
import com.compose.taskmanager.data.repositories.TaskRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * [DetailsViewModel] is a ViewModel class responsible for managing the UI-related data and
 * logic for the task details screen.
 *
 * It interacts with the [TaskRepositoryImpl] to fetch, update, and delete task data.
 *
 * @property repository An instance of [TaskRepositoryImpl] for performing data operations.
 * @property savedStateHandle A [SavedStateHandle] to manage the state of the ViewModel across configuration changes.
 */
@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val repository: TaskRepositoryImpl,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private var _taskDetails = MutableStateFlow(Task())
    val taskDetails = _taskDetails.asStateFlow()


    init {
        savedStateHandle.get<Int>("taskId")?.let { id ->
            getTaskById(id)
        }
    }

    fun getTaskById(id: Int){
        viewModelScope.launch {
            _taskDetails.value = repository.getTaskById(id)
        }
    }

    /**
     * Changes the status of a given task and updates the task details.
     *
     * This function performs the following actions:
     * 1. Inserts the provided `task` into the repository. This typically involves updating
     *    the task's status in the persistent data store (e.g., database).
     * 2. Updates the `_taskDetails` StateFlow with the new `task` object. This ensures
     *    that any UI components observing `_taskDetails` will be notified of the
     *    changes and can update accordingly.
     *
     * The operation is performed within a coroutine launched in the `viewModelScope`,
     * ensuring that it runs on the appropriate background thread and is automatically
     * cancelled when the ViewModel is cleared.
     *
     * @param task The [Task] object with the updated status. The function assumes that the
     *             `task`'s status property has already been modified prior to calling this function.
     *
     * @throws Exception if an error occurs during the insertion of the task into the repository.
     *         The specific exception type will depend on the implementation of `repository.insertTask`.
     *
     * @see Task
     * @see TaskRepository
     * @see viewModelScope
     * @see MutableStateFlow
     * @see kotlinx.coroutines.launch
     */
    fun changeStatusOfTask(task: Task) {
        viewModelScope.launch {
            repository.insertTask(task)
            _taskDetails.update{task}
        }
    }

    /**
     * Deletes a task from the repository.
     *
     * This function initiates the deletion of a task with the given ID. It launches a coroutine
     * within the ViewModel's scope to perform the deletion asynchronously.
     *
     * @param taskId The ID of the task to be deleted.
     * @throws Exception if any error occurs during the task deletion process in the repository
     */
    fun deleteTask(taskId: Int) {
        viewModelScope.launch {
            repository.deleteTask(taskId)
        }
    }
}

