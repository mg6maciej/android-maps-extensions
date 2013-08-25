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

import pl.mg6.android.maps.extensions.impl.ExtendedMapFactory;
import android.content.Context;
import android.util.AttributeSet;

import com.google.android.gms.maps.GoogleMapOptions;

public class MapView extends com.google.android.gms.maps.MapView {

	private GoogleMap map;

	public MapView(Context context) {
		super(context);
	}

	public MapView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MapView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public MapView(Context context, GoogleMapOptions options) {
		super(context, options);
	}

	public GoogleMap getExtendedMap() {
		if (map == null) {
			com.google.android.gms.maps.GoogleMap realMap = super.getMap();
			if (realMap != null) {
				map = ExtendedMapFactory.create(realMap, getContext());
			}
		}
		return map;
	}
}
