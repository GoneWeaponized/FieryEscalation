package backStage

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority


@Suppress("ClassName")
class locationGet(context: Context) {
    private val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context.applicationContext)
    @SuppressLint("MissingPermission")
    fun getLastLoc(onResult: (Location?) -> Unit) {
        fusedLocationClient.lastLocation
            .addOnSuccessListener {
                location -> onResult(location)
            }
            .addOnFailureListener { onResult(null) }
    }
    @SuppressLint("MissingPermission")
    fun requestLocationUpdates(onUpdate: (Location) -> Unit) {
        val request = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 3000)
            .setMinUpdateDistanceMeters(1f)
            .build()
        val callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let { onUpdate(it) }
            }
        }
        fusedLocationClient.requestLocationUpdates(request, callback, null)
    }
}