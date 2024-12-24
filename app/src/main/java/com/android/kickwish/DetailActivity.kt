package com.android.kickwish

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import java.text.NumberFormat
import java.util.Locale

class DetailActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_SNEAKER_ID = "extra_sneaker_id"
        private const val EXTRA_SNEAKER_NAME = "extra_sneaker_name"
        private const val EXTRA_SNEAKER_PRICE = "extra_sneaker_price"
        private const val EXTRA_SNEAKER_IMAGE = "extra_sneaker_image"
        private const val EXTRA_SNEAKER_DESC = "extra_sneaker_desc"

        fun createIntent(
            context: Context,
            id: Int,
            name: String,
            price: Double,
            imageResId: String,
            description: String
        ): Intent {
            return Intent(context, DetailActivity::class.java).apply {
                putExtra(EXTRA_SNEAKER_ID, id)
                putExtra(EXTRA_SNEAKER_NAME, name)
                putExtra(EXTRA_SNEAKER_PRICE, price)
                putExtra(EXTRA_SNEAKER_IMAGE, imageResId)
                putExtra(EXTRA_SNEAKER_DESC, description)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        setupToolBar()
        displaySneakerDetails()
        setupWishlistButton()
    }

    private fun setupToolBar() {
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowTitleEnabled(false)
        }
    }

    private fun displaySneakerDetails() {
        val sneakerImage: ImageView = findViewById(R.id.sneakerDetailImage)
        val sneakerName: TextView = findViewById(R.id.sneakerDetailName)
        val sneakerPrice: TextView = findViewById(R.id.sneakerDetailPrice)
        val sneakerDescription: TextView = findViewById(R.id.sneakerDescription)

        // Set image
        Picasso.get()
            .load(intent.getStringExtra(EXTRA_SNEAKER_IMAGE))
            .into(sneakerImage)

        // Set name
        sneakerName.text = intent.getStringExtra(EXTRA_SNEAKER_NAME)

        // Format and set price
        val price = intent.getDoubleExtra(EXTRA_SNEAKER_PRICE, 0.0)
        val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        sneakerPrice.text = formatter.format(price)

        // Set description (placeholder text)
        sneakerDescription.text = intent.getStringExtra(EXTRA_SNEAKER_DESC)
    }

    private fun setupWishlistButton() {
        val wishlistButton: Button = findViewById(R.id.addToWishlistButton)
        wishlistButton.setOnClickListener {
            // TODO: Implement wishlist functionality
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}