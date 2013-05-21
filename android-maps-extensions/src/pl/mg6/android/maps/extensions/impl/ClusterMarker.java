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

class ClusterMarker implements Marker {

	private long clusterId;

	private int lastCount = -1;

	private BaseClusteringStrategy strategy;

	private com.google.android.gms.maps.model.Marker virtual;

	private List<DelegatingMarker> markers = new ArrayList<DelegatingMarker>();

	public ClusterMarker(BaseClusteringStrategy strategy) {
		this.strategy = strategy;
	}

	long getClusterId() {
		return clusterId;
	}

	void setClusterId(long clusterId) {
		this.clusterId = clusterId;
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
		int count = markers.size();
		if (count == 0) {
			cacheVirtual();
		} else if (count == 1) {
			cacheVirtual();
			markers.get(0).changeVisible(true);
		} else {
			LatLngBounds.Builder builder = LatLngBounds.builder();
			for (DelegatingMarker m : markers) {
				builder.include(m.getPosition());
				m.changeVisible(false);
			}
			LatLng position = calculateCenter(builder.build());
			if (virtual == null || lastCount != count) {
				cacheVirtual();
				lastCount = count;
				virtual = strategy.getFromCacheOrCreate(count, position);
			} else {
				virtual.setPosition(position);
			}
		}
	}

	Marker getDisplayedMarker() {
		int count = markers.size();
		if (count == 0) {
			return null;
		} else if (count == 1) {
			return markers.get(0);
		} else {
			return this;
		}
	}

	void cacheVirtual() {
		if (virtual != null) {
			strategy.putInCache(virtual, lastCount);
			virtual = null;
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

	void reset() {
		markers.clear();
	}

	List<DelegatingMarker> getMarkersInternal() {
		return new ArrayList<DelegatingMarker>(markers);
	}

	@Override
	public Object getData() {
		return null;
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
		if (virtual != null) {
			return virtual.getPosition();
		}
		LatLngBounds.Builder builder = LatLngBounds.builder();
		for (DelegatingMarker m : markers) {
			builder.include(m.getPosition());
		}
		LatLng position = calculateCenter(builder.build());
		return position;
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
		if (virtual != null) {
			virtual.hideInfoWindow();
		}
	}

	@Override
	public boolean isCluster() {
		return true;
	}

	@Override
	public boolean isDraggable() {
		return false;
	}

	@Override
	public boolean isInfoWindowShown() {
		if (virtual != null) {
			return virtual.isInfoWindowShown();
		}
		return false;
	}

	@Override
	public boolean isVisible() {
		if (virtual != null) {
			return virtual.isVisible();
		}
		return false;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setAnchor(float anchorU, float anchorV) {
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
	public void setIcon(BitmapDescriptor icon) {
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
		if (virtual == null && markers.size() > 1) {
			refresh();
		}
		if (virtual != null) {
			virtual.showInfoWindow();
		}
	}
}
