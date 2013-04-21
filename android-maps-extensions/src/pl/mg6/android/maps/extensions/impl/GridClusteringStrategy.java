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
import java.util.List;
import java.util.Map;

import pl.mg6.android.maps.extensions.ClusteringSettings;
import pl.mg6.android.maps.extensions.Marker;
import pl.mg6.android.maps.extensions.utils.SphericalMercator;
import android.support.v4.util.LongSparseArray;

import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.VisibleRegion;

class GridClusteringStrategy extends BaseClusteringStrategy {

	private static final boolean DEBUG_GRID = false;
	private DebugHelper debugHelper;

	private boolean addMarkersDynamically;
	private double baseClusterSize;
	private IGoogleMap map;
	private Map<DelegatingMarker, ClusterMarker> markers;
	private double clusterSize;
	private int[] visibleClusters = new int[4];

	private LongSparseArray<ClusterMarker> clusters = new LongSparseArray<ClusterMarker>();
	private List<ClusterMarker> cache = new ArrayList<ClusterMarker>();

	private ClusterRefresher refresher;

	public GridClusteringStrategy(ClusteringSettings settings, IGoogleMap map, List<DelegatingMarker> markers, ClusterRefresher refresher) {
		super(settings, map);
		this.addMarkersDynamically = settings.isAddMarkersDynamically();
		this.baseClusterSize = settings.getClusterSize();
		this.map = map;
		this.markers = new HashMap<DelegatingMarker, ClusterMarker>();
		for (DelegatingMarker m : markers) {
			if (m.isVisible()) {
				this.markers.put(m, null);
			}
		}
		this.refresher = refresher;
		this.clusterSize = calculateClusterSize(map.getCameraPosition().zoom);
		recalculate();
	}

	@Override
	public void cleanup() {
		for (int i = 0; i < clusters.size(); i++) {
			ClusterMarker cluster = clusters.valueAt(i);
			cluster.cleanup();
		}
		clusters.clear();
		markers.clear();
		refresher.cleanup();
		if (DEBUG_GRID) {
			if (debugHelper != null) {
				debugHelper.cleanup();
			}
		}
		super.cleanup();
	}

	@Override
	public void onCameraChange(CameraPosition cameraPosition) {
		double clusterSize = calculateClusterSize(cameraPosition.zoom);
		if (this.clusterSize != clusterSize) {
			this.clusterSize = clusterSize;
			recalculate();
		} else if (addMarkersDynamically) {
			addMarkersInVisibleRegion();
		}
		if (DEBUG_GRID) {
			if (debugHelper == null) {
				debugHelper = new DebugHelper();
			}
			debugHelper.drawDebugGrid(map, clusterSize);
		}
	}

	@Override
	public void onAdd(DelegatingMarker marker) {
		if (!marker.isVisible()) {
			return;
		}
		addMarker(marker);
	}

	private void addMarker(DelegatingMarker marker) {
		LatLng position = marker.getPosition();
		if (!addMarkersDynamically || isPositionInVisibleClusters(position)) {
			long clusterId = calculateClusterId(position);
			ClusterMarker cluster = findClusterById(clusterId);
			cluster.add(marker);
			markers.put(marker, cluster);
			refresh(cluster);
		} else {
			markers.put(marker, null);
		}
	}

	private boolean isPositionInVisibleClusters(LatLng position) {
		int y = convLat(position.latitude);
		int x = convLng(position.longitude);
		int[] b = visibleClusters;
		return b[0] <= y && y <= b[2] && (b[1] <= x && x <= b[3] || b[1] > b[3] && (b[1] <= x || x <= b[3]));
	}

	@Override
	public void onRemove(DelegatingMarker marker) {
		if (!marker.isVisible()) {
			return;
		}
		removeMarker(marker);
	}

	private void removeMarker(DelegatingMarker marker) {
		ClusterMarker cluster = markers.remove(marker);
		if (cluster != null) {
			cluster.remove(marker);
			refresh(cluster);
		}
	}

	@Override
	public void onPositionChange(DelegatingMarker marker) {
		if (!marker.isVisible()) {
			return;
		}
		ClusterMarker oldCluster = markers.get(marker);
		if (oldCluster != null && isMarkerInCluster(marker, oldCluster)) {
			refresh(oldCluster);
		} else {
			if (oldCluster != null) {
				oldCluster.remove(marker);
				refresh(oldCluster);
			}
			addMarker(marker);
		}
	}

	@Override
	public Marker map(com.google.android.gms.maps.model.Marker original) {
		for (int i = 0; i < clusters.size(); i++) {
			ClusterMarker cluster = clusters.valueAt(i);
			if (original.equals(cluster.getVirtual())) {
				return cluster;
			}
		}
		return null;
	}

	@Override
	public List<Marker> getDisplayedMarkers() {
		List<Marker> displayedMarkers = new ArrayList<Marker>();
		for (int i = 0; i < clusters.size(); i++) {
			ClusterMarker cluster = clusters.valueAt(i);
			Marker displayedMarker = cluster.getDisplauedMarker();
			if (displayedMarker != null) {
				displayedMarkers.add(displayedMarker);
			}
		}
		return displayedMarkers;
	}

	private boolean isMarkerInCluster(DelegatingMarker marker, ClusterMarker cluster) {
		long clusterId = cluster.getClusterId();
		long markerClusterId = calculateClusterId(marker.getPosition());
		return clusterId == markerClusterId;
	}

	private ClusterMarker findClusterById(long clusterId) {
		ClusterMarker cluster = clusters.get(clusterId);
		if (cluster == null) {
			if (cache.size() > 0) {
				cluster = cache.remove(cache.size() - 1);
			} else {
				cluster = new ClusterMarker(this);
			}
			cluster.setClusterId(clusterId);
			clusters.put(clusterId, cluster);
		}
		return cluster;
	}

	@Override
	public void onVisibilityChangeRequest(DelegatingMarker marker, boolean visible) {
		if (visible) {
			addMarker(marker);
		} else {
			removeMarker(marker);
			marker.changeVisible(false);
		}
	}

	private void refresh(ClusterMarker cluster) {
		refresher.refresh(cluster);
	}

	private void recalculate() {
		for (int i = 0; i < clusters.size(); i++) {
			ClusterMarker cluster = clusters.valueAt(i);
			cluster.reset();
			cache.add(cluster);
		}
		clusters.clear();
		if (addMarkersDynamically) {
			calculateVisibleClusters();
		}
		for (DelegatingMarker marker : markers.keySet()) {
			addMarker(marker);
		}
		refresher.refreshAll();
	}

	private void addMarkersInVisibleRegion() {
		calculateVisibleClusters();
		for (DelegatingMarker marker : markers.keySet()) {
			boolean notInCluster = markers.get(marker) == null;
			if (notInCluster) {
				addMarker(marker);
			}
		}
		refresher.refreshAll();
	}

	private void calculateVisibleClusters() {
		Projection projection = map.getProjection();
		VisibleRegion visibleRegion = projection.getVisibleRegion();
		LatLngBounds bounds = visibleRegion.latLngBounds;
		visibleClusters[0] = convLat(bounds.southwest.latitude);
		visibleClusters[1] = convLng(bounds.southwest.longitude);
		visibleClusters[2] = convLat(bounds.northeast.latitude);
		visibleClusters[3] = convLng(bounds.northeast.longitude);
	}

	private long calculateClusterId(LatLng position) {
		long y = convLat(position.latitude);
		long x = convLng(position.longitude);
		long ret = (y << 32) + x;
		return ret;
	}

	private int convLat(double lat) {
		return (int) (SphericalMercator.scaleLatitude(lat) / clusterSize);
	}

	private int convLng(double lng) {
		return (int) (SphericalMercator.scaleLongitude(lng) / clusterSize);
	}

	private double calculateClusterSize(float zoom) {
		return baseClusterSize / (1 << Math.round(zoom));
	}
}
