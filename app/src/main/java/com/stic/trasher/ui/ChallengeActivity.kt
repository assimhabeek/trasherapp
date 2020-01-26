package com.stic.trasher.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.format.DateUtils
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.textview.MaterialTextView
import com.stic.trasher.R
import com.stic.trasher.adapters.TabAdapter
import com.stic.trasher.ui.framgments.CommentFragment
import com.stic.trasher.ui.framgments.ParticipantFragment
import com.stic.trasher.ui.framgments.PhotoFragment
import com.stic.trasher.utils.BitmapUtiles
import com.stic.trasher.utils.HttpClient
import de.hdodenhof.circleimageview.CircleImageView
import dz.stic.model.Challenge
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


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

    val REQUEST_IMAGE_CAPTURE = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_challenge)
        setupToolbar()
        val challengeId = intent.getIntExtra("challengeId", 0)
        bindElements()
        loadChallenge(challengeId)
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
                        challenge = response.body()!!

                        setupChallengeInfo()
                        createViewPage(challengeId)

                    }
                }

            })
    }

    @SuppressLint("SetTextI18n")
    private fun setupChallengeInfo() {

        creationDateTextview.text = timeAgo(challenge.creationDate.time)
        startingDateTextview.text = timeAgo(challenge.startingDate.time)
        endingDateTextview.text = timeAgo(challenge.endingDate.time)



        address.text =
            "${challenge.getrAddress().street}. ${challenge.getrAddress().city}, ${challenge.getrAddress().country}"
        BitmapUtiles.displayCircleImage(profileImage, challenge.getrOwner().photo)
        ownerName.text =
            "${challenge.getrOwner().lastName}   ${challenge.getrOwner().firstName}"

    }

    private fun setupToolbar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }


    private fun createViewPage(challengeId: Int) {

        adapter = TabAdapter(supportFragmentManager)

        val photoFragment = PhotoFragment()
        val commentFragment = CommentFragment()
        val participantFragment = ParticipantFragment()

        val bundle = Bundle()

        bundle.putSerializable("challenge", challenge)

        commentFragment.arguments = bundle
        photoFragment.arguments = bundle
        participantFragment.arguments = bundle


        adapter?.addFragment(photoFragment, "")
        adapter?.addFragment(commentFragment, "")
        adapter?.addFragment(participantFragment, "")

        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)

        tabLayout.getTabAt(0)?.setIcon(R.drawable.ic_photo)
        tabLayout.getTabAt(1)?.setIcon(R.drawable.ic_mode_comment_24px)
        tabLayout.getTabAt(2)?.setIcon(R.drawable.ic_group)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                setResult(SignUpActivity.SIGN_UP_Fail)
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return true
    }


    private fun timeAgo(date: Long): CharSequence {
        return DateUtils.getRelativeTimeSpanString(date)
    }


}
