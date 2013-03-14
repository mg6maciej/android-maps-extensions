package pl.mg6.android.maps.extensions;

import com.google.android.gms.maps.model.LatLng;

public interface Circle {

	LatLng getCenter();

	Object getData();

	int getFillColor();

	@Deprecated
	String getId();

	double getRadius();

	int getStrokeColor();

	float getStrokeWidth();

	float getZIndex();

	boolean including(LatLng position);

	boolean isVisible();

	void remove();

	void setCenter(LatLng center);

	void setFillColor(int fillColor);

	void setRadius(double radius);

	void setStrokeColor(int strokeColor);

	void setStrokeWidth(float strokeWidth);

	void setVisible(boolean visible);

	void setZIndex(float zIndex);

}