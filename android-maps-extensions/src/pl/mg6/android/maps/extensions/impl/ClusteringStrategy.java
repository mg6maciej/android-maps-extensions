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

interface ClusteringStrategy {

	void cleanup();

	void onCameraChange(CameraPosition cameraPosition);

	void onClusterGroupChange(DelegatingMarker marker);

	void onAdd(DelegatingMarker marker);

	void onRemove(DelegatingMarker marker);

	void onPositionChange(DelegatingMarker marker);

	void onVisibilityChangeRequest(DelegatingMarker marker, boolean visible);

	void onShowInfoWindow(DelegatingMarker marker);

	Marker map(com.google.android.gms.maps.model.Marker original);

	List<Marker> getDisplayedMarkers();

	float getMinZoomLevelNotClustered(Marker marker);
}
