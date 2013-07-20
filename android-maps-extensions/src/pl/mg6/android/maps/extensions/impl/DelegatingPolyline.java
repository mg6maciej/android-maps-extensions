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

import java.util.List;

import pl.mg6.android.maps.extensions.Polyline;

import com.google.android.gms.maps.model.LatLng;

class DelegatingPolyline implements Polyline {

	private com.google.android.gms.maps.model.Polyline real;
	private PolylineManager manager;

	private Object data;

	DelegatingPolyline(com.google.android.gms.maps.model.Polyline real, PolylineManager manager) {
		this.real = real;
		this.manager = manager;
	}

	@Override
	public int getColor() {
		return real.getColor();
	}

	@Override
	public Object getData() {
		return data;
	}

	@Deprecated
	@Override
	public String getId() {
		return real.getId();
	}

	@Override
	public List<LatLng> getPoints() {
		return real.getPoints();
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
	public boolean isGeodesic() {
		return real.isGeodesic();
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
	public void setColor(int color) {
		real.setColor(color);
	}

	@Override
	public void setData(Object data) {
		this.data = data;
	}

	@Override
	public void setGeodesic(boolean geodesic) {
		real.setGeodesic(geodesic);
	}

	@Override
	public void setPoints(List<LatLng> points) {
		real.setPoints(points);
	}

	@Override
	public void setVisible(boolean visible) {
		real.setVisible(visible);
	}

	@Override
	public void setWidth(float width) {
		real.setWidth(width);
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
		if (!(o instanceof DelegatingPolyline)) {
			return false;
		}
		DelegatingPolyline other = (DelegatingPolyline) o;
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
