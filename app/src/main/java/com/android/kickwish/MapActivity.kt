package com.android.kickwish

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.CalendarContract.Colors
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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
        val searchTextView = searchBar.findViewById<TextView>(androidx.appcompat.R.id.search_src_text)
        searchTextView.setTextColor(ContextCompat.getColor(this, R.color.primary))
        searchTextView.setHintTextColor(ContextCompat.getColor(this, R.color.secondary))
        setupToolBar()
        initializeMaps()

//        arrMaps = mutableListOf(
//            Store(1, "Store 1", "This is store 1", "112.747009", "-7.330517"),
//            Store(2, "Store 2", "This is store 2", "112.750916", "-7.344457"),
//            Store(3, "Store 3", "This is store 3", "112.697112", "-7.344807"),
//        )
        mapAdapter.loadData(arrMaps)

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
        mapAdapter = MapAdapter(arrMaps)
        _rvMap.layoutManager = LinearLayoutManager(this)
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
