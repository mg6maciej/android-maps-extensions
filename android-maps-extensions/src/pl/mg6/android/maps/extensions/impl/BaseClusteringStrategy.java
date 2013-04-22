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
import android.support.v4.util.SparseArrayCompat;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

abstract class BaseClusteringStrategy implements ClusteringStrategy {

	private final MarkerOptions markerOptions = new MarkerOptions();

	private SparseArrayCompat<List<Marker>> markersCache = new SparseArrayCompat<List<Marker>>();

	private IconDataProvider iconDataProvider;
	private SparseArrayCompat<IconData> iconDataCache = new SparseArrayCompat<IconData>();

	private IGoogleMap map;

	public BaseClusteringStrategy(ClusteringSettings settings, IGoogleMap map) {
		this.iconDataProvider = settings.getIconDataProvider();
		this.map = map;
	}

	@Override
	public void cleanup() {
		clearCache();
	}

	Marker getFromCacheOrCreate(int markersCount, LatLng position) {
		Marker marker = null;
		List<Marker> cacheEntry = markersCache.get(markersCount);
		if (cacheEntry != null && cacheEntry.size() > 0) {
			marker = cacheEntry.remove(cacheEntry.size() - 1);
			marker.setPosition(position);
			marker.setVisible(true);
		} else {
			IconData iconData = getIconData(markersCount);
			marker = map.addMarker(markerOptions.position(position).icon(iconData.icon).anchor(iconData.horizAnchor, iconData.vertAnchor));
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

	void putInCache(Marker marker, int markersCount) {
		marker.setVisible(false);
		List<Marker> cacheEntry = markersCache.get(markersCount);
		if (cacheEntry == null) {
			cacheEntry = new ArrayList<Marker>();
			markersCache.put(markersCount, cacheEntry);
		}
		cacheEntry.add(marker);
	}

	private void clearCache() {
		for (int i = 0; i < markersCache.size(); i++) {
			List<Marker> cacheEntry = markersCache.valueAt(i);
			for (Marker marker : cacheEntry) {
				marker.remove();
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
