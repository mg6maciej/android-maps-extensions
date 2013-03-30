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
import com.google.android.gms.maps.model.LatLngBounds;

public interface GroundOverlay {

	float getBearing();

	LatLngBounds getBounds();

	Object getData();

	float getHeight();

	@Deprecated
	String getId();

	LatLng getPosition();

	float getTransparency();

	float getWidth();

	float getZIndex();

	boolean isVisible();

	void remove();

	void setBearing(float bearing);

	void setData(Object data);

	void setDimensions(float width, float height);

	void setDimensions(float width);

	void setPosition(LatLng position);

	void setPositionFromBounds(LatLngBounds bounds);

	void setTransparency(float transparency);

	void setVisible(boolean visible);

	void setZIndex(float zIndex);
}