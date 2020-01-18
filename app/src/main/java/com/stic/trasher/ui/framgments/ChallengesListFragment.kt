package com.stic.trasher.ui.framgments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.stic.trasher.ui.ChallengeActivity
import com.stic.trasher.R
import com.stic.trasher.adapters.ChallengesRecyclerViewAdapter
import dz.stic.model.Challenge

class ChallengesListFragment : Fragment() {


    private var challenges: ArrayList<Challenge> = ArrayList()
    private lateinit var recyclerViewAdapter: ChallengesRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(R.layout.framgment_challenges_list, container, false)

        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view)
        recyclerViewAdapter = ChallengesRecyclerViewAdapter(challenges,
            object : ChallengesRecyclerViewAdapter.OnItemClickListener {
                override fun onItemClicked(challenge: Challenge) {
                    val intent = Intent(activity, ChallengeActivity::class.java)
                    intent.putExtra("challengeId", challenge.id)
                    startActivity(intent)
                }
            })
        val rlm: RecyclerView.LayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = rlm
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = recyclerViewAdapter


        updateChallengesList()
        return view
    }

    fun updateChallengesList() {
        challenges.addAll(arguments?.getSerializable("challenges") as ArrayList<Challenge>)
        recyclerViewAdapter.notifyDataSetChanged()
    }

}


