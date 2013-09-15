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

import com.google.android.gms.maps.model.TileProvider;

public class TileOverlayOptions {

	public final com.google.android.gms.maps.model.TileOverlayOptions real = new com.google.android.gms.maps.model.TileOverlayOptions();
	private Object data;

	public TileOverlayOptions data(Object data) {
		this.data = data;
		return this;
	}

	public Object getData() {
		return data;
	}

	public TileProvider getTileProvider() {
		return real.getTileProvider();
	}

	public float getZIndex() {
		return real.getZIndex();
	}

	public boolean isVisible() {
		return real.isVisible();
	}

	public TileOverlayOptions tileProvider(TileProvider tileProvider) {
		real.tileProvider(tileProvider);
		return this;
	}

	public TileOverlayOptions visible(boolean visible) {
		real.visible(visible);
		return this;
	}

	public TileOverlayOptions zIndex(float zIndex) {
		real.zIndex(zIndex);
		return this;
	}
}
