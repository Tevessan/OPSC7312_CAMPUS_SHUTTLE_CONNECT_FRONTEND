package com.example.campusshuttleconnect

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Find views by ID
        val languageSpinner = findViewById<Spinner>(R.id.language_spinner)
        val biometricsSwitch = findViewById<Switch>(R.id.biometrics_switch)
        val updateProfile = findViewById<LinearLayout>(R.id.profile_update)

        // Handle Language Change
        languageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                val selectedLanguage = parent?.getItemAtPosition(position).toString()
                Toast.makeText(this@SettingsActivity, "Language set to $selectedLanguage", Toast.LENGTH_SHORT).show()

                // You can implement logic here to change the app's locale
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        // Handle Biometrics Toggle
        biometricsSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Toast.makeText(this, "Biometrics enabled", Toast.LENGTH_SHORT).show()
                // Add logic to enable biometrics (e.g., Fingerprint, Face ID, etc.)
            } else {
                Toast.makeText(this, "Biometrics disabled", Toast.LENGTH_SHORT).show()
                // Add logic to disable biometrics
            }
        }

        // Handle Profile Update
        updateProfile.setOnClickListener {
            Toast.makeText(this, "Navigating to Profile Update", Toast.LENGTH_SHORT).show()
            // Add navigation to profile update screen if implemented
        }
    }
}

