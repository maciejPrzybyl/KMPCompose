package org.macpry.kmpcompose.screens.maps

import androidx.compose.runtime.Composable
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
actual fun Map(mapsState: MapsState) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(mapsState.locationToZoom.latitude, mapsState.locationToZoom.longitude),
            10f
        )
    }
    GoogleMap(
        cameraPositionState = cameraPositionState,
        properties = MapProperties(isMyLocationEnabled = mapsState.showUserLocationButton)
    )
}
