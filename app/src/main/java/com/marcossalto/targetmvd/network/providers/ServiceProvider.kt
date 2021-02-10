package com.marcossalto.targetmvd.network.providers

import com.marcossalto.targetmvd.BuildConfig
import com.marcossalto.targetmvd.network.services.HeadersInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object ServiceProvider {

    private var URL_API: String? = null

    private fun build(): Retrofit {
        val client = OkHttpClient.Builder()
            .addInterceptor(HeadersInterceptor())
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
            .build()

        return Retrofit.Builder()
            .baseUrl(URL_API)
            .addConverterFactory(MoshiConverterFactory.create().withNullSerialization())
            .client(client)
            .build()
    }

    fun <T> create(klass: Class<T>, url: String? = BuildConfig.API_URL): T {
        URL_API = url
        return build().create(klass)
    }
}