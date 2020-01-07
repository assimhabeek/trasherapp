package com.stic.trasher.ui.framgments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.ybq.android.spinkit.SpinKitView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.koushikdutta.ion.Ion
import com.stic.trasher.R
import com.stic.trasher.adapters.ChallengesTabAdapter
import com.stic.trasher.controll.ChallengeViewPager
import android.R.id
import android.widget.ProgressBar
import androidx.core.view.isVisible


class ChallengesFragment : Fragment() {

    private lateinit var ctx: Context
    private lateinit var root: View
    private lateinit var refreshButton: FloatingActionButton
    private lateinit var progressBar: SpinKitView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        root = inflater.inflate(R.layout.framgment_challenges, container, false)
        refreshButton = root.findViewById(R.id.refresh_challenge_button)
        progressBar = root.findViewById<SpinKitView>(R.id.loadingChallenges)


        loadChallenges()

        refreshButton.setOnClickListener {
            loadChallenges()
        }

        return root
    }


    fun loadChallenges() {
        refreshButton.isEnabled = false
        progressBar.isVisible = true

        Ion.with(context)
            .load("${resources.getString(R.string.base_url)}${resources.getString(R.string.challenges_url)}")
            .asString()
            .withResponse()
            .setCallback { e, response ->
                println(e)

                if (response != null && response.headers.code() == 200 && response.result != null) {
                    createViewPager(response.result)
                } else {
                    // get From local database
                    createViewPager("")
                }
                refreshButton.isEnabled = true
                progressBar.isVisible = false
            }
    }

    fun createViewPager(challenges: String) {
        val viewPager: ChallengeViewPager = root.findViewById(R.id.viewPager)
        val tabLayout: TabLayout = root.findViewById(R.id.tabLayout)
        val adapter = fragmentManager?.let { ChallengesTabAdapter(it) }

        adapter?.addFragment(
            ChallengesListFragment.newInstance(challenges),
            resources.getString(R.string.list)
        )

        adapter?.addFragment(
            ChallengesMapFragment.newInstance(challenges),
            resources.getString(R.string.map)
        )

        adapter?.addFragment(
            ChallengesCommentFragment(),
            resources.getString(R.string.comments)
        )
        viewPager.adapter = adapter
        viewPager.setPagingEnabled(false)
        tabLayout.setupWithViewPager(viewPager)
        tabLayout.getTabAt(0)?.setIcon(R.drawable.ic_list_24px)
        tabLayout.getTabAt(1)?.setIcon(R.drawable.ic_street_map)
        tabLayout.getTabAt(2)?.setIcon(R.drawable.ic_mode_comment_24px)
    }
}


