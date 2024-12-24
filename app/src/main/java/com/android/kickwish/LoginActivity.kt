package com.android.kickwish

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class LoginActivity : AppCompatActivity() {
    private lateinit var _gotoSignUp: TextView
    private lateinit var _btnLogIn: Button
    private lateinit var _etEmail: EditText
    private lateinit var _etPassword: EditText
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initializeMain()

        _gotoSignUp.setOnClickListener {
            startActivity(
                Intent(this, RegisterActivity::class.java)
            )
        }
    }

    fun loginUser() {
        val email = _etEmail.text.toString().trim()
        val password = _etPassword.text.toString().trim()

        // Periksa apakah email atau password kosong
        if (email.isEmpty() || password.isEmpty()) {
            // Menampilkan toast dengan error jika salah satu input kosong
            Toast.makeText(this, "Email dan Password tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return
        }

        // Periksa apakah email dan password cocok dengan data yang ada di Firestore
        db.collection("users")
            .whereEqualTo("email", email)
            .whereEqualTo("password", password)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    for (document in documents) {
                        // Gunakan Firebase Document ID sebagai userId
                        val userId = document.id // ID dokumen Firestore
                        val username = document.getString("name") ?: "Unknown"

                        // Tampilkan toast jika login berhasil
                        Toast.makeText(
                            this,
                            "Login Successful! Hello! $username",
                            Toast.LENGTH_SHORT
                        ).show()

                        // Menyimpan data user ke SharedPreferences
                        val sharedPreferences = getSharedPreferences("userData", MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString("userId", userId)
                        editor.putString("username", username)
                        editor.apply()

                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                } else {
                    // Jika email atau password salah
                    Toast.makeText(this, "Email or Password is Incorrect", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                // Menangani kesalahan saat mengakses Firestore
                Toast.makeText(this, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    fun initializeMain() {
        this._gotoSignUp = findViewById(R.id.tvSignUp)
        this._btnLogIn = findViewById(R.id.btnLogIn)
        this._etEmail = findViewById(R.id.etEmail)
        this._etPassword = findViewById(R.id.etPassword)

        _btnLogIn.setOnClickListener {
            loginUser()
        }
    }
}