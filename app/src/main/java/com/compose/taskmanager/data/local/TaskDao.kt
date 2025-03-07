package com.compose.taskmanager.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import java.time.LocalDate

@Dao
interface TaskDao {

    @Query("SELECT * FROM task")
    suspend fun getAllTask(): List<Task>

    @Upsert
    suspend fun insertTask(task: Task)

    @Query("DELETE FROM task WHERE id =:taskId")
    fun deleteTask(taskId: Int)

    @Query("SELECT * FROM task WHERE dueDate =:selectedDate")
    fun getTasksByDate(selectedDate: LocalDate): Flow<List<Task>>

    @Query("SELECT * FROM task WHERE status =:status")
    suspend fun getTasksByStatus(status: String): List<Task>

    @Query("SELECT * FROM task WHERE id =:taskId")
    fun getTaskById(taskId: Int): Task
}