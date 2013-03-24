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
import android.support.v4.util.LongSparseArray;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.VisibleRegion;

class GridClusteringStrategy extends BaseClusteringStrategy {

	private boolean addMarkersDynamically;
	private GoogleMap map;
	private Map<DelegatingMarker, ClusterMarker> markers;
	private double clusterSize;
	private int[] visibleClusters = new int[4];

	private LongSparseArray<ClusterMarker> clusters = new LongSparseArray<ClusterMarker>();
	private List<ClusterMarker> cache = new ArrayList<ClusterMarker>();

	private ClusterRefresher refresher = new ClusterRefresher();

	public GridClusteringStrategy(ClusteringSettings settings, GoogleMap map, List<DelegatingMarker> markers) {
		super(settings, map);
		this.addMarkersDynamically = settings.isAddMarkersDynamically();
		this.map = map;
		this.markers = new HashMap<DelegatingMarker, ClusterMarker>();
		for (DelegatingMarker m : markers) {
			if (m.isVisible()) {
				this.markers.put(m, null);
			}
		}
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
		int y = (int) ((position.latitude + 90.0) / clusterSize);
		int x = (int) ((position.longitude + 180.0) / clusterSize);
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
		visibleClusters[0] = (int) ((bounds.southwest.latitude + 90.0) / clusterSize);
		visibleClusters[1] = (int) ((bounds.southwest.longitude + 180.0) / clusterSize);
		visibleClusters[2] = (int) ((bounds.northeast.latitude + 90.0) / clusterSize);
		visibleClusters[3] = (int) ((bounds.northeast.longitude + 180.0) / clusterSize);
	}

	private long calculateClusterId(LatLng position) {
		long y = (int) ((position.latitude + 90.0) / clusterSize);
		long x = (int) ((position.longitude + 180.0) / clusterSize);
		long ret = (y << 32) + x;
		return ret;
	}

	private double calculateClusterSize(float zoom) {
		return (1 << ((int) (23.5f - zoom))) / 100000.0;
	}
}
