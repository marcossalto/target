package com.marcossalto.targetmvd.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.marcossalto.targetmvd.R
import com.marcossalto.targetmvd.network.providers.ServiceProvider
import com.marcossalto.targetmvd.network.services.ApiService
import com.marcossalto.targetmvd.network.util.toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class SplashActivity : AppCompatActivity() {
    private var service = ServiceProvider.create(ApiService::class.java)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        GlobalScope.launch(Dispatchers.Main) {
            val server = service.checkServerStatus()
            toast(server.online.toString())
        }
    }
}