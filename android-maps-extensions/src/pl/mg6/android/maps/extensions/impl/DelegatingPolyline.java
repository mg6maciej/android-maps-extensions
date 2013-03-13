package pl.mg6.android.maps.extensions.impl;

import java.util.List;

import pl.mg6.android.maps.extensions.Polyline;

import com.google.android.gms.maps.model.LatLng;

class DelegatingPolyline implements Polyline {

	private com.google.android.gms.maps.model.Polyline real;
	private DelegatingGoogleMap map;

	private Object data;

	DelegatingPolyline(com.google.android.gms.maps.model.Polyline real, DelegatingGoogleMap map) {
		this.real = real;
		this.map = map;
	}

	@Override
	public int getColor() {
		return real.getColor();
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
	public List<LatLng> getPoints() {
		return real.getPoints();
	}

	@Override
	public float getWidth() {
		return real.getWidth();
	}

	@Override
	public float getZIndex() {
		return real.getZIndex();
	}

	@Override
	public boolean isGeodesic() {
		return real.isGeodesic();
	}

	@Override
	public boolean isVisible() {
		return real.isVisible();
	}

	@Override
	public void remove() {
		real.remove();
		map.remove(real);
	}

	@Override
	public void setColor(int color) {
		real.setColor(color);
	}

	@Override
	public void setData(Object data) {
		this.data = data;
	}

	@Override
	public void setGeodesic(boolean geodesic) {
		real.setGeodesic(geodesic);
	}

	@Override
	public void setPoints(List<LatLng> points) {
		real.setPoints(points);
	}

	@Override
	public void setVisible(boolean visible) {
		real.setVisible(visible);
	}

	@Override
	public void setWidth(float width) {
		real.setWidth(width);
	}

	@Override
	public void setZIndex(float zIndex) {
		real.setZIndex(zIndex);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof DelegatingPolyline)) {
			return false;
		}
		DelegatingPolyline other = (DelegatingPolyline) o;
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
