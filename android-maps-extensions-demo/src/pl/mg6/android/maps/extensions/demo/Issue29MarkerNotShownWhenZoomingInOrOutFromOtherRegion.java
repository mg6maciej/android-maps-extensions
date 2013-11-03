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
import android.view.View;

import com.androidmapsextensions.ClusteringSettings;
import com.androidmapsextensions.GoogleMap;
import com.androidmapsextensions.MarkerOptions;
import com.androidmapsextensions.SupportMapFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;

public class Issue29MarkerNotShownWhenZoomingInOrOutFromOtherRegion extends BaseActivity {

    private static final LatLng OTHER_POSITION = new LatLng(52.399, 23.900);
    private static final float OTHER_ZOOM = 10;
    private static final LatLng POZNAN_POSITION = new LatLng(52.399, 16.900);
    private static final float POZNAN_ZOOM = 9;

    private GoogleMap mMap;

    public void onClickIssue(View v) {
        mMap.addMarker(new MarkerOptions().title("Poznań").position(POZNAN_POSITION));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(POZNAN_POSITION, POZNAN_ZOOM));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.issue29_zoom_in_out);
        initializeMap();
        initializeClustering();
        initializeCameraPosition();
    }

    private void initializeMap() {
        FragmentManager fm = getSupportFragmentManager();
        SupportMapFragment f = (SupportMapFragment) fm.findFragmentById(R.id.map);
        mMap = f.getExtendedMap();
    }

    private void initializeClustering() {
        ClusteringSettings settings = new ClusteringSettings();
        settings.clusterOptionsProvider(new DemoClusterOptionsProvider(getResources()));
        settings.addMarkersDynamically(true);
        mMap.setClustering(settings);
    }

    private void initializeCameraPosition() {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(OTHER_POSITION, OTHER_ZOOM));
    }
}
