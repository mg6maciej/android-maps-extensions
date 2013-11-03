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

import android.graphics.Point;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidmapsextensions.ClusterGroup;
import com.androidmapsextensions.ClusteringSettings;
import com.androidmapsextensions.GoogleMap;
import com.androidmapsextensions.Marker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class DeclusterificationExampleFragment extends BaseFragment {

    private List<Marker> declusterifiedMarkers;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.simple_map, container, false);
    }

    @Override
    protected void setUpMap() {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.0, 19.0), 7.0f));
        map.setClustering(new ClusteringSettings().clusterOptionsProvider(new DemoClusterOptionsProvider(getResources())));
        MarkerGenerator.addMarkersInPoland(map);

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker.isCluster()) {
                    declusterify(marker);
                    return true;
                }
                return false;
            }
        });
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng position) {
                clusterifyMarkers();
            }
        });
    }

    private void declusterify(Marker cluster) {
        clusterifyMarkers();
        declusterifiedMarkers = cluster.getMarkers();
        LatLng clusterPosition = cluster.getPosition();
        double distance = calculateDistanceBetweenMarkers();
        double currentDistance = -declusterifiedMarkers.size() / 2 * distance;
        for (Marker marker : declusterifiedMarkers) {
            marker.setData(marker.getPosition());
            marker.setClusterGroup(ClusterGroup.NOT_CLUSTERED);
            LatLng newPosition = new LatLng(clusterPosition.latitude, clusterPosition.longitude + currentDistance);
            marker.animatePosition(newPosition);
            currentDistance += distance;
        }
    }

    private double calculateDistanceBetweenMarkers() {
        Projection projection = map.getProjection();
        Point point = projection.toScreenLocation(new LatLng(0.0, 0.0));
        point.x += getResources().getDimensionPixelSize(R.dimen.distance_between_markers);
        LatLng nextPosition = projection.fromScreenLocation(point);
        return nextPosition.longitude;
    }

    private void clusterifyMarkers() {
        if (declusterifiedMarkers != null) {
            for (Marker marker : declusterifiedMarkers) {
                LatLng position = (LatLng) marker.getData();
                marker.setPosition(position);
                marker.setClusterGroup(ClusterGroup.DEFAULT);
            }
            declusterifiedMarkers = null;
        }
    }
}
