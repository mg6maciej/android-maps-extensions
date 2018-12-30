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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        drawerLayout = findViewById(R.id.drawer);
        initDrawerToggle();

        String[] screens = {"Demo", "Animate markers", "Cluster groups", "\"Declusterification\"",
                "Grid clustering (dynamic)", "No clustering (dynamic)", "Grid clustering", "No clustering"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, screens);
        ListView listView = findViewById(R.id.list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (GooglePlayServicesErrorDialogFragment.showDialogIfNotAvailable(MainActivity.this)) {
                    startExample(position);
                }
            }
        });
        if (savedInstanceState == null) {
            if (GooglePlayServicesErrorDialogFragment.showDialogIfNotAvailable(this)) {
                replaceMainFragment(new DemoFragment());
            }
        }
    }

    private void startExample(int position) {
        if (position == 0) {
            replaceMainFragment(new DemoFragment());
        } else if (position == 1) {
            replaceMainFragment(new AnimateMarkersFragment());
        } else if (position == 2) {
            replaceMainFragment(new ClusterGroupsFragment());
        } else if (position == 3) {
            replaceMainFragment(new DeclusterificationExampleFragment());
        } else {
            int clusteringType;
            if (position == 4) {
                clusteringType = LaunchTimeTestFragment.CLUSTERING_ENABLED_DYNAMIC;
            } else if (position == 5) {
                clusteringType = LaunchTimeTestFragment.CLUSTERING_DISABLED_DYNAMIC;
            } else if (position == 6) {
                clusteringType = LaunchTimeTestFragment.CLUSTERING_ENABLED;
            } else {
                clusteringType = LaunchTimeTestFragment.CLUSTERING_DISABLED;
            }
            Fragment fragment = LaunchTimeTestFragment.newInstance(clusteringType);
            replaceMainFragment(fragment);
        }
        drawerLayout.closeDrawers();
    }

    private void initDrawerToggle() {
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    private void replaceMainFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction tx = fm.beginTransaction();
        tx.replace(R.id.main_container, fragment);
        tx.commit();
    }
}
