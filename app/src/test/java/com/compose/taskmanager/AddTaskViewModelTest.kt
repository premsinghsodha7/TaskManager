package com.compose.taskmanager

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.compose.taskmanager.data.local.Task
import com.compose.taskmanager.domain.repositories.TaskRepository
import com.compose.taskmanager.domain.usecase.ValidateTaskUseCase
import com.compose.taskmanager.presentation.ui.screen.add.AddTaskViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

@ExperimentalCoroutinesApi
class AddTaskViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var viewModel: AddTaskViewModel
    private lateinit var repository: TaskRepository
    private lateinit var validateTaskUseCase: ValidateTaskUseCase

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mock(TaskRepository::class.java)
        validateTaskUseCase = ValidateTaskUseCase()
        viewModel = AddTaskViewModel(validateTaskUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test add task`() = runTest{
        `when`(repository.insertTask(Task(title = "New Task", description = "New Description"))).thenReturn(Unit)
        viewModel.submitData()
    }
}