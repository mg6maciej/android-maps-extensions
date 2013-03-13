package pl.mg6.android.maps.extensions;

import com.google.android.gms.maps.model.LatLng;

public interface Marker {

	/**
	 * WARNING: may be changed in future API when this is fixed:
	 * http://code.google.com/p/gmaps-api-issues/issues/detail?id=4650
	 */
	Object getData();

	/**
	 * http://code.google.com/p/gmaps-api-issues/issues/detail?id=5101
	 */
	@Deprecated
	String getId();

	LatLng getPosition();

	String getSnippet();

	String getTitle();

	void hideInfoWindow();

	boolean isDraggable();

	boolean isInfoWindowShown();

	boolean isVisible();

	void remove();

	/**
	 * WARNING: may be changed in future API when this is fixed:
	 * http://code.google.com/p/gmaps-api-issues/issues/detail?id=4650
	 */
	void setData(Object data);

	void setDraggable(boolean draggable);

	void setPosition(LatLng position);

	void setSnippet(String snippet);

	void setTitle(String title);

	void setVisible(boolean visible);

	void showInfoWindow();
}