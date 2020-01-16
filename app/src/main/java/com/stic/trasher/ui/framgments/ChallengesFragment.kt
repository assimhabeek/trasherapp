package com.stic.trasher.ui.framgments

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.github.ybq.android.spinkit.SpinKitView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.stic.trasher.R
import com.stic.trasher.adapters.ChallengesTabAdapter
import com.stic.trasher.controll.ChallengeViewPager
import com.stic.trasher.utils.HttpClient
import dz.stic.model.Challenge
import retrofit2.Call
import retrofit2.Response


class ChallengesFragment : Fragment() {

    private lateinit var root: View
    private lateinit var refreshButton: FloatingActionButton
    private lateinit var progressBar: SpinKitView
    private lateinit var mapFragment: ChallengesMapFragment
    private lateinit var listFragment: ChallengesListFragment
    private lateinit var viewPager: ChallengeViewPager
    private lateinit var tabLayout: TabLayout
    private var adapter: ChallengesTabAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        root = inflater.inflate(R.layout.framgment_challenges, container, false)
        refreshButton = root.findViewById(R.id.refresh_challenge_button)
        progressBar = root.findViewById(R.id.loadingChallenges)
        viewPager = root.findViewById(R.id.viewPager)
        tabLayout = root.findViewById(R.id.tabLayout)


        refreshButton.setOnClickListener { loadChallenges() }


        loadChallenges()

        return root
    }

    fun createViewPager() {
        adapter = fragmentManager?.let { ChallengesTabAdapter(it) }

        adapter?.addFragment(listFragment, resources.getString(R.string.list))
        adapter?.addFragment(mapFragment, resources.getString(R.string.map))
        viewPager.adapter = adapter
        viewPager.setPagingEnabled(false)
        tabLayout.setupWithViewPager(viewPager)
        tabLayout.getTabAt(0)?.setIcon(R.drawable.ic_list_24px)
        tabLayout.getTabAt(1)?.setIcon(R.drawable.ic_street_map)

    }

    fun loadChallenges() {
        refreshButton.isEnabled = false
        progressBar.isVisible = true
        if (activity != null) {
            HttpClient.challengeService(activity as Activity)
                .findAll()
                .enqueue(object : retrofit2.Callback<ArrayList<Challenge>> {

                    override fun onFailure(call: Call<ArrayList<Challenge>>, t: Throwable) {
                        Toast.makeText(activity, t.message, Toast.LENGTH_LONG)
                            .show()
                    }

                    override fun onResponse(
                        call: Call<ArrayList<Challenge>>,
                        response: Response<ArrayList<Challenge>>
                    ) {
                        if (response.code() == 200) {
                            val challenges = response.body()
                            if (challenges != null) {
                                refreshButton.isEnabled = true
                                progressBar.isVisible = false

                                mapFragment = ChallengesMapFragment()
                                listFragment = ChallengesListFragment()
                                val b = Bundle()
                                b.putSerializable("challenges", challenges)
                                listFragment.arguments = b
                                mapFragment.arguments = b

                            }
                        } else {
                            Toast.makeText(activity, response.message(), Toast.LENGTH_LONG)
                                .show()
                        }

                        createViewPager()
                    }

                })
        }
    }

}


