
package com.example.experiment08

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val tvWelcome = findViewById<TextView>(R.id.tvWelcome)
        val btnBack = findViewById<Button>(R.id.btnBack)

        val studentName = intent.getStringExtra("STUDENT_NAME") ?: "Student"
        tvWelcome.text = "Welcome, $studentName!"

        btnBack.setOnClickListener {
            finish()
        }
    }
}
