package com.stic.trasher.services

import dz.stic.model.Challenge
import dz.stic.model.Client
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET

interface ChallengeServices {

    @GET("/api/challenges")
    fun getAllChallenges(@Body user: Client): Call<List<Challenge>>

}
