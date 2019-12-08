package com.stic.trasher.ui.framgments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.stic.trasher.Challenge
import com.stic.trasher.R
import com.stic.trasher.adapters.ChallengesListAdapter

class ChallengesListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(R.layout.framgment_challenges_list, container, false)

        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view)

        val challenges: Array<Challenge> = arrayOf(
            Challenge(
                1,
                "Challenge 01",
                "Address of challenge 01",
                R.drawable.ic_email_24px
            ),
            Challenge(
                2,
                "Challenge 02",
                "Address of challenge 02",
                R.drawable.ic_account_circle_24px
            ),
            Challenge(
                3,
                "Challenge 03",
                "Address of challenge 03",
                R.drawable.ic_local_phone_24px
            ),
            Challenge(
                4,
                "Challenge 04",
                "Address of challenge 04",
                R.drawable.ic_lock_24px
            ),
            Challenge(
                5,
                "Challenge 05",
                "Address of challenge 05",
                R.drawable.ic_email_24px
            )
        )

        val recyclerViewAdapter = ChallengesListAdapter(challenges)
        val rlm: RecyclerView.LayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = rlm
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = recyclerViewAdapter
        recyclerViewAdapter.notifyDataSetChanged()

        return view
    }
}


