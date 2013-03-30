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

import com.google.android.gms.maps.model.LatLng;

public interface Circle {

	boolean contains(LatLng position);

	LatLng getCenter();

	Object getData();

	int getFillColor();

	@Deprecated
	String getId();

	double getRadius();

	int getStrokeColor();

	float getStrokeWidth();

	float getZIndex();

	boolean isVisible();

	void remove();

	void setCenter(LatLng center);

	void setData(Object data);

	void setFillColor(int fillColor);

	void setRadius(double radius);

	void setStrokeColor(int strokeColor);

	void setStrokeWidth(float strokeWidth);

	void setVisible(boolean visible);

	void setZIndex(float zIndex);
}