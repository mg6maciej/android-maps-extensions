package pl.mg6.android.maps.extensions.impl;

import android.graphics.Point;

import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.VisibleRegion;

public class ProjectionWrapper implements IProjection {

	private Projection projection;

	public ProjectionWrapper(Projection projection) {
		this.projection = projection;
	}

	@Override
	public LatLng fromScreenLocation(Point arg0) {
		return projection.fromScreenLocation(arg0);
	}

	@Override
	public VisibleRegion getVisibleRegion() {
		return projection.getVisibleRegion();
	}

	@Override
	public Point toScreenLocation(LatLng arg0) {
		return projection.toScreenLocation(arg0);
	}

	public Projection getProjection() {
		return projection;
	}
}
