package com.stic.trasher.ui.framgments

import android.Manifest.permission.*
import android.annotation.TargetApi
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import com.stic.trasher.R
import com.stic.trasher.utils.GsonUtil
import dz.stic.model.Challenge
import org.mapsforge.core.graphics.Bitmap
import org.mapsforge.core.model.LatLong
import org.mapsforge.map.android.graphics.AndroidGraphicFactory
import org.mapsforge.map.android.util.AndroidUtil
import org.mapsforge.map.android.view.MapView
import org.mapsforge.map.layer.cache.TileCache
import org.mapsforge.map.layer.overlay.Marker
import org.mapsforge.map.layer.renderer.TileRendererLayer
import org.mapsforge.map.reader.MapFile
import org.mapsforge.map.rendertheme.InternalRenderTheme
import java.io.File


class ChallengesMapFragment : Fragment(), LocationListener {

    private lateinit var lm: LocationManager

    private var mapView: MapView? = null
    private lateinit var tileCache: TileCache
    private lateinit var tileRendererLayer: TileRendererLayer

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    private val requiredPermissions =
        arrayOf(READ_EXTERNAL_STORAGE, ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION)

    private val permissionsWithProvider = hashMapOf(
        Pair(LocationManager.NETWORK_PROVIDER, ACCESS_COARSE_LOCATION),
        Pair(LocationManager.GPS_PROVIDER, ACCESS_FINE_LOCATION)
    )

    private lateinit var ctx: Context
    private lateinit var challenges: ArrayList<Challenge>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.framgment_challenges_map, container, false)

        if (context != null) {

            ctx = context as Context
            lm = ctx.getSystemService(Context.LOCATION_SERVICE) as LocationManager

            challenges = GsonUtil.fromJsonToChallege(arguments?.getString("challenges"))


            setupMap(view)

            if (isAllProvidersDisabled()) {
                displayNoServiceToast()
            }

            requestPermissionIfNeeded()
            onPermissionGranted()

        }

        return view
    }

    private fun onPermissionGranted() {
        if (
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                findUnAskedPermissions(requiredPermissions).isEmpty()
            } else {
                true
            }
        ) {
            drawMap()
            useLocationWhenReady(getLocation())
            listenToLocationChange()
        }
    }

    private fun setupMap(view: View) {
        AndroidGraphicFactory.createInstance(activity?.application)
        mapView = view.findViewById(R.id.mapView)
        mapView?.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        mapView?.isClickable = true
        mapView?.mapScaleBar?.isVisible = true
        mapView?.setBuiltInZoomControls(true)
        mapView?.setZoomLevelMin(10.toByte())
        mapView?.setZoomLevelMax(20.toByte())
        mapView?.setZoomLevel(16.toByte())
    }

    private fun requestPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            val unGranted = findUnAskedPermissions(requiredPermissions)

            if (unGranted.isNotEmpty()) {
                requestPermissions(unGranted.toTypedArray(), ALL_PERMISSIONS_RESULT)
            }
        }

    }

    private fun findUnAskedPermissions(wanted: Array<String>): List<String> {
        return wanted.filter { !hasPermission(it) }
    }

    private fun hasPermission(permission: String): Boolean {
        return checkSelfPermission(ctx, permission) == PackageManager.PERMISSION_GRANTED
    }


    @TargetApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {


        if (requestCode == ALL_PERMISSIONS_RESULT && grantResults.isNotEmpty() && grantResults.any { it == PackageManager.PERMISSION_DENIED }) {
            val rejected = findUnAskedPermissions(permissions)
            if (rejected.isNotEmpty() && shouldShowRequestPermissionRationale(rejected[0])) {
                "These permissions are mandatory for the application. Please allow access."
                    .showMessageOKCancel(
                        DialogInterface.OnClickListener { dialog, which ->
                            requestPermissions(rejected.toTypedArray(), ALL_PERMISSIONS_RESULT)
                        })
            }
        }
        onPermissionGranted()

    }


    private fun String.showMessageOKCancel(okListener: DialogInterface.OnClickListener) {
        val act = activity
        if (act != null) {
            AlertDialog.Builder(act)
                .setMessage(this)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show()
        }
    }


    private fun isAllProvidersDisabled(): Boolean {
        return !lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
                && !lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

    }

    private fun displayNoServiceToast() {
        Toast.makeText(
            ctx,
            resources.getString(R.string.no_location_service),
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onDestroy() {
        mapView?.destroyAll()
        AndroidGraphicFactory.clearResourceMemoryCache()
        super.onDestroy()
    }


    private fun getLocationProvider(): String? {
        return lm.getBestProvider(Criteria(), true)
    }


    private fun drawMap() {
        if (mapView != null) {
            val mp: MapView = mapView!!
            tileCache = AndroidUtil.createTileCache(
                ctx, "mapcache",
                mp.model.displayModel.tileSize, 1f,
                mp.model.frameBufferModel.overdrawFactor
            )

            val file = File(
                Environment.getExternalStorageDirectory().absolutePath,
                "/maps/constantine.map"
            )
            val mapDataStore = MapFile(file)
            tileRendererLayer = TileRendererLayer(
                tileCache, mapDataStore,
                mp.model.mapViewPosition,
                AndroidGraphicFactory.INSTANCE
            )

            tileRendererLayer.setXmlRenderTheme(InternalRenderTheme.OSMARENDER)
            mp.layerManager.layers.add(tileRendererLayer)

        }
    }

    private fun getLocation(): Location? {
        val pr = getLocationProvider()
        val permission = permissionsWithProvider[pr]
        return if (
            !isAllProvidersDisabled()
            && pr != null
            && permission != null
            && checkSelfPermission(
                ctx,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        )
            lm.getLastKnownLocation(pr)
        else null
    }

    private fun listenToLocationChange() {
        val pr = getLocationProvider()
        val permission = permissionsWithProvider[pr]

        if (
            !isAllProvidersDisabled()
            && pr != null
            && permission != null
            && checkSelfPermission(
                ctx, permission
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            lm.requestLocationUpdates(pr, 50, 2.0f, this)

        }

    }

    private fun useLocationWhenReady(loc: Location?) {
        if (loc != null) {
            mapView?.setCenter(LatLong(loc.latitude, loc.longitude))

            drawMarker(LatLong(loc.latitude, loc.longitude), R.drawable.ic_location_on)

            challenges.forEach {
                run {
                    drawMarker(
                        LatLong(
                            it.getrAddress().latitiude,
                            it.getrAddress().longitude
                        ),
                        R.drawable.ic_flag
                    )
                }
            }
        }
    }

    override fun onLocationChanged(location: Location?) {
        if (context != null) {
            useLocationWhenReady(location)
        }
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        Toast.makeText(ctx, "status changed", Toast.LENGTH_SHORT).show()
    }

    override fun onProviderEnabled(provider: String?) {
        Toast.makeText(ctx, "enable", Toast.LENGTH_SHORT).show()
    }

    override fun onProviderDisabled(provider: String?) {
        Toast.makeText(ctx, "disabled", Toast.LENGTH_SHORT).show()
    }


    companion object {
        private const val ALL_PERMISSIONS_RESULT = 99

        fun newInstance(challenges: String): ChallengesMapFragment {
            val bundle = Bundle()
            val fragment = ChallengesMapFragment()
            bundle.putString("challenges", challenges)
            fragment.arguments = bundle
            return fragment
        }

    }

    private fun drawMarker(geoPoint: LatLong, drawable: Int) {
        val drawable: Drawable = resources.getDrawable(drawable)
        val bitmap: Bitmap = AndroidGraphicFactory.convertToBitmap(drawable)
        bitmap.scaleTo(130, 130)
        val marker = Marker(
            geoPoint, bitmap, 0, -bitmap.height / 2
        )
        if (mapView != null) {
            val mp = mapView!!
            mp.layerManager.layers.add(marker);
        }
    }


}