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

public class SphericalMercator {

	private SphericalMercator() {
	}

	public static double fromLatitude(double latitude) {
		double radians = Math.toRadians(latitude + 90) / 2;
		return Math.toDegrees(Math.log(Math.tan(radians)));
	}

	public static double toLatitude(double mercator) {
		double radians = Math.atan(Math.exp(Math.toRadians(mercator)));
		return Math.toDegrees(2 * radians) - 90;
	}
}
