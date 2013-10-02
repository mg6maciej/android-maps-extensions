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

import pl.mg6.android.maps.extensions.Polygon;
import pl.mg6.android.maps.extensions.PolygonOptions;

class PolygonManager {

	private final IGoogleMap factory;

	private final Map<com.google.android.gms.maps.model.Polygon, Polygon> polygons;

	public PolygonManager(IGoogleMap factory) {
		this.factory = factory;
		this.polygons = new HashMap<com.google.android.gms.maps.model.Polygon, Polygon>();
	}

	public Polygon addPolygon(PolygonOptions polygonOptions) {
		Polygon polygon = createPolygon(polygonOptions.real);
		polygon.setData(polygonOptions.getData());
		return polygon;
	}

	public Polygon addPolygon(com.google.android.gms.maps.model.PolygonOptions polygonOptions) {
		Polygon polygon = createPolygon(polygonOptions);
		return polygon;
	}

	private Polygon createPolygon(com.google.android.gms.maps.model.PolygonOptions polygonOptions) {
		com.google.android.gms.maps.model.Polygon real = factory.addPolygon(polygonOptions);
		Polygon polygon = new DelegatingPolygon(real, this);
		polygons.put(real, polygon);
		return polygon;
	}

	public void clear() {
		polygons.clear();
	}

	public List<Polygon> getPolygons() {
		return new ArrayList<Polygon>(polygons.values());
	}

	public void onRemove(com.google.android.gms.maps.model.Polygon real) {
		polygons.remove(real);
	}
}
