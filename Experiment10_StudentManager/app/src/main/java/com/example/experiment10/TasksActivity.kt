package com.example.experiment10

import android.graphics.Typeface
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class TasksActivity : AppCompatActivity() {

    private lateinit var database: TaskDatabaseHelper
    private lateinit var taskListContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tasks)

        database = TaskDatabaseHelper(this)
        taskListContainer = findViewById(R.id.taskListContainer)

        val etTitle = findViewById<EditText>(R.id.etTaskTitle)
        val etSubject = findViewById<EditText>(R.id.etSubject)
        val etDueDate = findViewById<EditText>(R.id.etDueDate)

        findViewById<Button>(R.id.btnSaveTask).setOnClickListener {
            val title = etTitle.text.toString().trim()
            val subject = etSubject.text.toString().trim()
            val dueDate = etDueDate.text.toString().trim()

            if (title.isEmpty() || subject.isEmpty() || dueDate.isEmpty()) {
                Toast.makeText(
                    this,
                    "Please fill in all fields",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val result = database.addTask(title, subject, dueDate)
                if (result != -1L) {
                    Toast.makeText(this, "Task saved successfully", Toast.LENGTH_SHORT).show()
                    etTitle.text.clear()
                    etSubject.text.clear()
                    etDueDate.text.clear()
                    loadTasks()
                } else {
                    Toast.makeText(this, "Error saving task", Toast.LENGTH_SHORT).show()
                }
            }
        }

        findViewById<Button>(R.id.btnRefreshTasks).setOnClickListener {
            loadTasks()
            Toast.makeText(this, "Task list refreshed", Toast.LENGTH_SHORT).show()
        }

        loadTasks()
    }

    private fun loadTasks() {
        taskListContainer.removeAllViews()
        val tasks = database.getAllTasks()

        if (tasks.isEmpty()) {
            val emptyMessage = TextView(this).apply {
                text = "No tasks added yet. Fill in the form above to add a task."
                textSize = 16f
                setPadding(8, 24, 8, 24)
            }
            taskListContainer.addView(emptyMessage)
            return
        }

        for (task in tasks) {
            val card = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(16, 16, 16, 16)
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    bottomMargin = 24
                }
            }

            val titleView = TextView(this).apply {
                text = task.title
                textSize = 18f
                setTypeface(null, Typeface.BOLD)
            }

            val detailView = TextView(this).apply {
                text = "Subject: ${task.subject}\nDue Date: ${task.dueDate}"
                textSize = 14f
                setPadding(0, 4, 0, 8)
            }

            val completedCheckBox = CheckBox(this).apply {
                text = if (task.isCompleted) "Completed" else "Pending"
                // Prevent listener from triggering on initial setup
                setOnCheckedChangeListener(null)
                isChecked = task.isCompleted
                setOnCheckedChangeListener { _, checked ->
                    database.setTaskCompleted(task.id, checked)
                    Toast.makeText(
                        this@TasksActivity,
                        if (checked) "Task marked completed" else "Task marked pending",
                        Toast.LENGTH_SHORT
                    ).show()
                    loadTasks()
                }
            }

            val deleteButton = Button(this).apply {
                text = "Delete Task"
                setOnClickListener {
                    database.deleteTask(task.id)
                    Toast.makeText(
                        this@TasksActivity,
                        "Task deleted",
                        Toast.LENGTH_SHORT
                    ).show()
                    loadTasks()
                }
            }

            card.addView(titleView)
            card.addView(detailView)
            card.addView(completedCheckBox)
            card.addView(deleteButton)
            taskListContainer.addView(card)
        }
    }

    override fun onDestroy() {
        database.close()
        super.onDestroy()
    }
}
