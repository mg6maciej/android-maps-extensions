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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pl.mg6.android.maps.extensions.ClusteringSettings;
import pl.mg6.android.maps.extensions.ClusteringSettings.IconProvider;
import pl.mg6.android.maps.extensions.Marker;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.SparseArray;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

class GridClusteringStrategy implements ClusteringStrategy {

	private MarkerOptions markerOptions = new MarkerOptions();

	private GoogleMap provider;
	private Map<DelegatingMarker, ClusterMarker> markers;
	private double clusterSize;

	private SparseArray<ClusterMarker> clusters = new SparseArray<ClusterMarker>();
	private List<ClusterMarker> cache = new ArrayList<ClusterMarker>();
	private SparseArray<List<com.google.android.gms.maps.model.Marker>> virualMarkersCache = new SparseArray<List<com.google.android.gms.maps.model.Marker>>();

	private BitmapDescriptor defaultIcon;
	private IconProvider iconProvider;

	private SparseArray<BitmapDescriptor> iconCache = new SparseArray<BitmapDescriptor>();

	private Set<ClusterMarker> refreshQueue = new HashSet<ClusterMarker>();
	private boolean refreshPending;
	private Handler refresher = new Handler(new Callback() {
		public boolean handleMessage(Message msg) {
			for (ClusterMarker cluster : refreshQueue) {
				cluster.refresh();
			}
			refreshQueue.clear();
			refreshPending = false;
			return true;
		}
	});

	public GridClusteringStrategy(ClusteringSettings settings, GoogleMap provider, List<DelegatingMarker> markers) {
		this.defaultIcon = settings.getDefaultIcon();
		this.iconProvider = settings.getIconProvider();
		this.provider = provider;
		this.markers = new HashMap<DelegatingMarker, ClusterMarker>();
		for (DelegatingMarker m : markers) {
			this.markers.put(m, null);
		}
		this.clusterSize = calculateClusterSize(provider.getCameraPosition().zoom);
		recalculate();
	}

	@Override
	public void cleanup() {
		for (int i = 0; i < clusters.size(); i++) {
			ClusterMarker cluster = clusters.valueAt(i);
			cluster.cleanup();
		}
		clearCache();
		for (DelegatingMarker marker : markers.keySet()) {
			if (marker.isVisible()) {
				marker.changeVisible(true);
			}
		}
		refresher.removeMessages(0);
	}

	@Override
	public void onZoomChange(float zoom) {
		double clusterSize = calculateClusterSize(zoom);
		if (this.clusterSize != clusterSize) {
			this.clusterSize = clusterSize;
			recalculate();
		}
	}

	@Override
	public void onAdd(DelegatingMarker marker) {
		addMarker(marker, true);
	}

	private void addMarker(DelegatingMarker marker, boolean refresh) {
		LatLng position = marker.getPosition();
		int clusterId = calculateClusterId(position);
		ClusterMarker cluster = findClusterById(clusterId);
		cluster.add(marker);
		markers.put(marker, cluster);
		if (refresh && marker.isVisible()) {
			refresh(cluster);
		}
	}

	@Override
	public void onRemove(DelegatingMarker marker) {
		ClusterMarker cluster = markers.remove(marker);
		cluster.remove(marker);
		refresh(cluster);
	}

	@Override
	public void onPositionChange(DelegatingMarker marker) {
		ClusterMarker oldCluster = markers.get(marker);
		if (isMarkerInCluster(marker, oldCluster)) {
			if (marker.isVisible()) {
				refresh(oldCluster);
			}
		} else {
			oldCluster.remove(marker);
			refresh(oldCluster);
			addMarker(marker, true);
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
		int clusterId = cluster.getClusterId();
		int markerClusterId = calculateClusterId(marker.getPosition());
		return clusterId == markerClusterId;
	}

	private ClusterMarker findClusterById(int clusterId) {
		ClusterMarker cluster = clusters.get(clusterId);
		if (cluster == null) {
			if (cache.size() > 0) {
				cluster = cache.remove(cache.size() - 1);
				cluster.setClusterId(clusterId);
			} else {
				cluster = new ClusterMarker(clusterId, this);
			}
			clusters.put(clusterId, cluster);
		}
		return cluster;
	}

	@Override
	public void onVisibilityChangeRequest(DelegatingMarker marker, boolean visible) {
		ClusterMarker cluster = markers.get(marker);
		refresh(cluster);
	}

	com.google.android.gms.maps.model.Marker addMarker(MarkerOptions options) {
		return provider.addMarker(options);
	}

	BitmapDescriptor getIcon(int markersCount) {
		BitmapDescriptor icon = iconCache.get(markersCount);
		if (icon == null) {
			if (iconProvider != null) {
				icon = iconProvider.getIcon(markersCount);
			}
			if (icon == null) {
				icon = defaultIcon;
			}
			iconCache.put(markersCount, icon);
		}
		return icon;
	}

	private void refresh(ClusterMarker cluster) {
		refreshQueue.add(cluster);
		if (!refreshPending) {
			refresher.sendEmptyMessage(0);
			refreshPending = true;
		}
	}

	private void recalculate() {
		for (int i = 0; i < clusters.size(); i++) {
			ClusterMarker cluster = clusters.valueAt(i);
			cluster.reset();
			cache.add(cluster);
		}
		clusters.clear();
		if (clusterSize == 0.0) {
			for (DelegatingMarker marker : markers.keySet()) {
				markers.put(marker, null);
				if (marker.isVisible()) {
					marker.changeVisible(true);
				}
			}
		} else {
			for (DelegatingMarker marker : markers.keySet()) {
				addMarker(marker, false);
			}
			for (int i = 0; i < clusters.size(); i++) {
				ClusterMarker cluster = clusters.valueAt(i);
				refresh(cluster);
			}
		}
	}

	private int calculateClusterId(LatLng position) {
		int y = (int) ((position.latitude + 180.0) / clusterSize);
		int x = (int) ((position.longitude + 90.0) / clusterSize);
		return (y << 16) + x;
	}

	private double calculateClusterSize(float zoom) {
		return (1 << ((int) (23.5f - zoom))) / 100000.0;
	}

	com.google.android.gms.maps.model.Marker getVirtualByCount(LatLng position, int markersCount) {
		com.google.android.gms.maps.model.Marker marker = null;
		List<com.google.android.gms.maps.model.Marker> c = virualMarkersCache.get(markersCount);
		if (c != null && c.size() > 0) {
			marker = c.remove(c.size() - 1);
			marker.setPosition(position);
			marker.setVisible(true);
		} else {
			BitmapDescriptor icon = getIcon(markersCount);
			marker = provider.addMarker(markerOptions.position(position).icon(icon).anchor(0.5f, 0.5f));
		}
		return marker;
	}

	void cacheVirtual(com.google.android.gms.maps.model.Marker virtual, int markersCount) {
		if (virtual != null) {
			virtual.setVisible(false);
			List<com.google.android.gms.maps.model.Marker> c = virualMarkersCache.get(markersCount);
			if (c == null) {
				c = new ArrayList<com.google.android.gms.maps.model.Marker>();
				virualMarkersCache.put(markersCount, c);
			}
			c.add(virtual);
		}
	}

	private void clearCache() {
		for (int i = 0; i < virualMarkersCache.size(); i++) {
			List<com.google.android.gms.maps.model.Marker> c = virualMarkersCache.valueAt(i);
			for (com.google.android.gms.maps.model.Marker v : c) {
				v.remove();
			}
		}
		virualMarkersCache.clear();
	}
}
