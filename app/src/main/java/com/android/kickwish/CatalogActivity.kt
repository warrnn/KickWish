package com.android.kickwish

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
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
    private lateinit var searchView: SearchView
    private var sneakersList: MutableList<Sneaker> = mutableListOf()
    private var filteredSneakersList: MutableList<Sneaker> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catalog)

        setupToolBar()
        setupSearchView()

        recyclerView = findViewById(R.id.sneakersRecyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        getDataFromDatabase(Firebase.firestore)

        sneakerAdapter = SneakerAdapter(filteredSneakersList)
        recyclerView.adapter = sneakerAdapter
    }

    private fun setupToolBar() {
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowTitleEnabled(false)
        }
    }

    private fun setupSearchView() {
        searchView = findViewById(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filterSneakers(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterSneakers(newText)
                return true
            }
        })
    }

    private fun filterSneakers(query: String?) {
        filteredSneakersList.clear()

        if (query.isNullOrBlank()) {
            filteredSneakersList.addAll(sneakersList)
        } else {
            val searchQuery = query.lowercase()
            filteredSneakersList.addAll(
                sneakersList.filter { sneaker ->
                    sneaker.name.lowercase().contains(searchQuery)
                }
            )
        }

        sneakerAdapter.notifyDataSetChanged()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun getDataFromDatabase(db: FirebaseFirestore) {
        db.collection("sneakers").get()
            .addOnSuccessListener { result ->
                sneakersList.clear()
                filteredSneakersList.clear()

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

                filteredSneakersList.addAll(sneakersList)
                sneakerAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Log.e("Error Firebase", it.message.toString())
            }
    }
}