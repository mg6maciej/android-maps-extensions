/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.mapdemo;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.mapdemo.view.FeatureView;

/**
 * The main activity of the API library demo gallery.
 * <p>
 * The main layout lists the demonstrated features, with buttons to launch them.
 */
public final class MainActivity extends ListActivity {

    /**
     * A simple POJO that holds the details about the demo that are used by the List Adapter.
     */
    private static class DemoDetails {
        /**
         * The resource id of the title of the demo.
         */
        private final int titleId;

        /**
         * The resources id of the description of the demo.
         */
        private final int descriptionId;

        /**
         * The demo activity's class.
         */
        private final Class<? extends FragmentActivity> activityClass;

        public DemoDetails(
                int titleId, int descriptionId, Class<? extends FragmentActivity> activityClass) {
            super();
            this.titleId = titleId;
            this.descriptionId = descriptionId;
            this.activityClass = activityClass;
        }
    }

    /**
     * A custom array adapter that shows a {@link FeatureView} containing details about the demo.
     */
    private static class CustomArrayAdapter extends ArrayAdapter<DemoDetails> {

        /**
         * @param demos An array containing the details of the demos to be displayed.
         */
        public CustomArrayAdapter(Context context, DemoDetails[] demos) {
            super(context, R.layout.feature, R.id.title, demos);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            FeatureView featureView;
            if (convertView instanceof FeatureView) {
                featureView = (FeatureView) convertView;
            } else {
                featureView = new FeatureView(getContext());
            }

            DemoDetails demo = getItem(position);

            featureView.setTitleId(demo.titleId);
            featureView.setDescriptionId(demo.descriptionId);

            return featureView;
        }
    }

    private static final DemoDetails[] demos = {new DemoDetails(
            R.string.basic_map, R.string.basic_description, BasicMapActivity.class),
            new DemoDetails(R.string.camera_demo, R.string.camera_description,
                    CameraDemoActivity.class),
            new DemoDetails(R.string.events_demo, R.string.events_description,
                    EventsDemoActivity.class),
            new DemoDetails(R.string.layers_demo, R.string.layers_description,
                    LayersDemoActivity.class),
            new DemoDetails(
                    R.string.locationsource_demo, R.string.locationsource_description,
                    LocationSourceDemoActivity.class),
            new DemoDetails(R.string.uisettings_demo, R.string.uisettings_description,
                    UiSettingsDemoActivity.class),
            new DemoDetails(R.string.groundoverlay_demo, R.string.groundoverlay_description,
                    GroundOverlayDemoActivity.class),
            new DemoDetails(R.string.marker_demo, R.string.marker_description,
                    MarkerDemoActivity.class),
            new DemoDetails(R.string.polygon_demo, R.string.polygon_description,
                    PolygonDemoActivity.class),
            new DemoDetails(R.string.polyline_demo, R.string.polyline_description,
                    PolylineDemoActivity.class),
            new DemoDetails(R.string.circle_demo, R.string.circle_description,
                    CircleDemoActivity.class),
            new DemoDetails(R.string.tile_overlay_demo, R.string.tile_overlay_description,
                    TileOverlayDemoActivity.class),
            new DemoDetails(R.string.options_demo, R.string.options_description,
                    OptionsDemoActivity.class),
            new DemoDetails(R.string.multi_map_demo, R.string.multi_map_description,
                    MultiMapDemoActivity.class),
            new DemoDetails(R.string.retain_map, R.string.retain_map_description,
                    RetainMapActivity.class),
            new DemoDetails(R.string.raw_mapview_demo, R.string.raw_mapview_description,
                    RawMapViewDemoActivity.class),
            new DemoDetails(R.string.programmatic_demo, R.string.programmatic_description,
                    ProgrammaticDemoActivity.class)};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ListAdapter adapter = new CustomArrayAdapter(this, demos);

        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        DemoDetails demo = (DemoDetails) getListAdapter().getItem(position);
        startActivity(new Intent(this, demo.activityClass));
    }
}
