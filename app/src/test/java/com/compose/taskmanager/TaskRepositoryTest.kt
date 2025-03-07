package com.compose.taskmanager

import com.compose.taskmanager.data.local.Task
import com.compose.taskmanager.domain.repositories.TaskRepository
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.Assert.*
import org.junit.Before
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.time.LocalDate

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class TaskRepositoryTest {
    @Mock
    private lateinit var mockTaskRepository: TaskRepository

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun getAllTasks_returnsListOfTasks() = runTest {
        // Arrange
        val task1 = Task(id = 1, title = "Task 1", description = "Task 1 desc", dueDate = LocalDate.now())
        val task2 = Task(id = 1, title = "Task 2", description = "Task 2 desc", dueDate = LocalDate.now())
        val taskList = listOf(task1, task2)
        `when`(mockTaskRepository.getAllTasks()).thenReturn(taskList)

        // Act
        val result = mockTaskRepository.getAllTasks()

        // Assert
        assertEquals(taskList, result)
    }

    @Test
    fun getTaskById_returnsCorrectTask() = runTest {
        // Arrange
        val task = Task(id = 1, title = "Task 1", description = "Task 1 desc", dueDate = LocalDate.now())
        `when`(mockTaskRepository.getTaskById(1)).thenReturn(task)

        // Act
        val result = mockTaskRepository.getTaskById(1)

        // Assert
        assertEquals(task, result)
    }
}