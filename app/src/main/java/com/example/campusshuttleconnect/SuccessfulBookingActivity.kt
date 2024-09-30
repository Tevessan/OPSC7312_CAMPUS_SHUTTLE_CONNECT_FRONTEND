package com.example.campusshuttleconnect

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SuccessfulBookingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_successful_booking)

        // Get the booking details passed from the BookingReportActivity
        val selectedDay = intent.getStringExtra("selected_day")
        val selectedTime = intent.getStringExtra("selected_time")
        val selectedDirection = intent.getStringExtra("selected_direction")

        // Find the shuttle info TextView and display the booking details
        val shuttleInfoText = findViewById<TextView>(R.id.shuttleInfoText)
        shuttleInfoText.text = "Your shuttle is booked for $selectedTime on $selectedDay ($selectedDirection)"

        // Back to Home button click listener
        val backToHomeButton = findViewById<Button>(R.id.backToHomeButton)
        backToHomeButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)  // Navigate back to the main/home page
            startActivity(intent)
            finish()  // Optional: To prevent going back to this page
        }
    }
}
