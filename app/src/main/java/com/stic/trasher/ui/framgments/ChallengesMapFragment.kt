package com.stic.trasher.ui.framgments

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.TargetApi
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import com.mapswithme.maps.api.MWMPoint
import com.mapswithme.maps.api.MapsWithMeApi
import com.stic.trasher.R


class ChallengesMapFragment : Fragment(), LocationListener {


    private lateinit var lm: LocationManager
    private val permissionsWithProvider = hashMapOf(
        Pair(LocationManager.NETWORK_PROVIDER, ACCESS_COARSE_LOCATION),
        Pair(LocationManager.GPS_PROVIDER, ACCESS_FINE_LOCATION)
    )
    private lateinit var ctx: Context
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        if (context != null) {
            ctx = context as Context
            lm = ctx.getSystemService(Context.LOCATION_SERVICE) as LocationManager


            if (isAllProvidersDisabled()) {
                displayNoServiceToast()
            } else {
                requestPermissionIfNeeded()
                useLocationWhenReady(getLocation())
                listenToLocationChange()
            }
        }


        return inflater.inflate(R.layout.framgment_challenges_map, container, false)
    }


    private fun requestPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            val unGranted = findUnAskedPermissions(permissionsWithProvider.values.toTypedArray())
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

        useLocationWhenReady(getLocation())
        listenToLocationChange()
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

    private fun getLocationProvider(): String? {
        return lm.getBestProvider(Criteria(), true)
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
            Toast.makeText(
                ctx,
                String.format("%f,%f,%f", loc.altitude, loc.latitude, loc.longitude),
                Toast.LENGTH_SHORT
            ).show()
            val p = MWMPoint(loc.latitude, loc.longitude, "Constantine")
            val act = activity
            if (act != null)
                MapsWithMeApi.showPointOnMap(act, p.lat, p.lon, p.name)
        }
    }

    override fun onLocationChanged(location: Location?) {
        useLocationWhenReady(location)
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
    }
}