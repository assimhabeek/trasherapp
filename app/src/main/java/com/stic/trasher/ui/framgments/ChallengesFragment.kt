package com.stic.trasher.ui.framgments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.stic.trasher.R
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.stic.trasher.adapters.ChallengesTabAdapter


class ChallengesFragment : Fragment() {

    private lateinit var ctx: Context

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root: View = inflater.inflate(R.layout.framgment_challenges, container, false)
        val viewPager: ViewPager = root.findViewById(R.id.viewPager)
        val tabLayout: TabLayout = root.findViewById(R.id.tabLayout)

        val adapter = fragmentManager?.let { ChallengesTabAdapter(it) }
        adapter?.addFragment(ChallengesListFragment(), resources.getString(R.string.list))
        adapter?.addFragment(ChallengesMapFragment(), resources.getString(R.string.map))
        adapter?.addFragment(ChallengesCommentFragment(), resources.getString(R.string.comments))
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)

        tabLayout.getTabAt(0)?.setIcon(R.drawable.ic_list_24px)
        tabLayout.getTabAt(1)?.setIcon(R.drawable.ic_street_map)
        tabLayout.getTabAt(2)?.setIcon(R.drawable.ic_mode_comment_24px)

        return root
    }

}


