package com.marcossalto.targetmvd.network.services

import com.marcossalto.targetmvd.network.models.ServerStatusSerializer
import retrofit2.http.GET

interface ApiService {
    @GET("status")
    suspend fun checkServerStatus(): ServerStatusSerializer
}