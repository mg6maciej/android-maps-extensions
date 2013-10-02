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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.mg6.android.maps.extensions.TileOverlay;
import pl.mg6.android.maps.extensions.TileOverlayOptions;

class TileOverlayManager {

	private final IGoogleMap factory;

	private final Map<com.google.android.gms.maps.model.TileOverlay, TileOverlay> tileOverlays;

	public TileOverlayManager(IGoogleMap factory) {
		this.factory = factory;
		this.tileOverlays = new HashMap<com.google.android.gms.maps.model.TileOverlay, TileOverlay>();
	}

	public TileOverlay addTileOverlay(TileOverlayOptions tileOverlayOptions) {
		TileOverlay tileOverlay = createTileOverlay(tileOverlayOptions.real);
		tileOverlay.setData(tileOverlayOptions.getData());
		return tileOverlay;
	}

	public TileOverlay addTileOverlay(com.google.android.gms.maps.model.TileOverlayOptions tileOverlayOptions) {
		TileOverlay tileOverlay = createTileOverlay(tileOverlayOptions);
		return tileOverlay;
	}

	private TileOverlay createTileOverlay(com.google.android.gms.maps.model.TileOverlayOptions tileOverlayOptions) {
		com.google.android.gms.maps.model.TileOverlay real = factory.addTileOverlay(tileOverlayOptions);
		TileOverlay tileOverlay = new DelegatingTileOverlay(real, this);
		tileOverlays.put(real, tileOverlay);
		return tileOverlay;
	}

	public void clear() {
		tileOverlays.clear();
	}

	public List<TileOverlay> getTileOverlays() {
		return new ArrayList<TileOverlay>(tileOverlays.values());
	}

	public void onRemove(com.google.android.gms.maps.model.TileOverlay real) {
		tileOverlays.remove(real);
	}
}
