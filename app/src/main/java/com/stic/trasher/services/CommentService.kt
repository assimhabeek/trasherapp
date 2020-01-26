package com.stic.trasher.services

import dz.stic.model.Comment
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface CommentService {

    @POST("comments/")
    fun createComment(@Body comment: Comment): Call<Comment>


}
