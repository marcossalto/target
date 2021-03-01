package com.marcossalto.targetmvd.ui.target

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.marcossalto.targetmvd.R
import com.marcossalto.targetmvd.databinding.FragmentMapBinding
import com.marcossalto.targetmvd.util.permissions.PermissionFragment
import com.marcossalto.targetmvd.util.permissions.PermissionResponse
import com.marcossalto.targetmvd.util.permissions.checkNotGrantedPermissions
import com.marcossalto.targetmvd.util.permissions.locationPermissions

class MapFragment : PermissionFragment(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var supportMapFragment: SupportMapFragment
    private lateinit var binding: FragmentMapBinding
    private lateinit var targetActivityViewModel: TargetActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapBinding.inflate(layoutInflater, container, false)
        val factory = TargetActivityViewModelFactory()
        targetActivityViewModel = ViewModelProvider(requireActivity(), factory).get(TargetActivityViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        supportMapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        supportMapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.map = googleMap
        checkLocationPermission()
    }

    private fun checkLocationPermission() {
        if (requireContext().checkNotGrantedPermissions(locationPermissions).isEmpty()) {
            getLocation()
        } else {
            askForLocationPermission {
                getLocation()
            }
        }
    }

    private fun getLocation() {
        try {
            map.isMyLocationEnabled = true
            targetActivityViewModel.getLocation(requireContext()) { location ->
                with(location) {
                    addMarker(latitude, longitude)
                }
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    private fun addMarker(latitude: Double, longitude: Double) {
        val location = LatLng(latitude, longitude)
        map.addMarker(
            MarkerOptions()
                .position(location)
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker))
        )
        drawCircle(location)
        moveCamera(location)
    }

    private fun drawCircle(point: LatLng) {
        val circleOptions = CircleOptions().apply {
            center(point)
            radius(CIRCLE_RADIUS)
            strokeColor(ContextCompat.getColor(requireContext(), R.color.yellow))
            fillColor(ContextCompat.getColor(requireContext(), R.color.white_60))
            strokeWidth(CIRCLE_STROKE_WIDTH)
        }

        map.addCircle(circleOptions)
    }

    private fun moveCamera(latLng: LatLng) {
        val cameraPosition = CameraPosition.Builder().apply {
            target(latLng)
            zoom(GOOGLE_MAPS_ZOOM)
            bearing(GOOGLE_MAPS_BEARING)
            tilt(GOOGLE_MAPS_TILT)
        }.build()
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    private fun askForLocationPermission(permissionGranted: () -> Unit) {
        requestPermission(
            locationPermissions,
            object : PermissionResponse {
                override fun granted() {
                    permissionGranted()
                }

                override fun denied() = Unit

                override fun foreverDenied() = Unit
            })
    }

    companion object {
        const val GOOGLE_MAPS_ZOOM = 17f
        const val GOOGLE_MAPS_BEARING = 0f
        const val GOOGLE_MAPS_TILT = 0f
        const val CIRCLE_RADIUS = 90.0
        const val CIRCLE_STROKE_WIDTH = 2f

        @JvmStatic
        fun getInstance() =
            MapFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}
