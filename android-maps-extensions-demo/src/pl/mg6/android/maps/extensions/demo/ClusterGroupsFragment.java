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
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidmapsextensions.ClusterGroup;
import com.androidmapsextensions.ClusterOptions;
import com.androidmapsextensions.ClusterOptionsProvider;
import com.androidmapsextensions.ClusteringSettings;
import com.androidmapsextensions.GoogleMap;
import com.androidmapsextensions.Marker;
import com.androidmapsextensions.MarkerOptions;
import com.androidmapsextensions.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class ClusterGroupsFragment extends BaseFragment {

    private static final int DYNAMIC_GROUP = ClusterGroup.FIRST_USER;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.simple_map, container, false);
    }

    @Override
    protected void setUpMap() {
        map.setClustering(new ClusteringSettings().clusterOptionsProvider(new ClusterOptionsProvider() {
            @Override
            public ClusterOptions getClusterOptions(List<Marker> markers) {
                float hue;
                if (markers.get(0).getClusterGroup() == DYNAMIC_GROUP) {
                    hue = BitmapDescriptorFactory.HUE_ORANGE;
                } else {
                    hue = BitmapDescriptorFactory.HUE_ROSE;
                }
                BitmapDescriptor blueIcon = BitmapDescriptorFactory.defaultMarker(hue);
                return new ClusterOptions().icon(blueIcon);
            }
        }));

        map.addMarker(new MarkerOptions().position(new LatLng(0, 0)));
        map.addMarker(new MarkerOptions().position(new LatLng(3, 1)));
        map.addMarker(new MarkerOptions().position(new LatLng(2, 0.5)));
        map.addMarker(new MarkerOptions().position(new LatLng(0.5, 2)));

        BitmapDescriptor greenIcon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
        final Marker single = map.addMarker(new MarkerOptions().position(new LatLng(10, 10)).icon(greenIcon).clusterGroup(ClusterGroup.NOT_CLUSTERED));

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
                map.addMarker(new MarkerOptions().position(position).icon(yellowIcon).clusterGroup(DYNAMIC_GROUP));
            }
        });
    }
}
