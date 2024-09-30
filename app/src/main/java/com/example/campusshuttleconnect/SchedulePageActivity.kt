package com.example.campusshuttleconnect

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException
import kotlin.random.Random // Import for random number generation

// Data class for Shuttle
data class Shuttle(val id: Int, val destination: String, val departureTime: String)

class SchedulePageActivity : AppCompatActivity() {

    private lateinit var nextShuttleToGautrainMorningButton: Button
    private lateinit var nextShuttleToSandtonMorningButton: Button
    private lateinit var nextShuttleToGautrainAfternoonButton: Button
    private lateinit var nextShuttleToSandtonAfternoonButton: Button
    private lateinit var shuttleCapacityProgress: ProgressBar
    private lateinit var shuttleCapacityPercentage: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule_page)

        // Initialize UI elements
        nextShuttleToGautrainMorningButton = findViewById(R.id.nextShuttleToGautrainMorningButton)
        nextShuttleToSandtonMorningButton = findViewById(R.id.nextShuttleToSandtonMorningButton)
        nextShuttleToGautrainAfternoonButton = findViewById(R.id.nextShuttleToGautrainAfternoonButton)
        nextShuttleToSandtonAfternoonButton = findViewById(R.id.nextShuttleToSandtonAfternoonButton)
        shuttleCapacityProgress = findViewById(R.id.shuttleCapacityProgress)
        shuttleCapacityPercentage = findViewById(R.id.shuttleCapacityPercentage)

        // Menu Icon Click Event
        val menuIcon = findViewById<ImageView>(R.id.menuIcon)
        menuIcon.setOnClickListener {
            val intent = Intent(this, SideMenuActivity::class.java)
            startActivity(intent)
        }

        // Generate random shuttle capacity
        updateShuttleCapacity()

        // Fetch Shuttle schedule data
        fetchShuttleSchedule()
    }

    private fun updateShuttleCapacity() {
        val randomCapacity = Random.nextInt(0, 101) // Generate random value between 0 and 100
        shuttleCapacityProgress.progress = randomCapacity
        shuttleCapacityPercentage.text = "$randomCapacity%" // Update text to show the percentage
    }

    private fun fetchShuttleSchedule() {
        val url = "https://10.0.2.2:7040/api/Shuttle"  // Replace with your actual API endpoint
        val client = OkHttpClient()

        // Make network request using OkHttp
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@SchedulePageActivity, "Failed to fetch shuttle data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.let {
                    val responseBody = it.string()

                    // Parse the JSON response
                    val gson = Gson()
                    val shuttleList = gson.fromJson(responseBody, Array<Shuttle>::class.java)

                    // Update the UI on the main thread
                    Handler(Looper.getMainLooper()).post {
                        if (shuttleList.isNotEmpty()) {
                            // Assuming first shuttle is to Gautrain Station and second is to Sandton
                            nextShuttleToGautrainMorningButton.text = "Next Shuttle To ${shuttleList[0].destination} (Morning)"
                            nextShuttleToSandtonMorningButton.text = "Next Shuttle To ${shuttleList[1].destination} (Morning)"
                            nextShuttleToGautrainAfternoonButton.text = "Next Shuttle To ${shuttleList[0].destination} (Afternoon)"
                            nextShuttleToSandtonAfternoonButton.text = "Next Shuttle To ${shuttleList[1].destination} (Afternoon)"
                        } else {
                            Toast.makeText(this@SchedulePageActivity, "No shuttle data available", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        })
    }
}
