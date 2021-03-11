package com.marcossalto.targetmvd.ui.target

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Point
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.marcossalto.targetmvd.R
import com.marcossalto.targetmvd.databinding.FragmentMapBinding
import com.marcossalto.targetmvd.models.TargetModel
import com.marcossalto.targetmvd.util.extensions.getTargetIcon
import com.marcossalto.targetmvd.util.permissions.PermissionFragment
import com.marcossalto.targetmvd.util.permissions.PermissionResponse
import com.marcossalto.targetmvd.util.permissions.checkNotGrantedPermissions
import com.marcossalto.targetmvd.util.permissions.locationPermissions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MapFragment : PermissionFragment(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var supportMapFragment: SupportMapFragment
    private lateinit var binding: FragmentMapBinding
    private lateinit var targetActivityViewModel: TargetActivityViewModel
    private var targetModelMap: HashMap<TargetModel, Marker> = HashMap()
    private var targetMarkerMap: HashMap<String, TargetModel> = HashMap()
    private var selectedTarget: TargetModel? = null

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
        observeTargets()
        observeNewTargets()
        observeDeletedTargets()
        supportMapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        supportMapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        checkLocationPermission()
        map.setOnMarkerClickListener { marker ->
            showTargetInfo(marker)
            return@setOnMarkerClickListener true
        }
    }

    private fun showTargetInfo(marker: Marker) {
        val target: TargetModel? = targetMarkerMap[marker.id]
        target?.run {
            targetActivityViewModel.showTargetInformation(target)
            selectTargetMarker(target)
            centerMarker(marker)
        }
    }

    private fun centerMarker(marker: Marker){
        val projection: Projection = map.projection
        val markerLatLng = LatLng(
            marker.position.latitude,
            marker.position.longitude
        )
        val markerScreenPosition: Point = projection.toScreenLocation(markerLatLng)
        val pointHalfScreenAbove = Point(
            markerScreenPosition.x,
            markerScreenPosition.y + markerScreenPosition.y / 2
        )

        val aboveMarkerLatLng: LatLng = projection
            .fromScreenLocation(pointHalfScreenAbove)

        val center = CameraUpdateFactory.newLatLng(aboveMarkerLatLng)
        map.moveCamera(center)
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


    private fun observeTargets() {
        targetActivityViewModel.targets.observe(
            viewLifecycleOwner, Observer {
            it.forEach { target ->
                addTargetMarker(target)
            }
        })
    }

    private fun observeNewTargets() {
        targetActivityViewModel.newTarget.observe(viewLifecycleOwner, Observer {
            addTargetMarker(it)
        })
        targetActivityViewModel.newMatch.observe(viewLifecycleOwner, Observer {

        })
        targetActivityViewModel.newConversation.observe(viewLifecycleOwner, Observer {

        })
    }

    private fun observeDeletedTargets() {
        targetActivityViewModel.deletedTarget.observe(viewLifecycleOwner, Observer { target ->
            targetModelMap[target]?.run {
                remove()
                targetMarkerMap.remove(id)
                targetModelMap.remove(target)
            }
        })
    }

    private fun addTargetMarker(target: TargetModel) {
        val position = LatLng(target.lat, target.lng)
        lifecycleScope.launch {
            target.topic?.run {
                val marker = map.addMarker(
                    MarkerOptions()
                        .position(position)
                        .icon(
                            bitmapDescriptorWithOvalBackground(
                                ContextCompat.getDrawable(requireContext(), R.drawable.ic_oval_marker_bg),
                                ContextCompat.getDrawable(requireContext(), getTargetIcon())
                            )
                        )
                )
                targetModelMap[target] = marker
                targetMarkerMap[marker.id] = target
            }
        }
    }

    private suspend fun bitmapDescriptorWithOvalBackground(
        background: Drawable?,
        iconVectorDrawable: Drawable?
    ): BitmapDescriptor? {

        return withContext(Dispatchers.IO) {

            background?.let { back ->
                val backgroundWidth = back.intrinsicWidth
                val backgroundHeight = back.intrinsicHeight

                iconVectorDrawable?.let { icon ->
                    val drawableWidth = icon.intrinsicWidth
                    val drawableHeight = icon.intrinsicHeight

                    back.setBounds(0, 0, backgroundWidth, backgroundHeight)

                    icon.setBounds(
                        0,
                        0,
                        drawableWidth,
                        drawableHeight
                    )

                    val bitmap = Bitmap.createBitmap(backgroundWidth,
                        backgroundHeight,
                        Bitmap.Config.ARGB_8888)

                    val canvas = Canvas(bitmap)
                    back.draw(canvas)

                    canvas.translate(
                        (backgroundWidth / 2 - drawableWidth / 2).toFloat(),
                        (backgroundHeight / 2 - drawableHeight / 2).toFloat()
                    )
                    icon.draw(canvas)

                    return@withContext BitmapDescriptorFactory.fromBitmap(bitmap)
                }
            } ?: return@withContext null
        }
    }

    private fun selectTargetMarker(target: TargetModel) {
        lifecycleScope.launch {
            selectedTarget?.let {
                setIcon(it, false)
            }
            target.let {
                selectedTarget = it
                setIcon(it, true)
            }
        }
    }

    private suspend fun setIcon(target: TargetModel, selected: Boolean){
        targetModelMap[target]?.setIcon(getMarkerDrawable(target,selected))
    }

    private suspend fun getMarkerDrawable(target: TargetModel, selected: Boolean) : BitmapDescriptor? {
        return bitmapDescriptorWithOvalBackground(
            ContextCompat.getDrawable(requireContext(),
                if (selected) R.drawable.ic_oval_marker_bg_light_blue
                else R.drawable.ic_oval_marker_bg),
            ContextCompat.getDrawable(requireContext(), target.topic.getTargetIcon())
        )
    }

    companion object {
        const val GOOGLE_MAPS_ZOOM = 11f
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
