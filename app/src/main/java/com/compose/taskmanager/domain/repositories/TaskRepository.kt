
package com.compose.taskmanager.domain.repositories

import com.compose.taskmanager.data.local.Task
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface TaskRepository {
    /**
     * Inserts a new task into the data source.
     *
     * This function is a suspend function, meaning it should be called within a coroutine
     * or another suspend function. It handles the potentially long-running operation of
     * inserting a task without blocking the main thread.
     *
     * @param task The [Task] object to be inserted.
     */
    suspend fun insertTask(task: Task)

    /**
     * Deletes a task with the specified ID.
     *
     * This function removes a task from the data source (e.g., a database or local storage) based on its unique identifier.
     *
     * @param taskId The ID of the task to be deleted. Must be a positive integer.
     */
    suspend fun deleteTask(taskId: Int)

    /**
     * Retrieves all tasks from the data source.
     *
     * This function fetches a list of all available tasks. It is a suspending function,
     * meaning it should be called within a coroutine or another suspending function.
     *
     * @return A [List] of [Task] objects representing all tasks in the data source.
     *         Returns an empty list if no tasks are found.
     */
    suspend fun getAllTasks(): List<Task>

    /**
     * Retrieves a list of tasks filtered by the specified status.
     *
     * This function asynchronously fetches tasks from a data source and returns
     * only those tasks that match the given status. The status parameter is a
     * string that represents the desired status of the tasks (e.g., "IN_PROGRESS", "COMPLETED").
     *
     * @param status The status to filter tasks by. This should be a string representing a valid task status.
     * @return A list of [Task] objects that have the specified status. Returns an empty list if no tasks match the status or if an error occurs.
     *
     */
    suspend fun getTasksByStatus(status: String): List<Task>

    /**
     * Retrieves a list of tasks for a given date.
     *
     * This function fetches tasks that are scheduled for the specified date.
     * It returns a Flow that emits a list of [Task] objects.
     * The Flow allows for reactive updates, meaning any changes to the task data
     * that affect the given date will result in a new emission from the Flow.
     *
     * @param selectedDate The date for which to retrieve tasks.
     * @return A Flow emitting a list of [Task] objects scheduled for the [selectedDate].
     *         If no tasks are found for the date, the Flow will emit an empty list.
     */
    suspend fun getTasksByDate(selectedDate: LocalDate): Flow<List<Task>>

    /**
     * Retrieves a task by its ID.
     *
     * This function simulates fetching a task from a data source (e.g., a database or network).
     * It uses a suspend function, indicating that it might perform long-running operations
     * and should be called within a coroutine.
     *
     * @param taskId The ID of the task to retrieve.
     * @return The Task object associated with the given ID.
     */
    suspend fun getTaskById(taskId: Int): Task
}
