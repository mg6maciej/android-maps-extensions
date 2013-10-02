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
package pl.mg6.android.maps.extensions.utils;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

public final class LatLngUtils {

	private LatLngUtils() {
	}

	public static float distanceBetween(LatLng first, LatLng second) {
		float[] distance = new float[1];
		Location.distanceBetween(first.latitude, first.longitude, second.latitude, second.longitude, distance);
		return distance[0];
	}

	public static LatLng fromLocation(Location location) {
		return new LatLng(location.getLatitude(), location.getLongitude());
	}
}
