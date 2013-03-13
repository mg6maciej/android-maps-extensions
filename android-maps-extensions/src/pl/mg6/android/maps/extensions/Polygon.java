package pl.mg6.android.maps.extensions;

import java.util.List;

import com.google.android.gms.maps.model.LatLng;

public interface Polygon {

	Object getData();

	int getFillColor();

	List<List<LatLng>> getHoles();

	@Deprecated
	String getId();

	List<LatLng> getPoints();

	int getStrokeColor();

	float getStrokeWidth();

	float getZIndex();

	boolean isGeodesic();

	boolean isVisible();

	void remove();

	void setData(Object data);

	void setFillColor(int fillColor);

	void setGeodesic(boolean geodesic);

	void setHoles(List<? extends List<LatLng>> holes);

	void setPoints(List<LatLng> points);

	void setStrokeColor(int strokeColor);

	void setStrokeWidth(float strokeWidth);

	void setVisible(boolean visible);

	void setZIndex(float zIndex);
}