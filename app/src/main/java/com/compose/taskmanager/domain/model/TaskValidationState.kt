package com.compose.taskmanager.domain.model


/**
 * Represents the validation state of a task's properties.
 *
 * This data class holds boolean flags indicating whether each of the task's
 * key properties (title, description, and due date) are valid, as well as
 * a general success flag representing if the overall task validation was successful.
 *
 * @property isValidTitle Indicates whether the task's title is valid.
 * @property isValidDescription Indicates whether the task's description is valid.
 * @property isValidDueDate Indicates whether the task's due date is valid.
 * @property isSuccessful Indicates whether the overall task validation process was successful.
 *                       This might be true only when all individual validations (title, description, dueDate) are true,
 *                       or it can be used to represent other successful validation results.
 */
data class TaskValidationState (
    var isValidTitle: Boolean = false,
    var isValidDescription: Boolean = false,
    var isValidDueDate: Boolean = false,
    var isSuccessful:Boolean = false
)