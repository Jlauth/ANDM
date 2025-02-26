package com.example.andm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.andm.ui.theme.ANDMTheme

class MainActivity : ComponentActivity() {
//    private lateinit var database: TaskDatabase
//    private lateinit var repository: TaskRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        database = TaskDatabase.getDatabase(this)
//        repository = TaskRepository(database.taskDao())
//
//        lifecycleScope.launch {
//            val task = repository.getAllTasks()
//            Log.d("DatabaseCheck", "Nb de tasks: ${task.size}")
//        }

        enableEdgeToEdge()
        setContent {
            ANDMTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        TaskScreen()
                    }
                }
            }
        }
    }
}

@Composable
fun TaskScreen() {
    val taskList = remember { TaskList() }
    val showCompleted = remember { mutableStateOf(false) }
    val newTaskTitle = remember { mutableStateOf("") }
    val newTaskDescription = remember { mutableStateOf("") }
    // var newTaskDescription2 by remember { mutableStateOf("") } // convention ok

    Column(modifier = Modifier.padding(30.dp)) {
        Text(
            "\uD83D\uDCCB Liste des tâches \uD83D\uDCCB",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Add a new task
        TextField(
            value = newTaskTitle.value,
            onValueChange = { newTaskTitle.value = it },
            label = { Text("Titre de la tâche") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(5.dp))

        TextField(
            value = newTaskDescription.value,
            onValueChange = { newTaskDescription.value = it },
            label = { Text("Description de la tâche") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {
                if (newTaskTitle.value.isNotBlank()) {
                    taskList.addTask(newTaskTitle.value, newTaskDescription.value)
                    newTaskTitle.value = "" // Reset input
                    newTaskDescription.value = "" // Reset input
                }
            }) {
                Text("Ajouter une tâche")
            }

            Button(onClick = { showCompleted.value = !showCompleted.value }) {
                Text(if (showCompleted.value) "Tâches en cours" else "Tâches terminées")
            }
        }
        Spacer(modifier = Modifier.height(30.dp))

        // Show filtered tasks
        val filteredTasks = taskList.tasks.filter { it.isDone == showCompleted.value }

        LazyColumn {
            items(filteredTasks.size) { index ->
                val task = filteredTasks[index]
                Column(
                    modifier = Modifier.height(90.dp)
                ) {
                    Text(
                        text = buildAnnotatedString {
                            append("- ${task.title} : ${task.description} ")
                            withStyle(style = SpanStyle(color = if (task.isDone) Color.Green else Color.Red)) {
                                append(if (task.isDone) "✔" else "❌")
                            }
                        }
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(onClick = { taskList.removeTask(taskList.tasks.indexOf(task)) }) {
                            Text("Supprimer")
                        }
                        if (!task.isDone) {
                            Button(onClick = { taskList.completeTask(taskList.tasks.indexOf(task)) }) {
                                Text("Terminer")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TaskScreenPreview() {
    ANDMTheme {
        TaskScreen()
    }
}
