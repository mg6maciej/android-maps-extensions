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
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidmapsextensions.ClusteringSettings;
import com.androidmapsextensions.MarkerOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;

public class Issue29GoogleCodeMarkerNotShownWhenZoomingInOrOutFromOtherRegionFragment extends BaseFragment {

    private static final LatLng OTHER_POSITION = new LatLng(52.399, 23.900);
    private static final float OTHER_ZOOM = 10;
    private static final LatLng POZNAN_POSITION = new LatLng(52.399, 16.900);
    private static final float POZNAN_ZOOM = 9;

    private void onClickIssue() {
        map.addMarker(new MarkerOptions().title("Poznań").position(POZNAN_POSITION));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(POZNAN_POSITION, POZNAN_ZOOM));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.issue29_zoom_in_out, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.issue_29_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickIssue();
            }
        });
    }

    @Override
    protected void setUpMap() {
        initializeClustering();
        initializeCameraPosition();
    }

    private void initializeClustering() {
        ClusteringSettings settings = new ClusteringSettings();
        settings.clusterOptionsProvider(new DemoClusterOptionsProvider(getResources()));
        settings.addMarkersDynamically(true);
        map.setClustering(settings);
    }

    private void initializeCameraPosition() {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(OTHER_POSITION, OTHER_ZOOM));
    }
}
