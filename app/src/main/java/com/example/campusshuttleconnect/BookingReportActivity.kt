package com.example.campusshuttleconnect

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject
import java.io.IOException
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class BookingReportActivity : AppCompatActivity() {

    private val client = getUnsafeOkHttpClient()  // Using unsafe OkHttp client to bypass SSL issues

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_report)

        // Setup window insets for the layout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Menu icon click listener
        val menuIcon = findViewById<ImageView>(R.id.menuIcon)
        menuIcon.setOnClickListener {
            val intent = Intent(this, SideMenuActivity::class.java)
            startActivity(intent)
        }

        // Spinners for date, time, and direction
        val spinnerDay = findViewById<Spinner>(R.id.spinnerDay)
        val spinnerTime = findViewById<Spinner>(R.id.spinnerTime)
        val spinnerDirection = findViewById<Spinner>(R.id.spinnerDirection)

        // Book Seat button
        val bookSeatButton = findViewById<Button>(R.id.bookSeatButton)
        bookSeatButton.setOnClickListener {
            // Get the selected values from spinners
            val selectedDay = spinnerDay.selectedItem.toString()
            val selectedTime = spinnerTime.selectedItem.toString()
            val selectedDirection = spinnerDirection.selectedItem.toString()

            // Post the booking data to the backend
            postBookingData(selectedDay, selectedTime, selectedDirection)
        }
    }

    private fun postBookingData(day: String, time: String, direction: String) {
        val url = "https://10.0.2.2:7040/api/Booking"  // Replace with your actual backend URL

        // Retrieve the JWT token from SharedPreferences
        val token = getToken()

        // Add debug log to check the token value
        Log.d("BookingReportActivity", "JWT Token: $token")

        if (token == null) {
            Toast.makeText(this, "JWT Token not found. Please log in again.", Toast.LENGTH_SHORT).show()
            return
        }

        // Create JSON object with booking data
        val jsonObject = JSONObject()
        jsonObject.put("day", day)
        jsonObject.put("time", time)
        jsonObject.put("direction", direction)

        // Create a request body with JSON
        val body = RequestBody.create("application/json; charset=utf-8".toMediaTypeOrNull(), jsonObject.toString())

        // Build the request
        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization", "Bearer $token")  // Add the JWT token here
            .post(body)
            .build()

        // Make the network request asynchronously
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    // Handle failure
                    Toast.makeText(this@BookingReportActivity, "Network Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                runOnUiThread {
                    if (response.isSuccessful) {
                        // Navigate to SuccessfulBookingActivity on success
                        val intent = Intent(this@BookingReportActivity, SuccessfulBookingActivity::class.java)
                        intent.putExtra("selected_day", day)
                        intent.putExtra("selected_time", time)
                        intent.putExtra("selected_direction", direction)
                        startActivity(intent)
                    } else {
                        // Log and show the exact error response from the server
                        Log.e("BookingReportActivity", "Error: ${response.message} - ${responseBody ?: "No response body"}")
                        Toast.makeText(this@BookingReportActivity, "Booking failed: ${response.code}. ${responseBody ?: "No response body"}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }

    // Function to retrieve the JWT token from SharedPreferences
    private fun getToken(): String? {
        val sharedPref = getSharedPreferences("user_prefs", MODE_PRIVATE)
        return sharedPref.getString("jwt_token", null)
    }

    // Trust all certificates (for development purposes only)
    private fun getUnsafeOkHttpClient(): OkHttpClient {
        return try {
            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                override fun checkClientTrusted(chain: Array<java.security.cert.X509Certificate?>?, authType: String?) {}
                override fun checkServerTrusted(chain: Array<java.security.cert.X509Certificate?>?, authType: String?) {}
                override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate?>? = arrayOfNulls(0)
            })

            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, java.security.SecureRandom())
            val sslSocketFactory = sslContext.socketFactory

            OkHttpClient.Builder()
                .sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
                .hostnameVerifier { _, _ -> true }
                .build()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}
