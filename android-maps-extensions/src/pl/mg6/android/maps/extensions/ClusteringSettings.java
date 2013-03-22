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

import com.google.android.gms.maps.model.MarkerOptions;

public class ClusteringSettings {

	private boolean addMarkersDynamically;

	private boolean enabled = true;

	private IconDataProvider iconDataProvider;

	public ClusteringSettings addMarkersDynamically(boolean addMarkersDynamically) {
		this.addMarkersDynamically = addMarkersDynamically;
		return this;
	}

	public ClusteringSettings enabled(boolean enabled) {
		this.enabled = enabled;
		return this;
	}

	public IconDataProvider getIconDataProvider() {
		return iconDataProvider;
	}

	public ClusteringSettings iconDataProvider(IconDataProvider iconDataProvider) {
		this.iconDataProvider = iconDataProvider;
		return this;
	}

	public boolean isAddMarkersDynamically() {
		return addMarkersDynamically;
	}

	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof ClusteringSettings)) {
			return false;
		}
		ClusteringSettings other = (ClusteringSettings) o;
		if (enabled != other.enabled) {
			return false;
		}
		if (addMarkersDynamically != other.addMarkersDynamically) {
			return false;
		}
		if (enabled == false && other.enabled == false) {
			return true;
		}
		return iconDataProvider.equals(other.iconDataProvider);
	}

	@Override
	public int hashCode() {
		// TODO: implement, low priority
		return super.hashCode();
	}

	public interface IconDataProvider {

		MarkerOptions getIconData(int markersCount);
	}
}
