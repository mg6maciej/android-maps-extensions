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

import pl.mg6.android.maps.extensions.GroundOverlay;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

class DelegatingGroundOverlay implements GroundOverlay {

	private com.google.android.gms.maps.model.GroundOverlay real;
	private GroundOverlayManager manager;

	private Object data;

	DelegatingGroundOverlay(com.google.android.gms.maps.model.GroundOverlay real, GroundOverlayManager manager) {
		this.real = real;
		this.manager = manager;
	}

	@Override
	public float getBearing() {
		return real.getBearing();
	}

	@Override
	public LatLngBounds getBounds() {
		return real.getBounds();
	}

	@Override
	public Object getData() {
		return data;
	}

	@Override
	public float getHeight() {
		return real.getHeight();
	}

	@Deprecated
	@Override
	public String getId() {
		return real.getId();
	}

	@Override
	public LatLng getPosition() {
		return real.getPosition();
	}

	@Override
	public float getTransparency() {
		return real.getTransparency();
	}

	@Override
	public float getWidth() {
		return real.getWidth();
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
	public void setBearing(float bearing) {
		real.setBearing(bearing);
	}

	@Override
	public void setData(Object data) {
		this.data = data;
	}

	@Override
	public void setDimensions(float width, float height) {
		real.setDimensions(width, height);
	}

	@Override
	public void setDimensions(float width) {
		real.setDimensions(width);
	}

	@Override
	public void setPosition(LatLng position) {
		real.setPosition(position);
	}

	@Override
	public void setPositionFromBounds(LatLngBounds bounds) {
		real.setPositionFromBounds(bounds);
	}

	@Override
	public void setTransparency(float transparency) {
		real.setTransparency(transparency);
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
		if (!(o instanceof DelegatingGroundOverlay)) {
			return false;
		}
		DelegatingGroundOverlay other = (DelegatingGroundOverlay) o;
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
