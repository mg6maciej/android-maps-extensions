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

import pl.mg6.android.maps.extensions.AnimationSettings;
import pl.mg6.android.maps.extensions.Marker;
import pl.mg6.android.maps.extensions.lazy.LazyMarker;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;

class DelegatingMarker implements Marker {

	private LazyMarker real;
	private MarkerManager manager;

	private int clusterGroup;
	private Object data;

	private LatLng position;
	private boolean visible;

	DelegatingMarker(LazyMarker real, MarkerManager manager) {
		this.real = real;
		this.manager = manager;

		this.position = real.getPosition();
		this.visible = real.isVisible();
	}

	@Override
	public void animatePosition(LatLng target) {
		animatePosition(target, new AnimationSettings(), null);
	}

	@Override
	public void animatePosition(LatLng target, AnimationSettings settings) {
		animatePosition(target, settings, null);
	}

	@Override
	public void animatePosition(LatLng target, AnimationCallback callback) {
		animatePosition(target, new AnimationSettings(), callback);
	}

	@Override
	public void animatePosition(LatLng target, AnimationSettings settings, AnimationCallback callback) {
		if (target == null || settings == null) {
			throw new IllegalArgumentException();
		}
		manager.onAnimateMarkerPosition(this, target, settings, callback);
	}

	@Override
	public int getClusterGroup() {
		return clusterGroup;
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
		if (position == null) {
			position = real.getPosition();
		}
		return position;
	}

	@Override
	public float getRotation() {
		return real.getRotation();
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
	public boolean isFlat() {
		return real.isFlat();
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
		manager.onRemove(this);
		real.remove();
	}

	@Override
	public void setAnchor(float anchorU, float anchorV) {
		real.setAnchor(anchorU, anchorV);
	}

	@Override
	public void setClusterGroup(int clusterGroup) {
		if (this.clusterGroup != clusterGroup) {
			this.clusterGroup = clusterGroup;
			manager.onClusterGroupChange(this);
		}
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
	public void setFlat(boolean flat) {
		real.setFlat(flat);
	}

	@Override
	public void setIcon(BitmapDescriptor icon) {
		real.setIcon(icon);
	}

	@Override
	public void setInfoWindowAnchor(float anchorU, float anchorV) {
		real.setInfoWindowAnchor(anchorU, anchorV);
	}

	@Override
	public void setPosition(LatLng position) {
		this.position = position;
		real.setPosition(position);
		manager.onPositionChange(this);
	}

	void setPositionDuringAnimation(LatLng position) {
		this.position = position;
		real.setPosition(position);
		manager.onPositionDuringAnimationChange(this);
	}

	@Override
	public void setRotation(float rotation) {
		real.setRotation(rotation);
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
			manager.onVisibilityChangeRequest(this, visible);
		}
	}

	@Override
	public void showInfoWindow() {
		manager.onShowInfoWindow(this);
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

	void clearCachedPosition() {
		position = null;
	}

	void forceShowInfoWindow() {
		real.showInfoWindow();
	}

	void setVirtualPosition(LatLng position) {
		real.setPosition(position);
	}
}
