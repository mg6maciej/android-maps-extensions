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
package com.androidmapsextensions.impl;

import android.graphics.Bitmap;
import android.location.Location;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.CancelableCallback;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnCameraIdleListener;
import com.google.android.gms.maps.GoogleMap.OnCameraMoveCanceledListener;
import com.google.android.gms.maps.GoogleMap.OnCameraMoveListener;
import com.google.android.gms.maps.GoogleMap.OnCameraMoveStartedListener;
import com.google.android.gms.maps.GoogleMap.OnCircleClickListener;
import com.google.android.gms.maps.GoogleMap.OnGroundOverlayClickListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowCloseListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.GoogleMap.OnPolygonClickListener;
import com.google.android.gms.maps.GoogleMap.OnPolylineClickListener;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;

//TODO: to be deleted when com.google.android.gms.maps.GoogleMap becomes an interface
interface IGoogleMap {

    Circle addCircle(CircleOptions options);

    GroundOverlay addGroundOverlay(GroundOverlayOptions options);

    Marker addMarker(MarkerOptions options);

    Polygon addPolygon(PolygonOptions options);

    Polyline addPolyline(PolylineOptions options);

    TileOverlay addTileOverlay(TileOverlayOptions options);

    void animateCamera(CameraUpdate update, CancelableCallback callback);

    void animateCamera(CameraUpdate update, int durationMs, CancelableCallback callback);

    void animateCamera(CameraUpdate update);

    void clear();

    CameraPosition getCameraPosition();

    int getMapType();

    float getMaxZoomLevel();

    float getMinZoomLevel();

    Location getMyLocation();

    IProjection getProjection();

    UiSettings getUiSettings();

    boolean isBuildingsEnabled();

    boolean isIndoorEnabled();

    boolean isMyLocationEnabled();

    boolean isTrafficEnabled();

    void resetMinMaxZoomPreference();

    void moveCamera(CameraUpdate update);

    void setBuildingsEnabled(boolean enabled);

    boolean setIndoorEnabled(boolean enabled);

    void setInfoWindowAdapter(InfoWindowAdapter adapter);

    void setLatLngBoundsForCameraTarget(LatLngBounds bounds);

    void setLocationSource(LocationSource source);

    void setMapType(int type);

    void setMaxZoomPreference(float zoom);

    void setMinZoomPreference(float zoom);

    void setMyLocationEnabled(boolean enabled);

    void setOnCameraChangeListener(OnCameraChangeListener listener);

    void setOnCameraIdleListener(OnCameraIdleListener listener);

    void setOnCameraMoveCanceledListener(OnCameraMoveCanceledListener listener);

    void setOnCameraMoveListener(OnCameraMoveListener listener);

    void setOnCameraMoveStartedListener(OnCameraMoveStartedListener listener);

    void setOnCircleClickListener(OnCircleClickListener listener);

    void setOnGroundOverlayClickListener(OnGroundOverlayClickListener listener);

    void setOnInfoWindowClickListener(OnInfoWindowClickListener listener);

    void setOnInfoWindowCloseListener(OnInfoWindowCloseListener listener);

    void setOnInfoWindowLongClickListener(OnInfoWindowLongClickListener listener);

    void setOnMapClickListener(OnMapClickListener listener);

    void setOnMapLoadedCallback(GoogleMap.OnMapLoadedCallback callback);

    void setOnMapLongClickListener(OnMapLongClickListener listener);

    void setOnMarkerClickListener(OnMarkerClickListener listener);

    void setOnMarkerDragListener(OnMarkerDragListener listener);

    void setOnMyLocationButtonClickListener(OnMyLocationButtonClickListener listener);

    void setOnMyLocationChangeListener(OnMyLocationChangeListener listener);

    void setOnPolygonClickListener(OnPolygonClickListener listener);

    void setOnPolylineClickListener(OnPolylineClickListener listener);

    void setPadding(int left, int top, int right, int bottom);

    void setTrafficEnabled(boolean enabled);

    void snapshot(GoogleMap.SnapshotReadyCallback callback);

    void snapshot(GoogleMap.SnapshotReadyCallback callback, Bitmap bitmap);

    void stopAnimation();

    GoogleMap getMap();
}