package com.android.kickwish

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.SimpleAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.kickwish.Data.User
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class RegisterActivity : AppCompatActivity() {
    var dataRegister = ArrayList<User>()
    var data: MutableList<Map<String, String>> = ArrayList()
    lateinit var _etName: EditText
    lateinit var _etEmail: EditText
    lateinit var _etPassword: EditText
    lateinit var lvAdapter: SimpleAdapter

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.register_activity)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val db = Firebase.firestore

        _etName = findViewById(R.id.etName)
        _etEmail = findViewById(R.id.etEmail)
        _etPassword = findViewById(R.id.etPassword)
        val _btnSignUp = findViewById<Button>(R.id.btnSignUp)

        lvAdapter = SimpleAdapter(
            this,
            data,
            android.R.layout.simple_list_item_2,
            arrayOf<String>("Nama", "Email", "Password"),
            intArrayOf(
                android.R.id.text1,
                android.R.id.text2,
            )
        )

        fun ReadData(db: FirebaseFirestore) {
            db.collection("DataRegister").get()
                .addOnSuccessListener { result ->
                    dataRegister.clear()
                    for (document in result) {
                        val readData = User(
                            document.data.get("nama").toString(),
                            document.data.get("email").toString(),
                            document.data.get("password").toString()
                        )
                        dataRegister.add(readData)

                        data.clear()
                        dataRegister.forEach {
                            val dt: MutableMap<String, String> = HashMap(2)
                            dt["Nama"] = it.nama
                            dt["Email"] = it.email
                            dt["Password"] = it.password
                            data.add(dt)
                        }
                    }
                    lvAdapter.notifyDataSetChanged()
                }
                .addOnFailureListener {
                    Log.d("Firebase", it.message.toString())
                }
        }

        fun TambahData(db: FirebaseFirestore, nama: String, email: String, password: String) {
            val dataBaru = User(nama, email, password)
            db.collection("DataRegister")
                .document(dataBaru.nama)
                .set(dataBaru)
                .addOnSuccessListener {
                    _etName.setText("")
                    _etEmail.setText("")
                    _etPassword.setText("")
                    Log.d("Firebase", "Data Berhasil Disimpan")
                    ReadData(db)
                }
                .addOnFailureListener {
                    Log.d("Firebase", it.message.toString())
                }
        }

        ReadData(db)

        _btnSignUp.setOnClickListener {
            TambahData(
                db,
                _etName.text.toString(),
                _etEmail.text.toString(),
                _etPassword.text.toString()
            )
            ReadData(db)
        }
    }
}
