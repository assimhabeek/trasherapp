package com.stic.trasher.ui.framgments

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.stic.trasher.R
import dz.stic.model.Challenge
import org.mapsforge.core.graphics.Bitmap
import org.mapsforge.core.model.LatLong
import org.mapsforge.map.android.graphics.AndroidGraphicFactory
import org.mapsforge.map.android.util.AndroidUtil
import org.mapsforge.map.android.view.MapView
import org.mapsforge.map.layer.overlay.Marker
import org.mapsforge.map.layer.renderer.TileRendererLayer
import org.mapsforge.map.reader.MapFile
import org.mapsforge.map.rendertheme.InternalRenderTheme
import java.io.File


class ChallengesMapFragment : Fragment() {


    private var challenges: ArrayList<Challenge> = ArrayList()
    private lateinit var mapView: MapView
    private lateinit var locationManager: LocationManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        AndroidGraphicFactory.createInstance(activity?.application)
        mapView = MapView(context)
        if (context != null) {
            setupMap()
            setupLocationManager()
            drawChallengesOnMap()
        }

        return mapView
    }


    private fun setupMap() {
        mapView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        mapView.isClickable = true
        mapView.mapScaleBar?.isVisible = true
        mapView.setBuiltInZoomControls(true)
        mapView.setZoomLevelMin(10.toByte())
        mapView.setZoomLevelMax(20.toByte())
        mapView.setZoomLevel(16.toByte())
        val tileCache = AndroidUtil.createTileCache(
            context, "mapcache",
            mapView.model.displayModel.tileSize, 1f,
            mapView.model.frameBufferModel.overdrawFactor
        )

        val file = File(
            Environment.getExternalStorageDirectory().absolutePath,
            "/maps/constantine.map"
        )

        val mapDataStore = MapFile(file)

        val tileRendererLayer = TileRendererLayer(
            tileCache, mapDataStore,
            mapView.model.mapViewPosition,
            AndroidGraphicFactory.INSTANCE
        )

        tileRendererLayer.setXmlRenderTheme(InternalRenderTheme.OSMARENDER)
        mapView.layerManager.layers.add(tileRendererLayer)

    }

    private fun setupLocationManager() {
        locationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ContextCompat.checkSelfPermission(
                context!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val location: Location? =
                locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            drawLocation(location)
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                1,
                1.0f,
                object : LocationListener {
                    override fun onLocationChanged(location: Location?) {
                        drawLocation(location)
                    }

                    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                        if (context != null)
                            Toast.makeText(context, "Status Changed", Toast.LENGTH_SHORT).show()
                    }

                    override fun onProviderEnabled(provider: String?) {
                        if (context != null)
                            Toast.makeText(
                                context,
                                "Location Provider is Enabled",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                    }

                    override fun onProviderDisabled(provider: String?) {
                        if (context != null)
                            Toast.makeText(
                                context,
                                "Location Provider is Disabled",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                    }

                })
        }
    }


    private fun drawLocation(location: Location?) {
        if (location != null && mapView.layerManager != null) {
            mapView.setCenter(LatLong(location.latitude,location.longitude))
            mapView.layerManager.layers.add(
                createMarker(
                    LatLong(location.latitude, location.longitude),
                    R.drawable.ic_location_on_green
                )
            )
        }
    }

    private fun createMarker(geoPoint: LatLong, drawable: Int): Marker {
        val drawable: Drawable = resources.getDrawable(drawable)
        val bitmap: Bitmap = AndroidGraphicFactory.convertToBitmap(drawable)
        bitmap.scaleTo(130, 130)
        return Marker(
            geoPoint, bitmap, 0, -bitmap.height / 2
        )
    }

    fun drawChallengesOnMap() {
        challenges.addAll(arguments?.getSerializable("challenges") as ArrayList<Challenge>)
        var markers = challenges.map {
            createMarker(
                LatLong(
                    it.getrAddress().latitiude,
                    it.getrAddress().longitude
                ), R.drawable.ic_flag
            )
        }

        if (mapView.layerManager != null) {
            mapView.layerManager.layers.addAll(markers)
        }
    }


    override fun onDestroy() {
        mapView.destroyAll()
        AndroidGraphicFactory.clearResourceMemoryCache()
        super.onDestroy()
    }
}