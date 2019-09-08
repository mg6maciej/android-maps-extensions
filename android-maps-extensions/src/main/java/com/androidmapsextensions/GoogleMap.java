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
package com.androidmapsextensions;

import android.graphics.Bitmap;
import android.location.Location;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.PointOfInterest;

import java.util.List;

public interface GoogleMap {

    int MAP_TYPE_HYBRID = com.google.android.gms.maps.GoogleMap.MAP_TYPE_HYBRID;
    int MAP_TYPE_NONE = com.google.android.gms.maps.GoogleMap.MAP_TYPE_NONE;
    int MAP_TYPE_NORMAL = com.google.android.gms.maps.GoogleMap.MAP_TYPE_NORMAL;
    int MAP_TYPE_SATELLITE = com.google.android.gms.maps.GoogleMap.MAP_TYPE_SATELLITE;
    int MAP_TYPE_TERRAIN = com.google.android.gms.maps.GoogleMap.MAP_TYPE_TERRAIN;

    Circle addCircle(CircleOptions circleOptions);

    GroundOverlay addGroundOverlay(GroundOverlayOptions groundOverlayOptions);

    Marker addMarker(MarkerOptions markerOptions);

    Polygon addPolygon(PolygonOptions polygonOptions);

    Polyline addPolyline(PolylineOptions polylineOptions);

    TileOverlay addTileOverlay(TileOverlayOptions tileOverlayOptions);

    void animateCamera(CameraUpdate cameraUpdate, CancelableCallback cancelableCallback);

    void animateCamera(CameraUpdate cameraUpdate, int time, CancelableCallback cancelableCallback);

    void animateCamera(CameraUpdate cameraUpdate);

    void clear();

    CameraPosition getCameraPosition();

    /**
     * Get a list of markers that could be clicked by user. This is a mix of normal (only visible) and cluster markers.
     *
     * @return list of markers that are displayed at current zoom level
     */
    List<Marker> getDisplayedMarkers();

    int getMapType();

    /**
     * WARNING: may be changed in future API when this is fixed: http://code.google.com/p/gmaps-api-issues/issues/detail?id=5106
     */
    List<Circle> getCircles();

    List<GroundOverlay> getGroundOverlays();

    List<Marker> getMarkers();

    Marker getMarkerShowingInfoWindow();

    List<Polygon> getPolygons();

    List<Polyline> getPolylines();

    List<TileOverlay> getTileOverlays();

    float getMaxZoomLevel();

    float getMinZoomLevel();

    /**
     * Get the minimum zoom level at which marker will be displayed. This function can take as a parameter only markers added via GoogleMap.addMarker and
     * visible. When clustering is not used, it will always return 0.
     * <p/>
     * <code>
     * float zoom = map.getMinZoomLevelNotClustered(marker);
     * map.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), zoom));
     * </code>
     *
     * @param marker
     * @return value in range [0, 25] inclusive or Float.POSITIVE_INFINITY when this marker is very near another visible marker
     */
    float getMinZoomLevelNotClustered(Marker marker);

    Location getMyLocation();

    Projection getProjection();

    UiSettings getUiSettings();

    boolean isBuildingsEnabled();

    boolean isIndoorEnabled();

    boolean isMyLocationEnabled();

    boolean isTrafficEnabled();

    void moveCamera(CameraUpdate cameraUpdate);

    void resetMinMaxZoomPreference();

    void setBuildingsEnabled(boolean buildingsEnabled);

    void setClustering(ClusteringSettings clusteringSettings);

    boolean setIndoorEnabled(boolean indoorEnabled);

    void setInfoWindowAdapter(InfoWindowAdapter infoWindowAdapter);

    void setLatLngBoundsForCameraTarget(LatLngBounds latLngBounds);

    void setLocationSource(LocationSource locationSource);

    boolean setMapStyle(MapStyleOptions mapStyleOptions);

    void setMapType(int mapType);

    void setMaxZoomPreference(float maxZoomPreference);

    void setMinZoomPreference(float minZoomPreference);

    void setMyLocationEnabled(boolean myLocationEnabled);

    void setOnCameraChangeListener(OnCameraChangeListener onCameraChangeListener);

    void setOnCameraIdleListener(OnCameraIdleListener onCameraIdleListener);

    void setOnCameraMoveCanceledListener(OnCameraMoveCanceledListener onCameraMoveCanceledListener);

    void setOnCameraMoveListener(OnCameraMoveListener onCameraMoveListener);

    void setOnCameraMoveStartedListener(OnCameraMoveStartedListener onCameraMoveStartedListener);

    void setOnCircleClickListener(OnCircleClickListener onCircleClickListener);

    void setOnGroundOverlayClickListener(OnGroundOverlayClickListener onGroundOverlayClickListener);

    void setOnInfoWindowClickListener(OnInfoWindowClickListener onInfoWindowClickListener);

    void setOnInfoWindowCloseListener(OnInfoWindowCloseListener onInfoWindowCloseListener);

    void setOnInfoWindowLongClickListener(OnInfoWindowLongClickListener onInfoWindowLongClickListener);

    void setOnMapClickListener(OnMapClickListener onMapClickListener);

    void setOnMapLoadedCallback(OnMapLoadedCallback onMapLoadedCallback);

    void setOnMapLongClickListener(OnMapLongClickListener onMapLongClickListener);

    void setOnMarkerClickListener(OnMarkerClickListener onMarkerClickListener);

    void setOnMarkerDragListener(OnMarkerDragListener onMarkerDragListener);

    void setOnMyLocationButtonClickListener(OnMyLocationButtonClickListener listener);

    void setOnMyLocationChangeListener(OnMyLocationChangeListener onMyLocationChangeListener);

    void setOnPoiClickListener(OnPoiClickListener onPoiClickListener);

    void setOnPolygonClickListener(OnPolygonClickListener onPolygonClickListener);

    void setOnPolylineClickListener(OnPolylineClickListener onPolylineClickListener);

    void setPadding(int left, int top, int right, int bottom);

    void setTrafficEnabled(boolean trafficEnabled);

    void snapshot(SnapshotReadyCallback callback);

    void snapshot(SnapshotReadyCallback callback, Bitmap bitmap);

    void stopAnimation();

    com.google.android.gms.maps.GoogleMap getOriginalMap();

    interface CancelableCallback extends com.google.android.gms.maps.GoogleMap.CancelableCallback {

        @Override
        void onCancel();

        @Override
        void onFinish();
    }

    interface InfoWindowAdapter {

        View getInfoContents(Marker marker);

        View getInfoWindow(Marker marker);
    }

    interface OnCameraChangeListener extends com.google.android.gms.maps.GoogleMap.OnCameraChangeListener {

        @Override
        void onCameraChange(CameraPosition cameraPosition);
    }

    interface OnCameraIdleListener extends com.google.android.gms.maps.GoogleMap.OnCameraIdleListener {

        @Override
        void onCameraIdle();
    }

    interface OnCameraMoveCanceledListener extends com.google.android.gms.maps.GoogleMap.OnCameraMoveCanceledListener {

        @Override
        void onCameraMoveCanceled();
    }

    interface OnCameraMoveListener extends com.google.android.gms.maps.GoogleMap.OnCameraMoveListener {

        @Override
        void onCameraMove();
    }

    interface OnCameraMoveStartedListener extends com.google.android.gms.maps.GoogleMap.OnCameraMoveStartedListener {

        int REASON_API_ANIMATION = com.google.android.gms.maps.GoogleMap.OnCameraMoveStartedListener.REASON_API_ANIMATION;
        int REASON_DEVELOPER_ANIMATION = com.google.android.gms.maps.GoogleMap.OnCameraMoveStartedListener.REASON_DEVELOPER_ANIMATION;
        int REASON_GESTURE = com.google.android.gms.maps.GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE;

        @Override
        void onCameraMoveStarted(int reason);
    }

    interface OnCircleClickListener {

        void onCircleClick(Circle circle);
    }

    interface OnGroundOverlayClickListener {

        void onGroundOverlayClick(GroundOverlay groundOverlay);
    }

    interface OnInfoWindowClickListener {

        void onInfoWindowClick(Marker marker);
    }

    interface OnInfoWindowCloseListener {

        void onInfoWindowClose(Marker marker);
    }

    interface OnInfoWindowLongClickListener {

        void onInfoWindowLongClick(Marker marker);
    }

    interface OnMapClickListener extends com.google.android.gms.maps.GoogleMap.OnMapClickListener {

        @Override
        void onMapClick(LatLng position);
    }

    interface OnMapLoadedCallback extends com.google.android.gms.maps.GoogleMap.OnMapLoadedCallback {

        @Override
        void onMapLoaded();
    }

    interface OnMapLongClickListener extends com.google.android.gms.maps.GoogleMap.OnMapLongClickListener {

        @Override
        void onMapLongClick(LatLng position);
    }

    interface OnMarkerClickListener {

        boolean onMarkerClick(Marker marker);
    }

    interface OnMarkerDragListener {

        void onMarkerDragStart(Marker marker);

        void onMarkerDrag(Marker marker);

        void onMarkerDragEnd(Marker marker);
    }

    interface OnMyLocationButtonClickListener extends com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener {

        @Override
        boolean onMyLocationButtonClick();
    }

    interface OnMyLocationChangeListener extends com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener {

        @Override
        void onMyLocationChange(Location location);
    }

    interface OnPoiClickListener extends com.google.android.gms.maps.GoogleMap.OnPoiClickListener {

        @Override
        void onPoiClick(PointOfInterest pointOfInterest);
    }

    interface OnPolygonClickListener {

        void onPolygonClick(Polygon polygon);
    }

    interface OnPolylineClickListener {

        void onPolylineClick(Polyline polyline);
    }

    interface SnapshotReadyCallback extends com.google.android.gms.maps.GoogleMap.SnapshotReadyCallback {

        @Override
        void onSnapshotReady(Bitmap snapshot);
    }
}