package com.stic.trasher.utils

import android.app.Activity
import com.stic.trasher.services.AuthService
import com.stic.trasher.services.ChallengeService
import com.stic.trasher.services.UserService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.google.gson.Gson
import java.sql.Date


object HttpClient {

    private fun clientWithAuth(activity: Activity): OkHttpClient =
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

    var gson = GsonBuilder()
        .registerTypeAdapter(Date::class.java, GsonDateAdapter())
        .serializeNulls()
        .create()

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.43.186:3000/api/")
        .client(client())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    private fun retrofitWithAuth(activity: Activity) = Retrofit.Builder()
        .baseUrl("http://192.168.43.186:3000/api/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(clientWithAuth(activity))
        .build()


    var authService = retrofit.create(AuthService::class.java)

    fun userService(activity: Activity) =
        retrofitWithAuth(activity).create(UserService::class.java)

    fun challengeService(activity: Activity) =
        retrofitWithAuth(activity).create(ChallengeService::class.java)

}
