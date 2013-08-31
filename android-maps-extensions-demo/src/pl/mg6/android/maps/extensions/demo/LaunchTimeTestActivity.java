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

import java.util.Locale;
import java.util.Random;

import pl.mg6.android.maps.extensions.ClusteringSettings;
import pl.mg6.android.maps.extensions.GoogleMap;
import pl.mg6.android.maps.extensions.MarkerOptions;
import pl.mg6.android.maps.extensions.SupportMapFragment;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

public class LaunchTimeTestActivity extends FragmentActivity {

	private static final String TAG = LaunchTimeTestActivity.class.getSimpleName();

	public static final String EXTRA_CLUSTERING_TYPE = "clusteringType";
	public static final int CLUSTERING_DISABLED = 0;
	public static final int CLUSTERING_DISABLED_DYNAMIC = 1;
	public static final int CLUSTERING_ENABLED = 2;
	public static final int CLUSTERING_ENABLED_DYNAMIC = 3;

	private static final int MARKERS_COUNT = 20000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.launch_time_test);

		FragmentManager fm = getSupportFragmentManager();
		SupportMapFragment f = (SupportMapFragment) fm.findFragmentById(R.id.map);
		GoogleMap map = f.getExtendedMap();

		int clusteringType = getIntent().getIntExtra(EXTRA_CLUSTERING_TYPE, CLUSTERING_DISABLED);

		ClusteringSettings settings = new ClusteringSettings();
		switch (clusteringType) {
			case CLUSTERING_DISABLED_DYNAMIC:
				settings.enabled(false).addMarkersDynamically(true);
				break;
			case CLUSTERING_ENABLED:
				settings.clusterOptionsProvider(new DemoClusterOptionsProvider(getResources()));
				break;
			case CLUSTERING_ENABLED_DYNAMIC:
				settings.clusterOptionsProvider(new DemoClusterOptionsProvider(getResources())).addMarkersDynamically(true);
				break;
			default:
				settings.enabled(false);
		}

		map.setClustering(settings);

		Random r = new Random(0);
		MarkerOptions options = new MarkerOptions();

		long start = SystemClock.uptimeMillis();
		for (int i = 0; i < MARKERS_COUNT; i++) {
			LatLng position = new LatLng(r.nextDouble() * 170 - 85, r.nextDouble() * 360 - 180);
			map.addMarker(options.position(position));
		}
		long end = SystemClock.uptimeMillis();
		long time = end - start;
		float zoom = map.getCameraPosition().zoom;
		String format = "Time adding %d markers (option: %d, zoom: %.1f): %d";
		String text = String.format(Locale.US, format, MARKERS_COUNT, clusteringType, zoom, time);
		Log.i(TAG, text);
	}
}
