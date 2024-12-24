package com.android.kickwish

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var directionBTN: FloatingActionButton

    private var longitude =  "112.73732"
    private var latitude =  "-7.3392457"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_map)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        directionBTN = findViewById(R.id.fabDir)

        directionBTN.setOnClickListener {
            navigateToMap(latitude, longitude)
        }

        setupToolBar()

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment

        mapFragment?.getMapAsync(this)
    }

    private fun navigateToMap(destinationLat: String, destinationLong: String){
        val mapUri = Uri.parse("https://maps.google.com/maps?daddr=$destinationLat,$destinationLong")

        val intent = Intent(Intent.ACTION_VIEW, mapUri)

        startActivity(intent)
    }

    override fun onMapReady(map: GoogleMap) {
        val latLng = LatLng(latitude.toDouble(),longitude.toDouble())
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 19f))

        val markerOptions = MarkerOptions()
        markerOptions.position(latLng)
        map.addMarker(markerOptions)
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