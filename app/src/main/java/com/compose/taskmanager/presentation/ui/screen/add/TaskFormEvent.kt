package com.compose.taskmanager.presentation.ui.screen.add

import java.time.LocalDate

/**
 * Represents events that can occur within a task form.
 * These events are typically triggered by user interactions
 * with the form's input fields or actions.
 */
sealed interface TaskFormEvent {
    data class TitleChanged(val title: String): TaskFormEvent
    data class DescriptionChanged(val description: String): TaskFormEvent
    data class DueDateChanged(val dueDate: LocalDate): TaskFormEvent
    data class EstimateTaskChanged(val estimateTime: Int): TaskFormEvent
    data class PriorityChanged(val priority: String): TaskFormEvent
    data class StatusChanged(val status: TaskStatus): TaskFormEvent

    data object Submit: TaskFormEvent
}

/**
 * @brief Represents the status of a task.
 */
enum class TaskStatus{
    InProgress,
    Completed
}