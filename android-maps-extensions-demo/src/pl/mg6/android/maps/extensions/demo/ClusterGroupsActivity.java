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
package pl.mg6.android.maps.extensions.demo;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import pl.mg6.android.maps.extensions.ClusteringSettings;
import pl.mg6.android.maps.extensions.GoogleMap;
import pl.mg6.android.maps.extensions.Marker;
import pl.mg6.android.maps.extensions.SupportMapFragment;

public class ClusterGroupsActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.simple_map);

		FragmentManager fm = getSupportFragmentManager();
		SupportMapFragment f = (SupportMapFragment) fm.findFragmentById(R.id.map);
		final GoogleMap map = f.getExtendedMap();

		map.setClustering(new ClusteringSettings().iconDataProvider(new ClusteringSettings.IconDataProvider() {
			@Override
			public MarkerOptions getIconData(int markersCount) {
				BitmapDescriptor blueIcon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE);
				return new MarkerOptions().icon(blueIcon);
			}
		}));

		map.addMarker(new MarkerOptions().position(new LatLng(0, 0)));
		map.addMarker(new MarkerOptions().position(new LatLng(3, 1)));
		map.addMarker(new MarkerOptions().position(new LatLng(2, 0.5)));
		map.addMarker(new MarkerOptions().position(new LatLng(0.5, 2)));

		BitmapDescriptor greenIcon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
		final Marker single = map.addMarker(new MarkerOptions().position(new LatLng(10, 10)).icon(greenIcon));
		single.setClusterGroup(-1);

		map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
			@Override
			public void onMapClick(LatLng position) {
				single.setPosition(position);
			}
		});

		map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
			@Override
			public void onMapLongClick(LatLng position) {
				BitmapDescriptor yellowIcon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW);
				Marker alien = map.addMarker(new MarkerOptions().position(position).icon(yellowIcon));
				alien.setClusterGroup(123);
			}
		});
	}
}
