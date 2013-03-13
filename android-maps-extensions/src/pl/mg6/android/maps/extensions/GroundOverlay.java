package pl.mg6.android.maps.extensions;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

public interface GroundOverlay {

	float getBearing();

	LatLngBounds getBounds();

	Object getData();

	float getHeight();

	@Deprecated
	String getId();

	LatLng getPosition();

	float getTransparency();

	float getWidth();

	float getZIndex();

	boolean isVisible();

	void remove();

	void setBearing(float bearing);

	void setData(Object data);

	void setDimensions(float width, float height);

	void setDimensions(float width);

	void setPosition(LatLng position);

	void setPositionFromBounds(LatLngBounds bounds);

	void setTransparency(float transparency);

	void setVisible(boolean visible);

	void setZIndex(float zIndex);
}