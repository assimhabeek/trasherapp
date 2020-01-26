package com.stic.trasher.ui.framgments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.stic.trasher.R
import com.stic.trasher.adapters.PhotosRecyclerViewAdapter
import com.stic.trasher.utils.HttpClient
import dz.stic.model.Challenge
import dz.stic.model.Photo
import pl.aprilapps.easyphotopicker.ChooserType
import pl.aprilapps.easyphotopicker.EasyImage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import pl.aprilapps.easyphotopicker.MediaSource
import pl.aprilapps.easyphotopicker.MediaFile
import pl.aprilapps.easyphotopicker.DefaultCallback
import android.content.Intent
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import okhttp3.RequestBody
import java.sql.Date
import java.util.*
import kotlin.collections.ArrayList


class PhotoFragment : Fragment() {

    private var photos: ArrayList<Photo> = ArrayList()
    private lateinit var recyclerViewAdapter: PhotosRecyclerViewAdapter
    private lateinit var rlm: RecyclerView.LayoutManager
    private var challenge: Challenge? = null
    private lateinit var easyImage: EasyImage
    private lateinit var takePicture: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.framgment_photos, container, false)


        val recyclerView: RecyclerView = view.findViewById(R.id.photos_recycler_view)
        takePicture = view.findViewById(R.id.take_picture)

        if (activity != null) {

            easyImage = EasyImage
                .Builder(context!!)
                .setChooserType(ChooserType.CAMERA_AND_GALLERY)
                .setCopyImagesToPublicGalleryFolder(false)
                .setFolderName("Trasher Images")
                .allowMultiple(true)
                .build()

            recyclerViewAdapter = PhotosRecyclerViewAdapter(photos, activity!!)
            rlm = LinearLayoutManager(context)

            recyclerView.layoutManager = rlm
            recyclerView.itemAnimator = DefaultItemAnimator()
            recyclerView.adapter = recyclerViewAdapter
            updatePhotosList()

        }

        takePicture.setOnClickListener {
            easyImage.openChooser(this)
        }

        return view
    }

    fun updatePhotosList() {
        challenge = arguments?.getSerializable("challenge") as Challenge

        if (activity != null && challenge != null) {
            HttpClient.challengeService(activity!!)
                .findPhotos(challenge!!.id)
                .enqueue(object : Callback<ArrayList<Photo>> {
                    override fun onFailure(call: Call<ArrayList<Photo>>, t: Throwable) {
                        t.printStackTrace()
                    }

                    override fun onResponse(
                        call: Call<ArrayList<Photo>>,
                        response: Response<ArrayList<Photo>>
                    ) {
                        if (response.code() == 200) {
                            photos.addAll(response.body()!!)
                            photos.sortBy { it.creationDate }
                            recyclerViewAdapter.notifyDataSetChanged()

                            rlm.scrollToPosition(photos.size - 1)

                        }
                    }

                })

        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        easyImage.handleActivityResult(
            requestCode,
            resultCode,
            data,
            activity!!,
            object : DefaultCallback() {
                override fun onMediaFilesPicked(imageFiles: Array<MediaFile>, source: MediaSource) {
                    onPhotosReturned(imageFiles)
                }

                override fun onImagePickerError(error: Throwable, source: MediaSource) {
                    //Some error handling
                    error.printStackTrace()
                }

                override fun onCanceled(source: MediaSource) {
                    //Not necessary to remove any files manually anymore
                }
            })
    }

    private fun onPhotosReturned(imageFiles: Array<MediaFile>) {
        imageFiles
            .forEach {
                val requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), it.file)
                val body = MultipartBody.Part.createFormData("file", it.file.name, requestFile)
                println(body.body().contentLength())
                HttpClient.imagesService(activity!!)
                    .upload(body)
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

                                val fileName = String(response.body()!!.bytes())
                                savePhoto(fileName)
                            }
                        }

                    })
            }


    }

    private fun savePhoto(fileName: String) {
        val photo = Photo(fileName, Date(Date().time), challenge)
        HttpClient.photosService(activity!!)
            .createPhoto(photo)
            .enqueue(object : Callback<Photo> {
                override fun onFailure(call: Call<Photo>, t: Throwable) {
                    t.printStackTrace()
                }

                override fun onResponse(call: Call<Photo>, response: Response<Photo>) {
                    if (response.code() == 200) {
                        photos.add(response.body()!!)
                        recyclerViewAdapter.notifyDataSetChanged()
                        rlm.scrollToPosition(photos.size - 1)
                    }
                }

            })

    }

}