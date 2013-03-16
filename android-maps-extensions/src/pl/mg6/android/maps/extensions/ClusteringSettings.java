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

public class ClusteringSettings {

	private boolean enabled = true;

	private BitmapDescriptor defaultIcon;

	private IconProvider iconProvider;

	public ClusteringSettings defaultIcon(BitmapDescriptor defaultIcon) {
		this.defaultIcon = defaultIcon;
		return this;
	}

	public ClusteringSettings enabled(boolean enabled) {
		this.enabled = enabled;
		return this;
	}

	public BitmapDescriptor getDefaultIcon() {
		return defaultIcon;
	}

	public IconProvider getIconProvider() {
		return iconProvider;
	}

	public ClusteringSettings iconProvider(IconProvider iconProvider) {
		this.iconProvider = iconProvider;
		return this;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public interface IconProvider {

		BitmapDescriptor getIcon(Marker cluster);
	}
}
