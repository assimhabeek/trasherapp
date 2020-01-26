package com.stic.trasher.services

import dz.stic.model.Participant
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Path

interface ParticipantService {

    @POST("participants/")
    fun createParticipant(@Body participant: Participant): Call<Participant>


    @DELETE("participants/{id}")
    fun deleteParticipant(@Path("id") id: Int): Call<Participant>

}
