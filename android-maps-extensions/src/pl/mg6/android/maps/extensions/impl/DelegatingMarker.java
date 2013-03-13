package pl.mg6.android.maps.extensions.impl;

import pl.mg6.android.maps.extensions.Marker;

import com.google.android.gms.maps.model.LatLng;

class DelegatingMarker implements Marker {

	private com.google.android.gms.maps.model.Marker real;
	private DelegatingGoogleMap map;

	private Object data;

	DelegatingMarker(com.google.android.gms.maps.model.Marker real, DelegatingGoogleMap map) {
		this.real = real;
		this.map = map;
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
	public boolean isDraggable() {
		return real.isDraggable();
	}

	@Override
	public boolean isInfoWindowShown() {
		return real.isInfoWindowShown();
	}

	@Override
	public boolean isVisible() {
		return real.isVisible();
	}

	@Override
	public void remove() {
		map.remove(real);
		real.remove();
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
	public void setPosition(LatLng position) {
		real.setPosition(position);
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
		real.setVisible(visible);
	}

	@Override
	public void showInfoWindow() {
		real.showInfoWindow();
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
}
