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

public class ClusterOptions {

	public static final float DEFAULT_ANCHOR_U = 0.5f;
	public static final float DEFAULT_ANCHOR_V = 1.0f;
	public static final float DEFAULT_INFO_WINDOW_ANCHOR_U = 0.5f;
	public static final float DEFAULT_INFO_WINDOW_ANCHOR_V = 0.0f;

	private float anchorU = DEFAULT_ANCHOR_U;
	private float anchorV = DEFAULT_ANCHOR_V;
	private boolean flat;
	private BitmapDescriptor icon;
	private float infoWindowAnchorU = DEFAULT_INFO_WINDOW_ANCHOR_U;
	private float infoWindowAnchorV = DEFAULT_INFO_WINDOW_ANCHOR_V;
	private float rotation;

	public ClusterOptions anchor(float anchorU, float anchorV) {
		this.anchorU = anchorU;
		this.anchorV = anchorV;
		return this;
	}

	public ClusterOptions flat(boolean flat) {
		this.flat = flat;
		return this;
	}

	public float getAnchorU() {
		return anchorU;
	}

	public float getAnchorV() {
		return anchorV;
	}

	public BitmapDescriptor getIcon() {
		return icon;
	}

	public float getInfoWindowAnchorU() {
		return infoWindowAnchorU;
	}

	public float getInfoWindowAnchorV() {
		return infoWindowAnchorV;
	}

	public float getRotation() {
		return rotation;
	}

	public ClusterOptions icon(BitmapDescriptor icon) {
		this.icon = icon;
		return this;
	}

	public ClusterOptions infoWindowAnchor(float u, float v) {
		this.infoWindowAnchorU = u;
		this.infoWindowAnchorV = v;
		return this;
	}

	public boolean isFlat() {
		return flat;
	}

	public ClusterOptions rotation(float rotation) {
		this.rotation = rotation;
		return this;
	}
}
