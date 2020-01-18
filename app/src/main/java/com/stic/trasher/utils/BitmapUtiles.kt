package com.stic.trasher.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import de.hdodenhof.circleimageview.CircleImageView

object BitmapUtiles {


    fun displayImage(
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

    private fun bytesToImage(data: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(data, 0, data.size)

    }

}