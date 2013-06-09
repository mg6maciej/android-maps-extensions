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

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;

import pl.mg6.android.maps.extensions.AnimationSettings;
import pl.mg6.android.maps.extensions.Marker;

class MarkerAdapter implements Marker {

	private com.google.android.gms.maps.model.Marker marker;

	public MarkerAdapter(com.google.android.gms.maps.model.Marker marker) {
		this.marker = marker;
	}

	@Override
	public void animatePosition(LatLng target) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void animatePosition(LatLng target, AnimationSettings settings) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object getData() {
		throw new UnsupportedOperationException();
	}

	@Override
	@Deprecated
	public String getId() {
		return marker.getId();
	}

	@Override
	public List<Marker> getMarkers() {
		throw new UnsupportedOperationException();
	}

	@Override
	public LatLng getPosition() {
		return marker.getPosition();
	}

	@Override
	public String getSnippet() {
		return marker.getSnippet();
	}

	@Override
	public String getTitle() {
		return marker.getTitle();
	}

	@Override
	public void hideInfoWindow() {
		marker.hideInfoWindow();
	}

	@Override
	public boolean isCluster() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isDraggable() {
		return marker.isDraggable();
	}

	@Override
	public boolean isInfoWindowShown() {
		return marker.isInfoWindowShown();
	}

	@Override
	public boolean isVisible() {
		return marker.isVisible();
	}

	@Override
	public void remove() {
		marker.remove();
	}

	@Override
	public void setAnchor(float anchorU, float anchorV) {
		marker.setAnchor(anchorU, anchorV);
	}

	@Override
	public void setData(Object data) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setDraggable(boolean draggable) {
		marker.setDraggable(draggable);
	}

	@Override
	public void setIcon(BitmapDescriptor icon) {
		marker.setIcon(icon);
	}

	@Override
	public void setPosition(LatLng position) {
		marker.setPosition(position);
	}

	@Override
	public void setSnippet(String snippet) {
		marker.setSnippet(snippet);
	}

	@Override
	public void setTitle(String title) {
		marker.setTitle(title);
	}

	@Override
	public void setVisible(boolean visible) {
		marker.setVisible(visible);
	}

	@Override
	public void showInfoWindow() {
		marker.showInfoWindow();
	}

	com.google.android.gms.maps.model.Marker getMarker() {
		return marker;
	}
}
