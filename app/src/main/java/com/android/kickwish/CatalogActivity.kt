package com.android.kickwish

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.kickwish.Adapters.SneakerAdapter
import com.android.kickwish.Models.Sneaker
import com.google.firebase.firestore.FirebaseFirestore

class CatalogActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var sneakerAdapter: SneakerAdapter
    private val sneakers = mutableListOf<Sneaker>() // List to hold sneakers
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catalog)

        recyclerView = findViewById(R.id.sneakersRecyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance()

        // Initialize the adapter with an empty list
        sneakerAdapter = SneakerAdapter(sneakers)
        recyclerView.adapter = sneakerAdapter

        // Fetch data from Firestore
        fetchSneakersFromFirestore()
    }

    private fun fetchSneakersFromFirestore() {
        firestore.collection("sneakers")
            .get()
            .addOnSuccessListener { documents ->
                sneakers.clear() // Clear the list before adding new data
                for (document in documents) {
                    val sneaker = document.toObject(Sneaker::class.java)
                    sneakers.add(sneaker)
                }
                if (sneakers.isEmpty()) {
                    Toast.makeText(this@CatalogActivity, "No sneakers found", Toast.LENGTH_SHORT).show()
                } else {
                    sneakerAdapter.notifyDataSetChanged() // Notify adapter of data change
                }
            }
            .addOnFailureListener { exception ->
                Log.e("CatalogActivity", "Failed to read sneakers", exception)
                Toast.makeText(this@CatalogActivity, "Failed to load sneakers", Toast.LENGTH_SHORT).show()
            }
    }
}