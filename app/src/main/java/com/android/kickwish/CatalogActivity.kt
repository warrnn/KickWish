package com.android.kickwish

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.kickwish.Adapters.SneakerAdapter
import com.android.kickwish.Models.Sneaker

class CatalogActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var sneakerAdapter: SneakerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catalog)

        setupAppBar()

        recyclerView = findViewById(R.id.sneakersRecyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        // Sample data
        val sneakers = listOf(
            Sneaker(1, "Air Jordan 1 Retro High Dior", 102600000.0, R.drawable.jordan_dior),
            Sneaker(2, "Air Jordan 1 Retro High Dior", 102600000.0, R.drawable.jordan_dior),
            Sneaker(3, "Air Jordan 1 Retro High Dior", 102600000.0, R.drawable.jordan_dior),
            Sneaker(4, "Air Jordan 1 Retro High Dior", 102600000.0, R.drawable.jordan_dior),
            Sneaker(5, "Air Jordan 1 Retro High Dior", 102600000.0, R.drawable.jordan_dior),
            Sneaker(6, "Air Jordan 1 Retro High Dior", 102600000.0, R.drawable.jordan_dior)
        )

        sneakerAdapter = SneakerAdapter(sneakers)
        recyclerView.adapter = sneakerAdapter
    }

    private fun setupAppBar() {
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
}