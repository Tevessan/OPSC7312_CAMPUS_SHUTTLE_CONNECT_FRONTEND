package com.example.campusshuttleconnect

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SideMenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_side_menu)

        // Find views by ID
        val vcShuttleConnect = findViewById<TextView>(R.id.tv_vc_shuttle_connect)
        val faq = findViewById<TextView>(R.id.tv_faq)
        val shuttleTimes = findViewById<TextView>(R.id.tv_shuttle_times)
        val contact = findViewById<TextView>(R.id.tv_contact)
        val booking = findViewById<TextView>(R.id.tv_booking)
        val settings = findViewById<TextView>(R.id.tv_settings)

        val logout = findViewById<TextView>(R.id.logout)  // Logout

        // Setup click listeners
        faq.setOnClickListener {
            val intent = Intent(this, FaqPageActivity::class.java)
            startActivity(intent)
        }

        shuttleTimes.setOnClickListener {
            val intent = Intent(this, GeneralShuttleTimesActivity::class.java)
            startActivity(intent)
        }

        contact.setOnClickListener {
            val intent = Intent(this, ContactPageActivity::class.java)
            startActivity(intent)
        }

        booking.setOnClickListener {
            val intent = Intent(this, BookingReportActivity::class.java)
            startActivity(intent)
        }

        settings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }



        logout.setOnClickListener {
            clearToken()

            // Navigate to the Login Activity
            val intent = Intent(this, LogInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // Clear backstack
            startActivity(intent)
            finish() // Close the SideMenuActivity
        }
    }

    // Function to clear the JWT token from SharedPreferences
    private fun clearToken() {
        val sharedPref = getSharedPreferences("user_prefs", MODE_PRIVATE)
        with(sharedPref.edit()) {
            remove("jwt_token")  // Remove the token
            apply()
        }
    }
}


