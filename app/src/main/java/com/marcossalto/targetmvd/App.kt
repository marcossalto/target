package com.marcossalto.targetmvd

import android.app.Application
import com.squareup.otto.Bus

val bus: Bus by lazy {
    App.bus!!
}

class App : Application() {

    companion object {
        var bus: Bus? = null
    }

    override fun onCreate() {
        super.onCreate()

        bus = Bus()
    }
}