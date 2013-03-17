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
import pl.mg6.android.maps.extensions.ClusteringSettings.IconDataProvider;
import android.util.SparseArray;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

abstract class BaseClusteringStrategy implements ClusteringStrategy {

	private final MarkerOptions markerOptions = new MarkerOptions();

	private SparseArray<List<Marker>> markersCache = new SparseArray<List<Marker>>();

	private IconDataProvider iconDataProvider;
	private SparseArray<IconData> iconDataCache = new SparseArray<IconData>();

	private GoogleMap provider;

	public BaseClusteringStrategy(ClusteringSettings settings, GoogleMap realMap) {
		this.iconDataProvider = settings.getIconDataProvider();
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
			IconData iconData = getIconData(markersCount);
			marker = provider.addMarker(markerOptions.position(position).icon(iconData.icon).anchor(iconData.horizAnchor, iconData.vertAnchor));
		}
		return marker;
	}

	private IconData getIconData(int markersCount) {
		IconData iconData = iconDataCache.get(markersCount);
		if (iconData == null) {
			MarkerOptions markerOptions = iconDataProvider.getIconData(markersCount);
			iconData = new IconData(markerOptions);
			iconDataCache.put(markersCount, iconData);
		}
		return iconData;
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

	private static class IconData {

		private BitmapDescriptor icon;

		private float horizAnchor;

		private float vertAnchor;

		private IconData(MarkerOptions markerOptions) {
			icon = markerOptions.getIcon();
			horizAnchor = markerOptions.getAnchorU();
			vertAnchor = markerOptions.getAnchorV();
		}
	}
}
