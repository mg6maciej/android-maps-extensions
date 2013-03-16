package pl.mg6.android.maps.extensions.demo;

import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import pl.mg6.android.maps.extensions.Circle;
import pl.mg6.android.maps.extensions.ClusteringSettings;
import pl.mg6.android.maps.extensions.ClusteringSettings.IconProvider;
import pl.mg6.android.maps.extensions.GoogleMap;
import pl.mg6.android.maps.extensions.GoogleMap.InfoWindowAdapter;
import pl.mg6.android.maps.extensions.GoogleMap.OnInfoWindowClickListener;
import pl.mg6.android.maps.extensions.GoogleMap.OnMapClickListener;
import pl.mg6.android.maps.extensions.Marker;
import pl.mg6.android.maps.extensions.SupportMapFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.LatLngBounds.Builder;

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
						return;
					}
				}
			}
		});

		BitmapDescriptor defaultIcon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN);
		IconProvider iconProvider = new IconProvider() {

			@Override
			public BitmapDescriptor getIcon(Marker cluster) {
				if (cluster.getMarkers().size() > 10) {
					return BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE);
				}
				return null;
			}
		};
		map.setClustering(new ClusteringSettings().defaultIcon(defaultIcon).iconProvider(iconProvider));

		map.setInfoWindowAdapter(new InfoWindowAdapter() {

			private TextView tv = new TextView(DemoActivity.this);

			private Collator collator = Collator.getInstance();
			private Comparator<Marker> comparator = new Comparator<Marker>() {
				public int compare(Marker lhs, Marker rhs) {
					String leftTitle = lhs.getTitle();
					String rightTitle = rhs.getTitle();
					if (leftTitle == null && rightTitle == null) {
						return 0;
					}
					if (leftTitle == null) {
						return 1;
					}
					if (rightTitle == null) {
						return -1;
					}
					return collator.compare(leftTitle, rightTitle);
				}
			};

			@Override
			public View getInfoWindow(Marker marker) {
				return null;
			}

			@Override
			public View getInfoContents(Marker marker) {
				if (marker.isCluster()) {
					List<Marker> markers = marker.getMarkers();
					int i = 0;
					String text = "";
					while (i < 3 && markers.size() > 0) {
						Marker m = Collections.min(markers, comparator);
						text += m.getTitle() + "\n";
						markers.remove(m);
						i++;
					}
					if (markers.size() > 0) {
						text += "and " + markers.size() + " more...";
					} else {
						text = text.substring(0, text.length() - 1);
					}
					tv.setTextColor(Color.BLACK);
					tv.setText(text);
					return tv;
				}
				return null;
			}
		});

		map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

			@Override
			public void onInfoWindowClick(Marker marker) {
				if (marker.isCluster()) {
					List<Marker> markers = marker.getMarkers();
					Builder builder = LatLngBounds.builder();
					for (Marker m : markers) {
						builder.include(m.getPosition());
					}
					LatLngBounds bounds = builder.build();
					map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, getResources().getDimensionPixelSize(R.dimen.padding)));
				}
			}
		});

		MarkerGenerator.addMarkersInPoland(map);
	}

	private void addCircles() {
		Circle circle;
		circle = map.addCircle(new CircleOptions().center(new LatLng(0.0, 0.0)).radius(2000000));
		circle.setData("first circle");
		circle = map.addCircle(new CircleOptions().center(new LatLng(30.0, 30.0)).radius(1000000));
		circle.setData("second circle");
	}

	public void onClusterClick(View view) {
		BitmapDescriptor defaultIcon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN);
		IconProvider iconProvider = new IconProvider() {

			@Override
			public BitmapDescriptor getIcon(Marker cluster) {
				if (cluster.getMarkers().size() > 10) {
					return BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE);
				}
				return null;
			}
		};
		map.setClustering(new ClusteringSettings().defaultIcon(defaultIcon).iconProvider(iconProvider));
	}

	public void onNormalClick(View view) {
		map.setClustering(new ClusteringSettings().enabled(false));
	}
}
