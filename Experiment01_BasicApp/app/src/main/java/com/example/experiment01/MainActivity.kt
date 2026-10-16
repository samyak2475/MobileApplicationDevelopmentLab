package com.example.experiment01

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val showDetailsButton = findViewById<Button>(R.id.btnShowDetails)

        showDetailsButton.setOnClickListener {

            Toast.makeText(
                this,
                "Welcome to Mobile Application Development!",
                Toast.LENGTH_LONG
            ).show()

        }
    }
}