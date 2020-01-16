package com.stic.trasher

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.AsyncTask
import android.widget.Toast
import com.stic.trasher.utils.HttpClient
import dz.stic.model.Challenge
import java.lang.Exception


class ChallengeActivity : AppCompatActivity() {

    private lateinit var challenge: Challenge

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_challenge)
        val challengeId = intent.getIntExtra("challengeId", 0)
        loadChallenge(challengeId)
    }

    fun loadChallenge(challengeId:Int){
        Thread(Runnable {
            try {
               val responnse = HttpClient.challengeService(this).findById(challengeId).execute()
                if (responnse.code()==200){
                    print(responnse.body()!!.id)
                }
            }catch (e:Exception){
                e.printStackTrace()
            }

        })
    }
}
