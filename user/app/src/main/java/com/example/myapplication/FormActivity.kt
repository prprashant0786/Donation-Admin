package com.example.myapplication

// FormActivity.kt
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class FormActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)

        val selectedButton = intent.getStringExtra("selectedButton")

        if(!selectedButton.equals("Other Items")) {
            val etItemName: EditText = findViewById(R.id.etItemName)
            etItemName.setText(selectedButton)
        }

        val database = FirebaseDatabase.getInstance()
        val formDataReference = database.getReference("formData")

        val submitButton: Button = findViewById(R.id.btnSubmit)

        submitButton.setOnClickListener {
            val name = findViewById<EditText>(R.id.etName).text.toString()
            val address = findViewById<EditText>(R.id.etAddress).text.toString()
            val phoneNumber = findViewById<EditText>(R.id.etPhoneNumber).text.toString()
            val itemName = findViewById<EditText>(R.id.etItemName).text.toString()
            val itemQuantityText = findViewById<EditText>(R.id.etItemQuantity).text.toString()

            if (name.isNotEmpty() && address.isNotEmpty() && phoneNumber.isNotEmpty()
                && itemName.isNotEmpty() && itemQuantityText.isNotEmpty()) {

                val key =generateKey()
                // Convert itemQuantityText to Int only if it's not empty
                val itemQuantity = itemQuantityText.toInt()

                val formData = FormData(key,name, address, phoneNumber, itemName, itemQuantity)

                formDataReference.push().setValue(formData)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            clearForm()
                            Toast.makeText(this, "Thanks for Donating", Toast.LENGTH_LONG).show()
                            openSuccessActivity()
                        } else {
                            Log.e("Firebase", "Error: ${task.exception}")
                        }
                    }
            } else {
                Toast.makeText(this, "Please fill in all details", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun clearForm() {
        findViewById<EditText>(R.id.etName).text.clear()
        findViewById<EditText>(R.id.etAddress).text.clear()
        findViewById<EditText>(R.id.etPhoneNumber).text.clear()
        findViewById<EditText>(R.id.etItemName).text.clear()
        findViewById<EditText>(R.id.etItemQuantity).text.clear()
    }

    private fun openSuccessActivity() {
        val intent = Intent(this, SuccessActivity::class.java)
        startActivity(intent)
        finish() // Optional: Finish the current activity to prevent going back to it.
    }

    private fun generateKey(): String {
        val currentTime = System.currentTimeMillis()
        val dateFormat = SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.getDefault())
        val formattedDate = dateFormat.format(Date(currentTime))

        val randomNumber = (1000..9999).random() // Generate a random 4-digit number

        return "$formattedDate@$randomNumber"
    }
}
