package com.example.experiment10

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var database: TaskDatabaseHelper
    private lateinit var tvTotalTasks: TextView
    private lateinit var tvPendingTasks: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        database = TaskDatabaseHelper(this)

        tvTotalTasks = findViewById(R.id.tvTotalTasks)
        tvPendingTasks = findViewById(R.id.tvPendingTasks)

        findViewById<Button>(R.id.btnAddTask).setOnClickListener {
            val intent = Intent(this, TasksActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.btnViewTasks).setOnClickListener {
            val intent = Intent(this, TasksActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.btnAbout).setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("About App")
                .setMessage("Student Productivity Manager\n\nVersion 1.0\n\nA task management application designed for students to organize coursework, track subjects, set due dates, and monitor pending tasks.")
                .setPositiveButton("OK", null)
                .show()
        }

        updateDashboard()
    }

    override fun onResume() {
        super.onResume()
        updateDashboard()
    }

    private fun updateDashboard() {
        val total = database.getTotalCount()
        val pending = database.getPendingCount()

        tvTotalTasks.text = "Total tasks: $total"
        tvPendingTasks.text = "Pending tasks: $pending"
    }

    override fun onDestroy() {
        database.close()
        super.onDestroy()
    }
}
