package com.android.kickwish

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.kickwish.Adapters.ProfileAdapter
import com.android.kickwish.Models.Sneaker
import com.android.kickwish.Models.Wish
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class ProfileActivity : AppCompatActivity() {
    private lateinit var profileAdapter: ProfileAdapter
    private lateinit var _rvProfile: RecyclerView
    private var arrWishes:MutableList<Wish> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val sharedPreferences = getSharedPreferences("userData", MODE_PRIVATE)
        val username = sharedPreferences.getString("username", "user")
        val email = sharedPreferences.getString("email", "email")

        setupToolBar()

        initializeProfile()

        _rvProfile.adapter = profileAdapter
        profileAdapter.loadData(arrWishes)

        profileAdapter.setOnItemClickCallBack(object : ProfileAdapter.OnItemClickCallBack{
            override fun onItemClicked(wish: Wish) {
                //go to wishlistactivity
                val intent = Intent(this@ProfileActivity, WishlistActivity::class.java)
                startActivity(intent)
            }
        })


    }

    fun getDatafromDatabase(db: FirebaseFirestore){
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

                profileAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Log.e("Error Firebase", it.message.toString())
            }
    }


    private fun initializeProfile(){
        this._rvProfile = findViewById(R.id.profileRecView)
        this._rvProfile.layoutManager = LinearLayoutManager(this)

        getDatafromDatabase(Firebase.firestore)
        profileAdapter = ProfileAdapter(arrWishes)
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