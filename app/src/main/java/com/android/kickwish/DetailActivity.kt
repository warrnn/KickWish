package com.android.kickwish

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import java.text.NumberFormat
import java.util.Locale

class DetailActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    companion object {
        private const val EXTRA_SNEAKER_NAME = "extra_sneaker_name"
        private const val EXTRA_SNEAKER_PRICE = "extra_sneaker_price"
        private const val EXTRA_SNEAKER_IMAGE = "extra_sneaker_image"
        private const val EXTRA_SNEAKER_DESC = "extra_sneaker_desc"

        fun createIntent(
            context: Context,
            name: String,
            price: Double,
            imageSrc: String,
            description: String
        ): Intent {
            return Intent(context, DetailActivity::class.java).apply {
                putExtra(EXTRA_SNEAKER_NAME, name)
                putExtra(EXTRA_SNEAKER_PRICE, price)
                putExtra(EXTRA_SNEAKER_IMAGE, imageSrc)
                putExtra(EXTRA_SNEAKER_DESC, description)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // Initialize Firebase instances
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

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

        // Check if sneaker is already in wishlist
        checkWishlistStatus(wishlistButton)

        wishlistButton.setOnClickListener {
            addToWishlist(wishlistButton)
        }
    }

    private fun checkWishlistStatus(button: Button) {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            button.text = "Login to Add to Wishlist"
            return
        }

        db.collection("wishlists")
            .whereEqualTo("userId", currentUser.uid)
            .whereEqualTo("name", intent.getStringExtra(EXTRA_SNEAKER_NAME))
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    button.text = "Remove from Wishlist"
                } else {
                    button.text = "Add to Wishlist"
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error checking wishlist status: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun addToWishlist(button: Button) {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show()
            // TODO: Navigate to login screen
            return
        }

        val sneakerName = intent.getStringExtra(EXTRA_SNEAKER_NAME)
        val sneakerPrice = intent.getDoubleExtra(EXTRA_SNEAKER_PRICE, 0.0)
        val sneakerImage = intent.getStringExtra(EXTRA_SNEAKER_IMAGE)

        // Check if already in wishlist
        db.collection("wishlists")
            .whereEqualTo("userId", currentUser.uid)
            .whereEqualTo("name", sneakerName)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    // Add to wishlist
                    val wishlistItem = hashMapOf(
                        "userId" to currentUser.uid,
                        "sneakerName" to sneakerName,
                        "sneakerPrice" to sneakerPrice,
                        "sneakerImage" to sneakerImage,
                        "timestamp" to FieldValue.serverTimestamp()
                    )

                    db.collection("wishlist")
                        .add(wishlistItem)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Added to wishlist", Toast.LENGTH_SHORT).show()
                            button.text = "Remove from Wishlist"
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Error adding to wishlist: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    // Remove from wishlist
                    val document = documents.documents[0]
                    db.collection("wishlists").document(document.id)
                        .delete()
                        .addOnSuccessListener {
                            Toast.makeText(this, "Removed from wishlist", Toast.LENGTH_SHORT).show()
                            button.text = "Add to Wishlist"
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Error removing from wishlist: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}