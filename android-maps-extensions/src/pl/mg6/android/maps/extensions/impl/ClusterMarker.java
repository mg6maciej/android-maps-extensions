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

import com.google.android.gms.maps.model.LatLng;

import pl.mg6.android.maps.extensions.Marker;

class ClusterMarker implements Marker {

	private com.google.android.gms.maps.model.Marker virtual;
	
	private List<DelegatingMarker> markers = new ArrayList<DelegatingMarker>();
	
	public ClusterMarker(com.google.android.gms.maps.model.Marker virtual) {
		this.virtual = virtual;
	}
	
	void add(DelegatingMarker marker) {
		markers.add(marker);
	}
	
	void fixVisibility() {
		DelegatingMarker marker = getSingleVisibleMarker();
		if (marker != null) {
			virtual.setVisible(false);
			marker.changeVisible(true);
		} else {
			virtual.setVisible(true);
			for (DelegatingMarker m : markers) {
				m.changeVisible(false);
			}
		}
	}
	
	void cleanup() {
		virtual.remove();
	}
	
	private DelegatingMarker getSingleVisibleMarker() {
		DelegatingMarker marker = null;
		for (DelegatingMarker m : markers) {
			if (m.isVisible()) {
				if (marker != null) {
					return null;
				}
				marker = m;
			}
		}
		return marker;
	}
	
	@Override
	public Object getData() {
		return null;
	}

	@Deprecated
	@Override
	public String getId() {
		return virtual.getId();
	}

	@Override
	public LatLng getPosition() {
		return null;
	}

	@Override
	public String getSnippet() {
		return null;
	}

	@Override
	public String getTitle() {
		return null;
	}

	@Override
	public void hideInfoWindow() {

	}

	@Override
	public boolean isDraggable() {
		return false;
	}

	@Override
	public boolean isInfoWindowShown() {
		return false;
	}

	@Override
	public boolean isVisible() {
		return false;
	}

	@Override
	public void remove() {

	}

	@Override
	public void setData(Object data) {

	}

	@Override
	public void setDraggable(boolean draggable) {

	}

	@Override
	public void setPosition(LatLng position) {

	}

	@Override
	public void setSnippet(String snippet) {

	}

	@Override
	public void setTitle(String title) {

	}

	@Override
	public void setVisible(boolean visible) {

	}

	@Override
	public void showInfoWindow() {

	}
}
