package org.macpry.kmpcompose.screens.maps

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitView
import kotlinx.cinterop.CValue
import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreLocation.CLLocationCoordinate2D
import platform.CoreLocation.CLLocationCoordinate2DMake
import platform.CoreLocation.CLLocationManager
import platform.MapKit.MKCoordinateRegionMakeWithDistance
import platform.MapKit.MKMapView

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun Map(mapsState: MapsState, onLocationPermissionsChanged: (Boolean) -> Unit) {
    val mapView = remember { MKMapView() }
    val locationManager = CLLocationManager()
    locationManager.requestWhenInUseAuthorization()
    mapView.setShowsUserLocation(true)
    UIKitView(
        modifier = Modifier.fillMaxSize(),
        factory = { mapView },
        update = {
            mapView.zoomToLocation(
                CLLocationCoordinate2DMake(
                    mapsState.locationToZoom.latitude,
                    mapsState.locationToZoom.longitude
                )
            )
        }
    )

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopEnd) {
        IconButton(
            {
                mapView.userLocation.location?.let {
                    mapView.zoomToLocation(it.coordinate)
                }
            },
            Modifier.statusBarsPadding()
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null
            )
        }
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun MKMapView.zoomToLocation(coordinate: CValue<CLLocationCoordinate2D>) {
    setRegion(
        MKCoordinateRegionMakeWithDistance(
            centerCoordinate = coordinate,
            10_000.0, 10_000.0
        ),
        animated = true
    )
}
