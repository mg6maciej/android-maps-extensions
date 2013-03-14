package pl.mg6.android.maps.extensions.impl;

interface ClusteringStrategy extends MarkerStateChangeListener {

	void onAdd(DelegatingMarker marker);
}
