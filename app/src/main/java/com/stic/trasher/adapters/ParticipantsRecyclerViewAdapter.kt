package com.stic.trasher.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.stic.trasher.R
import com.stic.trasher.utils.BitmapUtiles
import de.hdodenhof.circleimageview.CircleImageView
import dz.stic.model.Participant
import dz.stic.model.Client


class ParticipantsRecyclerViewAdapter(private val participants: ArrayList<Participant>) :
    RecyclerView.Adapter<ParticipantsRecyclerViewAdapter.ParticipantViewHolder>() {


    class ParticipantViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val participant: TextView = view.findViewById(R.id.participant)
        val image: CircleImageView = view.findViewById(R.id.participant_avatar)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParticipantViewHolder {
        return ParticipantViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item_participant,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return participants.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ParticipantViewHolder, position: Int) {
        val publisher: Client = participants[position].client
        holder.participant.text = "${publisher.lastName} ${publisher.firstName}"
        if (participants[position].client.photo != null)
            BitmapUtiles.displayCircleImage(
                holder.image,
                participants[position].client.photo,
                48,
                48
            )
    }


}