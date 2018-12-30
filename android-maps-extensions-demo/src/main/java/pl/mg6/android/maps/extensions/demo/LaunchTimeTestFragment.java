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
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidmapsextensions.ClusteringSettings;
import com.androidmapsextensions.MarkerOptions;
import com.androidmapsextensions.SupportMapFragment;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import java.util.Locale;
import java.util.Random;

public class LaunchTimeTestFragment extends BaseFragment {

    private static final String TAG = LaunchTimeTestFragment.class.getSimpleName();

    private static final String EXTRA_CLUSTERING_TYPE = "clusteringType";
    public static final int CLUSTERING_DISABLED = 0;
    public static final int CLUSTERING_DISABLED_DYNAMIC = 1;
    public static final int CLUSTERING_ENABLED = 2;
    public static final int CLUSTERING_ENABLED_DYNAMIC = 3;

    private static final int MARKERS_COUNT = 20000;

    static LaunchTimeTestFragment newInstance(int clusteringType) {
        LaunchTimeTestFragment f = new LaunchTimeTestFragment();
        Bundle args = new Bundle();
        args.putInt(EXTRA_CLUSTERING_TYPE, clusteringType);
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.launch_time_test, container, false);
    }

    @Override
    protected SupportMapFragment createMapFragment() {
        GoogleMapOptions options = new GoogleMapOptions();
        options.camera(CameraPosition.builder().target(new LatLng(0.0, 0.0)).zoom(4.0f).build());
        return SupportMapFragment.newInstance(options);
    }

    @Override
    protected void setUpMap() {
        int clusteringType = 0;
        if (getArguments() != null) {
            clusteringType = getArguments().getInt(EXTRA_CLUSTERING_TYPE, CLUSTERING_DISABLED);
        }

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
