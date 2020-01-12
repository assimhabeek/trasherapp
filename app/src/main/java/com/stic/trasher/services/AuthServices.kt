package com.stic.trasher.services

import com.stic.trasher.utils.JWtRequest
import dz.stic.model.Client
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthServices {

    @POST("auth/login")
    fun login(@Body user: JWtRequest): Call<String>

    @POST("auth/register")
    fun register(@Body user: Client): Call<Boolean>

}
