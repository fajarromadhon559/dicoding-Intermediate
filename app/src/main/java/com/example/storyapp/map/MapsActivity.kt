package com.example.storyapp.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.example.storyapp.mainstory.StoryListAdapter
import com.example.storyapp.R
import com.example.storyapp.utils.ViewModelFactory
import com.example.storyapp.databinding.ActivityMapsBinding
import com.example.storyapp.local_data.SharedPreferences
import com.example.storyapp.Repo.Result
import com.example.storyapp.response.StoryResponseItem
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var sharedpref: SharedPreferences
    private lateinit var adapterStory: StoryListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        val mapsViewModel: MapViewModels by viewModels {
            factory
        }
        sharedpref = SharedPreferences(this)
        mapsViewModel.getStoryByMaps(1, sharedpref.getToken()).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                    }
                    is Result.Success -> {
                        showMarker(result.data)
                    }
                    is Result.Error -> {
                        Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        getMyLocation()
        setMapStyle()
    }
    private fun showMarker(listStory: List<StoryResponseItem>) {
        for (story in listStory) {
            val latlng = LatLng(story.lat, story.lon)
            mMap.addMarker(
                MarkerOptions()
                    .position(latlng)
                    .snippet(story.description)
                    .title(story.name)
            )
        }
    }
    private fun setMapStyle() {
        try {
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.maps_style))
            if (!success) {
                Log.e(MapStorys.TAG, "Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e(MapStorys.TAG, "Can't find style. Error: ", exception)
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }

    @SuppressLint("MissingPermission")
    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
}