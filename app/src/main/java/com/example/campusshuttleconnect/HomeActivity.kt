package com.example.campusshuttleconnect

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Bottom Navigation setup
        val homeIcon = findViewById<ImageView>(R.id.homeIcon)
        val profileIcon = findViewById<ImageView>(R.id.profileIcon)
        val searchIcon = findViewById<ImageView>(R.id.searchIcon)

        // Set click listeners for the bottom navigation icons
        homeIcon.setOnClickListener {
            // Already in HomeActivity
        }

        profileIcon.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        searchIcon.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }
    }
}
