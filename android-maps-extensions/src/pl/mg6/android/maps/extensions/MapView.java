package pl.mg6.android.maps.extensions;

import pl.mg6.android.maps.extensions.impl.DelegatingGoogleMap;
import android.content.Context;
import android.util.AttributeSet;

import com.google.android.gms.maps.GoogleMapOptions;

public class MapView extends com.google.android.gms.maps.MapView {

	private GoogleMap map;

	public MapView(Context context) {
		super(context);
	}

	public MapView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MapView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public MapView(Context context, GoogleMapOptions options) {
		super(context, options);
	}

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
