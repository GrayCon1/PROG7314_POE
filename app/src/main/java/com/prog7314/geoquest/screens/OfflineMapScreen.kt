package com.prog7314.geoquest.screens

import android.os.Bundle
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import org.maplibre.gl.maps.MapView
import org.maplibre.gl.maps.Style

@Composable
fun OfflineMapScreen() {
    val context = LocalContext.current
    val mapView = MapView(context)

    DisposableEffect(Unit) {
        mapView.onCreate(Bundle())
        mapView.onStart()
        mapView.onResume()

        onDispose {
            mapView.onPause()
            mapView.onStop()
            mapView.onDestroy()
        }
    }

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = {
            mapView.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            mapView.getMapAsync { mapLibreMap ->
                mapLibreMap.setStyle(Style.Builder().fromUri("asset://style.json"))
            }
            mapView
        }
    )
}