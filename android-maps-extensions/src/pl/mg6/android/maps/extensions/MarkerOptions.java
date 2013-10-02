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

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;

public class MarkerOptions {

	public final com.google.android.gms.maps.model.MarkerOptions real = new com.google.android.gms.maps.model.MarkerOptions();
	private Object data;
	private int clusterGroup;

	public MarkerOptions anchor(float u, float v) {
		real.anchor(u, v);
		return this;
	}

	public MarkerOptions clusterGroup(int clusterGroup) {
		this.clusterGroup = clusterGroup;
		return this;
	}

	public MarkerOptions data(Object data) {
		this.data = data;
		return this;
	}

	public MarkerOptions draggable(boolean draggable) {
		real.draggable(draggable);
		return this;
	}

	public MarkerOptions flat(boolean flat) {
		real.flat(flat);
		return this;
	}

	public float getAnchorU() {
		return real.getAnchorU();
	}

	public float getAnchorV() {
		return real.getAnchorV();
	}

	public int getClusterGroup() {
		return clusterGroup;
	}

	public Object getData() {
		return data;
	}

	public BitmapDescriptor getIcon() {
		return real.getIcon();
	}

	public float getInfoWindowAnchorU() {
		return real.getInfoWindowAnchorU();
	}

	public float getInfoWindowAnchorV() {
		return real.getInfoWindowAnchorV();
	}

	public LatLng getPosition() {
		return real.getPosition();
	}

	public float getRotation() {
		return real.getRotation();
	}

	public String getSnippet() {
		return real.getSnippet();
	}

	public String getTitle() {
		return real.getTitle();
	}

	public MarkerOptions icon(BitmapDescriptor icon) {
		real.icon(icon);
		return this;
	}

	public MarkerOptions infoWindowAnchor(float u, float v) {
		real.infoWindowAnchor(u, v);
		return this;
	}

	public boolean isDraggable() {
		return real.isDraggable();
	}

	public boolean isFlat() {
		return real.isFlat();
	}

	public boolean isVisible() {
		return real.isVisible();
	}

	public MarkerOptions position(LatLng position) {
		real.position(position);
		return this;
	}

	public MarkerOptions rotation(float rotation) {
		real.rotation(rotation);
		return this;
	}

	public MarkerOptions snippet(String snippet) {
		real.snippet(snippet);
		return this;
	}

	public MarkerOptions title(String title) {
		real.title(title);
		return this;
	}

	public MarkerOptions visible(boolean visible) {
		real.visible(visible);
		return this;
	}
}
