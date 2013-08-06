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

import pl.mg6.android.maps.extensions.AnimationSettings;
import pl.mg6.android.maps.extensions.ClusteringSettings;
import pl.mg6.android.maps.extensions.GoogleMap;
import pl.mg6.android.maps.extensions.Marker;
import pl.mg6.android.maps.extensions.SupportMapFragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class SimpleMapActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.simple_map);

		FragmentManager fm = getSupportFragmentManager();
		SupportMapFragment f = (SupportMapFragment) fm.findFragmentById(R.id.map);
		GoogleMap map = f.getExtendedMap();

		map.addMarker(new MarkerOptions().position(new LatLng(0, 0)));
		map.addMarker(new MarkerOptions().position(new LatLng(15, 3)));
		map.addMarker(new MarkerOptions().position(new LatLng(15, 3)));

		AnimationSettings anim = new AnimationSettings().duration(2000L);
		ClusteringSettings.IconDataProvider provider = new ClusteringSettings.IconDataProvider() {

			@Override
			public MarkerOptions getIconData(int markersCount) {
				return new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
			}
		};
		map.setClustering(new ClusteringSettings().iconDataProvider(provider));

		map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(Marker marker) {
				Toast.makeText(SimpleMapActivity.this, "Clicked marker at: " + marker.getPosition(), Toast.LENGTH_SHORT).show();
				return false;
			}
		});
	}
}
