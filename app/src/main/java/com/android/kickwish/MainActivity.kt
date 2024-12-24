package com.android.kickwish

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var _gotoCatalogCard: ConstraintLayout
    private lateinit var _gotoAICard: ConstraintLayout
    private lateinit var _gotoWishlistCard: ConstraintLayout
    private lateinit var _gotoMapCard: ConstraintLayout
    private lateinit var _gotoProfileCard: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initializeMain()

        _gotoCatalogCard.setOnClickListener {
            startActivity(
                Intent(this, CatalogActivity::class.java)
            )
        }
        _gotoAICard.setOnClickListener {
            startActivity(
                Intent(this, ChatbotActivity::class.java)
            )
        }
        _gotoWishlistCard.setOnClickListener {
            startActivity(
                Intent(this, WishlistActivity::class.java)
            )
        }
        _gotoMapCard.setOnClickListener {
            startActivity(
                Intent(this, MapActivity::class.java)
            )
        }
        _gotoProfileCard.setOnClickListener {
            startActivity(
                Intent(this, ProfileActivity::class.java)
            )
        }

        // Lanjut sini ya ges ya
    }

    fun initializeMain() {
        this._gotoCatalogCard = findViewById(R.id.card1)
        this._gotoAICard = findViewById(R.id.card2)
        this._gotoWishlistCard = findViewById(R.id.card3)
        this._gotoMapCard = findViewById(R.id.card4)
        this._gotoProfileCard = findViewById(R.id.card5)
    }
}