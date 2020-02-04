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
package com.androidmapsextensions

import android.graphics.Bitmap
import android.location.Location
import android.view.View
import com.google.android.gms.maps.*
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*

interface GoogleMap {
    fun addCircle(circleOptions: CircleOptions?): Circle?
    fun addGroundOverlay(groundOverlayOptions: GroundOverlayOptions?): GroundOverlay?
    fun addMarker(markerOptions: MarkerOptions?): Marker?
    fun addPolygon(polygonOptions: PolygonOptions?): Polygon?
    fun addPolyline(polylineOptions: PolylineOptions?): Polyline?
    fun addTileOverlay(tileOverlayOptions: TileOverlayOptions?): TileOverlay?
    fun animateCamera(cameraUpdate: CameraUpdate?, cancelableCallback: CancelableCallback?)
    fun animateCamera(cameraUpdate: CameraUpdate?, time: Int, cancelableCallback: CancelableCallback?)
    fun animateCamera(cameraUpdate: CameraUpdate?)
    fun clear()
    val cameraPosition: CameraPosition?
    /**
     * Get a list of markers that could be clicked by user. This is a mix of normal (only visible) and cluster markers.
     *
     * @return list of markers that are displayed at current zoom level
     */
    val displayedMarkers: List<Marker?>?

    var mapType: Int
    /**
     * WARNING: may be changed in future API when this is fixed: http://code.google.com/p/gmaps-api-issues/issues/detail?id=5106
     */
    val circles: List<Circle?>?

    val groundOverlays: List<GroundOverlay?>?
    val markers: List<Marker?>?
    val markerShowingInfoWindow: Marker?
    val polygons: List<Polygon?>?
    val polylines: List<Polyline?>?
    val tileOverlays: List<TileOverlay?>?
    val maxZoomLevel: Float
    val minZoomLevel: Float
    /**
     * Get the minimum zoom level at which marker will be displayed. This function can take as a parameter only markers added via GoogleMap.addMarker and
     * visible. When clustering is not used, it will always return 0.
     *
     *
     * `
     * float zoom = map.getMinZoomLevelNotClustered(marker);
     * map.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), zoom));
    ` *
     *
     * @param marker
     * @return value in range [0, 25] inclusive or Float.POSITIVE_INFINITY when this marker is very near another visible marker
     */
    fun getMinZoomLevelNotClustered(marker: Marker?): Float

    val myLocation: Location?
    val projection: Projection?
    val uiSettings: UiSettings?
    var isBuildingsEnabled: Boolean
    val isIndoorEnabled: Boolean
    var isMyLocationEnabled: Boolean
    var isTrafficEnabled: Boolean
    fun moveCamera(cameraUpdate: CameraUpdate?)
    fun resetMinMaxZoomPreference()
    fun setClustering(clusteringSettings: ClusteringSettings?)
    fun setIndoorEnabled(indoorEnabled: Boolean): Boolean
    fun setInfoWindowAdapter(infoWindowAdapter: InfoWindowAdapter?)
    fun setLatLngBoundsForCameraTarget(latLngBounds: LatLngBounds?)
    fun setLocationSource(locationSource: LocationSource?)
    fun setMapStyle(mapStyleOptions: MapStyleOptions?): Boolean
    fun setMaxZoomPreference(maxZoomPreference: Float)
    fun setMinZoomPreference(minZoomPreference: Float)
    fun setOnCameraChangeListener(onCameraChangeListener: OnCameraChangeListener?)
    fun setOnCameraIdleListener(onCameraIdleListener: OnCameraIdleListener?)
    fun setOnCameraMoveCanceledListener(onCameraMoveCanceledListener: OnCameraMoveCanceledListener?)
    fun setOnCameraMoveListener(onCameraMoveListener: OnCameraMoveListener?)
    fun setOnCameraMoveStartedListener(onCameraMoveStartedListener: OnCameraMoveStartedListener?)
    fun setOnCircleClickListener(onCircleClickListener: OnCircleClickListener?)
    fun setOnGroundOverlayClickListener(onGroundOverlayClickListener: OnGroundOverlayClickListener?)
    fun setOnInfoWindowClickListener(onInfoWindowClickListener: OnInfoWindowClickListener?)
    fun setOnInfoWindowCloseListener(onInfoWindowCloseListener: OnInfoWindowCloseListener?)
    fun setOnInfoWindowLongClickListener(onInfoWindowLongClickListener: OnInfoWindowLongClickListener?)
    fun setOnMapClickListener(onMapClickListener: OnMapClickListener?)
    fun setOnMapLoadedCallback(onMapLoadedCallback: OnMapLoadedCallback?)
    fun setOnMapLongClickListener(onMapLongClickListener: OnMapLongClickListener?)
    fun setOnMarkerClickListener(onMarkerClickListener: OnMarkerClickListener?)
    fun setOnMarkerDragListener(onMarkerDragListener: OnMarkerDragListener?)
    fun setOnMyLocationButtonClickListener(listener: OnMyLocationButtonClickListener?)
    fun setOnMyLocationChangeListener(onMyLocationChangeListener: OnMyLocationChangeListener?)
    fun setOnPoiClickListener(onPoiClickListener: OnPoiClickListener?)
    fun setOnPolygonClickListener(onPolygonClickListener: OnPolygonClickListener?)
    fun setOnPolylineClickListener(onPolylineClickListener: OnPolylineClickListener?)
    fun setPadding(left: Int, top: Int, right: Int, bottom: Int)
    fun snapshot(callback: SnapshotReadyCallback?)
    fun snapshot(callback: SnapshotReadyCallback?, bitmap: Bitmap?)
    fun stopAnimation()
    interface CancelableCallback : GoogleMap.CancelableCallback {
        override fun onCancel()
        override fun onFinish()
    }

    interface InfoWindowAdapter {
        fun getInfoContents(marker: Marker?): View?
        fun getInfoWindow(marker: Marker?): View?
    }

    interface OnCameraChangeListener : GoogleMap.OnCameraChangeListener {
        override fun onCameraChange(cameraPosition: CameraPosition)
    }

    interface OnCameraIdleListener : GoogleMap.OnCameraIdleListener {
        override fun onCameraIdle()
    }

    interface OnCameraMoveCanceledListener : GoogleMap.OnCameraMoveCanceledListener {
        override fun onCameraMoveCanceled()
    }

    interface OnCameraMoveListener : GoogleMap.OnCameraMoveListener {
        override fun onCameraMove()
    }

    interface OnCameraMoveStartedListener : GoogleMap.OnCameraMoveStartedListener {
        override fun onCameraMoveStarted(reason: Int)

        companion object {
            const val REASON_API_ANIMATION = GoogleMap.OnCameraMoveStartedListener.REASON_API_ANIMATION
            const val REASON_DEVELOPER_ANIMATION = GoogleMap.OnCameraMoveStartedListener.REASON_DEVELOPER_ANIMATION
            const val REASON_GESTURE = GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE
        }
    }

    interface OnCircleClickListener {
        fun onCircleClick(circle: Circle?)
    }

    interface OnGroundOverlayClickListener {
        fun onGroundOverlayClick(groundOverlay: GroundOverlay?)
    }

    interface OnInfoWindowClickListener {
        fun onInfoWindowClick(marker: Marker?)
    }

    interface OnInfoWindowCloseListener {
        fun onInfoWindowClose(marker: Marker?)
    }

    interface OnInfoWindowLongClickListener {
        fun onInfoWindowLongClick(marker: Marker?)
    }

    interface OnMapClickListener : GoogleMap.OnMapClickListener {
        override fun onMapClick(position: LatLng)
    }

    interface OnMapLoadedCallback : GoogleMap.OnMapLoadedCallback {
        override fun onMapLoaded()
    }

    interface OnMapLongClickListener : GoogleMap.OnMapLongClickListener {
        override fun onMapLongClick(position: LatLng)
    }

    interface OnMarkerClickListener {
        fun onMarkerClick(marker: Marker?): Boolean
    }

    interface OnMarkerDragListener {
        fun onMarkerDragStart(marker: Marker?)
        fun onMarkerDrag(marker: Marker?)
        fun onMarkerDragEnd(marker: Marker?)
    }

    interface OnMyLocationButtonClickListener : GoogleMap.OnMyLocationButtonClickListener {
        override fun onMyLocationButtonClick(): Boolean
    }

    interface OnMyLocationChangeListener : GoogleMap.OnMyLocationChangeListener {
        override fun onMyLocationChange(location: Location)
    }

    interface OnPoiClickListener : GoogleMap.OnPoiClickListener {
        override fun onPoiClick(pointOfInterest: PointOfInterest)
    }

    interface OnPolygonClickListener {
        fun onPolygonClick(polygon: Polygon?)
    }

    interface OnPolylineClickListener {
        fun onPolylineClick(polyline: Polyline?)
    }

    interface SnapshotReadyCallback : GoogleMap.SnapshotReadyCallback {
        override fun onSnapshotReady(snapshot: Bitmap)
    }
}