package com.stic.trasher.services

import dz.stic.model.Challenge
import dz.stic.model.Comment
import dz.stic.model.Participant
import dz.stic.model.Photo
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ChallengeService {

    @GET("challenges/")
    fun findAll(): Call<ArrayList<Challenge>>

    @GET("challenges/{id}")
    fun findById(@Path("id") user: Int): Call<Challenge>

    @POST("challenges/")
    fun create(@Body challenge: Challenge): Call<Challenge>

    @GET("challenges/{id}/photos")
    fun findPhotos(@Path("id") user: Int): Call<ArrayList<Photo>>

    @GET("challenges/{id}/comments")
    fun findComments(@Path("id") user: Int): Call<ArrayList<Comment>>

    @GET("challenges/{id}/participants")
    fun findParticipants(@Path("id") user: Int): Call<ArrayList<Participant>>

}
