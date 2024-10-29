package org.macpry.kmpcompose.screens.maps

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import org.macpry.kmpcompose.screens.maps.model.Coordinates
import platform.CoreLocation.CLLocationCoordinate2DMake
import platform.MapKit.MKCoordinateRegionMakeWithDistance
import platform.MapKit.MKMapView

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun Map(coordinates: Coordinates) {
    val mapView = remember { MKMapView() }
    UIKitView(
        modifier = Modifier.fillMaxSize(),
        factory = { mapView },
        update = {
            mapView.setRegion(
                MKCoordinateRegionMakeWithDistance(
                    centerCoordinate = CLLocationCoordinate2DMake(
                        coordinates.latitude,
                        coordinates.longitude
                    ),
                    10_000.0, 10_000.0
                ),
                animated = true
            )
        }
    )
}