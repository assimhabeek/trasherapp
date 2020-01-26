package com.stic.trasher.ui.framgments

import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.stic.trasher.R
import com.stic.trasher.adapters.CommentsRecyclerViewAdapter
import com.stic.trasher.utils.HttpClient
import com.stic.trasher.utils.SessionManager
import dz.stic.model.Challenge
import dz.stic.model.Comment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.sql.Date

class CommentFragment : Fragment() {

    private lateinit var rlm: RecyclerView.LayoutManager
    private var comments: ArrayList<Comment> = ArrayList()
    private lateinit var recyclerViewAdapter: CommentsRecyclerViewAdapter

    private lateinit var commentTextView: TextView
    private lateinit var sendButton: MaterialButton
    private var challenge: Challenge? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.framgment_comments, container, false)


        val recyclerView: RecyclerView = view.findViewById(R.id.comments_recycler_view)
        recyclerViewAdapter = CommentsRecyclerViewAdapter(comments)
        rlm = LinearLayoutManager(context)
        recyclerView.layoutManager = rlm
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = recyclerViewAdapter


        updateCommentsList()

        commentTextView = view.findViewById(R.id.comment_content_text)
        sendButton = view.findViewById(R.id.send_comment_button)

        sendButton.setOnClickListener {

            if (activity != null && !commentTextView.text.isNullOrEmpty() && challenge != null) {

                postComment()

            }
        }

        return view
    }

    private fun postComment() {
        val client = SessionManager.getUser(activity!!)
        if (client != null) {
            val comment =
                Comment(
                    0,
                    commentTextView.text.toString(),
                    Date(java.util.Date().time),
                    true,
                    client,
                    challenge
                )
            HttpClient.commentsService(activity!!)
                .createComment(comment)
                .enqueue(object : Callback<Comment> {
                    override fun onFailure(call: Call<Comment>, t: Throwable) {
                        Toast.makeText(activity, R.string.commenting_failed, Toast.LENGTH_LONG)
                            .show()
                    }

                    override fun onResponse(call: Call<Comment>, response: Response<Comment>) {
                        if (response.isSuccessful) {
                            comments.add(response.body()!!)
                            recyclerViewAdapter.notifyDataSetChanged()
                            rlm.scrollToPosition(comments.size - 1)
                            commentTextView.text = ""
                        }
                    }

                })
        }
    }

    fun updateCommentsList() {
        challenge = arguments?.getSerializable("challenge") as Challenge

        if (activity != null && challenge != null) {
            HttpClient.challengeService(activity!!)
                .findComments(challenge!!.id)
                .enqueue(object : Callback<ArrayList<Comment>> {
                    override fun onFailure(call: Call<ArrayList<Comment>>, t: Throwable) {
                        t.printStackTrace()
                    }

                    override fun onResponse(
                        call: Call<ArrayList<Comment>>,
                        response: Response<ArrayList<Comment>>
                    ) {
                        if (response.code() == 200) {
                            comments.addAll(response.body()!!)
                            comments.sortBy { it.creationDate }
                            recyclerViewAdapter.notifyDataSetChanged()
                            rlm.scrollToPosition(comments.size - 1)

                        }
                    }

                })

        }

    }

}