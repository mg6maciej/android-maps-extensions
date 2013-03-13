package pl.mg6.android.maps.extensions;

import java.util.List;

import com.google.android.gms.maps.model.LatLng;

public interface Polyline {

	int getColor();

	Object getData();

	@Deprecated
	String getId();

	List<LatLng> getPoints();

	float getWidth();

	float getZIndex();

	boolean isGeodesic();

	boolean isVisible();

	void remove();

	void setColor(int color);

	void setData(Object data);

	void setGeodesic(boolean geodesic);

	void setPoints(List<LatLng> points);

	void setVisible(boolean visible);

	void setWidth(float width);

	void setZIndex(float zIndex);
}