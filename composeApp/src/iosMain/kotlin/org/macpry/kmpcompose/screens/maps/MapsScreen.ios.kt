package org.macpry.kmpcompose.screens.maps

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
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
            mapView.setRegion(
                MKCoordinateRegionMakeWithDistance(
                    centerCoordinate = CLLocationCoordinate2DMake(
                        mapsState.locationToZoom.latitude,
                        mapsState.locationToZoom.longitude
                    ),
                    10_000.0, 10_000.0
                ),
                animated = true
            )
        }
    )
}
