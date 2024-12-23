package com.android.kickwish

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.kickwish.R
import com.android.kickwish.adapter.SneakerAdapter
import com.android.kickwish.database.Sneaker
import com.google.firebase.database.*
import com.google.firebase.database.DatabaseReference

class CatalogActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var sneakerAdapter: SneakerAdapter
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catalog)

        recyclerView = findViewById(R.id.sneakersRecyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance().getReference("sneakers")

        // Fetch data from Firebase
        fetchSneakersFromFirebase()
    }

    private fun fetchSneakersFromFirebase() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val sneakers = mutableListOf<Sneaker>()
                for (sneakerSnapshot in snapshot.children) {
                    val sneaker = sneakerSnapshot.getValue(Sneaker::class.java)
                    sneaker?.let { sneakers.add(it) }
                }
                sneakerAdapter = SneakerAdapter(sneakers)
                recyclerView.adapter = sneakerAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("CatalogActivity", "Failed to read sneakers", error.toException())
            }
        })
    }
}