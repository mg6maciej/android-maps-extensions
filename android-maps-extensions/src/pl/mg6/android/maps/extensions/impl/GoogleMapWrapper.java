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

// TODO: to be deleted when com.google.android.gms.maps.GoogleMap becomes an interface
class GoogleMapWrapper implements IGoogleMap {

	private GoogleMap map;

	public GoogleMapWrapper(GoogleMap map) {
		this.map = map;
	}

	@Override
	public final Circle addCircle(CircleOptions arg0) {
		return map.addCircle(arg0);
	}

	@Override
	public final GroundOverlay addGroundOverlay(GroundOverlayOptions arg0) {
		return map.addGroundOverlay(arg0);
	}

	@Override
	public final Marker addMarker(MarkerOptions arg0) {
		return map.addMarker(arg0);
	}

	@Override
	public final Polygon addPolygon(PolygonOptions arg0) {
		return map.addPolygon(arg0);
	}

	@Override
	public final Polyline addPolyline(PolylineOptions arg0) {
		return map.addPolyline(arg0);
	}

	@Override
	public final TileOverlay addTileOverlay(TileOverlayOptions arg0) {
		return map.addTileOverlay(arg0);
	}

	@Override
	public final void animateCamera(CameraUpdate arg0, CancelableCallback arg1) {
		map.animateCamera(arg0, arg1);
	}

	@Override
	public final void animateCamera(CameraUpdate arg0, int arg1, CancelableCallback arg2) {
		map.animateCamera(arg0, arg1, arg2);
	}

	@Override
	public final void animateCamera(CameraUpdate arg0) {
		map.animateCamera(arg0);
	}

	@Override
	public final void clear() {
		map.clear();
	}

	@Override
	public final CameraPosition getCameraPosition() {
		return map.getCameraPosition();
	}

	@Override
	public final int getMapType() {
		return map.getMapType();
	}

	@Override
	public final float getMaxZoomLevel() {
		return map.getMaxZoomLevel();
	}

	@Override
	public final float getMinZoomLevel() {
		return map.getMinZoomLevel();
	}

	@Override
	public final Location getMyLocation() {
		return map.getMyLocation();
	}

	@Override
	public final ProjectionWrapper getProjection() {
		return new ProjectionWrapper(map.getProjection());
	}

	@Override
	public final UiSettings getUiSettings() {
		return map.getUiSettings();
	}

	@Override
	public final boolean isIndoorEnabled() {
		return map.isIndoorEnabled();
	}

	@Override
	public final boolean isMyLocationEnabled() {
		return map.isMyLocationEnabled();
	}

	@Override
	public final boolean isTrafficEnabled() {
		return map.isTrafficEnabled();
	}

	@Override
	public final void moveCamera(CameraUpdate arg0) {
		map.moveCamera(arg0);
	}

	@Override
	public final boolean setIndoorEnabled(boolean arg0) {
		return map.setIndoorEnabled(arg0);
	}

	@Override
	public final void setInfoWindowAdapter(InfoWindowAdapter arg0) {
		map.setInfoWindowAdapter(arg0);
	}

	@Override
	public final void setLocationSource(LocationSource arg0) {
		map.setLocationSource(arg0);
	}

	@Override
	public final void setMapType(int arg0) {
		map.setMapType(arg0);
	}

	@Override
	public final void setMyLocationEnabled(boolean arg0) {
		map.setMyLocationEnabled(arg0);
	}

	@Override
	public final void setOnCameraChangeListener(OnCameraChangeListener arg0) {
		map.setOnCameraChangeListener(arg0);
	}

	@Override
	public final void setOnInfoWindowClickListener(OnInfoWindowClickListener arg0) {
		map.setOnInfoWindowClickListener(arg0);
	}

	@Override
	public final void setOnMapClickListener(OnMapClickListener arg0) {
		map.setOnMapClickListener(arg0);
	}

	@Override
	public final void setOnMapLongClickListener(OnMapLongClickListener arg0) {
		map.setOnMapLongClickListener(arg0);
	}

	@Override
	public final void setOnMarkerClickListener(OnMarkerClickListener arg0) {
		map.setOnMarkerClickListener(arg0);
	}

	@Override
	public final void setOnMarkerDragListener(OnMarkerDragListener arg0) {
		map.setOnMarkerDragListener(arg0);
	}

	@Override
	public final void setOnMyLocationChangeListener(OnMyLocationChangeListener arg0) {
		map.setOnMyLocationChangeListener(arg0);
	}

	@Override
	public final void setTrafficEnabled(boolean arg0) {
		map.setTrafficEnabled(arg0);
	}

	@Override
	public final void stopAnimation() {
		map.stopAnimation();
	}

	@Override
	public GoogleMap getMap() {
		return map;
	}
}
