package com.stic.trasher.ui.framgments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.stic.trasher.R
import com.stic.trasher.adapters.ParticipantsRecyclerViewAdapter
import com.stic.trasher.utils.HttpClient
import com.stic.trasher.utils.SessionManager
import dz.stic.model.Challenge
import dz.stic.model.Client
import dz.stic.model.Participant
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ParticipantFragment : Fragment() {

    private var participants: ArrayList<Participant> = ArrayList()
    private lateinit var recyclerViewAdapter: ParticipantsRecyclerViewAdapter
    private lateinit var enroll: MaterialButton
    private lateinit var client: Client
    private var challenge: Challenge? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.framgment_participants, container, false)


        val recyclerView: RecyclerView = view.findViewById(R.id.participant_recycler_view)
        recyclerViewAdapter = ParticipantsRecyclerViewAdapter(participants)

        enroll = view.findViewById(R.id.enroll)
        client = SessionManager.getUser(activity!!)!!

        val rlm: RecyclerView.LayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = rlm
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = recyclerViewAdapter


        updateParticipantsList()


        return view
    }

    private fun isParticipating(): Boolean =
        participants.any { it.client.id == client.id }

    private fun toggleEnrollButtonText() {
        if (isParticipating()) {
            enroll.text = resources.getString(R.string.cancel)
        } else {
            enroll.text = resources.getString(R.string.enroll)
        }
    }

    fun updateParticipantsList() {
        challenge = arguments?.getSerializable("challenge") as Challenge

        if (activity != null && challenge != null) {
            HttpClient.challengeService(activity!!)
                .findParticipants(challenge!!.id)
                .enqueue(object : Callback<ArrayList<Participant>> {
                    override fun onFailure(call: Call<ArrayList<Participant>>, t: Throwable) {
                        t.printStackTrace()
                    }

                    override fun onResponse(
                        call: Call<ArrayList<Participant>>,
                        response: Response<ArrayList<Participant>>
                    ) {
                        if (response.code() == 200) {
                            participants.addAll(response.body()!!)
                            recyclerViewAdapter.notifyDataSetChanged()
                            toggleEnrollButtonText()

                            enroll.setOnClickListener {
                                if (isParticipating()) {
                                    deleteParticipant()
                                } else {
                                    createParticipant()
                                }
                            }

                        }
                    }

                })

        }

    }

    fun deleteParticipant() {
        val part = participants.find { it.client.id == client.id }
        if (part != null) {
            HttpClient.participantService(activity!!)
                .deleteParticipant(part.id)
                .enqueue(object : Callback<Participant> {
                    override fun onFailure(call: Call<Participant>, t: Throwable) {
                        t.printStackTrace()
                    }

                    override fun onResponse(call: Call<Participant>, response: Response<Participant>) {
                        if (response.code() == 200) {
                            participants.remove(participants.find { it.id == part.id }!!)
                            recyclerViewAdapter.notifyDataSetChanged()
                            toggleEnrollButtonText()
                        }
                    }

                })
        }
    }

    fun createParticipant() {
        val participant = Participant(client, challenge, 0)
        HttpClient.participantService(activity!!)
            .createParticipant(participant)
            .enqueue(object : Callback<Participant> {
                override fun onFailure(call: Call<Participant>, t: Throwable) {
                    t.printStackTrace()
                }

                override fun onResponse(call: Call<Participant>, response: Response<Participant>) {
                    if (response.code() == 200) {
                        participants.add(response.body()!!)
                        recyclerViewAdapter.notifyDataSetChanged()
                        toggleEnrollButtonText()
                    }
                }

            })
    }
}