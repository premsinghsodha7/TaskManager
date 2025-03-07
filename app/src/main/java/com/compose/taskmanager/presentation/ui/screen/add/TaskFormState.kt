package com.compose.taskmanager.presentation.ui.screen.add

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate

/**
 * Represents the state of a task form.
 *
 * This data class holds the information entered by the user in a task creation or editing form.
 * It includes the task's title, description, due date, estimated time to complete, and selected priority.
 *
 * @property title The title of the task. Defaults to an empty string.
 * @property description The description of the task. Defaults to an empty string.
 * @property dueDate The due date of the task. Defaults to the current date.
 * @property estimateTask The estimated time (e.g., in hours) required to complete the task. Defaults to 3.
 * @property selectedPriority The selected priority of the task (e.g., "Low", "Medium", "High"). Defaults to "Low".
 *
 * @constructor Creates a TaskFormState object with the provided values.
 *
 * @RequiresApi Requires at least Android Oreo (API level 26) due to the use of `java.time.LocalDate`.
 */
data class TaskFormState @RequiresApi(Build.VERSION_CODES.O) constructor(
    var title: String = "",
    var description: String = "",
    var dueDate: LocalDate = LocalDate.now(),
    var estimateTask: Int = 3,
    var selectedPriority: String = "Low",
)