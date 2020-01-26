package com.stic.trasher.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.ImageView
import de.hdodenhof.circleimageview.CircleImageView

object BitmapUtiles {


    fun displayCircleImage(
        profileImage: CircleImageView,
        photo: ByteArray,
        width: Int = 100,
        height: Int = 100
    ) {
        val bmp = bytesToImage(photo)
        profileImage.setImageBitmap(
            Bitmap.createScaledBitmap(
                bmp,
                width,
                height,
                false
            )
        )
    }

    fun displayImage(
        profileImage: ImageView,
        photo: ByteArray) {
        val bmp = bytesToImage(photo)
        profileImage.setImageBitmap(
            Bitmap.createBitmap(bmp)
        )
    }

    private fun bytesToImage(data: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(data, 0, data.size)

    }

}