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
package pl.mg6.android.maps.extensions.impl;

import java.util.List;

import pl.mg6.android.maps.extensions.Circle;
import pl.mg6.android.maps.extensions.ClusteringSettings;
import pl.mg6.android.maps.extensions.GoogleMap;
import pl.mg6.android.maps.extensions.GroundOverlay;
import pl.mg6.android.maps.extensions.Marker;
import pl.mg6.android.maps.extensions.Polygon;
import pl.mg6.android.maps.extensions.Polyline;
import pl.mg6.android.maps.extensions.TileOverlay;
import android.location.Location;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.TileOverlayOptions;

public class DelegatingGoogleMap implements GoogleMap {

	private GoogleMapWrapper real;

	private InfoWindowAdapter infoWindowAdapter;
	private OnCameraChangeListener onCameraChangeListener;
	private OnMarkerDragListener onMarkerDragListener;

	private MarkerManager markerManager;
	private PolylineManager polylineManager;
	private PolygonManager polygonManager;
	private CircleManager circleManager;
	private GroundOverlayManager groundOverlayManager;
	private TileOverlayManager tileOverlayManager;

	private Marker markerShowingInfoWindow;

	public DelegatingGoogleMap(com.google.android.gms.maps.GoogleMap real) {
		this.real = new GoogleMapWrapper(real);
		this.markerManager = new MarkerManager(this.real);
		this.polylineManager = new PolylineManager(this.real);
		this.polygonManager = new PolygonManager(this.real);
		this.circleManager = new CircleManager(this.real);
		this.groundOverlayManager = new GroundOverlayManager(this.real);
		this.tileOverlayManager = new TileOverlayManager(this.real);

		real.setInfoWindowAdapter(new DelegatingInfoWindowAdapter());
		real.setOnCameraChangeListener(new DelegatingOnCameraChangeListener());
		real.setOnMarkerDragListener(new DelegatingOnMarkerDragListener());
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
		markerManager.clear();
		polylineManager.clear();
		polygonManager.clear();
		circleManager.clear();
		groundOverlayManager.clear();
		tileOverlayManager.clear();
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
		if (markerShowingInfoWindow != null && !markerShowingInfoWindow.isInfoWindowShown()) {
			markerShowingInfoWindow = null;
		}
		return markerShowingInfoWindow;
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
	public void setClustering(ClusteringSettings clusteringSettings) {
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
	public void setLocationSource(LocationSource locationSource) {
		real.setLocationSource(locationSource);
	}

	@Override
	public void setMapType(int mapType) {
		real.setMapType(mapType);
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
	public void setOnInfoWindowClickListener(OnInfoWindowClickListener onInfoWindowClickListener) {
		com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener realOnInfoWindowClickListener = null;
		if (onInfoWindowClickListener != null) {
			realOnInfoWindowClickListener = new DelegatingOnInfoWindowClickListener(onInfoWindowClickListener);
		}
		real.setOnInfoWindowClickListener(realOnInfoWindowClickListener);
	}

	@Override
	public void setOnMapClickListener(OnMapClickListener onMapClickListener) {
		real.setOnMapClickListener(onMapClickListener);
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
	public void setOnMyLocationChangeListener(OnMyLocationChangeListener onMyLocationChangeListener) {
		real.setOnMyLocationChangeListener(onMyLocationChangeListener);
	}

	@Override
	public void setTrafficEnabled(boolean trafficEnabled) {
		real.setTrafficEnabled(trafficEnabled);
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
			markerShowingInfoWindow = markerManager.map(marker);
			if (infoWindowAdapter != null) {
				return infoWindowAdapter.getInfoWindow(markerShowingInfoWindow);
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

		private OnInfoWindowClickListener onInfoWindowClickListener;

		private DelegatingOnInfoWindowClickListener(OnInfoWindowClickListener onInfoWindowClickListener) {
			this.onInfoWindowClickListener = onInfoWindowClickListener;
		}

		@Override
		public void onInfoWindowClick(com.google.android.gms.maps.model.Marker marker) {
			onInfoWindowClickListener.onInfoWindowClick(markerManager.map(marker));
		}
	}

	private class DelegatingOnMarkerClickListener implements com.google.android.gms.maps.GoogleMap.OnMarkerClickListener {

		private OnMarkerClickListener onMarkerClickListener;

		private DelegatingOnMarkerClickListener(OnMarkerClickListener onMarkerClickListener) {
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
