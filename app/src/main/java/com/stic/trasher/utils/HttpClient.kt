package com.stic.trasher.utils

import com.stic.trasher.services.AuthServices
import okhttp3.OkHttpClient
import retrofit2.Retrofit


object HttpClient {

    private val authToken =
        "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhNTZhZ2RhaGFiZWVrYSIsImV4cCI6MTU3ODc1NzQwMywiaWF0IjoxNTc4NzM5NDAzfQ.7EoRw5Y3LHyaHQqu2uJ5VFiLTS0FFlZCqNpY5XftoBPdSsP5OIldG-MAFqIePMDSnsrA9JvEjUysj0GUvQP1cw"

    private val client: OkHttpClient =
        OkHttpClient
            .Builder()
            .addInterceptor { chain ->
                chain.run {
                    proceed(
                        request()
                            .newBuilder()
                            .addHeader("Authorization", authToken)
                            .addHeader("Content-Type", "application/json")
                            .build()
                    )
                }
            }
            .build()


    private val retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.43.186:3000/api")
        .client(client)
        .build()

    private val retrofitWithAuth = Retrofit.Builder()
        .baseUrl("http://192.168.43.186:3000/api")
        .client(client)
        .build()


    var authService = retrofit.create<AuthServices>(AuthServices::class.java)


}