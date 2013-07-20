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

import pl.mg6.android.maps.extensions.TileOverlay;

class DelegatingTileOverlay implements TileOverlay {

	private com.google.android.gms.maps.model.TileOverlay real;
	private TileOverlayManager manager;

	private Object data;

	DelegatingTileOverlay(com.google.android.gms.maps.model.TileOverlay real, TileOverlayManager manager) {
		this.real = real;
		this.manager = manager;
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
		manager.onRemove(real);
		real.remove();
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
