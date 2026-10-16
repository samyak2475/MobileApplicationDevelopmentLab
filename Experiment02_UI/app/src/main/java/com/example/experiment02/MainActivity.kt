package com.example.experiment02

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        // Find UI components
        val name = findViewById<EditText>(R.id.etName)
        val email = findViewById<EditText>(R.id.etEmail)
        val password = findViewById<EditText>(R.id.etPassword)

        val genderGroup =
            findViewById<RadioGroup>(R.id.radioGroupGender)

        val male =
            findViewById<RadioButton>(R.id.rbMale)

        val female =
            findViewById<RadioButton>(R.id.rbFemale)

        val branchSpinner =
            findViewById<Spinner>(R.id.spinnerBranch)

        val python =
            findViewById<CheckBox>(R.id.cbPython)

        val java =
            findViewById<CheckBox>(R.id.cbJava)

        val machineLearning =
            findViewById<CheckBox>(R.id.cbML)

        val registerButton =
            findViewById<Button>(R.id.btnRegister)


        // Branch options
        val branches = arrayOf(
            "Select Branch",
            "AI & Data Science",
            "Computer Science",
            "Information Technology",
            "Electronics",
            "Mechanical"
        )

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            branches
        )

        adapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        branchSpinner.adapter = adapter


        // Register button click event
        registerButton.setOnClickListener {

            val studentName = name.text.toString().trim()
            val studentEmail = email.text.toString().trim()
            val studentPassword = password.text.toString().trim()

            // Validate name
            if (studentName.isEmpty()) {
                name.error = "Please enter your name"
                name.requestFocus()
                return@setOnClickListener
            }

            // Validate email
            if (studentEmail.isEmpty()) {
                email.error = "Please enter your email"
                email.requestFocus()
                return@setOnClickListener
            }

            // Validate password
            if (studentPassword.isEmpty()) {
                password.error = "Please enter your password"
                password.requestFocus()
                return@setOnClickListener
            }

            // Validate gender
            val selectedGenderId =
                genderGroup.checkedRadioButtonId

            if (selectedGenderId == -1) {
                Toast.makeText(
                    this,
                    "Please select your gender",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            val selectedGender =
                findViewById<RadioButton>(selectedGenderId).text.toString()


            // Get selected branch
            val selectedBranch =
                branchSpinner.selectedItem.toString()

            if (selectedBranch == "Select Branch") {

                Toast.makeText(
                    this,
                    "Please select your branch",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }


            // Get selected skills
            val skills = mutableListOf<String>()

            if (python.isChecked) {
                skills.add("Python")
            }

            if (java.isChecked) {
                skills.add("Java")
            }

            if (machineLearning.isChecked) {
                skills.add("Machine Learning")
            }


            // Registration successful
            Toast.makeText(
                this,
                "Registration Successful!\n" +
                        "Name: $studentName\n" +
                        "Branch: $selectedBranch\n" +
                        "Gender: $selectedGender",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}