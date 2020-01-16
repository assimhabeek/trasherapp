package com.stic.trasher.services

import dz.stic.model.Challenge
import retrofit2.Call
import retrofit2.http.GET

interface ChallengeService {

    @GET("/api/challenges")
    fun getAllChallenges(): Call<ArrayList<Challenge>>

}
