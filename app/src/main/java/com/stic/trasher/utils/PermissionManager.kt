package com.stic.trasher.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.stic.trasher.ui.SplashActivity

object PermissionManager {
    // List of all permissions for the app
    @SuppressLint("InlinedApi")
    val APP_PERMISSIONS = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.CAMERA
    )


    fun redirectIfPermissionsMessing(activity: Activity) {
        if (!isAllGranted(activity)) {
            activity.startActivity(Intent(activity, SplashActivity::class.java))
            activity.finish()
        }
    }


    fun isAllGranted(ctx: Context): Boolean = APP_PERMISSIONS.all {
        hasPermission(
            ctx,
            it
        )
    }


    fun hasPermission(ctx: Context, perm: String): Boolean =
        ContextCompat.checkSelfPermission(ctx, perm) == PackageManager.PERMISSION_GRANTED

}