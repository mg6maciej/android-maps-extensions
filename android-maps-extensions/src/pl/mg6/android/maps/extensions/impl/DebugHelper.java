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

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

class DebugHelper {

	private List<Polyline> gridLines = new ArrayList<Polyline>();

	void drawDebugGrid(IGoogleMap map, double clusterSize) {
		cleanup();
		IProjection projection = map.getProjection();
		LatLngBounds bounds = projection.getVisibleRegion().latLngBounds;
		double minY = -180 + clusterSize * (int) (SphericalMercator.scaleLatitude(bounds.southwest.latitude) / clusterSize);
		double minX = -180 + clusterSize * (int) (SphericalMercator.scaleLongitude(bounds.southwest.longitude) / clusterSize);
		double maxY = -180 + clusterSize * (int) (SphericalMercator.scaleLatitude(bounds.northeast.latitude) / clusterSize);
		double maxX = -180 + clusterSize * (int) (SphericalMercator.scaleLongitude(bounds.northeast.longitude) / clusterSize);

		for (double y = minY; y <= maxY; y += clusterSize) {
			gridLines.add(map.addPolyline(new PolylineOptions().width(1.0f).add(new LatLng(SphericalMercator.toLatitude(y), bounds.southwest.longitude),
					new LatLng(SphericalMercator.toLatitude(y), bounds.northeast.longitude))));
		}
		if (minX <= maxX) {
			for (double x = minX; x <= maxX; x += clusterSize) {
				gridLines.add(map.addPolyline(new PolylineOptions().width(1.0f).add(new LatLng(bounds.southwest.latitude, x),
						new LatLng(bounds.northeast.latitude, x))));
			}
		} else {
			for (double x = -180; x <= minX; x += clusterSize) {
				gridLines.add(map.addPolyline(new PolylineOptions().width(1.0f).add(new LatLng(bounds.southwest.latitude, x),
						new LatLng(bounds.northeast.latitude, x))));
			}
			for (double x = maxX; x < 180; x += clusterSize) {
				gridLines.add(map.addPolyline(new PolylineOptions().width(1.0f).add(new LatLng(bounds.southwest.latitude, x),
						new LatLng(bounds.northeast.latitude, x))));
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
