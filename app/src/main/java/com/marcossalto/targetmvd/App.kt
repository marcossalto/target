package com.marcossalto.targetmvd

import android.app.Application
import com.marcossalto.targetmvd.util.Prefs
import com.squareup.otto.Bus

val bus: Bus by lazy {
    App.bus!!
}

val prefs: Prefs by lazy {
    App.prefs!!
}

class App : Application() {

    companion object {
        var bus: Bus? = null
        var prefs: Prefs? = null
    }

    override fun onCreate() {
        super.onCreate()

        bus = Bus()
        prefs = Prefs(applicationContext)
    }
}