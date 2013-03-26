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

import pl.mg6.android.maps.extensions.utils.SphericalMercator;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

class DebugHelper {

	private List<Polyline> gridLines = new ArrayList<Polyline>();

	void drawDebugGrid(GoogleMap map, double clusterSize) {
		cleanup();
		Projection projection = map.getProjection();
		LatLngBounds bounds = projection.getVisibleRegion().latLngBounds;
		double minX = -180 + clusterSize * (int) (SphericalMercator.scaleLatitude(bounds.southwest.latitude) / clusterSize);
		double minY = -180 + clusterSize * (int) (SphericalMercator.scaleLongitude(bounds.southwest.longitude) / clusterSize);
		double maxX = -180 + clusterSize * (int) (SphericalMercator.scaleLatitude(bounds.northeast.latitude) / clusterSize);
		double maxY = -180 + clusterSize * (int) (SphericalMercator.scaleLongitude(bounds.northeast.longitude) / clusterSize);

		for (double x = minX; x <= maxX; x += clusterSize) {
			gridLines.add(map.addPolyline(new PolylineOptions().width(1.0f).add(new LatLng(SphericalMercator.toLatitude(x), bounds.southwest.longitude),
					new LatLng(SphericalMercator.toLatitude(x), bounds.northeast.longitude))));
		}
		if (minY <= maxY) {
			for (double y = minY; y <= maxY; y += clusterSize) {
				gridLines.add(map.addPolyline(new PolylineOptions().width(1.0f).add(new LatLng(bounds.southwest.latitude, y),
						new LatLng(bounds.northeast.latitude, y))));
			}
		} else {
			for (double y = -180; y <= minY; y += clusterSize) {
				gridLines.add(map.addPolyline(new PolylineOptions().width(1.0f).add(new LatLng(bounds.southwest.latitude, y),
						new LatLng(bounds.northeast.latitude, y))));
			}
			for (double y = maxY; y < 180; y += clusterSize) {
				gridLines.add(map.addPolyline(new PolylineOptions().width(1.0f).add(new LatLng(bounds.southwest.latitude, y),
						new LatLng(bounds.northeast.latitude, y))));
			}
		}
	}

	void cleanup() {
		for (Polyline polyline : gridLines) {
			polyline.remove();
		}
		gridLines.clear();
	}
}
