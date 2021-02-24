package com.marcossalto.targetmvd.ui.target

import android.content.Context
import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.marcossalto.targetmvd.network.managers.ILocationManager
import com.marcossalto.targetmvd.network.managers.LocationManager
import com.marcossalto.targetmvd.ui.base.BaseViewModel

class TargetActivityViewModel(
        private val locationManager: ILocationManager
    ) : BaseViewModel(null)  {

    fun getLocation(context: Context, successAction: (location: Location) -> Unit) {
        locationManager.getLocation(context, successAction)
    }
}

class TargetActivityViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TargetActivityViewModel(LocationManager)  as T
    }
}