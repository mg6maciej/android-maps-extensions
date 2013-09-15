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
package pl.mg6.android.maps.extensions;

import com.google.android.gms.maps.model.LatLng;

public class CircleOptions {

	public final com.google.android.gms.maps.model.CircleOptions real = new com.google.android.gms.maps.model.CircleOptions();
	private Object data;

	public CircleOptions center(LatLng center) {
		real.center(center);
		return this;
	}

	public CircleOptions data(Object data) {
		this.data = data;
		return this;
	}

	public CircleOptions fillColor(int color) {
		real.fillColor(color);
		return this;
	}

	public LatLng getCenter() {
		return real.getCenter();
	}

	public Object getData() {
		return data;
	}

	public int getFillColor() {
		return real.getFillColor();
	}

	public double getRadius() {
		return real.getRadius();
	}

	public int getStrokeColor() {
		return real.getStrokeColor();
	}

	public float getStrokeWidth() {
		return real.getStrokeWidth();
	}

	public float getZIndex() {
		return real.getZIndex();
	}

	public boolean isVisible() {
		return real.isVisible();
	}

	public CircleOptions radius(double radius) {
		real.radius(radius);
		return this;
	}

	public CircleOptions strokeColor(int color) {
		real.strokeColor(color);
		return this;
	}

	public CircleOptions strokeWidth(float width) {
		real.strokeWidth(width);
		return this;
	}

	public CircleOptions visible(boolean visible) {
		real.visible(visible);
		return this;
	}

	public CircleOptions zIndex(float zIndex) {
		real.zIndex(zIndex);
		return this;
	}
}
