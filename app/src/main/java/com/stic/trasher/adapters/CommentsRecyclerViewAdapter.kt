package com.stic.trasher.adapters

import android.annotation.SuppressLint
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.stic.trasher.R
import com.stic.trasher.utils.BitmapUtiles
import de.hdodenhof.circleimageview.CircleImageView
import dz.stic.model.Comment
import dz.stic.model.Client


class CommentsRecyclerViewAdapter(private val comments: ArrayList<Comment>) :
    RecyclerView.Adapter<CommentsRecyclerViewAdapter.CommentViewHolder>() {


    class CommentViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val content: TextView = view.findViewById(R.id.comment_content)
        val publisher: TextView = view.findViewById(R.id.comment_publisher)
        val image: CircleImageView = view.findViewById(R.id.comment_avatar)
        val time: TextView = view.findViewById(R.id.comment_time)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        return CommentViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item_comment,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return comments.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val publisher: Client = comments[position].getClient()

        holder.content.text = comments[position].content
        holder.publisher.text = "${publisher.lastName} ${publisher.firstName}"
        holder.time.text = DateUtils.getRelativeTimeSpanString(comments[position].creationDate.time)
        if (comments[position].client.photo != null)
            BitmapUtiles.displayCircleImage(holder.image, comments[position].client.photo, 48, 48)

    }


}