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

import pl.mg6.android.maps.extensions.Marker;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

class ClusterMarker implements Marker {

	private static final MarkerOptions SINGLE_INSTANCE = new MarkerOptions();

	private int clusterId;

	private GridClusteringStrategy provider;

	private com.google.android.gms.maps.model.Marker virtual;

	private List<DelegatingMarker> markers = new ArrayList<DelegatingMarker>();

	public ClusterMarker(int clusterId, GridClusteringStrategy provider) {
		this.clusterId = clusterId;
		this.provider = provider;
	}

	public int getClusterId() {
		return clusterId;
	}

	com.google.android.gms.maps.model.Marker getVirtual() {
		return virtual;
	}

	void add(DelegatingMarker marker) {
		markers.add(marker);
	}

	void remove(DelegatingMarker marker) {
		markers.remove(marker);
	}

	void refresh() {
		Object markerOrBounds = getSingleVisibleMarkerOrBounds();
		if (markerOrBounds instanceof DelegatingMarker) {
			if (virtual != null) {
				virtual.setVisible(false);
			}
			((DelegatingMarker) markerOrBounds).changeVisible(true);
		} else {
			if (markerOrBounds instanceof LatLngBounds) {
				LatLng position = calculateCenter((LatLngBounds) markerOrBounds);
				if (virtual == null) {
					BitmapDescriptor icon = provider.getIcon(this);
					virtual = provider.addMarker(SINGLE_INSTANCE.position(position).icon(icon));
				} else {
					virtual.setPosition(position);
					virtual.setVisible(true);
				}
			} else {
				if (virtual != null) {
					virtual.setVisible(false);
				}
			}
			for (DelegatingMarker m : markers) {
				m.changeVisible(false);
			}
		}
	}

	LatLng calculateCenter(LatLngBounds bounds) {
		if (bounds.southwest.longitude > bounds.northeast.longitude) {
			// TODO: incorrect
			return new LatLng((bounds.southwest.latitude + bounds.northeast.latitude) / 2.0, (bounds.southwest.longitude + bounds.northeast.longitude) / 2.0);
		} else {
			return new LatLng((bounds.southwest.latitude + bounds.northeast.latitude) / 2.0, (bounds.southwest.longitude + bounds.northeast.longitude) / 2.0);
		}
	}

	void cleanup() {
		if (virtual != null) {
			virtual.remove();
		}
	}

	private Object getSingleVisibleMarkerOrBounds() {
		DelegatingMarker marker = null;
		LatLngBounds.Builder builder = null;
		for (DelegatingMarker m : markers) {
			if (m.isVisible()) {
				if (builder != null) {
					builder.include(m.getPosition());
				} else if (marker != null) {
					builder = LatLngBounds.builder();
					builder.include(marker.getPosition());
					builder.include(m.getPosition());
				} else {
					marker = m;
				}
			}
		}
		if (builder != null) {
			return builder.build();
		} else {
			return marker;
		}
	}

	@Override
	public Object getData() {
		throw new UnsupportedOperationException();
	}

	@Deprecated
	@Override
	public String getId() {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<Marker> getMarkers() {
		return new ArrayList<Marker>(markers);
	}

	@Override
	public LatLng getPosition() {
		if (virtual != null && virtual.isVisible()) {
			return virtual.getPosition();
		}
		throw new UnsupportedOperationException();
	}

	@Override
	public String getSnippet() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getTitle() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void hideInfoWindow() {
		if (virtual != null && virtual.isVisible()) {
			virtual.hideInfoWindow();
		}
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isCluster() {
		return true;
	}

	@Override
	public boolean isDraggable() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isInfoWindowShown() {
		if (virtual != null && virtual.isVisible()) {
			return virtual.isInfoWindowShown();
		}
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isVisible() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setData(Object data) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setDraggable(boolean draggable) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setPosition(LatLng position) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setSnippet(String snippet) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setTitle(String title) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setVisible(boolean visible) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void showInfoWindow() {
		if (virtual != null && virtual.isVisible()) {
			virtual.showInfoWindow();
		}
		throw new UnsupportedOperationException();
	}
}
