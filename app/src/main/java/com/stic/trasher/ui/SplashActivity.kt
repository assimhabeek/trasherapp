package com.stic.trasher.ui

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.widget.ProgressBar
import androidx.core.app.ActivityCompat
import com.github.ybq.android.spinkit.style.Wave
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.stic.trasher.utils.PermissionManager.APP_PERMISSIONS
import com.stic.trasher.utils.PermissionManager.hasPermission
import com.stic.trasher.utils.PermissionManager.isAllGranted
import com.stic.trasher.R


class SplashActivity : Activity() {


    companion object {
        private const val SPLASH_TIMEOUT: Long = 1000

        private const val PERMISSIONS_REQUEST_CODE = 40

    }


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        initProgressBar()


        // Check for app permissions
        // In case one or more permissions are not granted,
        // ActivityCompat.requestPermissions() will request permissions
        // and the control goes to onRequestPermissionsResult() callback method.
        if (checkAndRequestPermissions()) {
            // All permissions are granted already. Proceed ahead
            moveToLoginActivity()
        }

        // The else case isn't required. The checkAndRequestPermissions() will control the flow

    }


    private fun initProgressBar() {

        val progressBar = findViewById<ProgressBar>(R.id.spin_kit)

        val wave = Wave()

        progressBar.indeterminateDrawable = wave

    }


    private fun checkAndRequestPermissions(): Boolean =
        if (isAllGranted(this))
            true
        else {
            askForUnGrantedPermissions()
            false
        }


    private fun askForUnGrantedPermissions() {
        ActivityCompat.requestPermissions(
            this,
            APP_PERMISSIONS.filter { !hasPermission(this, it) }.toTypedArray(),
            PERMISSIONS_REQUEST_CODE
        )
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            val deniedPermissions =
                grantResults.zip(permissions)
                    .filter { it.first == PackageManager.PERMISSION_DENIED }

            when {

                // all granted
                deniedPermissions.isEmpty() -> moveToLoginActivity()


                // some are not granted
                shouldDisplayExplanation(deniedPermissions) ->
                    MaterialAlertDialogBuilder(this)
                        .setTitle(R.string.required_permission)
                        .setCancelable(false)
                        .setPositiveButton(R.string.try_again) { d, _ ->
                            d.dismiss()
                            askForUnGrantedPermissions()
                        }
                        .setMessage(R.string.please_grant_required_permissions)
                        .show()


                // never ask is checked
                else -> {
                    MaterialAlertDialogBuilder(this)
                        .setCancelable(false)
                        .setTitle(R.string.required_permission)
                        .setPositiveButton(R.string.ok) { d, _ ->
                            d.dismiss()
                            val intent = Intent(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", packageName, null)
                            ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                            finish()
                        }
                        .setMessage(R.string.go_to_setting_required_permissions)
                        .show()
                }
            }
        }
    }

    private fun shouldDisplayExplanation(deniedPermissions: List<Pair<Int, String>>) =
        deniedPermissions.all {
            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                it.second
            )
        }


    private fun moveToLoginActivity() {

        Handler().postDelayed({

            val intent = Intent(this, LoginActivity::class.java)

            startActivity(intent)

            finish()

        }, SPLASH_TIMEOUT)

    }


}
