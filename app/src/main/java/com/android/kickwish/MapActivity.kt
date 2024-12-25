package com.android.kickwish

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.kickwish.Adapters.MapAdapter
import com.android.kickwish.Models.Store
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.ktx.firestore
import java.util.Locale

class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var directionBTN: FloatingActionButton
    private lateinit var searchBar: SearchView
    private lateinit var mapAdapter: MapAdapter
    private lateinit var _rvMap: RecyclerView
    private var arrMaps: MutableList<Store> = mutableListOf()
    private lateinit var googleMap: GoogleMap

    private var longitude = "112.73732"
    private var latitude = "-7.3392457"

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
        searchBar = findViewById(R.id.searchBar)
        setupToolBar()

        initializeMaps()

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        directionBTN.setOnClickListener {
            navigateToMap(latitude, longitude)
        }

        mapAdapter.setOnItemClickCallBack(object : MapAdapter.OnItemClickCallBack {
            override fun onItemClicked(store: Store) {
                updateMap(store.lat.toDouble(), store.long.toDouble())
                latitude = store.lat
                longitude = store.long
            }
        })

        searchBar.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }

        })
    }

    fun getDataFromDatabase(db: FirebaseFirestore) {
        db.collection("stores").get()
            .addOnSuccessListener { result ->
                arrMaps.clear()

                for (document in result){
                    val data = Store(
                        document.data["name"].toString(),
                        document.data["desc"].toString(),
                        document.data["long"].toString(),
                        document.data["lat"].toString()
                    )
                    arrMaps.add(data)
                }

                mapAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Log.e("Error Firebase", it.message.toString())
            }
    }

    private fun filterList(query: String?){
        if (query != null){
            val filteredList = ArrayList<Store>()
            for (i in arrMaps) {
                if (i.name.lowercase(Locale.ROOT).contains(query)){
                    filteredList.add(i)
                }
            }

            if(filteredList.isEmpty()){
//                Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show()
            }else{
                mapAdapter.setFilteredList(filteredList)
            }
        }
    }

    private fun navigateToMap(destinationLat: String, destinationLong: String) {
        val mapUri = Uri.parse("https://maps.google.com/maps?daddr=$destinationLat,$destinationLong")
        val intent = Intent(Intent.ACTION_VIEW, mapUri)
        startActivity(intent)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        updateMap(latitude.toDouble(), longitude.toDouble())
    }

    private fun updateMap(lat: Double, long: Double) {
        val latLng = LatLng(lat, long)
        googleMap.clear()
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 19f))
        googleMap.addMarker(MarkerOptions().position(latLng))
    }

    private fun initializeMaps() {
        _rvMap = findViewById(R.id.mapRecView)
        _rvMap.layoutManager = LinearLayoutManager(this)

        getDataFromDatabase(Firebase.firestore)
        mapAdapter = MapAdapter(arrMaps)
        _rvMap.adapter = mapAdapter
    }

    private fun setupToolBar() {
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
