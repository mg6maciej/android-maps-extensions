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
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidmapsextensions.ClusteringSettings;
import com.androidmapsextensions.MarkerOptions;
import com.google.android.gms.maps.model.LatLng;

public class Issue15GoogleCodeInfoWindowNotShowingFragment extends BaseFragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.simple_map, container, false);
    }

    @Override
    protected void setUpMap() {
        map.setClustering(new ClusteringSettings().addMarkersDynamically(true));
        MarkerOptions options = new MarkerOptions().position(new LatLng(0, -90)).title("title");
        map.addMarker(options);
        new Handler().post(new Runnable() {

            @Override
            public void run() {
                map.getDisplayedMarkers().get(0).showInfoWindow();
            }
        });
    }
}
