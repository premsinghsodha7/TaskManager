package com.compose.taskmanager

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.compose.taskmanager.data.local.Task
import com.compose.taskmanager.data.repositories.TaskRepositoryImpl
import com.compose.taskmanager.domain.repositories.TaskRepository
import com.compose.taskmanager.presentation.ui.screen.details.DetailsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@OptIn(ExperimentalCoroutinesApi::class)
class DetailsViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var taskRepository: TaskRepositoryImpl
    @Mock
    private lateinit var savedStateHandle: SavedStateHandle

    private lateinit var detailsViewModel: DetailsViewModel

    private val task = Task(0)

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        detailsViewModel = DetailsViewModel(taskRepository, savedStateHandle)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getTaskById returns task when found`() = runTest {
        `when`(taskRepository.getTaskById(1)).thenReturn(task)
        detailsViewModel.getTaskById(1)

        val actualTask = detailsViewModel.taskDetails.first()

        assertEquals(task, actualTask)
    }

    @Test
    fun `getTaskById returns null when not found`() = runTest {
        `when`(taskRepository.getTaskById(2)).thenReturn(Task())
        detailsViewModel.getTaskById(2)
        val actualTask = detailsViewModel.taskDetails.first()
        assertEquals(Task(), actualTask)
    }
}