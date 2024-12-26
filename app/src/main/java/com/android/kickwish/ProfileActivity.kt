package com.android.kickwish

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.kickwish.Adapters.ProfileAdapter
import com.android.kickwish.Models.Sneaker
import com.android.kickwish.Models.User
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

        val logout = findViewById<Button>(R.id.btnLogout)
        val edit = findViewById<Button>(R.id.btnEdit)

        setupToolBar()

        initializeProfile()

        _rvProfile.adapter = profileAdapter
        profileAdapter.loadData(arrWishes)

        profileAdapter.setOnItemClickCallBack(object : ProfileAdapter.OnItemClickCallBack{
            override fun onItemClicked(wish: Wish) {
                val intent = Intent(this@ProfileActivity, WishlistActivity::class.java)
                startActivity(intent)
            }
        })

        logout.setOnClickListener {
            val sharedPreferences = getSharedPreferences("userData", MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()

            val intent = Intent(this@ProfileActivity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        edit.setOnClickListener {
            val editProfileFragment = EditProfileFragment()
            editProfileFragment.show(supportFragmentManager, "EditProfileFragment")
        }

    }

    fun getProfilefromDatabase(db: FirebaseFirestore) {
        val sharedPreferences = getSharedPreferences("userData", MODE_PRIVATE)
        val userId = sharedPreferences.getString("userId", "user")

        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                var user: User? = null
                for (document in result) {
                    if (document.id == userId) {
                        user = User(
                            document.data["name"].toString(),
                            document.data["email"].toString(),
                            document.data["password"].toString()
                        )
                        break
                    }
                }

                user?.let {
                    val tvNama = findViewById<TextView>(R.id.TVnama)
                    val tvEmail = findViewById<TextView>(R.id.TVemail)

                    tvNama.text = it.name
                    tvEmail.text = it.email
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Error Firebase", exception.message.toString())
            }
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

        getProfilefromDatabase(Firebase.firestore)
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