package com.stic.trasher.utils

import android.app.Activity
import com.stic.trasher.services.*
import com.stic.trasher.utils.GsonUtil.gson
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object HttpClient {

    const val BASE_URL = "http://192.168.43.186:3000/api/"


    private fun clientWithAuthJson(activity: Activity): OkHttpClient =
        OkHttpClient
            .Builder()
            .addInterceptor { chain ->
                chain.proceed(
                    chain.request().newBuilder()
                        .addHeader("Authorization", SessionManager.getSessionToken(activity))
                        .addHeader("Content-Type", "application/json")
                        .build()
                )
            }
            .build()

    private fun clientWithAuthCustom(activity: Activity): OkHttpClient =
        OkHttpClient
            .Builder()
            .addInterceptor { chain ->
                chain.proceed(
                    chain.request().newBuilder()
                        .addHeader("Authorization", SessionManager.getSessionToken(activity))
                        .build()
                )
            }
            .build()

    private fun client(): OkHttpClient =
        OkHttpClient
            .Builder()
            .addInterceptor { chain ->
                chain.proceed(
                    chain.request().newBuilder()
                        .addHeader("Content-Type", "application/json")
                        .build()
                )
            }
            .build()


    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    private fun retrofitWithAuthCustom(activity: Activity) = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(clientWithAuthCustom(activity))
        .build()


    private fun retrofitWithAuthJson(activity: Activity) = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(clientWithAuthJson(activity))
        .build()


    var authService = retrofit.create(AuthService::class.java)


    fun challengeService(activity: Activity) =
        retrofitWithAuthJson(activity).create(ChallengeService::class.java)


    fun userService(activity: Activity) =
        retrofitWithAuthJson(activity).create(UserService::class.java)

    fun imagesService(activity: Activity) =
        retrofitWithAuthCustom(activity).create(ImageService::class.java)

    fun commentsService(activity: Activity) =
        retrofitWithAuthJson(activity).create(CommentService::class.java)

    fun photosService(activity: Activity) =
        retrofitWithAuthJson(activity).create(PhotoService::class.java)

    fun participantService(activity: Activity) =
        retrofitWithAuthJson(activity).create(ParticipantService::class.java)

}
