package com.iroid.emergency.main.home

import com.iroid.emergency.db.LocationEntity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Transformations
import java.util.concurrent.Executors
import kotlin.collections.ArrayList

class LocationHistoryViewModel(application: Application) : AndroidViewModel(application) {

    private val locationRepository = LocationRepository.getInstance(
        application.applicationContext,
        Executors.newSingleThreadExecutor()
    )

    val locationsLiveData = Transformations.map(locationRepository.getLocations()) { locations ->
        val newLocations: ArrayList<LocationEntity> = ArrayList()
        locations?.forEach { location ->
            newLocations.add(LocationEntity(location.id,location.latitude,location.longitude,location.foreground,location.recordedAt))
        }
        return@map newLocations
    }
}
