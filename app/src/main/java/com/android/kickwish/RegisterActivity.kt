package com.android.kickwish

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.SimpleAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.kickwish.Data.User
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {
    private var dataRegister = ArrayList<User>()
    private var data: MutableList<Map<String, String>> = ArrayList()
    private lateinit var _etName: EditText
    private lateinit var _etEmail: EditText
    private lateinit var _etPassword: EditText
    private lateinit var lvAdapter: SimpleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContentView(R.layout.register_activity)

        val db = FirebaseFirestore.getInstance()

        _etName = findViewById(R.id.etName)
        _etEmail = findViewById(R.id.etEmail)
        _etPassword = findViewById(R.id.etPassword)
        val _btnSignUp = findViewById<Button>(R.id.btnSignUp)
//        val _tvLogin = findViewById<TextView>(R.id.tvLogIn) // TextView untuk login

        lvAdapter = SimpleAdapter(
            this,
            data,
            android.R.layout.simple_list_item_2,
            arrayOf("Nama", "Email", "Password"),
            intArrayOf(android.R.id.text1, android.R.id.text2)
        )

        fun readData() {
            db.collection("DataRegister").get()
                .addOnSuccessListener { result ->
                    dataRegister.clear()
                    for (document in result) {
                        val readData = User(
                            document.getString("nama") ?: "",
                            document.getString("email") ?: "",
                            document.getString("password") ?: ""
                        )
                        dataRegister.add(readData)
                        data.clear()
                        dataRegister.forEach {
                            val dt = mapOf("Nama" to it.nama, "Email" to it.email, "Password" to it.password)
                            data.add(dt)
                        }
                    }
                    lvAdapter.notifyDataSetChanged()
                }
                .addOnFailureListener { e ->
                    Log.e("FirebaseError", "Error fetching data", e)
                }
        }

        fun addData(nama: String, email: String, password: String) {
            val dataBaru = User(nama, email, password)
            db.collection("DataRegister").document(dataBaru.nama)
                .set(dataBaru)
                .addOnSuccessListener {
                    _etName.setText("")
                    _etEmail.setText("")
                    _etPassword.setText("")
                    Log.d("Firebase", "Data Berhasil Disimpan")
                    readData()
                }
                .addOnFailureListener { e ->
                    Log.e("FirebaseError", "Error saving data", e)
                }
        }

        readData()

        _btnSignUp.setOnClickListener {
            addData(
                _etName.text.toString(),
                _etEmail.text.toString(),
                _etPassword.text.toString()
            )
        }

//        // Intent ke halaman login saat TextView diklik
//        _tvLogin.setOnClickListener {
//            val intent = Intent(this, LoginActivity::class.java)
//            startActivity(intent)
//        }
    }
}
