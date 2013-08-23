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

import java.util.Arrays;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

public final class LatLngBoundsUtils {

	private LatLngBoundsUtils() {
	}

	public static LatLngBounds fromCenterAndPositions(LatLng center, Iterable<LatLng> positions) {
		LatLngBounds.Builder builder = LatLngBounds.builder();
		builder.include(center);
		for (LatLng position : positions) {
			LatLng other = new LatLng(2 * center.latitude - position.latitude, 2 * center.longitude - position.longitude);
			builder.include(position);
			builder.include(other);
		}
		return builder.build();
	}

	public static LatLngBounds fromCenterAndPositions(LatLng center, LatLng... positions) {
		return fromCenterAndPositions(center, Arrays.asList(positions));
	}
}
