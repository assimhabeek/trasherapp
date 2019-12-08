package com.stic.trasher.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.stic.trasher.Challenge
import com.stic.trasher.R
import de.hdodenhof.circleimageview.CircleImageView


class ChallengesListAdapter(private val challenges: Array<Challenge>) :
    RecyclerView.Adapter<ChallengesListAdapter.ChallengeViewHolder>() {

    class ChallengeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.name)
        val address: TextView = view.findViewById(R.id.address)
        val image: CircleImageView = view.findViewById(R.id.challenge_avatar)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChallengeViewHolder {
        return ChallengeViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item_challenge,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return challenges.size
    }

    override fun onBindViewHolder(holder: ChallengeViewHolder, position: Int) {
        holder.name.text = challenges[position].name
        holder.address.text = challenges[position].address
        holder.image.setImageResource(challenges[position].image)
    }

}