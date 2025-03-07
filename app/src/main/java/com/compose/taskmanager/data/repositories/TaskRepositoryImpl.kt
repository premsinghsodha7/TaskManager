package com.compose.taskmanager.data.repositories

import com.compose.taskmanager.data.local.Task
import com.compose.taskmanager.data.local.TaskDao
import com.compose.taskmanager.domain.repositories.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(private val taskDao: TaskDao): TaskRepository {

    override suspend fun insertTask(task: Task) {
        withContext(Dispatchers.IO) {
            taskDao.insertTask(task)
        }
    }

    override suspend fun deleteTask(taskId: Int) {
        withContext(Dispatchers.IO) {
            taskDao.deleteTask(taskId)
        }
    }

    override suspend fun getAllTasks(): List<Task> {
        return taskDao.getAllTask()
    }

    override suspend fun getTasksByStatus(status: String): List<Task> {
        return taskDao.getTasksByStatus(status)
    }

    override suspend fun getTasksByDate(selectedDate: LocalDate): Flow<List<Task>> {
        return taskDao.getTasksByDate(selectedDate)
    }

    override suspend fun getTaskById(taskId: Int): Task = withContext(Dispatchers.IO) {
        taskDao.getTaskById(taskId)
    }
}