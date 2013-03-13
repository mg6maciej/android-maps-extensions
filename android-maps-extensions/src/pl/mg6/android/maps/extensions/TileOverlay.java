package pl.mg6.android.maps.extensions;

public interface TileOverlay {

	void clearTileCache();

	Object getData();

	@Deprecated
	String getId();

	float getZIndex();

	boolean isVisible();

	void remove();

	void setData(Object data);

	void setVisible(boolean visible);

	void setZIndex(float zIndex);
}