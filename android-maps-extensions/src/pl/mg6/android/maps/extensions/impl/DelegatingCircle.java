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

import pl.mg6.android.maps.extensions.Circle;
import pl.mg6.android.maps.extensions.utils.LatLngUtils;

import com.google.android.gms.maps.model.LatLng;

class DelegatingCircle implements Circle {

	private com.google.android.gms.maps.model.Circle real;
	private CircleManager manager;

	private Object data;

	DelegatingCircle(com.google.android.gms.maps.model.Circle real, CircleManager manager) {
		this.real = real;
		this.manager = manager;
	}

	@Override
	public boolean contains(LatLng position) {
		LatLng center = getCenter();
		double radius = getRadius();
		float distance = LatLngUtils.distanceBetween(position, center);
		return distance < radius;
	}

	@Override
	public LatLng getCenter() {
		return real.getCenter();
	}

	@Override
	public Object getData() {
		return data;
	}

	@Override
	public int getFillColor() {
		return real.getFillColor();
	}

	@Deprecated
	@Override
	public String getId() {
		return real.getId();
	}

	@Override
	public double getRadius() {
		return real.getRadius();
	}

	@Override
	public int getStrokeColor() {
		return real.getStrokeColor();
	}

	@Override
	public float getStrokeWidth() {
		return real.getStrokeWidth();
	}

	@Override
	public float getZIndex() {
		return real.getZIndex();
	}

	@Override
	public boolean isVisible() {
		return real.isVisible();
	}

	@Override
	public void remove() {
		manager.onRemove(real);
		real.remove();
	}

	@Override
	public void setCenter(LatLng center) {
		real.setCenter(center);
	}

	@Override
	public void setData(Object data) {
		this.data = data;
	}

	@Override
	public void setFillColor(int fillColor) {
		real.setFillColor(fillColor);
	}

	@Override
	public void setRadius(double radius) {
		real.setRadius(radius);
	}

	@Override
	public void setStrokeColor(int strokeColor) {
		real.setStrokeColor(strokeColor);
	}

	@Override
	public void setStrokeWidth(float strokeWidth) {
		real.setStrokeWidth(strokeWidth);
	}

	@Override
	public void setVisible(boolean visible) {
		real.setVisible(visible);
	}

	@Override
	public void setZIndex(float zIndex) {
		real.setZIndex(zIndex);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof DelegatingCircle)) {
			return false;
		}
		DelegatingCircle other = (DelegatingCircle) o;
		return real.equals(other.real);
	}

	@Override
	public int hashCode() {
		return real.hashCode();
	}

	@Override
	public String toString() {
		return real.toString();
	}
}
