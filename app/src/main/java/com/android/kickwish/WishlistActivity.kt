package com.android.kickwish

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.kickwish.Adapters.WishlistAdapter
import com.android.kickwish.Models.Sneaker
import com.android.kickwish.Models.Wish
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class WishlistActivity : AppCompatActivity() {
    private lateinit var db: Firebase
    private lateinit var wishlistAdapter: WishlistAdapter
    private lateinit var _rvWishlist: RecyclerView
    private lateinit var _tvNoItems: TextView
    private var arrWishes: MutableList<Wish> = mutableListOf()

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
        getDataFromFirebase(db.firestore)

        _rvWishlist.adapter = wishlistAdapter
        wishlistAdapter.loadData(arrWishes)

        wishlistAdapter.setItemOnClickCallBack(
            object : WishlistAdapter.OnItemClickCallBack {
                override fun deleteWish(wish: Wish) {
                    AlertDialog.Builder(this@WishlistActivity)
                        .setTitle("Delete Sneaker From Wishlist?")
                        .setMessage("Are you sure you want to delete ${wish.sneaker.name} from your wishlist?")
                        .setPositiveButton(
                            "Yes",
                            DialogInterface.OnClickListener { _, _ ->
                                wish.documentId?.let { id ->
                                    db.firestore.collection("wishlist").document(id)
                                        .delete()
                                        .addOnSuccessListener {
                                            arrWishes.remove(wish)
                                            wishlistAdapter.notifyDataSetChanged()
                                            Toast.makeText(
                                                this@WishlistActivity,
                                                "${wish.sneaker.name} Removed From Wishlist",
                                                Toast.LENGTH_LONG
                                            ).show()
                                            getDataFromFirebase(db.firestore)
                                        }
                                        .addOnFailureListener {
                                            Log.e("Error Firebase", it.message.toString())
                                        }
                                }
                            }
                        )
                        .setNegativeButton(
                            "Cancel",
                            DialogInterface.OnClickListener { dialog, _ ->
                                Toast.makeText(
                                    this@WishlistActivity,
                                    "Cancelled",
                                    Toast.LENGTH_LONG
                                ).show()
                                dialog.dismiss()
                            }
                        ).show()
                }

                override fun exploreWish(wish: Wish) {
                    startActivity(
                        Intent(Intent.ACTION_VIEW)
                            .apply {
                                data =
                                    Uri.parse("https://www.google.com/search?q=${wish.sneaker.name}")
                            }
                    )
                }
            }
        )
    }

    fun initializeWishlist() {
        this.db = Firebase
        this._rvWishlist = findViewById(R.id.rvWishlist)
        this._tvNoItems = findViewById(R.id.tvNoItems)
        this.wishlistAdapter = WishlistAdapter(arrWishes)
        this._rvWishlist.layoutManager = LinearLayoutManager(this)
    }

    fun getDataFromFirebase(db: FirebaseFirestore) {
        val sharedPreferences = getSharedPreferences("userData", MODE_PRIVATE)
        val userId = sharedPreferences.getString("userId", "user")

        db.collection("wishlist")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { result ->
                arrWishes.clear()

                for (document in result) {
                    val data = Wish(
                        Sneaker(
                            document.data["sneaker_name"].toString(),
                            document.data["sneaker_price"].toString().toDouble(),
                            document.data["sneaker_image"].toString(),
                            document.data["sneaker_description"].toString()
                        ),
                        document.data["userId"].toString(),
                        document.id
                    )
                    arrWishes.add(data)
                }

                wishlistAdapter.notifyDataSetChanged()
                updateUI()
            }
            .addOnFailureListener {
                Log.e("Error Firebase", it.message.toString())
            }
    }

    private fun updateUI() {
        if (arrWishes.isEmpty()) {
            _tvNoItems.visibility = View.VISIBLE
            _rvWishlist.visibility = View.GONE
        } else {
            _tvNoItems.visibility = View.GONE
            _rvWishlist.visibility = View.VISIBLE
        }
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