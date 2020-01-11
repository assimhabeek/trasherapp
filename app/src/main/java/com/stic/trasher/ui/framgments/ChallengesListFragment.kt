package com.stic.trasher.ui.framgments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.stic.trasher.R
import com.stic.trasher.adapters.ChallengesRecyclerViewAdapter
import dz.stic.model.Challenge

class ChallengesListFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(R.layout.framgment_challenges_list, container, false)

        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view)
/*
        val challenges: ArrayList<Challenge> =
            GsonUtil.fromJsonToChallege(arguments?.getString("challenges"))
*/


        val challenges = arguments?.getSerializable("challenges")

        val recyclerViewAdapter =
            ChallengesRecyclerViewAdapter(challenges as ArrayList<Challenge>)

        val rlm: RecyclerView.LayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = rlm
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = recyclerViewAdapter
        recyclerViewAdapter.notifyDataSetChanged()

        return view
    }

    companion object {
        fun newInstance(challenges: ArrayList<Challenge>): ChallengesListFragment {
            val bundle = Bundle()
            val fragment = ChallengesListFragment()
            bundle.putSerializable("challenges", challenges)
            fragment.arguments = bundle
            return fragment
        }
    }
}


