package com.stic.trasher.services

import dz.stic.model.Photo
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface PhotoService {

    @POST("photos/")
    fun createPhoto(@Body photo: Photo): Call<Photo>


}
