package com.stic.trasher.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.stic.trasher.ui.LoginActivity
import com.stic.trasher.ui.MainActivity
import dz.stic.model.Client

object SessionManager {

    private const val PERF_NAME = "USER_PREF"
    private const val TOKEN_KEY = "token"


    fun registerUserToken(activity: Activity, token: String) {
        val sharedPref = activity.getSharedPreferences(PERF_NAME, Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putString(TOKEN_KEY, token)
            commit()
        }
    }


    fun registerUser(activity: Activity, client: Client) {
        val sharedPref = activity.getSharedPreferences(PERF_NAME, Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putString("client", GsonUtil.gson.toJson(client))
            commit()
        }
    }

    fun getUser(activity: Activity): Client? {
        val sharedPref = activity.getSharedPreferences(PERF_NAME, Context.MODE_PRIVATE)
        return GsonUtil.gson.fromJson(sharedPref.getString("client", ""), Client::class.java)
    }


    fun logout(activity: Activity) {
        val sharedPref = activity.getSharedPreferences(PERF_NAME, Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            remove(TOKEN_KEY)
            commit()
        }
        redirectIfNotLoggedIn(activity)
    }

    fun getSessionToken(activity: Activity): String {
        val sharedPref = activity.getSharedPreferences(PERF_NAME, Context.MODE_PRIVATE)
        return sharedPref.getString(TOKEN_KEY, "").orEmpty()
    }

    fun redirectIfLoggedIn(activity: Activity) {
        if (isUserLoggedIn(activity)) {
            activity.startActivity(Intent(activity, MainActivity::class.java))
            activity.finish()
        }
    }

    fun redirectIfNotLoggedIn(activity: Activity) {
        if (!isUserLoggedIn(activity)) {
            activity.startActivity(Intent(activity, LoginActivity::class.java))
            activity.finish()
        }
    }

    private fun isUserLoggedIn(activity: Activity): Boolean {
        val sharedPref = activity.getSharedPreferences(PERF_NAME, Context.MODE_PRIVATE)
        return !sharedPref.getString(TOKEN_KEY, "").isNullOrEmpty()
    }


}