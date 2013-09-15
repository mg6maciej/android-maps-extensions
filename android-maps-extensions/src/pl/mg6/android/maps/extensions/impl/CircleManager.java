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

import pl.mg6.android.maps.extensions.Circle;
import pl.mg6.android.maps.extensions.CircleOptions;

class CircleManager {

	private final IGoogleMap factory;

	private final Map<com.google.android.gms.maps.model.Circle, Circle> circles;

	public CircleManager(IGoogleMap factory) {
		this.factory = factory;
		this.circles = new HashMap<com.google.android.gms.maps.model.Circle, Circle>();
	}

	public Circle addCircle(CircleOptions circleOptions) {
		Circle circle = createCircle(circleOptions.real);
		circle.setData(circleOptions.getData());
		return circle;
	}

	public Circle addCircle(com.google.android.gms.maps.model.CircleOptions circleOptions) {
		Circle circle = createCircle(circleOptions);
		return circle;
	}

	private Circle createCircle(com.google.android.gms.maps.model.CircleOptions circleOptions) {
		com.google.android.gms.maps.model.Circle real = factory.addCircle(circleOptions);
		Circle circle = new DelegatingCircle(real, this);
		circles.put(real, circle);
		return circle;
	}

	public void clear() {
		circles.clear();
	}

	public List<Circle> getCircles() {
		return new ArrayList<Circle>(circles.values());
	}

	public void onRemove(com.google.android.gms.maps.model.Circle real) {
		circles.remove(real);
	}
}
