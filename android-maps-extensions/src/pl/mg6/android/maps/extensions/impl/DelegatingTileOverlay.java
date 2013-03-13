package pl.mg6.android.maps.extensions.impl;

import pl.mg6.android.maps.extensions.TileOverlay;

class DelegatingTileOverlay implements TileOverlay {

	private com.google.android.gms.maps.model.TileOverlay real;
	private DelegatingGoogleMap map;

	private Object data;

	DelegatingTileOverlay(com.google.android.gms.maps.model.TileOverlay real, DelegatingGoogleMap map) {
		this.real = real;
		this.map = map;
	}

	@Override
	public void clearTileCache() {
		real.clearTileCache();
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
	public float getZIndex() {
		return real.getZIndex();
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
	public void setData(Object data) {
		this.data = data;
	}

	@Override
	public void setVisible(boolean visible) {
		real.setVisible(visible);
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
		if (!(o instanceof DelegatingTileOverlay)) {
			return false;
		}
		DelegatingTileOverlay other = (DelegatingTileOverlay) o;
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
