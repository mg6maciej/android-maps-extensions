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

import pl.mg6.android.maps.extensions.Marker;
import pl.mg6.android.maps.extensions.lazy.LazyMarker;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;

class DelegatingMarker implements Marker {

	private LazyMarker real;
	private DelegatingGoogleMap map;

	private Object data;

	private boolean visible;

	DelegatingMarker(LazyMarker real, DelegatingGoogleMap map) {
		this.real = real;
		this.map = map;

		this.visible = real.isVisible();
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
	public List<Marker> getMarkers() {
		return null;
	}

	@Override
	public LatLng getPosition() {
		return real.getPosition();
	}

	@Override
	public String getSnippet() {
		return real.getSnippet();
	}

	@Override
	public String getTitle() {
		return real.getTitle();
	}

	@Override
	public void hideInfoWindow() {
		real.hideInfoWindow();
	}

	@Override
	public boolean isCluster() {
		return false;
	}

	@Override
	public boolean isDraggable() {
		return real.isDraggable();
	}

	@Override
	public boolean isInfoWindowShown() {
		return real.isInfoWindowShown();
	}

	@Override
	public boolean isVisible() {
		return visible;
	}

	@Override
	public void remove() {
		real.remove();
		map.onRemove(this);
	}

	@Override
	public void setAnchor(float anchorU, float anchorV) {
		real.setAnchor(anchorU, anchorV);
	}

	@Override
	public void setData(Object data) {
		this.data = data;
	}

	@Override
	public void setDraggable(boolean draggable) {
		real.setDraggable(draggable);
	}

	@Override
	public void setIcon(BitmapDescriptor icon) {
		real.setIcon(icon);
	}

	@Override
	public void setPosition(LatLng position) {
		real.setPosition(position);
		map.onPositionChange(this);
	}

	@Override
	public void setSnippet(String snippet) {
		real.setSnippet(snippet);
	}

	@Override
	public void setTitle(String title) {
		real.setTitle(title);
	}

	@Override
	public void setVisible(boolean visible) {
		if (this.visible != visible) {
			this.visible = visible;
			map.onVisibilityChangeRequest(this, visible);
		}
	}

	@Override
	public void showInfoWindow() {
		map.onShowInfoWindow(this);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof DelegatingMarker)) {
			return false;
		}
		DelegatingMarker other = (DelegatingMarker) o;
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

	LazyMarker getReal() {
		return real;
	}

	void changeVisible(boolean visible) {
		real.setVisible(this.visible && visible);
	}

	void forceShowInfoWindow() {
		real.showInfoWindow();
	}
}
