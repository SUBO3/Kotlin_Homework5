package com.example.lab14

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
    private val locationRequestCode = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        setupWindowInsets()
        if (hasLocationPermissions()) {
            loadMap()
        } else {
            requestLocationPermissions()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationRequestCode && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
            loadMap()
        } else {
            finish()
        }
    }

    override fun onMapReady(map: GoogleMap) {
        if (hasLocationPermissions()) {
            setupMap(map)
        } else {
            requestLocationPermissions()
        }
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun hasLocationPermissions(): Boolean {
        return permissions.all { ActivityCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED }
    }

    private fun requestLocationPermissions() {
        ActivityCompat.requestPermissions(this, permissions, locationRequestCode)
    }

    private fun loadMap() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun setupMap(map: GoogleMap) {
        map.isMyLocationEnabled = true

        val taipei101 = LatLng(25.033611, 121.565000)
        val taipeiStation = LatLng(25.047924, 121.517081)
        val midPoint = LatLng(25.032435, 121.534905)

        map.addMarker(MarkerOptions().position(taipei101).title("台北101").draggable(true))
        map.addMarker(MarkerOptions().position(taipeiStation).title("台北車站").draggable(true))

        val polylineOptions = PolylineOptions()
            .add(taipei101)
            .add(midPoint)
            .add(taipeiStation)
            .color(Color.BLUE)
            .width(10f)
        map.addPolyline(polylineOptions)

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(25.035, 121.54), 13f))
    }
}
