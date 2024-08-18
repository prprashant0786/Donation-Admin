package com.example.myapplication
// MainActivity.kt
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {

    private lateinit var linearLayout: LinearLayout

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseApp.initializeApp(this)
        FirebaseMessaging.getInstance().subscribeToTopic("allDevices")
        linearLayout = findViewById(R.id.linear_layout)

        // Check for internet connectivity
        if (!isConnected()) {
            // Not connected to the internet
            Toast.makeText(this, "No internet connection", Toast.LENGTH_LONG).show()

            val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
            startActivity(intent)
        }

        val btncltLayout = findViewById<LinearLayout>(R.id.btnclt)
        val btnstationLayout = findViewById<LinearLayout>(R.id.btnstation)
        val btntoysLayout = findViewById<LinearLayout>(R.id.btntoys)
        val btnotherLayout = findViewById<LinearLayout>(R.id.btnother)

        btncltLayout.setOnClickListener { openNewActivity("Donate Clothes") }
        btnstationLayout.setOnClickListener { openNewActivity("Donate Stationary") }
        btntoysLayout.setOnClickListener { openNewActivity("Donate Toys") }
        btnotherLayout.setOnClickListener { openNewActivity("Other Item") }
    }

    private fun openNewActivity(clickedLayout: String) {
        val intent = Intent(this, FormActivity::class.java)
        intent.putExtra("selectedButton", clickedLayout)
        startActivity(intent)
    }

    private fun isConnected(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork
            val capabilities =
                connectivityManager.getNetworkCapabilities(network)
            capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || capabilities.hasTransport(
                NetworkCapabilities.TRANSPORT_CELLULAR
            ))
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            activeNetworkInfo != null && activeNetworkInfo.isConnected
        }
    }
}

