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

import pl.mg6.android.maps.extensions.AnimationSettings;
import pl.mg6.android.maps.extensions.ClusteringSettings;
import pl.mg6.android.maps.extensions.Marker;
import pl.mg6.android.maps.extensions.MarkerOptions;
import pl.mg6.android.maps.extensions.lazy.LazyMarker;
import pl.mg6.android.maps.extensions.lazy.LazyMarker.OnMarkerCreateListener;
import android.os.SystemClock;

import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

class MarkerManager implements OnMarkerCreateListener {

	private final IGoogleMap factory;

	private final Map<LazyMarker, DelegatingMarker> markers;
	private final Map<com.google.android.gms.maps.model.Marker, LazyMarker> createdMarkers;

	private Marker markerShowingInfoWindow;

	private ClusteringSettings clusteringSettings = new ClusteringSettings().enabled(false);
	private ClusteringStrategy clusteringStrategy = new NoClusteringStrategy(new ArrayList<DelegatingMarker>());

	private final MarkerAnimator markerAnimator = new MarkerAnimator();

	public MarkerManager(IGoogleMap factory) {
		this.factory = factory;
		this.markers = new HashMap<LazyMarker, DelegatingMarker>();
		this.createdMarkers = new HashMap<com.google.android.gms.maps.model.Marker, LazyMarker>();
	}

	public Marker addMarker(MarkerOptions markerOptions) {
		boolean visible = markerOptions.isVisible();
		markerOptions.visible(false);
		DelegatingMarker marker = createMarker(markerOptions.real);
		setExtendedOptions(marker, markerOptions);
		clusteringStrategy.onAdd(marker);
		marker.setVisible(visible);
		markerOptions.visible(visible);
		return marker;
	}

	private void setExtendedOptions(DelegatingMarker marker, MarkerOptions markerOptions) {
		marker.setClusterGroup(markerOptions.getClusterGroup());
		marker.setData(markerOptions.getData());
	}

	public Marker addMarker(com.google.android.gms.maps.model.MarkerOptions markerOptions) {
		boolean visible = markerOptions.isVisible();
		markerOptions.visible(false);
		DelegatingMarker marker = createMarker(markerOptions);
		clusteringStrategy.onAdd(marker);
		marker.setVisible(visible);
		markerOptions.visible(visible);
		return marker;
	}

	private DelegatingMarker createMarker(com.google.android.gms.maps.model.MarkerOptions markerOptions) {
		LazyMarker realMarker = new LazyMarker(factory.getMap(), markerOptions, this);
		DelegatingMarker marker = new DelegatingMarker(realMarker, this);
		markers.put(realMarker, marker);
		return marker;
	}

	public void clear() {
		markers.clear();
		createdMarkers.clear();
		clusteringStrategy.cleanup();
	}

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

	public List<Marker> getMarkers() {
		return new ArrayList<Marker>(markers.values());
	}

	public Marker getMarkerShowingInfoWindow() {
		if (markerShowingInfoWindow != null && !markerShowingInfoWindow.isInfoWindowShown()) {
			markerShowingInfoWindow = null;
		}
		return markerShowingInfoWindow;
	}

	public float getMinZoomLevelNotClustered(Marker marker) {
		return clusteringStrategy.getMinZoomLevelNotClustered(marker);
	}

	public void onAnimateMarkerPosition(DelegatingMarker marker, LatLng target, AnimationSettings settings, Marker.AnimationCallback callback) {
		markerAnimator.cancelAnimation(marker, Marker.AnimationCallback.CancelReason.ANIMATE_POSITION);
		markerAnimator.animate(marker, marker.getPosition(), target, SystemClock.uptimeMillis(), settings, callback);
	}

	public void onCameraChange(CameraPosition cameraPosition) {
		clusteringStrategy.onCameraChange(cameraPosition);
	}

	public void onClusterGroupChange(DelegatingMarker marker) {
		clusteringStrategy.onClusterGroupChange(marker);
	}

	public void onDragStart(DelegatingMarker marker) {
		markerAnimator.cancelAnimation(marker, Marker.AnimationCallback.CancelReason.DRAG_START);
	}

	public void onPositionChange(DelegatingMarker marker) {
		clusteringStrategy.onPositionChange(marker);
		markerAnimator.cancelAnimation(marker, Marker.AnimationCallback.CancelReason.SET_POSITION);
	}

	public void onPositionDuringAnimationChange(DelegatingMarker marker) {
		clusteringStrategy.onPositionChange(marker);
	}

	public void onRemove(DelegatingMarker marker) {
		markers.remove(marker.getReal());
		createdMarkers.remove(marker.getReal().getMarker());
		clusteringStrategy.onRemove(marker);
		markerAnimator.cancelAnimation(marker, Marker.AnimationCallback.CancelReason.REMOVE);
	}

	public void onShowInfoWindow(DelegatingMarker marker) {
		clusteringStrategy.onShowInfoWindow(marker);
	}

	public void onVisibilityChangeRequest(DelegatingMarker marker, boolean visible) {
		clusteringStrategy.onVisibilityChangeRequest(marker, visible);
	}

	public void setClustering(ClusteringSettings clusteringSettings) {
		if (clusteringSettings == null) {
			clusteringSettings = new ClusteringSettings().enabled(false);
		}
		if (!this.clusteringSettings.equals(clusteringSettings)) {
			this.clusteringSettings = clusteringSettings;
			clusteringStrategy.cleanup();
			ArrayList<DelegatingMarker> list = new ArrayList<DelegatingMarker>(markers.values());
			if (clusteringSettings.isEnabled()) {
				clusteringStrategy = new GridClusteringStrategy(clusteringSettings, factory, list, new ClusterRefresher());
			} else if (clusteringSettings.isAddMarkersDynamically()) {
				clusteringStrategy = new DynamicNoClusteringStrategy(factory, list);
			} else {
				clusteringStrategy = new NoClusteringStrategy(list);
			}
		}
	}

	public void setMarkerShowingInfoWindow(Marker marker) {
		this.markerShowingInfoWindow = marker;
	}

	@Override
	public void onMarkerCreate(LazyMarker marker) {
		createdMarkers.put(marker.getMarker(), marker);
	}

	public Marker map(com.google.android.gms.maps.model.Marker marker) {
		Marker cluster = clusteringStrategy.map(marker);
		if (cluster != null) {
			return cluster;
		}
		return mapToDelegatingMarker(marker);
	}

	public DelegatingMarker mapToDelegatingMarker(com.google.android.gms.maps.model.Marker marker) {
		LazyMarker lazy = createdMarkers.get(marker);
		DelegatingMarker delegating = markers.get(lazy);
		return delegating;
	}
}
