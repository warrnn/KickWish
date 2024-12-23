package com.android.kickwish

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.kickwish.Adapters.WishlistAdapter
import com.android.kickwish.Models.Sneaker
import com.android.kickwish.Models.Wish

class WishlistActivity : AppCompatActivity() {
    private lateinit var wishlistAdapter: WishlistAdapter
    private lateinit var _rvWishlist: RecyclerView
    private var arrWishes:MutableList<Wish> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_wishlist)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupToolBar()

        initializeWishlist()

        arrWishes = mutableListOf(
            Wish(
                Sneaker(1, "Air Jordan 1 Retro High Dior", 102600000.0, R.drawable.jordan_dior),
                1
            ),
            Wish(
                Sneaker(2, "Air Jordan 1 Retro High Dior", 102600000.0, R.drawable.jordan_dior),
                1
            ),
            Wish(
                Sneaker(3, "Air Jordan 1 Retro High Dior", 102600000.0, R.drawable.jordan_dior),
                1
            ),
        )

        _rvWishlist.adapter = wishlistAdapter
        wishlistAdapter.loadData(arrWishes)
    }

    fun initializeWishlist() {
        this._rvWishlist = findViewById(R.id.rvWishlist)
        this.wishlistAdapter = WishlistAdapter(arrWishes)
        this._rvWishlist.layoutManager = LinearLayoutManager(this)
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
}