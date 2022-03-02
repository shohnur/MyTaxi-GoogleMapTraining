package uz.mytaxi.ui.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.CountDownTimer
import android.view.View
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.gson.Gson
import kotlinx.android.synthetic.main.custom_info_contents.view.*
import kotlinx.android.synthetic.main.screen_map.*
import uz.mytaxi.R
import uz.mytaxi.base.BaseFragment
import uz.mytaxi.utils.*

class MapScreen : BaseFragment(R.layout.screen_map) {

    private lateinit var googleMap: GoogleMap
    private lateinit var placesClient: PlacesClient

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private var lastKnownLocation: Location? = null

    private val defaultLocation = LatLng(-33.8523341, 151.2106085)

    @SuppressLint("MissingPermission")
    override fun initialize() {

        requestLocationAccess()

        initMap()

        initClicks()

    }

    private fun initClicks() {

        arrayOf(nextBtn, whereEdt).forEach {
            it.setOnClickListener {
                inDevelopment(requireContext())
            }
        }

        menu.setOnClickListener {
            addFragment(ProfileScreen(), setAnim = 0, tag = "PROFILE")
        }

        myLocation.setOnClickListener {
            getDeviceLocation()
        }

    }

    private fun initMap() {

        Places.initialize(requireContext(), getString(R.string.google_maps_key))
        placesClient = Places.createClient(requireContext())

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync {
            googleMap = it

            googleMap.setInfoWindowAdapter(object : GoogleMap.InfoWindowAdapter {
                override fun getInfoContents(p0: Marker): View? {
                    val infoView = layoutInflater.inflate(
                        R.layout.custom_info_contents,
                        requireActivity().findViewById(R.id.map),
                        false
                    )
                    infoView.title.text = marker.title.toString()
                    infoView.snippet.text = marker.snippet.toString()
                    return infoView
                }

                override fun getInfoWindow(p0: Marker): View? {
                    return null
                }
            })

            getDeviceLocation()
            updateLocationUI()

            googleMap.setOnCameraChangeListener {
                val url =
                    "https://maps.googleapis.com/maps/api/geocode/json?latlng=${it.target.latitude},${it.target.longitude}&sensor=true&key=${
                        getString(R.string.google_maps_key)
                    }"
                val jsonObjectRequest = JsonObjectRequest(
                    Request.Method.GET, url, null,
                    { response ->
                        logi(response.toString(),"RESPONSE")
                        try {
                            val data =
                                Gson().fromJson(response.toString(), GeoCodingResp::class.java)
                            logi(data.results[0].formatted_address, tag = "Response")
                            from.text=data.results[0].formatted_address
                        } catch (e: Exception) {
                            loge(e.message.toString(), tag = "Response")
                        }
                    },
                    { error ->
                        error.printStackTrace()
                    }
                )

                Volley.newRequestQueue(requireContext()).add(jsonObjectRequest)
            }

        }
    }

    private fun getDeviceLocation() {
        try {
            val locationResult = fusedLocationProviderClient.lastLocation
            locationResult.addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    lastKnownLocation = task.result
                    if (lastKnownLocation != null) {
                        googleMap.animateCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                LatLng(
                                    lastKnownLocation!!.latitude,
                                    lastKnownLocation!!.longitude
                                ), 14f
                            )
                        )
                    }
                } else {
                    loge("Current location is null. Using defaults.")
                    loge("Exception: ${task.exception}")
                    googleMap.moveCamera(
                        CameraUpdateFactory
                            .newLatLngZoom(defaultLocation, 14f)
                    )
                    googleMap.uiSettings.isMyLocationButtonEnabled = false
                }
            }

        } catch (e: SecurityException) {
            loge("Exception: ${e.message}\n\n$e")
        }
    }

    private fun requestLocationAccess() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                101
            )
        }
    }

    private fun updateLocationUI() {
        if (map == null) {
            return
        }
        try {
            googleMap.isMyLocationEnabled = true
            googleMap.uiSettings.isMyLocationButtonEnabled = true

        } catch (e: SecurityException) {
            loge("Exception: ${e.message}\n\n$e")
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        logi(
            (!grantResults.any { it == PackageManager.PERMISSION_GRANTED }).toString() + requestCode,
            tag = "checkP"
        )
        if (requestCode == 101) {
            if (!grantResults.any { it == PackageManager.PERMISSION_GRANTED }) {
                toast(requireContext(), "Permission denied! Shutting down...")
                object : CountDownTimer(2000, 1000) {
                    override fun onTick(p0: Long) {
                    }

                    override fun onFinish() {
                        requireActivity().finish()
                    }
                }.start()
            } else toast(requireContext(), "Permission granted!")
        }
    }

}