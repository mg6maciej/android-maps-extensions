package pl.mg6.android.maps.extensions;

import pl.mg6.android.maps.extensions.impl.DelegatingGoogleMap;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMapOptions;

public class SupportMapFragment extends com.google.android.gms.maps.SupportMapFragment {

	// value taken from google-play-services.jar
	private static final String MAP_OPTIONS = "MapOptions";

	public static SupportMapFragment newInstance() {
		SupportMapFragment f = new SupportMapFragment();
		return f;
	}

	public static SupportMapFragment newInstance(GoogleMapOptions options) {
		SupportMapFragment f = new SupportMapFragment();
		Bundle args = new Bundle();
		args.putParcelable(MAP_OPTIONS, options);
		f.setArguments(args);
		return f;
	}

	private GoogleMap map;

	public GoogleMap getExtendedMap() {
		if (map == null) {
			com.google.android.gms.maps.GoogleMap realMap = super.getMap();
			if (realMap != null) {
				map = new DelegatingGoogleMap(realMap);
			}
		}
		return map;
	}
}
