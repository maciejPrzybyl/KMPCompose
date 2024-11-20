package org.macpry.kmpcompose.screens.maps

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.macpry.kmpcompose.screens.maps.model.Coordinates

class MapsViewModel : ViewModel() {

    fun setLocation(coordinates: Coordinates) {
        _mapsState.update { it.copy(locationToZoom = coordinates) }
    }

    private val _mapsState: MutableStateFlow<MapsState> = MutableStateFlow(MapsState())
    val mapsState = _mapsState.asStateFlow()

}

data class MapsState(
    val locationToZoom: Coordinates = Coordinates(0.0, 0.0),
    val showUserLocationButton: Boolean = false
)
