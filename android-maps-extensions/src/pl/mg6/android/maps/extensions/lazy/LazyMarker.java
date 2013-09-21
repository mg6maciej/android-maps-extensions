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
package pl.mg6.android.maps.extensions.lazy;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class LazyMarker {

	public interface OnMarkerCreateListener {

		void onMarkerCreate(LazyMarker marker);
	}

	private Marker marker;
	private GoogleMap map;
	private MarkerOptions markerOptions;
	private OnMarkerCreateListener listener;

	public LazyMarker(GoogleMap map, MarkerOptions options) {
		this(map, options, null);
	}

	public LazyMarker(GoogleMap map, MarkerOptions options, OnMarkerCreateListener listener) {
		if (options.isVisible()) {
			createMarker(map, options, listener);
		} else {
			this.map = map;
			this.markerOptions = copy(options);
			this.listener = listener;
		}
	}

	@Deprecated
	public String getId() {
		createMarker();
		return marker.getId();
	}

	public Marker getMarker() {
		return marker;
	}

	public LatLng getPosition() {
		if (marker != null) {
			return marker.getPosition();
		} else {
			return markerOptions.getPosition();
		}
	}

	public float getRotation() {
		if (marker != null) {
			return marker.getRotation();
		} else {
			return markerOptions.getRotation();
		}
	}

	public String getSnippet() {
		if (marker != null) {
			return marker.getSnippet();
		} else {
			return markerOptions.getSnippet();
		}
	}

	public String getTitle() {
		if (marker != null) {
			return marker.getTitle();
		} else {
			return markerOptions.getTitle();
		}
	}

	public void hideInfoWindow() {
		if (marker != null) {
			marker.hideInfoWindow();
		}
	}

	public boolean isDraggable() {
		if (marker != null) {
			return marker.isDraggable();
		} else {
			return markerOptions.isDraggable();
		}
	}

	public boolean isFlat() {
		if (marker != null) {
			return marker.isFlat();
		} else {
			return markerOptions.isFlat();
		}
	}

	public boolean isInfoWindowShown() {
		if (marker != null) {
			return marker.isInfoWindowShown();
		} else {
			return false;
		}
	}

	public boolean isVisible() {
		if (marker != null) {
			return marker.isVisible();
		} else {
			return false;
		}
	}

	public void remove() {
		if (marker != null) {
			marker.remove();
			marker = null;
		} else {
			map = null;
			markerOptions = null;
			listener = null;
		}
	}

	public void setAnchor(float anchorU, float anchorV) {
		if (marker != null) {
			marker.setAnchor(anchorU, anchorV);
		} else {
			markerOptions.anchor(anchorU, anchorV);
		}
	}

	public void setDraggable(boolean draggable) {
		if (marker != null) {
			marker.setDraggable(draggable);
		} else {
			markerOptions.draggable(draggable);
		}
	}

	public void setFlat(boolean flat) {
		if (marker != null) {
			marker.setFlat(flat);
		} else {
			markerOptions.flat(flat);
		}
	}

	public void setIcon(BitmapDescriptor icon) {
		if (marker != null) {
			marker.setIcon(icon);
		} else {
			markerOptions.icon(icon);
		}
	}

	public void setInfoWindowAnchor(float anchorU, float anchorV) {
		if (marker != null) {
			marker.setInfoWindowAnchor(anchorU, anchorV);
		} else {
			markerOptions.infoWindowAnchor(anchorU, anchorV);
		}
	}

	public void setPosition(LatLng position) {
		if (marker != null) {
			marker.setPosition(position);
		} else {
			markerOptions.position(position);
		}
	}

	public void setRotation(float rotation) {
		if (marker != null) {
			marker.setRotation(rotation);
		} else {
			markerOptions.rotation(rotation);
		}
	}

	public void setSnippet(String snippet) {
		if (marker != null) {
			marker.setSnippet(snippet);
		} else {
			markerOptions.snippet(snippet);
		}
	}

	public void setTitle(String title) {
		if (marker != null) {
			marker.setTitle(title);
		} else {
			markerOptions.title(title);
		}
	}

	public void setVisible(boolean visible) {
		if (marker != null) {
			marker.setVisible(visible);
		} else if (visible) {
			markerOptions.visible(true);
			createMarker();
		}
	}

	public void showInfoWindow() {
		if (marker != null) {
			marker.showInfoWindow();
		}
	}

	private void createMarker() {
		if (marker == null) {
			createMarker(map, markerOptions, listener);
			map = null;
			markerOptions = null;
			listener = null;
		}
	}

	private void createMarker(GoogleMap map, MarkerOptions options, OnMarkerCreateListener listener) {
		marker = map.addMarker(options);
		if (listener != null) {
			listener.onMarkerCreate(this);
		}
	}

	private static MarkerOptions copy(MarkerOptions options) {
		MarkerOptions copy = new MarkerOptions();
		copy.anchor(options.getAnchorU(), options.getAnchorV());
		copy.draggable(options.isDraggable());
		copy.flat(options.isFlat());
		copy.icon(options.getIcon());
		copy.infoWindowAnchor(options.getInfoWindowAnchorU(), options.getInfoWindowAnchorV());
		copy.position(options.getPosition());
		copy.rotation(options.getRotation());
		copy.snippet(options.getSnippet());
		copy.title(options.getTitle());
		copy.visible(options.isVisible());
		return copy;
	}
}
