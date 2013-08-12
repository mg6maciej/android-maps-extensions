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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		String[] screens = { "Demo", "Animate markers", "Cluster groups", "No clustering", "No clustering (dynamic)", "Grid clustering", "Grid clustering (dynamic)" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, screens);
		ListView listView = (ListView) findViewById(R.id.list);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent;
				if (position == 0) {
					intent = new Intent(MainActivity.this, DemoActivity.class);
				} else if (position == 1) {
					intent = new Intent(MainActivity.this, AnimateMarkersActivity.class);
				} else if (position == 2) {
					intent = new Intent(MainActivity.this, ClusterGroupsActivity.class);
				} else {
					intent = new Intent(MainActivity.this, LaunchTimeTestActivity.class);
					// normally: int clusteringType = LaunchTimeTestActivity.CLUSTERING_ENABLED;
					int clusteringType = position - 3;
					intent.putExtra(LaunchTimeTestActivity.EXTRA_CLUSTERING_TYPE, clusteringType);
				}
				startActivity(intent);
			}
		});
	}
}
