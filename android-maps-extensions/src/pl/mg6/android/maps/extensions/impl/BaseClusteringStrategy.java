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
import java.util.List;

import pl.mg6.android.maps.extensions.ClusteringSettings;
import pl.mg6.android.maps.extensions.ClusteringSettings.IconProvider;
import android.util.SparseArray;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

abstract class BaseClusteringStrategy implements ClusteringStrategy {

	private final MarkerOptions markerOptions = new MarkerOptions();

	private SparseArray<List<Marker>> markersCache = new SparseArray<List<Marker>>();

	private BitmapDescriptor defaultIcon;
	private IconProvider iconProvider;
	private SparseArray<BitmapDescriptor> iconCache = new SparseArray<BitmapDescriptor>();

	private GoogleMap provider;

	public BaseClusteringStrategy(ClusteringSettings settings, GoogleMap realMap) {
		this.defaultIcon = settings.getDefaultIcon();
		this.iconProvider = settings.getIconProvider();
		this.provider = realMap;
	}

	@Override
	public void cleanup() {
		clearCache();
	}

	Marker getFromCacheOrCreate(int markersCount, LatLng position) {
		Marker marker = null;
		List<Marker> c = markersCache.get(markersCount);
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

	private BitmapDescriptor getIcon(int markersCount) {
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

	void putInCache(Marker virtual, int markersCount) {
		virtual.setVisible(false);
		List<Marker> c = markersCache.get(markersCount);
		if (c == null) {
			c = new ArrayList<Marker>();
			markersCache.put(markersCount, c);
		}
		c.add(virtual);
	}

	private void clearCache() {
		for (int i = 0; i < markersCache.size(); i++) {
			List<com.google.android.gms.maps.model.Marker> c = markersCache.valueAt(i);
			for (com.google.android.gms.maps.model.Marker v : c) {
				v.remove();
			}
		}
		markersCache.clear();
	}
}
