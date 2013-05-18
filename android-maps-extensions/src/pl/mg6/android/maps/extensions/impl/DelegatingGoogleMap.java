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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import pl.mg6.android.maps.extensions.Circle;
import pl.mg6.android.maps.extensions.ClusteringSettings;
import pl.mg6.android.maps.extensions.GoogleMap;
import pl.mg6.android.maps.extensions.GroundOverlay;
import pl.mg6.android.maps.extensions.Marker;
import pl.mg6.android.maps.extensions.Polygon;
import pl.mg6.android.maps.extensions.Polyline;
import pl.mg6.android.maps.extensions.TileOverlay;
import pl.mg6.android.maps.extensions.lazy.LazyMarker;
import pl.mg6.android.maps.extensions.lazy.LazyMarker.OnMarkerCreateListener;
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

public class DelegatingGoogleMap implements GoogleMap, OnMarkerCreateListener {

	private GoogleMapWrapper real;

	private InfoWindowAdapter infoWindowAdapter;
	private OnCameraChangeListener onCameraChangeListener;
	private OnMarkerDragListener onMarkerDragListener;

	private Map<LazyMarker, DelegatingMarker> markers;
	private Map<com.google.android.gms.maps.model.Marker, LazyMarker> createdMarkers;
	private Map<com.google.android.gms.maps.model.Polyline, Polyline> polylines;
	private Map<com.google.android.gms.maps.model.Polygon, Polygon> polygons;
	private Map<com.google.android.gms.maps.model.Circle, Circle> circles;
	private Map<com.google.android.gms.maps.model.GroundOverlay, GroundOverlay> groundOverlays;
	private Map<com.google.android.gms.maps.model.TileOverlay, TileOverlay> tileOverlays;

	private Marker markerShowingInfoWindow;

	private ClusteringSettings clusteringSettings = new ClusteringSettings().enabled(false);
	private ClusteringStrategy clusteringStrategy = new NoClusteringStrategy(new ArrayList<DelegatingMarker>());

	public DelegatingGoogleMap(com.google.android.gms.maps.GoogleMap real) {
		this.real = new GoogleMapWrapper(real);
		this.markers = new HashMap<LazyMarker, DelegatingMarker>();
		this.createdMarkers = new HashMap<com.google.android.gms.maps.model.Marker, LazyMarker>();
		this.polylines = new HashMap<com.google.android.gms.maps.model.Polyline, Polyline>();
		this.polygons = new HashMap<com.google.android.gms.maps.model.Polygon, Polygon>();
		this.circles = new HashMap<com.google.android.gms.maps.model.Circle, Circle>();
		this.groundOverlays = new HashMap<com.google.android.gms.maps.model.GroundOverlay, GroundOverlay>();
		this.tileOverlays = new HashMap<com.google.android.gms.maps.model.TileOverlay, TileOverlay>();

		real.setInfoWindowAdapter(new DelegatingInfoWindowAdapter());
		real.setOnCameraChangeListener(new DelegatingOnCameraChangeListener());
		real.setOnMarkerDragListener(new DelegatingOnMarkerDragListener());
	}

	@Override
	public Circle addCircle(CircleOptions circleOptions) {
		com.google.android.gms.maps.model.Circle realCircle = real.addCircle(circleOptions);
		Circle circle = new DelegatingCircle(realCircle, this);
		circles.put(realCircle, circle);
		return circle;
	}

	@Override
	public GroundOverlay addGroundOverlay(GroundOverlayOptions groundOverlayOptions) {
		com.google.android.gms.maps.model.GroundOverlay realGroundOverlay = real.addGroundOverlay(groundOverlayOptions);
		GroundOverlay groundOverlay = new DelegatingGroundOverlay(realGroundOverlay, this);
		groundOverlays.put(realGroundOverlay, groundOverlay);
		return groundOverlay;
	}

	@Override
	public Marker addMarker(MarkerOptions markerOptions) {
		boolean visible = markerOptions.isVisible();
		markerOptions.visible(false);
		LazyMarker realMarker = new LazyMarker(real.getMap(), markerOptions, this);
		markerOptions.visible(visible);
		DelegatingMarker marker = new DelegatingMarker(realMarker, this);
		markers.put(realMarker, marker);
		clusteringStrategy.onAdd(marker);
		marker.setVisible(visible);
		return marker;
	}

	@Override
	public Polygon addPolygon(PolygonOptions polygonOptions) {
		com.google.android.gms.maps.model.Polygon realPolygon = real.addPolygon(polygonOptions);
		Polygon polygon = new DelegatingPolygon(realPolygon, this);
		polygons.put(realPolygon, polygon);
		return polygon;
	}

	@Override
	public Polyline addPolyline(PolylineOptions polylineOptions) {
		com.google.android.gms.maps.model.Polyline realPolyline = real.addPolyline(polylineOptions);
		Polyline polyline = new DelegatingPolyline(realPolyline, this);
		polylines.put(realPolyline, polyline);
		return polyline;
	}

	@Override
	public TileOverlay addTileOverlay(TileOverlayOptions tileOverlayOptions) {
		com.google.android.gms.maps.model.TileOverlay realTileOverlay = real.addTileOverlay(tileOverlayOptions);
		TileOverlay tileOverlay = new DelegatingTileOverlay(realTileOverlay, this);
		tileOverlays.put(realTileOverlay, tileOverlay);
		return tileOverlay;
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
		markers.clear();
		createdMarkers.clear();
		polylines.clear();
		polygons.clear();
		circles.clear();
		groundOverlays.clear();
		tileOverlays.clear();
		clusteringStrategy.cleanup();
	}

	@Override
	public CameraPosition getCameraPosition() {
		return real.getCameraPosition();
	}

	@Override
	public List<Marker> getDisplayedMarkers() {
		List<Marker> displayedMarkers = clusteringStrategy.getDisplayedMarkers();
		if (displayedMarkers == null) {
			displayedMarkers = getMarkers();
			Iterator<Marker> iterator = displayedMarkers.iterator();
			while (iterator.hasNext()) {
				Marker m = iterator.next();
				if (!m.isVisible()) {
					iterator.remove();
				}
			}
		}
		return displayedMarkers;
	}

	@Override
	public int getMapType() {
		return real.getMapType();
	}

	@Override
	public List<Circle> getCircles() {
		return new ArrayList<Circle>(circles.values());
	}

	@Override
	public List<GroundOverlay> getGroundOverlays() {
		return new ArrayList<GroundOverlay>(groundOverlays.values());
	}

	@Override
	public List<Marker> getMarkers() {
		return new ArrayList<Marker>(markers.values());
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
		return new ArrayList<Polygon>(polygons.values());
	}

	@Override
	public List<Polyline> getPolylines() {
		return new ArrayList<Polyline>(polylines.values());
	}

	@Override
	public List<TileOverlay> getTileOverlays() {
		return new ArrayList<TileOverlay>(tileOverlays.values());
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
		return clusteringStrategy.getMinZoomLevelNotClustered(marker);
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
		if (clusteringSettings == null) {
			clusteringSettings = new ClusteringSettings().enabled(false);
		}
		if (!this.clusteringSettings.equals(clusteringSettings)) {
			this.clusteringSettings = clusteringSettings;
			clusteringStrategy.cleanup();
			ArrayList<DelegatingMarker> list = new ArrayList<DelegatingMarker>(markers.values());
			if (clusteringSettings.isEnabled()) {
				clusteringStrategy = new GridClusteringStrategy(clusteringSettings, real, list, new ClusterRefresher());
			} else if (clusteringSettings.isAddMarkersDynamically()) {
				clusteringStrategy = new DynamicNoClusteringStrategy(real, list);
			} else {
				clusteringStrategy = new NoClusteringStrategy(list);
			}
		}
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

	void remove(com.google.android.gms.maps.model.Polyline polyline) {
		polylines.remove(polyline);
	}

	void remove(com.google.android.gms.maps.model.Polygon polygon) {
		polygons.remove(polygon);
	}

	void remove(com.google.android.gms.maps.model.Circle circle) {
		circles.remove(circle);
	}

	void remove(com.google.android.gms.maps.model.GroundOverlay groundOverlay) {
		groundOverlays.remove(groundOverlay);
	}

	void remove(com.google.android.gms.maps.model.TileOverlay tileOverlay) {
		tileOverlays.remove(tileOverlay);
	}

	void onRemove(DelegatingMarker marker) {
		markers.remove(marker.getReal());
		createdMarkers.remove(marker.getReal().getMarker());
		clusteringStrategy.onRemove(marker);
	}

	void onPositionChange(DelegatingMarker marker) {
		clusteringStrategy.onPositionChange(marker);
	}

	void onVisibilityChangeRequest(DelegatingMarker marker, boolean visible) {
		clusteringStrategy.onVisibilityChangeRequest(marker, visible);
	}

	void onShowInfoWindow(DelegatingMarker marker) {
		clusteringStrategy.onShowInfoWindow(marker);
	}

	@Override
	public void onMarkerCreate(LazyMarker marker) {
		createdMarkers.put(marker.getMarker(), marker);
	}

	private class DelegatingOnCameraChangeListener implements com.google.android.gms.maps.GoogleMap.OnCameraChangeListener {

		@Override
		public void onCameraChange(CameraPosition cameraPosition) {
			clusteringStrategy.onCameraChange(cameraPosition);
			if (onCameraChangeListener != null) {
				onCameraChangeListener.onCameraChange(cameraPosition);
			}
		}
	}

	private class DelegatingInfoWindowAdapter implements com.google.android.gms.maps.GoogleMap.InfoWindowAdapter {

		@Override
		public View getInfoWindow(com.google.android.gms.maps.model.Marker marker) {
			markerShowingInfoWindow = map(marker);
			if (infoWindowAdapter != null) {
				return infoWindowAdapter.getInfoWindow(markerShowingInfoWindow);
			}
			return null;
		}

		@Override
		public View getInfoContents(com.google.android.gms.maps.model.Marker marker) {
			if (infoWindowAdapter != null) {
				return infoWindowAdapter.getInfoContents(map(marker));
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
			onInfoWindowClickListener.onInfoWindowClick(map(marker));
		}
	}

	private class DelegatingOnMarkerClickListener implements com.google.android.gms.maps.GoogleMap.OnMarkerClickListener {

		private OnMarkerClickListener onMarkerClickListener;

		private DelegatingOnMarkerClickListener(OnMarkerClickListener onMarkerClickListener) {
			this.onMarkerClickListener = onMarkerClickListener;
		}

		@Override
		public boolean onMarkerClick(com.google.android.gms.maps.model.Marker marker) {
			return onMarkerClickListener.onMarkerClick(map(marker));
		}
	}

	private class DelegatingOnMarkerDragListener implements com.google.android.gms.maps.GoogleMap.OnMarkerDragListener {

		@Override
		public void onMarkerDragStart(com.google.android.gms.maps.model.Marker marker) {
			if (onMarkerDragListener != null) {
				onMarkerDragListener.onMarkerDragStart(map(marker));
			}
		}

		@Override
		public void onMarkerDrag(com.google.android.gms.maps.model.Marker marker) {
			if (onMarkerDragListener != null) {
				onMarkerDragListener.onMarkerDrag(map(marker));
			}
		}

		@Override
		public void onMarkerDragEnd(com.google.android.gms.maps.model.Marker marker) {
			LazyMarker lazy = createdMarkers.get(marker);
			DelegatingMarker delegating = markers.get(lazy);
			clusteringStrategy.onPositionChange(delegating);
			if (onMarkerDragListener != null) {
				onMarkerDragListener.onMarkerDragEnd(map(marker));
			}
		}
	}

	private Marker map(com.google.android.gms.maps.model.Marker marker) {
		Marker cluster = clusteringStrategy.map(marker);
		if (cluster != null) {
			return cluster;
		}
		LazyMarker lazy = createdMarkers.get(marker);
		DelegatingMarker delegating = markers.get(lazy);
		return delegating;
	}
}
