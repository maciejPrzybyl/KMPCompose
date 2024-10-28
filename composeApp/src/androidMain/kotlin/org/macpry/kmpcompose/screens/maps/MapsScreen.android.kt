package org.macpry.kmpcompose.screens.maps

import androidx.compose.runtime.Composable
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState
import org.macpry.kmpcompose.screens.maps.model.Coordinates

@Composable
actual fun Map(coordinates: Coordinates) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(coordinates.latitude, coordinates.longitude),
            10f
        )
    }
    GoogleMap(cameraPositionState = cameraPositionState)
}
