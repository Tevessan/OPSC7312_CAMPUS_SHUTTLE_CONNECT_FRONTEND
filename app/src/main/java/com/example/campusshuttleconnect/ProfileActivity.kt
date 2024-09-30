package com.example.campusshuttleconnect

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {

    private lateinit var etStudentName: EditText
    private lateinit var etStudentSurname: EditText
    private lateinit var etStudentNumber: EditText
    private lateinit var etEmail: EditText
    private lateinit var btnChangePassword: Button
    private lateinit var btnChangeEmail: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Initialize views
        etStudentName = findViewById(R.id.et_student_name)
        etStudentSurname = findViewById(R.id.et_student_surname)
        etStudentNumber = findViewById(R.id.et_student_number)
        etEmail = findViewById(R.id.et_email)
        btnChangePassword = findViewById(R.id.btn_change_password)
        btnChangeEmail = findViewById(R.id.btn_change_email)

        // Handle change password click
        btnChangePassword.setOnClickListener {
            Toast.makeText(this, "Change Password Clicked", Toast.LENGTH_SHORT).show()
            // You can add logic to navigate to a ChangePasswordActivity here
        }

        // Handle change email click
        btnChangeEmail.setOnClickListener {
            Toast.makeText(this, "Change Email Clicked", Toast.LENGTH_SHORT).show()
            // You can add logic to navigate to a ChangeEmailActivity here
        }
    }
}
