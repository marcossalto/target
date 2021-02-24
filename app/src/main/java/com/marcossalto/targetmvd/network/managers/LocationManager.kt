package com.marcossalto.targetmvd.network.managers

import android.content.Context
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

object LocationManager : ILocationManager {

    private var fusedLocationProviderClient: FusedLocationProviderClient? = null

    override fun saveLocation(lat: Double, lng: Double) {
        UserLocation.latitude = lat
        UserLocation.longitude = lng
        UserLocation.locationState = LocationState.SUCCESS
    }

    override fun isStateSuccess(): Boolean {
        return UserLocation.locationState == LocationState.SUCCESS
    }

    override fun getLatitude(): Double = UserLocation.latitude

    override fun getLongitude(): Double = UserLocation.longitude

    override fun getLocation(context: Context, successActionCallback: (location: Location) -> Unit) {
        if (fusedLocationProviderClient == null) {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        }
        try {
            fusedLocationProviderClient?.lastLocation?.addOnSuccessListener { location ->
                location?.run {
                    saveLocation(lat = latitude, lng = longitude)
                    successActionCallback(location)
                }
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }
}

object UserLocation {
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    var locationState: LocationState = LocationState.NONE
}

enum class LocationState {
    FAILURE,
    SUCCESS,
    NONE
}
