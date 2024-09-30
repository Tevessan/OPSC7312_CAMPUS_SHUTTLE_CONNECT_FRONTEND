package com.example.campusshuttleconnect


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.campusshuttleconnect.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class ShuttleTrackingPageActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shuttle_tracking_page)

        // Initialize the MapView
        mapView = findViewById(R.id.map_view)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this) // Set up the map

        // Initialize the Google Maps SDK (recommended)
        MapsInitializer.initialize(this)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        val shuttleLocation = LatLng(-33.852, 151.211)  // Example coordinates
        googleMap.addMarker(MarkerOptions().position(shuttleLocation).title("Your Shuttle"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(shuttleLocation, 15f))
    }

    // Handle the MapView lifecycle
    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}
