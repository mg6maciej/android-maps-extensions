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
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.androidmapsextensions.GoogleMap;
import com.androidmapsextensions.OnMapReadyCallback;
import com.androidmapsextensions.SupportMapFragment;

public abstract class BaseFragment extends Fragment implements OnMapReadyCallback {

    private SupportMapFragment mapFragment;
    protected GoogleMap map;

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        createMapFragmentIfNeeded();
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void createMapFragmentIfNeeded() {
        FragmentManager fm = getChildFragmentManager();
        mapFragment = (SupportMapFragment) fm.findFragmentById(R.id.map_container);
        if (mapFragment == null) {
            mapFragment = createMapFragment();
            FragmentTransaction tx = fm.beginTransaction();
            tx.add(R.id.map_container, mapFragment);
            tx.commit();
        }
    }

    protected SupportMapFragment createMapFragment() {
        return SupportMapFragment.newInstance();
    }

    private void setUpMapIfNeeded() {
        if (map == null) {
            mapFragment.getExtendedMapAsync(this);
        }
    }

    protected abstract void setUpMap();

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        setUpMap();
    }
}
