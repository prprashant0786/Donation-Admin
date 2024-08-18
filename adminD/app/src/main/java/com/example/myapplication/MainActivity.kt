package com.example.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class MainActivity : AppCompatActivity() {
    private lateinit var formDataReference: DatabaseReference
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView

    private val REQUEST_CODE_DETAIL_ACTIVITY = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FirebaseApp.initializeApp(this)

        val database = FirebaseDatabase.getInstance()
        formDataReference = database.getReference("formData")

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        recyclerView = findViewById(R.id.recyclerView)

        // Set up RecyclerView
        val adapter = FormDataAdapter(emptyList()) { formData ->
            // Handle delete button click
            deleteFormData(formData)
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)

        swipeRefreshLayout.setOnRefreshListener {
            // Refresh data when the user swipes down
            refreshData()
        }

        // Initial data load
        loadData()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_DETAIL_ACTIVITY && resultCode == RESULT_OK) {
            // Refresh data when returning from DetailActivity
            loadData()
        }
    }

    private fun loadData() {
        lifecycleScope.launch {
            try {
                val formDataList = fetchDataInBackground(formDataReference)
                updateUI(formDataList)
            } catch (e: Exception) {
                Log.e("Firebase", "Error fetching data", e)
            } finally {
                // Hide refreshing indicator
                swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    private fun refreshData() {
        // Refresh data when the user swipes down
        loadData()
    }

    private suspend fun fetchDataInBackground(formDataReference: DatabaseReference): List<FormData> {
        return suspendCoroutine { continuation ->
            formDataReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val formDataList = mutableListOf<FormData>()
                    for (childSnapshot in snapshot.children) {
                        val formData = childSnapshot.getValue(FormData::class.java)
                        formData?.let {
                            formDataList.add(it)
                        }
                    }
                    continuation.resume(formDataList)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle database error
                    continuation.resumeWithException(Exception("Firebase Error: ${error.message}"))
                }
            })
        }
    }



    private fun updateUI(formDataList: List<FormData>) {
        // Set up RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        val adapter = FormDataAdapter(formDataList) { formData ->
            // Handle delete button click
            navigateToDetailActivity(formData)
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
    }

    private fun deleteFormData(formData: FormData) {
        val dataToDeleteRef = formDataReference.child(formData.key)//TODO:- Delete this key

        // Remove the data from the database
        dataToDeleteRef.removeValue()
            .addOnSuccessListener {
                // Data deleted successfully
                // You can perform any additional actions here
            }
            .addOnFailureListener {
                // Handle the failure to delete data
                Log.e("Firebase", "Error deleting data: ${it.message}")
            }
    }

    private fun navigateToDetailActivity(formData: FormData) {
        // Intent to start DetailActivity and pass data
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("formData", formData)
        startActivityForResult(intent, REQUEST_CODE_DETAIL_ACTIVITY)
    }

}
