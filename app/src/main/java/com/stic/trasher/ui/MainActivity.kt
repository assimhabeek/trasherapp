package com.stic.trasher.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.stic.trasher.R
import com.stic.trasher.ui.framgments.ChallengesFragment
import com.stic.trasher.utils.HttpClient
import com.stic.trasher.utils.PermissionManager
import com.stic.trasher.utils.SessionManager
import dz.stic.model.Client


class MainActivity : AppCompatActivity() {

    private lateinit var mDrawer: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var drawerToggle: ActionBarDrawerToggle
    private lateinit var client: Client
    private lateinit var userEmail: TextView
    private lateinit var userFullName: TextView
    private lateinit var navHeader: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // AndroidGraphicFactory.createInstance(this.getApplication());

        PermissionManager.redirectIfPermissionsMessing(this)
        SessionManager.redirectIfNotLoggedIn(this)

        bindElements()
        setupToolbar()
        setupDrawerContent()

        loadClientInfo()

    }


    @SuppressLint("SetTextI18n")
    private fun loadClientInfo() {
        Thread(Runnable {
            try {
                val response = HttpClient.userService(this).me().execute()
                if (response.code() == 200) {
                    client = response.body()!!
                    userFullName.text = "${client.lastName} ${client.firstName}"
                    userEmail.text = client.email
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }).start()
    }


    private fun bindElements() {
        mDrawer = findViewById(R.id.mDrawer)
        navView = findViewById(R.id.nav_view)
        navHeader = navView.inflateHeaderView(R.layout.nav_header)
        userFullName = navHeader.findViewById(R.id.user_fullName)
        userEmail = navHeader.findViewById(R.id.user_email)
    }


    private fun setupToolbar() {

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        drawerToggle = ActionBarDrawerToggle(
            this,
            mDrawer,
            R.string.drawer_open,
            R.string.drawer_close
        )

        mDrawer.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

    }

    private fun setupDrawerContent() {

        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_challenges_fragment -> {
                    displayFragment(ChallengesFragment())
                    it.isChecked = true
                    title = it.title;
                    mDrawer.closeDrawers()
                }
                R.id.nav_logout -> SessionManager.logout(this)
                else -> Fragment()
            }


            true
        }
    }

    private fun displayFragment(fragment: Fragment){
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame_content, fragment)
            .commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return if (drawerToggle.onOptionsItemSelected(item)) true else super.onOptionsItemSelected(
            item
        )

    }

}
