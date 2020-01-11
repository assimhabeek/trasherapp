package com.stic.trasher.services

import dz.stic.model.Client
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthServices {

    @POST("auth/login")
    fun login(@Body user: Client): Call<JSONObject>

    @POST("auth/register")
    fun register(@Body user: Client): Call<Boolean>

}
