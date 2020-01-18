package com.stic.trasher.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.textview.MaterialTextView
import com.stic.trasher.R
import com.stic.trasher.adapters.TabAdapter
import com.stic.trasher.ui.framgments.CommentFramgment
import com.stic.trasher.ui.framgments.PhotoFramgment
import com.stic.trasher.utils.BitmapUtiles
import com.stic.trasher.utils.HttpClient
import de.hdodenhof.circleimageview.CircleImageView
import dz.stic.model.Challenge
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class ChallengeActivity : AppCompatActivity() {

    private lateinit var challenge: Challenge
    private lateinit var creationDateTextview: MaterialTextView
    private lateinit var endingDateTextview: MaterialTextView
    private lateinit var startingDateTextview: MaterialTextView
    private lateinit var profileImage: CircleImageView
    private lateinit var ownerName: MaterialTextView
    private lateinit var address: MaterialTextView

    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout
    private var adapter: TabAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_challenge)
        val challengeId = intent.getIntExtra("challengeId", 0)
        bindElements()
        loadChallenge(challengeId)
        createViewPage(challengeId)
    }

    private fun bindElements() {
        creationDateTextview = findViewById(R.id.creation_date)
        startingDateTextview = findViewById(R.id.starting_date)
        endingDateTextview = findViewById(R.id.ending_date)
        profileImage = findViewById(R.id.challenge_owner_image)
        ownerName = findViewById(R.id.owner_name)
        address = findViewById(R.id.address)
        viewPager = findViewById(R.id.challenge_view_pager)
        tabLayout = findViewById(R.id.challenge_tabLayout)
    }

    @SuppressLint("SetTextI18n")
    private fun loadChallenge(challengeId: Int) {
        HttpClient.challengeService(this).findById(challengeId)
            .enqueue(object : Callback<Challenge> {
                override fun onFailure(call: Call<Challenge>, t: Throwable) {
                    Toast.makeText(this@ChallengeActivity, t.message, Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call<Challenge>, response: Response<Challenge>) {
                    if (response.code() == 200 && response.body() != null) {
                        val sdf = SimpleDateFormat("dd-MM-yy", Locale.ENGLISH)
                        challenge = response.body()!!
                        creationDateTextview.text = sdf.format(challenge.creationDate)
                        startingDateTextview.text = sdf.format(challenge.startingDate)
                        endingDateTextview.text = sdf.format(challenge.endingDate)
                        address.text =
                            "${challenge.getrAddress().street}. ${challenge.getrAddress().city}, ${challenge.getrAddress().country}"
                        BitmapUtiles.displayImage(profileImage, challenge.getrOwner().photo)
                        ownerName.text =
                            "${challenge.getrOwner().lastName}   ${challenge.getrOwner().firstName}"
                    }
                }

            })
    }

    private fun createViewPage(challengeId: Int) {

        adapter = TabAdapter(supportFragmentManager)

        val photoFragment = PhotoFramgment()
        val commentFragment = CommentFramgment()

        val b = Bundle()
        b.putInt("challengeId", challengeId)
        photoFragment.arguments = b
        commentFragment.arguments = b

        adapter?.addFragment(photoFragment, "")
        adapter?.addFragment(commentFragment, "")

        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)

        tabLayout.getTabAt(0)?.setIcon(R.drawable.ic_photo)
        tabLayout.getTabAt(1)?.setIcon(R.drawable.ic_mode_comment_24px)
    }
}
