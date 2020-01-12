package com.stic.trasher.utils

import android.app.Activity
import com.stic.trasher.services.AuthServices
import com.stic.trasher.services.UserServices
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object HttpClient {

    private fun client(activity: Activity): OkHttpClient =
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


    private val retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.43.186:3000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private fun retrofitWithAuth(activity: Activity) = Retrofit.Builder()
        .baseUrl("http://192.168.43.186:3000/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client(activity))
        .build()


    var authService = retrofit.create(AuthServices::class.java)

    fun userServices(activity: Activity) =
        retrofitWithAuth(activity).create(UserServices::class.java)
}
