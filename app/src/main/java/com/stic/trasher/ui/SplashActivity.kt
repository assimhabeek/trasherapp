package com.stic.trasher.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.ProgressBar
import com.github.ybq.android.spinkit.style.Wave
import com.stic.trasher.R


class SplashActivity : Activity() {

    companion object {
        private const val SPLASH_TIMEOUT: Long = 3000
    }


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        initProgressBar()
        moveToPermissionsActivity()
    }


    private fun initProgressBar() {

        val progressBar = findViewById<ProgressBar>(R.id.spin_kit)

        val wave = Wave()

        progressBar.indeterminateDrawable = wave


    }

    private fun moveToPermissionsActivity() {

        Handler().postDelayed({

            val intent = Intent(this, LoginActivity::class.java)

            startActivity(intent)

            finish()

        }, SPLASH_TIMEOUT)


    }


}
