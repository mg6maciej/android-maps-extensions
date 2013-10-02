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

import pl.mg6.android.maps.extensions.Polyline;
import pl.mg6.android.maps.extensions.PolylineOptions;

class PolylineManager {

	private final IGoogleMap factory;

	private final Map<com.google.android.gms.maps.model.Polyline, Polyline> polylines;

	public PolylineManager(IGoogleMap factory) {
		this.factory = factory;
		this.polylines = new HashMap<com.google.android.gms.maps.model.Polyline, Polyline>();
	}

	public Polyline addPolyline(PolylineOptions polylineOptions) {
		Polyline polyline = createPolyline(polylineOptions.real);
		polyline.setData(polylineOptions.getData());
		return polyline;
	}

	public Polyline addPolyline(com.google.android.gms.maps.model.PolylineOptions polylineOptions) {
		Polyline polyline = createPolyline(polylineOptions);
		return polyline;
	}

	private Polyline createPolyline(com.google.android.gms.maps.model.PolylineOptions polylineOptions) {
		com.google.android.gms.maps.model.Polyline real = factory.addPolyline(polylineOptions);
		Polyline polyline = new DelegatingPolyline(real, this);
		polylines.put(real, polyline);
		return polyline;
	}

	public void clear() {
		polylines.clear();
	}

	public List<Polyline> getPolylines() {
		return new ArrayList<Polyline>(polylines.values());
	}

	public void onRemove(com.google.android.gms.maps.model.Polyline real) {
		polylines.remove(real);
	}
}
