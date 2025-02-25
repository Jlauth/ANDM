package com.example.andm

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList

// Includes methods for : add, complete, remove and get tasks
class TaskList {
    private val _tasks = mutableStateListOf<Task>()
    val tasks: SnapshotStateList<Task> get() = _tasks

    fun addTask(title: String, description: String) {
        _tasks.add(Task(title, description))
    }

    fun completeTask(index: Int) {
        if (index in tasks.indices) {
            val updatedTask = tasks[index].copy(isDone = true) // Create a new copy
            tasks[index] = updatedTask // Replace the old task by the new one
        }
    }

    fun removeTask(index: Int) {
        if (index in _tasks.indices) {
            _tasks.removeAt(index)
        }
    }

    fun getTasks(): List<Task> = _tasks
}
