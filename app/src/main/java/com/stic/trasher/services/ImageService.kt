package com.stic.trasher.services

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ImageService {

    @Headers(
        "Content-type: images/jpeg"
    )
    @GET("image/download/{name}")
    fun download(@Path("name") name: String): Call<ResponseBody>

    @Multipart
    @POST("image/upload/")
    fun upload(@Part image: MultipartBody.Part): Call<ResponseBody>


}
