package com.marcossalto.targetmvd.network.managers

import android.content.Context
import android.location.Location

interface ILocationManager {
    fun saveLocation(lat: Double, lng: Double)
    fun isStateSuccess(): Boolean
    fun getLatitude(): Double
    fun getLongitude(): Double
    fun getLocation(context: Context, successActionCallback: (location: Location) -> Unit)
}
