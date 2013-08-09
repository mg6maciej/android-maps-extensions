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

import com.google.android.gms.maps.model.CameraPosition;

class NoClusteringStrategy implements ClusteringStrategy {

	public NoClusteringStrategy(List<DelegatingMarker> markers) {
		for (DelegatingMarker marker : markers) {
			if (marker.isVisible()) {
				marker.changeVisible(true);
			}
		}
	}

	@Override
	public void cleanup() {

	}

	@Override
	public void onCameraChange(CameraPosition cameraPosition) {

	}

	@Override
	public void onClusterGroupChange(DelegatingMarker marker) {

	}

	@Override
	public void onAdd(DelegatingMarker marker) {

	}

	@Override
	public void onRemove(DelegatingMarker marker) {

	}

	@Override
	public void onPositionChange(DelegatingMarker marker) {

	}

	@Override
	public void onVisibilityChangeRequest(DelegatingMarker marker, boolean visible) {
		marker.changeVisible(visible);
	}

	@Override
	public void onShowInfoWindow(DelegatingMarker marker) {
		marker.forceShowInfoWindow();
	}

	@Override
	public Marker map(com.google.android.gms.maps.model.Marker original) {
		return null;
	}

	@Override
	public List<Marker> getDisplayedMarkers() {
		return null;
	}

	@Override
	public float getMinZoomLevelNotClustered(Marker marker) {
		return 0.0f;
	}
}
