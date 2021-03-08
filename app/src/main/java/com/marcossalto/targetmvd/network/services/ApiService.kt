package com.marcossalto.targetmvd.network.services

import com.marcossalto.targetmvd.network.models.*
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("status")
    suspend fun checkServerStatus(): ServerStatusSerializer

    @POST("users/")
    fun signUp(@Body user: UserSignUpSerializer): Call<User>

    @POST("users/sign_in")
    fun signIn(@Body user: UserSignInSerializer): Call<UserSignInResponseSerializer>

    @DELETE("users/sign_out")
    fun signOut(): Call<Void>

    @POST("users/facebook")
    fun login(@Body accessTokenSerializer: AccessTokenSerializer): Call<UserSignInResponseSerializer>

    @GET("topics")
    fun getTopics(): Call<TopicsSerializer>

    @GET("targets")
    fun getTargets(): Call<TargetsSerializer>

    @POST("targets")
    fun createTarget(@Body target: TargetSerializer): Call<TargetSerializer>

    @DELETE("targets/{id}")
    fun deleteTarget(@Path("id") id: Long): Call<Void>
}
