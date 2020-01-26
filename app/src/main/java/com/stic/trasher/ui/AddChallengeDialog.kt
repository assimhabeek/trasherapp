package com.stic.trasher.ui

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import com.google.android.material.button.MaterialButton
import com.stic.trasher.R
import com.stic.trasher.utils.HttpClient
import com.stic.trasher.utils.SessionManager
import dz.stic.model.Address
import dz.stic.model.Challenge
import org.mapsforge.core.model.LatLong
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class AddChallengeDialog(private val ctx: Activity, private val cord: LatLong) : Dialog(ctx) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_add_challenge)

        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        val street = findViewById<EditText>(R.id.street)
        val city = findViewById<EditText>(R.id.city)
        val country = findViewById<EditText>(R.id.country)
        val startingDate = findViewById<EditText>(R.id.starting_date)
        val endingDate = findViewById<EditText>(R.id.starting_date)
        val save = findViewById<MaterialButton>(R.id.add_challenge)
        save.setOnClickListener {
            val starting = java.sql.Date(sdf.parse(startingDate.text.toString())?.time!!)
            val ending = java.sql.Date(sdf.parse(endingDate.text.toString())?.time!!)
            val challenge = Challenge(java.sql.Date(Date().time), 1, starting, ending, null)

            challenge.setrOwner(SessionManager.getUser(ctx))

            challenge.setrAddress(
                Address(
                    cord.latitude,
                    cord.longitude,
                    street.text.toString(),
                    city.text.toString(),
                    "25000",
                    country.text.toString()
                )
            )

            HttpClient.challengeService(ctx)
                .create(challenge)
                .enqueue(object : retrofit2.Callback<Challenge> {
                    override fun onFailure(call: Call<Challenge>, t: Throwable) {
                        t.printStackTrace()
                    }

                    override fun onResponse(call: Call<Challenge>, response: Response<Challenge>) {
                        if (response.code() == 200) {
                            dismiss()
                        }
                    }

                })
        }

    }

}