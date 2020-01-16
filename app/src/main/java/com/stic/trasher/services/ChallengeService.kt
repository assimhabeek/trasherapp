package com.stic.trasher.services

import dz.stic.model.Challenge
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ChallengeService {

    @GET("challenges/")
    fun findAll(): Call<ArrayList<Challenge>>

    @GET("challenges/{id}")
    fun findById(@Path("id") user: Int): Call<Challenge>

}
