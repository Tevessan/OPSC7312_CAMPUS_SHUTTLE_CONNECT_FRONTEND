package com.example.campusshuttleconnect
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.example.campusshuttleconnect.MainActivity
import com.example.campusshuttleconnect.ProfileActivity
import com.example.campusshuttleconnect.R
import com.example.campusshuttleconnect.SearchActivity

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

        // Set up navigation bar click listeners
        findViewById<ImageView>(R.id.homeIcon).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        findViewById<ImageView>(R.id.profileIcon).setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
            finish()
        }

        findViewById<ImageView>(R.id.searchIcon).setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
            finish()
        }
    }
}
