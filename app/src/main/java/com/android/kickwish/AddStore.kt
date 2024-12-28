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
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore

class AddStore : AppCompatActivity() {
    private lateinit var db: Firebase
    private lateinit var _etName: EditText
    private lateinit var _etDesc: EditText
    private lateinit var _etLong: EditText
    private lateinit var _etLat: EditText
    private lateinit var _btnAdd: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContentView(R.layout.add_store)

        initializeAddStore()

    }

    fun initializeAddStore() {
        this.db = Firebase
        this._etName = findViewById(R.id.etName)
        this._etDesc = findViewById(R.id.etAlamat)
        this._etLong = findViewById(R.id.etLongitude)
        this._etLat = findViewById(R.id.etLat)
        this._btnAdd = findViewById(R.id.btnAdd)
    }
}
