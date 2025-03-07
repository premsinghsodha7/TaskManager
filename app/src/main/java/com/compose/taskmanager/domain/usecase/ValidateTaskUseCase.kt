package com.compose.taskmanager.domain.usecase

import android.os.Build
import androidx.annotation.RequiresApi
import com.compose.taskmanager.domain.model.TaskValidationState
import java.time.LocalDate

/**
 * `ValidateTaskUseCase` is a use case class responsible for validating the properties of a task
 * (title, description, and due date) before it can be considered valid.
 *
 * It provides a single entry point, `execute`, which takes the task's title, description, and due date
 * as input and returns a `TaskValidationState` object indicating the validity of each property and
 * whether the task as a whole is considered valid.
 */
@RequiresApi(Build.VERSION_CODES.O)
class ValidateTaskUseCase {

    fun execute(
        title: String,
        description: String,
        dueDate: LocalDate
    ): TaskValidationState {

        val titleResult = validateTitle(title)
        val descriptionResult = validateDescription(description)
        val dueDateResult = validateDueDate(dueDate)

        val isSuccessful = listOf(
            titleResult,
            descriptionResult,
            dueDateResult,
        ).all { it }

        return TaskValidationState(
            isValidTitle = titleResult,
            isValidDescription = descriptionResult,
            isValidDueDate = dueDateResult,
            isSuccessful = isSuccessful
        )
    }

    private fun validateTitle(title: String): Boolean {
        return title.isNotEmpty()
    }

    private fun validateDescription(description: String): Boolean {
        return description.isNotEmpty() || description.length > 5
    }

    private fun validateDueDate(dueDate: LocalDate): Boolean {
        return !dueDate.isBefore(LocalDate.now())
    }

}