package com.stic.trasher.services

import dz.stic.model.Client
import retrofit2.Call
import retrofit2.http.GET

interface UserServices {

    @GET("auth/me")
    fun me(): Call<Client>


}
