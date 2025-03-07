package com.compose.taskmanager.presentation.ui.screen.add

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compose.taskmanager.domain.model.TaskValidationState
import com.compose.taskmanager.domain.usecase.ValidateTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsible for handling the logic related to adding a new task.
 *
 * This class manages the state of the task form, handles user input, validates
 * the input data, and communicates the validation results to the UI.
 *
 * @property validateTaskUseCase Use case for validating the task data (title, description, due date).
 */
@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class AddTaskViewModel @Inject constructor(private val validateTaskUseCase: ValidateTaskUseCase): ViewModel() {

    private var _taskFormState = MutableStateFlow(TaskFormState())
    val taskFormState = _taskFormState.asStateFlow()

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    /**
     * Handles events related to the task form.
     *
     * This function receives a [TaskFormEvent] and updates the [TaskFormState] accordingly.
     * It uses a `when` expression to handle different types of events, such as changes to
     * the task's title, description, due date, estimated time, and priority, as well as
     * the submission of the form.
     *
     * @param event The [TaskFormEvent] to be processed.
     *
     * Events Handled:
     *   - [TaskFormEvent.TitleChanged]: Updates the task's title in the state.
     *   - [TaskFormEvent.DescriptionChanged]: Updates the task's description in the state.
     *   - [TaskFormEvent.DueDateChanged]: Updates the task's due date in the state.
     *   - [TaskFormEvent.EstimateTaskChanged]: Updates the task's estimated time in the state.
     *   - [TaskFormEvent.PriorityChanged]: Updates the task's selected priority in the state.
     *   - [TaskFormEvent.Submit]: Triggers the submission of the form data via the [submitData] function.
     *   - Other: Ignores any other type of event.
     */
    fun onEvent(event: TaskFormEvent) {
        when (event) {
            is TaskFormEvent.TitleChanged -> {
                _taskFormState.update { it.copy(title = event.title) }
            }
            is TaskFormEvent.DescriptionChanged -> {
                _taskFormState.update { it.copy(description = event.description) }
            }
            is TaskFormEvent.DueDateChanged -> {
                _taskFormState.update { it.copy(dueDate = event.dueDate) }
            }
            is TaskFormEvent.EstimateTaskChanged -> {
                _taskFormState.update { it.copy(estimateTask = event.estimateTime) }
            }
            is TaskFormEvent.PriorityChanged -> {
                _taskFormState.update { it.copy(selectedPriority = event.priority) }
            }
            is TaskFormEvent.Submit -> {
                submitData()
            }
            else -> {}
        }
    }

    /**
     * Submits the task data for validation.
     *
     * This function orchestrates the validation process for task data, including title,
     * description, and due date. It utilizes the [validateTaskUseCase] to perform the
     * validation and then sends a [ValidationEvent] through the [validationEventChannel]
     * based on the validation result.
     *
     * If the validation is successful, a [ValidationEvent.Success] is sent.
     * If the validation fails, a [ValidationEvent.Error] is sent with an error message
     * obtained using the [getErrorMessage] function.
     *
     * The entire operation is performed within the [viewModelScope] to ensure it is
     * lifecycle-aware and automatically cancelled when the ViewModel is cleared.
     *
     * @see ValidateTaskUseCase
     * @see ValidationEvent
     * @see viewModelScope
     * @see validationEventChannel
     * @see getErrorMessage
     * @see TaskFormState
     */
    fun submitData() {
        val validationState = validateTaskUseCase.execute(
            _taskFormState.value.title,
            _taskFormState.value.description,
            _taskFormState.value.dueDate
        )

        viewModelScope.launch {
            if (validationState.isSuccessful) {
                validationEventChannel.send(ValidationEvent.Success)
            } else {
                val errorMessage = getErrorMessage(validationState)
                validationEventChannel.send(ValidationEvent.Error(errorMessage))
            }
        }
    }

    /**
     * Retrieves an appropriate error message based on the provided TaskValidationState.
     *
     * This function examines the properties of the [TaskValidationState] to determine
     * which aspects of a task are invalid and returns a corresponding error message.
     * It prioritizes composite errors (e.g., invalid title AND description) before
     * individual errors. If all fields are valid according to TaskValidationState, it returns a generic message indicating the input needs to be corrected
     *
     * @param validationState The [TaskValidationState] object containing the validation
     *                        status of the task's title, description, and due date.
     * @return A [String] representing the error message to display to the user,
     *         based on the validation state.
     *
     * Example Scenarios:
     * - If both title and description are invalid, it returns "Enter a valid title and description."
     * - If only the title is invalid, it returns "Enter a valid title."
     * - If only the description is invalid, it returns "Enter a valid description."
     * - If only the due date is invalid, it returns "Choose a due date for today or a future date."
     * - If none of the above conditions match (all validations passed according to TaskValidationState), it returns "Fill in the task information correctly."
     */
    private fun getErrorMessage(validationState: TaskValidationState): String {
        return when {
            !validationState.isValidTitle && !validationState.isValidDescription ->
                "Enter a valid title and description."
            !validationState.isValidTitle -> "Enter a valid title."
            !validationState.isValidDescription -> "Enter a valid description."
            !validationState.isValidDueDate -> "Choose a due date for today or a future date."
            else -> "Fill in the task information correctly."
        }
    }

    /**
     * Resets the task form state to its initial, empty state.
     *
     * This function clears any previously entered or selected data in the task form,
     * such as task name, description, due date, etc., effectively preparing the form
     * for a new task creation or a fresh start. It does so by setting the value of
     * the internal `_taskFormState` LiveData/StateFlow to a new instance of
     * `TaskFormState`, which represents the default, empty form state.
     *
     * Call this function when you need to discard any existing task form data,
     * for instance:
     * - After successfully creating a task.
     * - When the user navigates away from the task creation/editing screen.
     * - When the user explicitly requests to clear the form.
     */
    fun resetTaskState() {
        _taskFormState.value = TaskFormState()
    }

    /**
     * Sealed class representing the result of a validation operation.
     * It can either be a [Success] indicating that the validation passed,
     * or an [Error] containing an error message explaining why the validation failed.
     */
    sealed class ValidationEvent {
        data object Success: ValidationEvent()
        data class Error(val errorMessage: String): ValidationEvent()
    }
}