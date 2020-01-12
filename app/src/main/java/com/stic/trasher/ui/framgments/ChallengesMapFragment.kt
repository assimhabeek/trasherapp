package com.stic.trasher.ui.framgments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import dz.stic.model.Challenge
import org.mapsforge.core.model.LatLong
import org.mapsforge.map.android.graphics.AndroidGraphicFactory
import org.mapsforge.map.android.util.AndroidUtil
import org.mapsforge.map.android.view.MapView
import org.mapsforge.map.layer.cache.TileCache
import org.mapsforge.map.layer.renderer.TileRendererLayer
import org.mapsforge.map.reader.MapFile
import org.mapsforge.map.rendertheme.InternalRenderTheme
import java.io.File


class ChallengesMapFragment : Fragment() {


    private var challenges: ArrayList<Challenge> = ArrayList()
    private lateinit var mapView: MapView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        AndroidGraphicFactory.createInstance(activity?.application)
        mapView = MapView(context)

        setupMap()

        return mapView
    }


    private fun setupMap() {
        mapView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        mapView.isClickable = true
        mapView.mapScaleBar?.isVisible = true
        mapView.setBuiltInZoomControls(true)
        mapView.setZoomLevelMin(10.toByte())
        mapView.setZoomLevelMax(20.toByte())
        mapView.setCenter(LatLong(36.245138, 6.570929))
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


    override fun onDestroy() {
        mapView.destroyAll()
        AndroidGraphicFactory.clearResourceMemoryCache()
        super.onDestroy()
    }
}