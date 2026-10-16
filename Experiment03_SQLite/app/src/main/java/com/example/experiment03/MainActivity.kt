package com.example.experiment03

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        // Database
        databaseHelper = DatabaseHelper(this)

        // Input fields
        val rollNo =
            findViewById<EditText>(R.id.etRollNo)

        val name =
            findViewById<EditText>(R.id.etName)

        val branch =
            findViewById<EditText>(R.id.etBranch)

        val marks =
            findViewById<EditText>(R.id.etMarks)

        // Buttons
        val addButton =
            findViewById<Button>(R.id.btnAdd)

        val updateButton =
            findViewById<Button>(R.id.btnUpdate)

        val deleteButton =
            findViewById<Button>(R.id.btnDelete)

        val viewButton =
            findViewById<Button>(R.id.btnView)

        // Output
        val recordsText =
            findViewById<TextView>(R.id.tvRecords)


        // ADD STUDENT
        addButton.setOnClickListener {

            if (!validateInput(
                    rollNo,
                    name,
                    branch,
                    marks
                )
            ) {
                return@setOnClickListener
            }

            val success = databaseHelper.addStudent(
                rollNo.text.toString().toInt(),
                name.text.toString(),
                branch.text.toString(),
                marks.text.toString().toDouble()
            )

            if (success) {

                Toast.makeText(
                    this,
                    "Student added successfully",
                    Toast.LENGTH_SHORT
                ).show()

                clearFields()

            } else {

                Toast.makeText(
                    this,
                    "Failed to add student. Roll number may already exist.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }


        // UPDATE STUDENT
        updateButton.setOnClickListener {

            if (!validateInput(
                    rollNo,
                    name,
                    branch,
                    marks
                )
            ) {
                return@setOnClickListener
            }

            val success = databaseHelper.updateStudent(
                rollNo.text.toString().toInt(),
                name.text.toString(),
                branch.text.toString(),
                marks.text.toString().toDouble()
            )

            if (success) {

                Toast.makeText(
                    this,
                    "Student updated successfully",
                    Toast.LENGTH_SHORT
                ).show()

                clearFields()

            } else {

                Toast.makeText(
                    this,
                    "Student not found",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


        // DELETE STUDENT
        deleteButton.setOnClickListener {

            if (rollNo.text.toString().isEmpty()) {

                rollNo.error = "Enter roll number"

                rollNo.requestFocus()

                return@setOnClickListener
            }

            val success =
                databaseHelper.deleteStudent(
                    rollNo.text.toString().toInt()
                )

            if (success) {

                Toast.makeText(
                    this,
                    "Student deleted successfully",
                    Toast.LENGTH_SHORT
                ).show()

                clearFields()

            } else {

                Toast.makeText(
                    this,
                    "Student not found",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


        // VIEW STUDENTS
        viewButton.setOnClickListener {

            val records =
                databaseHelper.getAllStudents()

            recordsText.text = records
        }
    }


    private fun validateInput(
        rollNo: EditText,
        name: EditText,
        branch: EditText,
        marks: EditText
    ): Boolean {

        if (rollNo.text.toString().isEmpty()) {

            rollNo.error = "Enter roll number"

            rollNo.requestFocus()

            return false
        }

        if (name.text.toString().trim().isEmpty()) {

            name.error = "Enter student name"

            name.requestFocus()

            return false
        }

        if (branch.text.toString().trim().isEmpty()) {

            branch.error = "Enter branch"

            branch.requestFocus()

            return false
        }

        if (marks.text.toString().isEmpty()) {

            marks.error = "Enter marks"

            marks.requestFocus()

            return false
        }

        return true
    }


    private fun clearFields() {

        findViewById<EditText>(
            R.id.etRollNo
        ).text.clear()

        findViewById<EditText>(
            R.id.etName
        ).text.clear()

        findViewById<EditText>(
            R.id.etBranch
        ).text.clear()

        findViewById<EditText>(
            R.id.etMarks
        ).text.clear()
    }
}