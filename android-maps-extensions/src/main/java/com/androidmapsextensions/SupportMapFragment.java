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
package com.androidmapsextensions;

import android.content.Context;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMapOptions;

public class SupportMapFragment extends com.google.android.gms.maps.SupportMapFragment implements MapHolder.Delegate {

    // value taken from google-play-services.jar
    private static final String MAP_OPTIONS = "MapOptions";

    public static SupportMapFragment newInstance() {
        SupportMapFragment f = new SupportMapFragment();
        return f;
    }

    public static SupportMapFragment newInstance(GoogleMapOptions options) {
        SupportMapFragment f = new SupportMapFragment();
        Bundle args = new Bundle();
        args.putParcelable(MAP_OPTIONS, options);
        f.setArguments(args);
        return f;
    }

    private final MapHolder mapHolder = new MapHolder(this);

    public GoogleMap getExtendedMap() {
        return mapHolder.getExtendedMap();
    }

    public void getExtendedMapAsync(OnMapReadyCallback callback) {
        mapHolder.getExtendedMapAsync(callback);
    }

    @Override
    public Context getContext() {
        return getActivity();
    }
}
