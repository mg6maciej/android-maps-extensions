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
package pl.mg6.android.maps.extensions.impl;

import android.location.Location;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.CancelableCallback;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;

//TODO: to be deleted when com.google.android.gms.maps.GoogleMap becomes an interface
interface IGoogleMap {

	Circle addCircle(CircleOptions arg0);

	GroundOverlay addGroundOverlay(GroundOverlayOptions arg0);

	Marker addMarker(MarkerOptions arg0);

	Polygon addPolygon(PolygonOptions arg0);

	Polyline addPolyline(PolylineOptions arg0);

	TileOverlay addTileOverlay(TileOverlayOptions arg0);

	void animateCamera(CameraUpdate arg0, CancelableCallback arg1);

	void animateCamera(CameraUpdate arg0, int arg1, CancelableCallback arg2);

	void animateCamera(CameraUpdate arg0);

	void clear();

	CameraPosition getCameraPosition();

	int getMapType();

	float getMaxZoomLevel();

	float getMinZoomLevel();

	Location getMyLocation();

	IProjection getProjection();

	UiSettings getUiSettings();

	boolean isIndoorEnabled();

	boolean isMyLocationEnabled();

	boolean isTrafficEnabled();

	void moveCamera(CameraUpdate arg0);

	boolean setIndoorEnabled(boolean arg0);

	void setInfoWindowAdapter(InfoWindowAdapter arg0);

	void setLocationSource(LocationSource arg0);

	void setMapType(int arg0);

	void setMyLocationEnabled(boolean arg0);

	void setOnCameraChangeListener(OnCameraChangeListener arg0);

	void setOnInfoWindowClickListener(OnInfoWindowClickListener arg0);

	void setOnMapClickListener(OnMapClickListener arg0);

	void setOnMapLongClickListener(OnMapLongClickListener arg0);

	void setOnMarkerClickListener(OnMarkerClickListener arg0);

	void setOnMarkerDragListener(OnMarkerDragListener arg0);

	void setOnMyLocationChangeListener(OnMyLocationChangeListener arg0);

	void setTrafficEnabled(boolean arg0);

	void stopAnimation();

	GoogleMap getMap();
}