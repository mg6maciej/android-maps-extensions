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

import java.util.List;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;

public interface Marker {

	interface AnimationCallback {

		enum CancelReason {
			ANIMATE_POSITION,
			DRAG_START,
			REMOVE,
			SET_POSITION,
		}

		void onFinish(Marker marker);

		void onCancel(Marker marker, CancelReason reason);
	}

	void animatePosition(LatLng target);

	void animatePosition(LatLng target, AnimationSettings settings);

	void animatePosition(LatLng target, AnimationCallback callback);

	void animatePosition(LatLng target, AnimationSettings settings, AnimationCallback callback);

	int getClusterGroup();

	/**
	 * WARNING: may be changed in future API when this is fixed: http://code.google.com/p/gmaps-api-issues/issues/detail?id=4650
	 */
	Object getData();

	/**
	 * http://code.google.com/p/gmaps-api-issues/issues/detail?id=5101
	 */
	@Deprecated
	String getId();

	/**
	 * @return list of markers inside cluster when isCluster() returns true, null otherwise
	 */
	List<Marker> getMarkers();

	LatLng getPosition();

	float getRotation();

	String getSnippet();

	String getTitle();

	void hideInfoWindow();

	/**
	 * @return true if this marker groups other markers, false otherwise
	 */
	boolean isCluster();

	boolean isDraggable();

	boolean isFlat();

	boolean isInfoWindowShown();

	boolean isVisible();

	void remove();

	void setAnchor(float anchorU, float anchorV);

	void setClusterGroup(int clusterGroup);

	/**
	 * WARNING: may be changed in future API when this is fixed: http://code.google.com/p/gmaps-api-issues/issues/detail?id=4650
	 */
	void setData(Object data);

	void setDraggable(boolean draggable);

	void setFlat(boolean flat);

	void setIcon(BitmapDescriptor icon);

	void setInfoWindowAnchor(float anchorU, float anchorV);

	void setPosition(LatLng position);

	void setRotation(float rotation);

	void setSnippet(String snippet);

	void setTitle(String title);

	void setVisible(boolean visible);

	void showInfoWindow();
}