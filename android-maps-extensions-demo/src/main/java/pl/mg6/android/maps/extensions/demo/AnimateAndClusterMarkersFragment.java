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
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidmapsextensions.AnimationSettings;
import com.androidmapsextensions.ClusteringSettings;
import com.androidmapsextensions.Marker;
import com.androidmapsextensions.MarkerOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.Random;

/**
 * Demo for issue #62 Cluster markers get stuck if marker animation in use
 * Demo will animate markers to force them to cluster / decluster and at the same time zoom the map in / out
 * If you run it for a while you'll start to see "ghost" cluster markers that are impossible to remove
 */
public class AnimateAndClusterMarkersFragment extends BaseFragment {
    private final static ClusteringSettings CLUSTERING_SETTINGS = new ClusteringSettings().enabled(true).addMarkersDynamically(true).minMarkersCount(2);
    private final static AnimationSettings ANIMATION_SETTINGS = new AnimationSettings().duration(3000);
    private final static int MARKER_ANIMATION_CYCLE = 3500;
    private final static int MAP_ANIMATION_CYCLE = 1000;
    private final static float MIN_ZOOM = 3F;
    private final static float MAX_ZOOM = 3.5F;

    private boolean zoomIn = true;
    private Random random = new Random();

    /**
     * Recursively move all the markers regardless if they belong to a cluster or not
     *
     * @param markers list of markers
     */
    private void moveMarkers(List<Marker> markers) {
        for (Marker marker : markers) {
            final List<Marker> clusteredMarkers = marker.getMarkers();
            if (clusteredMarkers == null)
                marker.animatePosition(randomPositionAcrossTheOcean(marker.getPosition()), ANIMATION_SETTINGS);
            else
                moveMarkers(clusteredMarkers);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.simple_map, container, false);
    }

    @Override
    protected void setUpMap() {
        final Handler markerAnimationHandler = new Handler();
        final Handler mapAnimationHandler = new Handler();

        map.setClustering(CLUSTERING_SETTINGS);
        map.addMarker(new MarkerOptions().title("RED").position(new LatLng(-15, -15)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        map.addMarker(new MarkerOptions().title("GREEN").position(new LatLng(-15, 15)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        map.addMarker(new MarkerOptions().title("BLUE").position(new LatLng(15, -15)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        map.addMarker(new MarkerOptions().title("YELLOW").position(new LatLng(15, 15)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
        map.addMarker(new MarkerOptions().title("CYAN").position(new LatLng(-20, -20)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
        map.addMarker(new MarkerOptions().title("AZURE").position(new LatLng(-20, 20)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        map.addMarker(new MarkerOptions().title("ORANGE").position(new LatLng(20, -20)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        map.addMarker(new MarkerOptions().title("ROSE").position(new LatLng(20, 20)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));


        markerAnimationHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    moveMarkers(map.getDisplayedMarkers());
                } catch (Exception e) {
                    Log.e("ERROR", "Error animating markers");
                } finally {
                    markerAnimationHandler.removeCallbacks(this);
                    markerAnimationHandler.postDelayed(this, MARKER_ANIMATION_CYCLE);
                }
            }
        }, 0);

        mapAnimationHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    map.animateCamera(CameraUpdateFactory.zoomTo(zoomIn ? MAX_ZOOM : MIN_ZOOM));
                    zoomIn = !zoomIn;
                } catch (Exception e) {
                    Log.e("ERROR", "Error animating map");
                } finally {
                    mapAnimationHandler.removeCallbacks(this);
                    mapAnimationHandler.postDelayed(this, MAP_ANIMATION_CYCLE);
                }
            }
        }, 0);
    }

    private LatLng randomPositionAcrossTheOcean(LatLng position) {
        double lat;
        double lng;
        if (position.latitude < 0) {
            lat = random.nextDouble() * 10 + 10;
        } else {
            lat = random.nextDouble() * 10 - 20;
        }
        if (position.longitude < 0) {
            lng = random.nextDouble() * 10 + 10;
        } else {
            lng = random.nextDouble() * 10 - 20;
        }
        return new LatLng(lat, lng);
    }
}
