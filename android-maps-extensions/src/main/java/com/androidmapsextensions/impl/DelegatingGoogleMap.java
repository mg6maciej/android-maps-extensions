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

import android.content.Context;
import android.graphics.Bitmap;
import android.location.Location;
import android.view.View;

import com.androidmapsextensions.Circle;
import com.androidmapsextensions.CircleOptions;
import com.androidmapsextensions.ClusteringSettings;
import com.androidmapsextensions.DefaultClusterOptionsProvider;
import com.androidmapsextensions.GoogleMap;
import com.androidmapsextensions.GroundOverlay;
import com.androidmapsextensions.GroundOverlayOptions;
import com.androidmapsextensions.Marker;
import com.androidmapsextensions.MarkerOptions;
import com.androidmapsextensions.Polygon;
import com.androidmapsextensions.PolygonOptions;
import com.androidmapsextensions.Polyline;
import com.androidmapsextensions.PolylineOptions;
import com.androidmapsextensions.TileOverlay;
import com.androidmapsextensions.TileOverlayOptions;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;

import java.util.List;

class DelegatingGoogleMap implements GoogleMap {

    private IGoogleMap real;
    private Context context;

    private InfoWindowAdapter infoWindowAdapter;
    private OnCameraChangeListener onCameraChangeListener;
    private OnMarkerDragListener onMarkerDragListener;

    private MarkerManager markerManager;
    private PolylineManager polylineManager;
    private PolygonManager polygonManager;
    private CircleManager circleManager;
    private GroundOverlayManager groundOverlayManager;
    private TileOverlayManager tileOverlayManager;

    DelegatingGoogleMap(IGoogleMap real, Context context) {
        this.real = real;
        this.context = context;
        createManagers();
        assignMapListeners();
    }

    @Override
    public Circle addCircle(CircleOptions circleOptions) {
        return circleManager.addCircle(circleOptions);
    }

    @Override
    public GroundOverlay addGroundOverlay(GroundOverlayOptions groundOverlayOptions) {
        return groundOverlayManager.addGroundOverlay(groundOverlayOptions);
    }

    @Override
    public Marker addMarker(MarkerOptions markerOptions) {
        return markerManager.addMarker(markerOptions);
    }

    @Override
    public Polygon addPolygon(PolygonOptions polygonOptions) {
        return polygonManager.addPolygon(polygonOptions);
    }

    @Override
    public Polyline addPolyline(PolylineOptions polylineOptions) {
        return polylineManager.addPolyline(polylineOptions);
    }

    @Override
    public TileOverlay addTileOverlay(TileOverlayOptions tileOverlayOptions) {
        return tileOverlayManager.addTileOverlay(tileOverlayOptions);
    }

    @Override
    public void animateCamera(CameraUpdate cameraUpdate, CancelableCallback cancelableCallback) {
        real.animateCamera(cameraUpdate, cancelableCallback);
    }

    @Override
    public void animateCamera(CameraUpdate cameraUpdate, int time, CancelableCallback cancelableCallback) {
        real.animateCamera(cameraUpdate, time, cancelableCallback);
    }

    @Override
    public void animateCamera(CameraUpdate cameraUpdate) {
        real.animateCamera(cameraUpdate);
    }

    @Override
    public void clear() {
        real.clear();
        clearManagers();
    }

    @Override
    public CameraPosition getCameraPosition() {
        return real.getCameraPosition();
    }

    @Override
    public List<Marker> getDisplayedMarkers() {
        return markerManager.getDisplayedMarkers();
    }

    @Override
    public int getMapType() {
        return real.getMapType();
    }

    @Override
    public List<Circle> getCircles() {
        return circleManager.getCircles();
    }

    @Override
    public List<GroundOverlay> getGroundOverlays() {
        return groundOverlayManager.getGroundOverlays();
    }

    @Override
    public List<Marker> getMarkers() {
        return markerManager.getMarkers();
    }

    @Override
    public Marker getMarkerShowingInfoWindow() {
        return markerManager.getMarkerShowingInfoWindow();
    }

    @Override
    public List<Polygon> getPolygons() {
        return polygonManager.getPolygons();
    }

    @Override
    public List<Polyline> getPolylines() {
        return polylineManager.getPolylines();
    }

    @Override
    public List<TileOverlay> getTileOverlays() {
        return tileOverlayManager.getTileOverlays();
    }

    @Override
    public float getMaxZoomLevel() {
        return real.getMaxZoomLevel();
    }

    @Override
    public float getMinZoomLevel() {
        return real.getMinZoomLevel();
    }

    @Override
    public float getMinZoomLevelNotClustered(Marker marker) {
        return markerManager.getMinZoomLevelNotClustered(marker);
    }

    @Override
    public Location getMyLocation() {
        return real.getMyLocation();
    }

    @Override
    public Projection getProjection() {
        return real.getProjection().getProjection();
    }

    @Override
    public UiSettings getUiSettings() {
        return real.getUiSettings();
    }

    @Override
    public boolean isBuildingsEnabled() {
        return real.isBuildingsEnabled();
    }

    @Override
    public boolean isIndoorEnabled() {
        return real.isIndoorEnabled();
    }

    @Override
    public boolean isMyLocationEnabled() {
        return real.isMyLocationEnabled();
    }

    @Override
    public boolean isTrafficEnabled() {
        return real.isTrafficEnabled();
    }

    @Override
    public void moveCamera(CameraUpdate cameraUpdate) {
        real.moveCamera(cameraUpdate);
    }

    @Override
    public void resetMinMaxZoomPreference() {
        real.resetMinMaxZoomPreference();
    }

    @Override
    public void setBuildingsEnabled(boolean buildingsEnabled) {
        real.setBuildingsEnabled(buildingsEnabled);
    }

    @Override
    public void setClustering(ClusteringSettings clusteringSettings) {
        if (clusteringSettings != null && clusteringSettings.isEnabled()
                && clusteringSettings.getClusterOptionsProvider() == null) {
            clusteringSettings.clusterOptionsProvider(new DefaultClusterOptionsProvider(context));
        }
        markerManager.setClustering(clusteringSettings);
    }

    @Override
    public boolean setIndoorEnabled(boolean indoorEnabled) {
        return real.setIndoorEnabled(indoorEnabled);
    }

    @Override
    public void setInfoWindowAdapter(final InfoWindowAdapter infoWindowAdapter) {
        this.infoWindowAdapter = infoWindowAdapter;
    }

    @Override
    public void setLatLngBoundsForCameraTarget(LatLngBounds latLngBounds) {
        real.setLatLngBoundsForCameraTarget(latLngBounds);
    }

    @Override
    public void setLocationSource(LocationSource locationSource) {
        real.setLocationSource(locationSource);
    }

    @Override
    public boolean setMapStyle(MapStyleOptions mapStyleOptions) {
        return real.setMapStyle(mapStyleOptions);
    }

    @Override
    public void setMapType(int mapType) {
        real.setMapType(mapType);
    }

    @Override
    public void setMaxZoomPreference(float maxZoomPreference) {
        real.setMaxZoomPreference(maxZoomPreference);
    }

    @Override
    public void setMinZoomPreference(float minZoomPreference) {
        real.setMinZoomPreference(minZoomPreference);
    }

    @Override
    public void setMyLocationEnabled(boolean myLocationEnabled) {
        real.setMyLocationEnabled(myLocationEnabled);
    }

    @Override
    public void setOnCameraChangeListener(OnCameraChangeListener onCameraChangeListener) {
        this.onCameraChangeListener = onCameraChangeListener;
    }

    @Override
    public void setOnCameraIdleListener(OnCameraIdleListener onCameraIdleListener) {
        real.setOnCameraIdleListener(onCameraIdleListener);
    }

    @Override
    public void setOnCameraMoveCanceledListener(OnCameraMoveCanceledListener onCameraMoveCanceledListener) {
        real.setOnCameraMoveCanceledListener(onCameraMoveCanceledListener);
    }

    @Override
    public void setOnCameraMoveListener(OnCameraMoveListener onCameraMoveListener) {
        real.setOnCameraMoveListener(onCameraMoveListener);
    }

    @Override
    public void setOnCameraMoveStartedListener(OnCameraMoveStartedListener onCameraMoveStartedListener) {
        real.setOnCameraMoveStartedListener(onCameraMoveStartedListener);
    }

    @Override
    public void setOnCircleClickListener(OnCircleClickListener onCircleClickListener) {
        circleManager.setOnCircleClickListener(onCircleClickListener);
    }

    @Override
    public void setOnGroundOverlayClickListener(OnGroundOverlayClickListener onGroundOverlayClickListener) {
        groundOverlayManager.setOnGroundOverlayClickListener(onGroundOverlayClickListener);
    }

    @Override
    public void setOnInfoWindowClickListener(OnInfoWindowClickListener onInfoWindowClickListener) {
        com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener realOnInfoWindowClickListener = null;
        if (onInfoWindowClickListener != null) {
            realOnInfoWindowClickListener = new DelegatingOnInfoWindowClickListener(onInfoWindowClickListener);
        }
        real.setOnInfoWindowClickListener(realOnInfoWindowClickListener);
    }

    @Override
    public void setOnInfoWindowCloseListener(OnInfoWindowCloseListener onInfoWindowCloseListener) {
        com.google.android.gms.maps.GoogleMap.OnInfoWindowCloseListener realOnInfoWindowCloseListener = null;
        if (onInfoWindowCloseListener != null) {
            realOnInfoWindowCloseListener = new DelegatingOnInfoWindowCloseListener(onInfoWindowCloseListener);
        }
        real.setOnInfoWindowCloseListener(realOnInfoWindowCloseListener);
    }

    @Override
    public void setOnInfoWindowLongClickListener(OnInfoWindowLongClickListener onInfoWindowLongClickListener) {
        com.google.android.gms.maps.GoogleMap.OnInfoWindowLongClickListener realOnInfoWindowLongClickListener = null;
        if (onInfoWindowLongClickListener != null) {
            realOnInfoWindowLongClickListener = new DelegatingOnInfoWindowLongClickListener(onInfoWindowLongClickListener);
        }
        real.setOnInfoWindowLongClickListener(realOnInfoWindowLongClickListener);
    }

    @Override
    public void setOnMapClickListener(OnMapClickListener onMapClickListener) {
        real.setOnMapClickListener(onMapClickListener);
    }

    @Override
    public void setOnMapLoadedCallback(OnMapLoadedCallback onMapLoadedCallback) {
        real.setOnMapLoadedCallback(onMapLoadedCallback);
    }

    @Override
    public void setOnMapLongClickListener(OnMapLongClickListener onMapLongClickListener) {
        real.setOnMapLongClickListener(onMapLongClickListener);
    }

    @Override
    public void setOnMarkerClickListener(OnMarkerClickListener onMarkerClickListener) {
        com.google.android.gms.maps.GoogleMap.OnMarkerClickListener realOnMarkerClickListener = null;
        if (onMarkerClickListener != null) {
            realOnMarkerClickListener = new DelegatingOnMarkerClickListener(onMarkerClickListener);
        }
        real.setOnMarkerClickListener(realOnMarkerClickListener);
    }

    @Override
    public void setOnMarkerDragListener(OnMarkerDragListener onMarkerDragListener) {
        this.onMarkerDragListener = onMarkerDragListener;
    }

    @Override
    public void setOnMyLocationButtonClickListener(OnMyLocationButtonClickListener listener) {
        real.setOnMyLocationButtonClickListener(listener);
    }

    @Override
    public void setOnMyLocationChangeListener(OnMyLocationChangeListener onMyLocationChangeListener) {
        real.setOnMyLocationChangeListener(onMyLocationChangeListener);
    }

    @Override
    public void setOnPoiClickListener(OnPoiClickListener onPoiClickListener) {
        real.setOnPoiClickListener(onPoiClickListener);
    }

    @Override
    public void setOnPolygonClickListener(OnPolygonClickListener onPolygonClickListener) {
        polygonManager.setOnPolygonClickListener(onPolygonClickListener);
    }

    @Override
    public void setOnPolylineClickListener(OnPolylineClickListener onPolylineClickListener) {
        polylineManager.setOnPolylineClickListener(onPolylineClickListener);
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        real.setPadding(left, top, right, bottom);
    }

    @Override
    public void setTrafficEnabled(boolean trafficEnabled) {
        real.setTrafficEnabled(trafficEnabled);
    }

    @Override
    public void snapshot(SnapshotReadyCallback callback) {
        real.snapshot(callback);
    }

    @Override
    public void snapshot(SnapshotReadyCallback callback, Bitmap bitmap) {
        real.snapshot(callback, bitmap);
    }

    @Override
    public void stopAnimation() {
        real.stopAnimation();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DelegatingGoogleMap)) {
            return false;
        }
        DelegatingGoogleMap other = (DelegatingGoogleMap) o;
        return real.equals(other.real);
    }

    @Override
    public int hashCode() {
        return real.hashCode();
    }

    @Override
    public String toString() {
        return real.toString();
    }

    private void createManagers() {
        markerManager = new MarkerManager(this.real);
        polylineManager = new PolylineManager(this.real);
        polygonManager = new PolygonManager(this.real);
        circleManager = new CircleManager(this.real);
        groundOverlayManager = new GroundOverlayManager(this.real);
        tileOverlayManager = new TileOverlayManager(this.real);
    }

    private void clearManagers() {
        markerManager.clear();
        polylineManager.clear();
        polygonManager.clear();
        circleManager.clear();
        groundOverlayManager.clear();
        tileOverlayManager.clear();
    }

    private void assignMapListeners() {
        real.setInfoWindowAdapter(new DelegatingInfoWindowAdapter());
        real.setOnCameraChangeListener(new DelegatingOnCameraChangeListener());
        real.setOnMarkerDragListener(new DelegatingOnMarkerDragListener());
    }

    private class DelegatingOnCameraChangeListener implements com.google.android.gms.maps.GoogleMap.OnCameraChangeListener {

        @Override
        public void onCameraChange(CameraPosition cameraPosition) {
            markerManager.onCameraChange(cameraPosition);
            if (onCameraChangeListener != null) {
                onCameraChangeListener.onCameraChange(cameraPosition);
            }
        }
    }

    private class DelegatingInfoWindowAdapter implements com.google.android.gms.maps.GoogleMap.InfoWindowAdapter {

        @Override
        public View getInfoWindow(com.google.android.gms.maps.model.Marker marker) {
            Marker mapped = markerManager.map(marker);
            markerManager.setMarkerShowingInfoWindow(mapped);
            if (infoWindowAdapter != null) {
                return infoWindowAdapter.getInfoWindow(mapped);
            }
            return null;
        }

        @Override
        public View getInfoContents(com.google.android.gms.maps.model.Marker marker) {
            if (infoWindowAdapter != null) {
                return infoWindowAdapter.getInfoContents(markerManager.map(marker));
            }
            return null;
        }
    }

    private class DelegatingOnInfoWindowClickListener implements com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener {

        private final OnInfoWindowClickListener onInfoWindowClickListener;

        public DelegatingOnInfoWindowClickListener(OnInfoWindowClickListener onInfoWindowClickListener) {
            this.onInfoWindowClickListener = onInfoWindowClickListener;
        }

        @Override
        public void onInfoWindowClick(com.google.android.gms.maps.model.Marker marker) {
            onInfoWindowClickListener.onInfoWindowClick(markerManager.map(marker));
        }
    }

    private class DelegatingOnInfoWindowCloseListener implements com.google.android.gms.maps.GoogleMap.OnInfoWindowCloseListener {

        private final OnInfoWindowCloseListener onInfoWindowCloseListener;

        public DelegatingOnInfoWindowCloseListener(OnInfoWindowCloseListener onInfoWindowCloseListener) {
            this.onInfoWindowCloseListener = onInfoWindowCloseListener;
        }

        @Override
        public void onInfoWindowClose(com.google.android.gms.maps.model.Marker marker) {
            onInfoWindowCloseListener.onInfoWindowClose(markerManager.map(marker));
        }
    }

    private class DelegatingOnInfoWindowLongClickListener implements com.google.android.gms.maps.GoogleMap.OnInfoWindowLongClickListener {

        private final OnInfoWindowLongClickListener onInfoWindowLongClickListener;

        public DelegatingOnInfoWindowLongClickListener(OnInfoWindowLongClickListener onInfoWindowLongClickListener) {
            this.onInfoWindowLongClickListener = onInfoWindowLongClickListener;
        }

        @Override
        public void onInfoWindowLongClick(com.google.android.gms.maps.model.Marker marker) {
            onInfoWindowLongClickListener.onInfoWindowLongClick(markerManager.map(marker));
        }
    }

    private class DelegatingOnMarkerClickListener implements com.google.android.gms.maps.GoogleMap.OnMarkerClickListener {

        private final OnMarkerClickListener onMarkerClickListener;

        public DelegatingOnMarkerClickListener(OnMarkerClickListener onMarkerClickListener) {
            this.onMarkerClickListener = onMarkerClickListener;
        }

        @Override
        public boolean onMarkerClick(com.google.android.gms.maps.model.Marker marker) {
            return onMarkerClickListener.onMarkerClick(markerManager.map(marker));
        }
    }

    private class DelegatingOnMarkerDragListener implements com.google.android.gms.maps.GoogleMap.OnMarkerDragListener {

        @Override
        public void onMarkerDragStart(com.google.android.gms.maps.model.Marker marker) {
            DelegatingMarker delegating = markerManager.mapToDelegatingMarker(marker);
            delegating.clearCachedPosition();
            markerManager.onDragStart(delegating);
            if (onMarkerDragListener != null) {
                onMarkerDragListener.onMarkerDragStart(delegating);
            }
        }

        @Override
        public void onMarkerDrag(com.google.android.gms.maps.model.Marker marker) {
            DelegatingMarker delegating = markerManager.mapToDelegatingMarker(marker);
            delegating.clearCachedPosition();
            if (onMarkerDragListener != null) {
                onMarkerDragListener.onMarkerDrag(delegating);
            }
        }

        @Override
        public void onMarkerDragEnd(com.google.android.gms.maps.model.Marker marker) {
            DelegatingMarker delegating = markerManager.mapToDelegatingMarker(marker);
            delegating.clearCachedPosition();
            markerManager.onPositionChange(delegating);
            if (onMarkerDragListener != null) {
                onMarkerDragListener.onMarkerDragEnd(delegating);
            }
        }
    }
}
