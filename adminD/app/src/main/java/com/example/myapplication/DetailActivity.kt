// DetailActivity.kt
package com.example.admin

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.admin.FormData
import com.example.admin.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class DetailActivity : AppCompatActivity() {
    private lateinit var detailNameTextView: TextView
    private lateinit var detailAddressTextView: TextView
    private lateinit var detailPhoneNumberTextView: TextView
    private lateinit var detailItemNameTextView: TextView
    private lateinit var detailItemQuantityTextView: TextView

    private lateinit var deleteButton: Button
    private lateinit var formDataReference: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        detailNameTextView = findViewById(R.id.detailNameTextView)
        detailAddressTextView = findViewById(R.id.detailAddressTextView)
        detailPhoneNumberTextView = findViewById(R.id.detailPhoneNumberTextView)
        detailItemNameTextView = findViewById(R.id.detailItemNameTextView)
        detailItemQuantityTextView = findViewById(R.id.detailItemQuantityTextView)

        deleteButton = findViewById(R.id.deleteButton)

        // Retrieve FormData from Intent
        val formData = intent.getSerializableExtra("formData") as? FormData
        if (formData != null) {
            formData.let {
                // Display details in TextViews
                detailNameTextView.text = "Name: ${it.name}"
                detailAddressTextView.text = "Address: ${it.address}"
                detailPhoneNumberTextView.text = "Phone Number: ${it.phoneNumber}"
                detailItemNameTextView.text = "Item Name: ${it.itemName}"
                detailItemQuantityTextView.text = "Item Quantity: ${it.itemQuantity}"

                deleteButton.setOnClickListener {
                    deleteFormData(formData)
                    setResult(RESULT_OK)
                    finish() // Close DetailActivity after deletion
                }
            }
        } else {
            // Handle the case where formData is null (e.g., show an error message)
            Log.e("DetailActivity", "Error: FormData is null")
        }
    }

    private fun deleteFormData(formData: FormData) {
        // Initialize Firebase reference
        val database = FirebaseDatabase.getInstance()
        formDataReference = database.getReference("formData")

        // Remove the data from the database
        formDataReference.child(formData.key)
            .removeValue()
            .addOnSuccessListener {
                // Data deleted successfully
                // You can perform any additional actions here

                // Set result to notify MainActivity that deletion was successful

            }
            .addOnFailureListener {
                // Handle the failure to delete data
                // You may want to show a Toast or log the error
                // for better error handling in a production app
            }
    }
}
