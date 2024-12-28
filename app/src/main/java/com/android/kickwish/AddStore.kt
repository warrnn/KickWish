package com.android.kickwish

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.kickwish.Models.User
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore

class AddStore : AppCompatActivity() {
    private lateinit var _etName: EditText
    private lateinit var _etEmail: EditText
    private lateinit var _etPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContentView(R.layout.register_activity)

        val db = FirebaseFirestore.getInstance()

        _etName = findViewById(R.id.etName)
        _etEmail = findViewById(R.id.etEmail)
        _etPassword = findViewById(R.id.etPassword)
        val _btnSignUp = findViewById<Button>(R.id.btnSignUp)
        val _tvLogin = findViewById<TextView>(R.id.tvLogIn)

        fun addData(name: String, email: String, password: String) {
            val dataBaru = User(name, email, password)

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                // Menampilkan toast dengan error jika salah satu input kosong
                Toast.makeText(this, "Nama,Email atau Password tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return
            }
            db.collection("users")
                .add(dataBaru)
                .addOnSuccessListener {
                    Log.d("Firebase", "Data Berhasil Disimpan")
                    _etName.setText("")
                    _etEmail.setText("")
                    _etPassword.setText("")
                    Toast.makeText(this, "Register Successful", Toast.LENGTH_SHORT).show()
                    startActivity(
                        Intent(this, LoginActivity::class.java)
                    )
                }
                .addOnFailureListener { e ->
                    Log.e("FirebaseError", "Error saving data", e)
                }
        }

        _btnSignUp.setOnClickListener {
            addData(
                _etName.text.toString(),
                _etEmail.text.toString(),
                _etPassword.text.toString()
            )
        }

        _tvLogin.setOnClickListener {
            startActivity(
                Intent(this, LoginActivity::class.java)
            )
        }
    }
}
