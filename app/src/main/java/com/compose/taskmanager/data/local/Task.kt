package com.compose.taskmanager.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.compose.taskmanager.presentation.ui.screen.add.TaskStatus
import java.time.LocalDate

@Entity (tableName = "Task")
data class Task(
    @PrimaryKey (autoGenerate = true)
    val id: Int = 0,
    val title: String = "",
    val description: String = "",
    val dueDate: LocalDate = LocalDate.now(),
    val priority: String = "",
    var status: String = TaskStatus.InProgress.name
)
