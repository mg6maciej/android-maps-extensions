package pl.mg6.android.maps.extensions.impl;

interface MarkerStateChangeListener {

	void onRemove(DelegatingMarker marker);

	void onPositionChange(DelegatingMarker marker);

	void onVisibilityChangeRequest(DelegatingMarker marker, boolean visible);
}
