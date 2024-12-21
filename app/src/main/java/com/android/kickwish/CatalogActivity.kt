package com.android.kickwish

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.kickwish.R
import com.android.kickwish.adapter.SneakerAdapter
import com.android.kickwish.database.Sneaker

class CatalogActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var sneakerAdapter: SneakerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catalog)

        recyclerView = findViewById(R.id.sneakersRecyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        // Sample data
        val sneakers = listOf(
            Sneaker(1, "Air Jordan 1 Retro High Dior", 102600000.0, R.drawable.dummy_wish_item),
            Sneaker(2, "Air Jordan 1 Retro High Dior", 102600000.0, R.drawable.dummy_wish_item),
            Sneaker(3, "Air Jordan 1 Retro High Dior", 102600000.0, R.drawable.dummy_wish_item),
            Sneaker(4, "Air Jordan 1 Retro High Dior", 102600000.0, R.drawable.dummy_wish_item),
            Sneaker(5, "Air Jordan 1 Retro High Dior", 102600000.0, R.drawable.dummy_wish_item),
            Sneaker(6, "Air Jordan 1 Retro High Dior", 102600000.0, R.drawable.dummy_wish_item)
        )

        sneakerAdapter = SneakerAdapter(sneakers)
        recyclerView.adapter = sneakerAdapter
    }
}