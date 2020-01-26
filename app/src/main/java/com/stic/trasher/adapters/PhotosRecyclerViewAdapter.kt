package com.stic.trasher.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.stic.trasher.R
import com.stic.trasher.utils.BitmapUtiles
import com.stic.trasher.utils.HttpClient
import dz.stic.model.Photo
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PhotosRecyclerViewAdapter(
    private val photos: ArrayList<Photo>,
    private val activity: Activity
) :
    RecyclerView.Adapter<PhotosRecyclerViewAdapter.PhotoViewHolder>() {


    class PhotoViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.challenge_photo)
        val time: TextView = view.findViewById(R.id.phone_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        return PhotoViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item_photo,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return photos.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.time.text = DateUtils.getRelativeTimeSpanString(photos[position].creationDate.time)

        try {
            HttpClient.imagesService(activity)
                .download(photos[position].path)
                .enqueue(object : Callback<ResponseBody> {
                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        t.printStackTrace()
                        println(t.message)
                    }

                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        if (response.code() == 200) {
                            val im = response.body()!!.bytes()
                            BitmapUtiles.displayImage(holder.image, im)
                        }
                    }

                })
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


}