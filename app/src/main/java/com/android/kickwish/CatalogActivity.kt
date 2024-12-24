package com.android.kickwish

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.kickwish.Adapters.SneakerAdapter
import com.android.kickwish.Models.Sneaker
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CatalogActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var sneakerAdapter: SneakerAdapter
    private var sneakersList: MutableList<Sneaker> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catalog)

        setupToolBar()

        recyclerView = findViewById(R.id.sneakersRecyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        getDataFromDatabase(Firebase.firestore)

        sneakerAdapter = SneakerAdapter(sneakersList)
        recyclerView.adapter = sneakerAdapter
    }

    private fun setupToolBar() {
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowTitleEnabled(false)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun getDataFromDatabase(db: FirebaseFirestore) {
        db.collection("sneakers").get()
            .addOnSuccessListener {
                result ->
                sneakersList.clear()
                for (document in result) {
                    val data = Sneaker(
                        document.data["id"].toString().toInt(),
                        document.data["name"].toString(),
                        document.data["price"].toString().toDouble(),
                        document.data["imageURL"].toString(),
                        document.data["description"].toString()
                    )
                    sneakersList.add(data)
                }
                sneakerAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Log.e("Error Firebase", it.message.toString())
            }
    }
}