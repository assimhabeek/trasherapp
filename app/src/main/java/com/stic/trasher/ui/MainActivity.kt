package com.stic.trasher.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.stic.trasher.R
import com.stic.trasher.ui.framgments.ChallengesFragment


class MainActivity : AppCompatActivity() {

    private lateinit var mDrawer: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var drawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
       // AndroidGraphicFactory.createInstance(this.getApplication());

        bindElements()
        setupToolbar()
        setupDrawerContent()


    }


    private fun bindElements() {
        mDrawer = findViewById(R.id.mDrawer)
        navView = findViewById(R.id.nav_view)
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
            val fragment: Fragment = when (it.itemId) {
                R.id.nav_challenges_fragment -> ChallengesFragment()
                R.id.nav_second_fragment -> Fragment(R.layout.framgment_two)
                R.id.nav_third_fragment -> Fragment(R.layout.framgment_three)
                else -> Fragment(R.layout.framgment_challenges)
            }

            supportFragmentManager
                .beginTransaction()
                .replace(R.id.frame_content, fragment)
                .commit()

            it.isChecked = true


            title = it.title;

            mDrawer.closeDrawers()


            true
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return if (drawerToggle.onOptionsItemSelected(item)) true else super.onOptionsItemSelected(
            item
        )

    }

}
