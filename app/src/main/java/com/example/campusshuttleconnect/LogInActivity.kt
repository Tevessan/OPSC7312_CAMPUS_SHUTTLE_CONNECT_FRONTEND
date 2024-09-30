package com.example.campusshuttleconnect

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject
import java.io.IOException
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class LogInActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var signInButton: Button
    private lateinit var notRegisteredText: TextView
    private val client = getUnsafeOkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check if the user is already logged in
        val token = getToken()
        if (token != null) {
            // Token exists, user is already logged in, redirect to SchedulePageActivity
            val intent = Intent(this, SchedulePageActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            // No token, proceed with the login
            setContentView(R.layout.activity_log_in)
            initViews()
        }
    }

    private fun initViews() {
        emailEditText = findViewById(R.id.email)
        passwordEditText = findViewById(R.id.password)
        signInButton = findViewById(R.id.signInButton)
        notRegisteredText = findViewById(R.id.notRegisteredText)

        signInButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in both fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Call the API to log in the user
            performLogin(email, password)
        }

        notRegisteredText.setOnClickListener {
            // Create an Intent to redirect to RegisterActivity
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    // Call the API directly using OkHttp for login
    private fun performLogin(email: String, password: String) {
        val json = JSONObject()
        json.put("email", email)
        json.put("password", password)

        val requestBody = RequestBody.create("application/json; charset=utf-8".toMediaTypeOrNull(), json.toString())

        val request = Request.Builder()
            .url("https://10.0.2.2:7040/api/Auth/login") // Ensure this matches your API URL
            .post(requestBody)
            .build()

        // Perform network request on a background thread
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@LogInActivity, "Network Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    Log.e("LogInActivity", "Error: ${e.message}", e)
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    val jsonResponse = JSONObject(responseBody ?: "")
                    val token = jsonResponse.optString("token")

                    runOnUiThread {
                        Toast.makeText(this@LogInActivity, "Login successful!", Toast.LENGTH_SHORT).show()

                        // Save the token in SharedPreferences
                        saveToken(token)

                        // Redirect to SchedulePageActivity
                        val intent = Intent(this@LogInActivity, SchedulePageActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@LogInActivity, "Login failed: ${response.code}", Toast.LENGTH_SHORT).show()
                        Log.e("LogInActivity", "Failed: ${response.body?.string()}")
                    }
                }
            }
        })
    }

    // Save JWT token to SharedPreferences
    private fun saveToken(token: String) {
        val sharedPref = getSharedPreferences("user_prefs", MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("jwt_token", token)
            apply()
        }
    }

    // Retrieve the token from SharedPreferences
    private fun getToken(): String? {
        val sharedPref = getSharedPreferences("user_prefs", MODE_PRIVATE)
        return sharedPref.getString("jwt_token", null)
    }

    // Trust all certificates (for development purposes only)
    fun getUnsafeOkHttpClient(): OkHttpClient {
        try {
            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                override fun checkClientTrusted(chain: Array<java.security.cert.X509Certificate?>?, authType: String?) {}

                override fun checkServerTrusted(chain: Array<java.security.cert.X509Certificate?>?, authType: String?) {}

                override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate?>? {
                    return arrayOfNulls(0)
                }
            })

            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, java.security.SecureRandom())
            val sslSocketFactory = sslContext.socketFactory

            return OkHttpClient.Builder()
                .sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
                .hostnameVerifier { _, _ -> true }
                .build()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}
