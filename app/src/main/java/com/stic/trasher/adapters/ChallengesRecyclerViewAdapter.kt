package com.stic.trasher.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.stic.trasher.R
import dz.stic.model.Address
import dz.stic.model.Challenge
import dz.stic.model.Client


class ChallengesRecyclerViewAdapter(
    private val challenges: ArrayList<Challenge>,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<ChallengesRecyclerViewAdapter.ChallengeViewHolder>() {


    interface OnItemClickListener{
        fun onItemClicked(challenge: Challenge)
    }

    class ChallengeViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val address: TextView = view.findViewById(R.id.challenge_address)
        val publisher: TextView = view.findViewById(R.id.challenge_publisher)

        fun bind(challenge: Challenge, clickListener: OnItemClickListener) {
            view.setOnClickListener {
                clickListener.onItemClicked(challenge)
            }
        }

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

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ChallengeViewHolder, position: Int) {
        val address: Address = challenges[position].getrAddress()
        val publisher: Client = challenges[position].getrOwner()
        holder.address.text = "${address.street}, ${address.city}, ${address.country}"
        holder.publisher.text = "${publisher.lastName} ${publisher.firstName}"

        holder.bind(challenges[position], itemClickListener)

    }


}