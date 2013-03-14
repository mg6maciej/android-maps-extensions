package pl.mg6.android.maps.extensions.demo;

import pl.mg6.android.maps.extensions.Circle;
import pl.mg6.android.maps.extensions.GoogleMap;
import pl.mg6.android.maps.extensions.GoogleMap.OnMapClickListener;
import pl.mg6.android.maps.extensions.GoogleMap.OnMarkerClickListener;
import pl.mg6.android.maps.extensions.Marker;
import pl.mg6.android.maps.extensions.SupportMapFragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class DemoActivity extends FragmentActivity {

	private GoogleMap map;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.demo);

		FragmentManager fm = getSupportFragmentManager();
		SupportMapFragment f = (SupportMapFragment) fm.findFragmentById(R.id.map);
		map = f.getExtendedMap();

		addCircles();

		map.setOnMapClickListener(new OnMapClickListener() {

			@Override
			public void onMapClick(LatLng position) {
				for (Circle circle : map.getCircles()) {
					if (circle.contains(position)) {
						Toast.makeText(DemoActivity.this, "Clicked " + circle.getData(), Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
		
		map.addMarker(new MarkerOptions().position(new LatLng(25.0, 0.0)));
		map.addMarker(new MarkerOptions().position(new LatLng(28.0, 1.0)));
		map.addMarker(new MarkerOptions().position(new LatLng(26.0, 2.0)));
		map.addMarker(new MarkerOptions().position(new LatLng(29.0, 5.0)));

		map.addMarker(new MarkerOptions().position(new LatLng(-25.0, 0.0)));
		map.addMarker(new MarkerOptions().position(new LatLng(-28.0, 1.0)));
		map.addMarker(new MarkerOptions().position(new LatLng(-26.0, 2.0)));
		map.addMarker(new MarkerOptions().position(new LatLng(-29.0, 5.0)));

		map.addMarker(new MarkerOptions().position(new LatLng(25.0, -10.0)));
		map.addMarker(new MarkerOptions().position(new LatLng(28.0, -14.0)));
		map.addMarker(new MarkerOptions().position(new LatLng(26.0, -20.0)));
		map.addMarker(new MarkerOptions().position(new LatLng(29.0, -50.0)));

		map.setClusteringEnabled(true);
		
		map.setOnMapClickListener(new OnMapClickListener() {
			
			@Override
			public void onMapClick(LatLng position) {
				map.addMarker(new MarkerOptions().position(position).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
			}
		});
		
		map.setOnMarkerClickListener(new OnMarkerClickListener() {
			
			@Override
			public boolean onMarkerClick(Marker marker) {
				if (marker != null) {
					marker.remove();
				}
				return true;
			}
		});
	}

	private void addCircles() {
		Circle circle;
		circle = map.addCircle(new CircleOptions().center(new LatLng(0.0, 0.0)).radius(2000000));
		circle.setData("first circle");
		circle = map.addCircle(new CircleOptions().center(new LatLng(30.0, 30.0)).radius(1000000));
		circle.setData("second circle");
	}
}
