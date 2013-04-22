package pl.mg6.android.maps.extensions.impl;

import android.graphics.Point;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.VisibleRegion;

public interface IProjection {

	LatLng fromScreenLocation(Point arg0);

	VisibleRegion getVisibleRegion();

	Point toScreenLocation(LatLng arg0);
}