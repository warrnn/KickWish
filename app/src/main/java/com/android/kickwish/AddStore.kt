package com.android.kickwish

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.kickwish.Models.Store
import com.android.kickwish.Models.User
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

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

        _btnAdd.setOnClickListener {
            val newStore = Store(
                _etName.text.toString(),
                _etDesc.text.toString(),
                _etLong.text.toString(),
                _etLat.text.toString()
            )

            if (newStore.name.isEmpty() || newStore.desc.isEmpty() || newStore.long.isEmpty() || newStore.lat.isEmpty()) {
                Toast.makeText(this, "Nama, Deskripsi, Longitude, atau Latitude tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            db.firestore.collection("store")
                .add(newStore)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "Store added with ID: ${documentReference.id}")
                    Toast.makeText(this, "Store successfully added!", Toast.LENGTH_SHORT).show()
                    clearFields()
                    // Optionally navigate back or to another screen
                    // finish()
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding store", e)
                    Toast.makeText(this, "Failed to add store: ${e.message}", Toast.LENGTH_LONG).show()
                }
        }
    }

    fun initializeAddStore() {
        this.db = Firebase
        this._etName = findViewById(R.id.etName)
        this._etDesc = findViewById(R.id.etAlamat)
        this._etLong = findViewById(R.id.etLongitude)
        this._etLat = findViewById(R.id.etLat)
        this._btnAdd = findViewById(R.id.btnAdd)
    }

    private fun clearFields() {
        _etName.text.clear()
        _etDesc.text.clear()
        _etLong.text.clear()
        _etLat.text.clear()
    }

    companion object {
        private const val TAG = "AddStore"
    }
}
