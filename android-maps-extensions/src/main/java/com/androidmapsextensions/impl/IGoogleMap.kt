/*
 * Copyright (C) 2013 Maciej Górski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.androidmapsextensions.impl

import android.graphics.Bitmap
import android.location.Location
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.LocationSource
import com.google.android.gms.maps.UiSettings
import com.google.android.gms.maps.model.*

//TODO: to be deleted when com.google.android.gms.maps.GoogleMap becomes an interface
internal interface IGoogleMap {
    fun addCircle(options: CircleOptions?): Circle?
    fun addGroundOverlay(options: GroundOverlayOptions?): GroundOverlay?
    fun addMarker(options: MarkerOptions?): Marker?
    fun addPolygon(options: PolygonOptions?): Polygon?
    fun addPolyline(options: PolylineOptions?): Polyline?
    fun addTileOverlay(options: TileOverlayOptions?): TileOverlay?
    fun animateCamera(update: CameraUpdate?, callback: GoogleMap.CancelableCallback?)
    fun animateCamera(update: CameraUpdate?, durationMs: Int, callback: GoogleMap.CancelableCallback?)
    fun animateCamera(update: CameraUpdate?)
    fun clear()
    val cameraPosition: CameraPosition?
    var mapType: Int
    val maxZoomLevel: Float
    val minZoomLevel: Float
    val myLocation: Location?
    val projection: IProjection?
    val uiSettings: UiSettings?
    var isBuildingsEnabled: Boolean
    val isIndoorEnabled: Boolean
    var isMyLocationEnabled: Boolean
    var isTrafficEnabled: Boolean
    fun resetMinMaxZoomPreference()
    fun moveCamera(update: CameraUpdate?)
    fun setIndoorEnabled(enabled: Boolean): Boolean
    fun setInfoWindowAdapter(adapter: GoogleMap.InfoWindowAdapter?)
    fun setLatLngBoundsForCameraTarget(bounds: LatLngBounds?)
    fun setLocationSource(source: LocationSource?)
    fun setMapStyle(mapStyleOptions: MapStyleOptions?): Boolean
    fun setMaxZoomPreference(zoom: Float)
    fun setMinZoomPreference(zoom: Float)
    fun setOnCameraChangeListener(listener: GoogleMap.OnCameraChangeListener?)
    fun setOnCameraIdleListener(listener: GoogleMap.OnCameraIdleListener?)
    fun setOnCameraMoveCanceledListener(listener: GoogleMap.OnCameraMoveCanceledListener?)
    fun setOnCameraMoveListener(listener: GoogleMap.OnCameraMoveListener?)
    fun setOnCameraMoveStartedListener(listener: GoogleMap.OnCameraMoveStartedListener?)
    fun setOnCircleClickListener(listener: GoogleMap.OnCircleClickListener?)
    fun setOnGroundOverlayClickListener(listener: GoogleMap.OnGroundOverlayClickListener?)
    fun setOnInfoWindowClickListener(listener: GoogleMap.OnInfoWindowClickListener?)
    fun setOnInfoWindowCloseListener(listener: GoogleMap.OnInfoWindowCloseListener?)
    fun setOnInfoWindowLongClickListener(listener: GoogleMap.OnInfoWindowLongClickListener?)
    fun setOnMapClickListener(listener: GoogleMap.OnMapClickListener?)
    fun setOnMapLoadedCallback(callback: GoogleMap.OnMapLoadedCallback?)
    fun setOnMapLongClickListener(listener: GoogleMap.OnMapLongClickListener?)
    fun setOnMarkerClickListener(listener: GoogleMap.OnMarkerClickListener?)
    fun setOnMarkerDragListener(listener: GoogleMap.OnMarkerDragListener?)
    fun setOnMyLocationButtonClickListener(listener: GoogleMap.OnMyLocationButtonClickListener?)
    fun setOnMyLocationChangeListener(listener: GoogleMap.OnMyLocationChangeListener?)
    fun setOnPoiClickListener(listener: GoogleMap.OnPoiClickListener?)
    fun setOnPolygonClickListener(listener: GoogleMap.OnPolygonClickListener?)
    fun setOnPolylineClickListener(listener: GoogleMap.OnPolylineClickListener?)
    fun setPadding(left: Int, top: Int, right: Int, bottom: Int)
    fun snapshot(callback: GoogleMap.SnapshotReadyCallback?)
    fun snapshot(callback: GoogleMap.SnapshotReadyCallback?, bitmap: Bitmap?)
    fun stopAnimation()
    val map: GoogleMap?
}