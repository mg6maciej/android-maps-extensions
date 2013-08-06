/*
 * Copyright (C) 2013 Maciej Górski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pl.mg6.android.maps.extensions.impl;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import pl.mg6.android.maps.extensions.ClusteringSettings;
import pl.mg6.android.maps.extensions.Marker;

import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

@RunWith(MockitoJUnitRunner.class)
public class GridClusteringStrategyTest {

	@Mock
	private IGoogleMap map;

	@Mock
	private ClusterRefresher refresher;

	@Mock
	private DelegatingMarker marker1;

	@Mock
	private DelegatingMarker marker2;

	@Mock
	private DelegatingMarker marker3;

	private ClusteringStrategy strategy;

	@Before
	public void init() {
		Mockito.when(map.getCameraPosition()).thenReturn(new CameraPosition(new LatLng(0, 0), 8, 0, 0));

		Mockito.when(marker1.isVisible()).thenReturn(true);
		Mockito.when(marker1.getPosition()).thenReturn(new LatLng(0, 0));

		Mockito.when(marker2.isVisible()).thenReturn(true);
		Mockito.when(marker2.getPosition()).thenReturn(new LatLng(50, 50));

		Mockito.when(marker3.isVisible()).thenReturn(true);
		Mockito.when(marker3.getPosition()).thenReturn(new LatLng(0.1, 0.1));

		ClusteringSettings settings = new ClusteringSettings();
		strategy = new GridClusteringStrategy(settings, map, new ArrayList<DelegatingMarker>(), refresher);
	}

	@Test
	public void whenAddedOneMarkerShouldDisplayOneMarker() {

		strategy.onAdd(marker1);

		List<Marker> markers = strategy.getDisplayedMarkers();

		Assert.assertNotNull(markers);
		Assert.assertEquals(1, markers.size());
		Assert.assertEquals(marker1, markers.get(0));
	}

	@Test
	public void whenAddedTwoMarkersShouldDisplayTwoMarkers() {

		strategy.onAdd(marker1);
		strategy.onAdd(marker2);

		List<Marker> markers = strategy.getDisplayedMarkers();

		Assert.assertNotNull(markers);
		Assert.assertEquals(2, markers.size());
		Assert.assertTrue(markers.contains(marker1));
		Assert.assertTrue(markers.contains(marker2));
	}

	@Test
	public void whenAddedTwoCloseMarkersShouldDisplayOneClusterMarker() {

		strategy.onAdd(marker1);
		strategy.onAdd(marker3);

		List<Marker> markers = strategy.getDisplayedMarkers();

		Assert.assertNotNull(markers);
		Assert.assertEquals(1, markers.size());
		Assert.assertTrue(markers.get(0) instanceof ClusterMarker);
	}

	@Test
	public void whenAddedTwoCloseMarkersAndZoomedInShouldDisplayTwoMarkers() {

		strategy.onAdd(marker1);
		strategy.onAdd(marker3);

		strategy.onCameraChange(new CameraPosition(new LatLng(0, 0), 21, 0, 0));

		List<Marker> markers = strategy.getDisplayedMarkers();

		Assert.assertNotNull(markers);
		Assert.assertEquals(2, markers.size());
		Assert.assertTrue(markers.contains(marker1));
		Assert.assertTrue(markers.contains(marker3));
	}

	@Test
	public void whenAddedTwoCloseMarkersAndRemovedThemShouldDisplayZeroMarkers() {

		strategy.onAdd(marker1);
		strategy.onAdd(marker2);

		strategy.onRemove(marker1);
		strategy.onRemove(marker2);

		List<Marker> markers = strategy.getDisplayedMarkers();

		Assert.assertNotNull(markers);
		Assert.assertEquals(0, markers.size());
	}

	@Test
	public void whenAddedMarkerAndHideItShouldDisplayZeroMarkers() {

		strategy.onAdd(marker1);

		strategy.onVisibilityChangeRequest(marker1, false);

		List<Marker> markers = strategy.getDisplayedMarkers();

		Assert.assertNotNull(markers);
		Assert.assertEquals(0, markers.size());
	}

	@Test
	public void whenAddedHiddenMarkerShouldDisplayZeroMarkers() {

		Mockito.when(marker1.isVisible()).thenReturn(false);

		strategy.onAdd(marker1);

		List<Marker> markers = strategy.getDisplayedMarkers();

		Assert.assertNotNull(markers);
		Assert.assertEquals(0, markers.size());
	}

	@Test
	public void whenAddedTwoMarkersAndChangedPositionShouldDisplayOnClusterMarker() {

		strategy.onAdd(marker1);
		strategy.onAdd(marker2);

		Mockito.when(marker2.getPosition()).thenReturn(new LatLng(0.1, 0.1));

		strategy.onPositionChange(marker2);

		List<Marker> markers = strategy.getDisplayedMarkers();

		Assert.assertNotNull(markers);
		Assert.assertEquals(1, markers.size());
		Assert.assertTrue(markers.get(0) instanceof ClusterMarker);
	}

	@Test
	public void whenChangingPositionAndCameraZoomShouldNotCrashBadly() {
		strategy.onAdd(marker1);
		strategy.onAdd(marker2);

		strategy.onCameraChange(new CameraPosition(new LatLng(0, 0), 21, 0, 0));

		Mockito.when(marker2.getPosition()).thenReturn(new LatLng(0.1, 0.1));
		strategy.onPositionChange(marker2);

		strategy.onCameraChange(new CameraPosition(new LatLng(0, 0), 8, 0, 0));

		Mockito.when(marker2.getPosition()).thenReturn(new LatLng(50, 50));
		strategy.onPositionChange(marker2);

		strategy.onCameraChange(new CameraPosition(new LatLng(0, 0), 21, 0, 0));
	}

	@Test(expected = UnsupportedOperationException.class)
	public void whenNotAddedShouldThrowException() {

		strategy.getMinZoomLevelNotClustered(marker1);
	}

	@Test
	public void whenSingleMarkerAddedShouldReturnZero() {

		strategy.onAdd(marker1);

		Assert.assertEquals(0.0f, strategy.getMinZoomLevelNotClustered(marker1), 0.001f);
	}

	@Test
	public void whenAddedMarkersAreFarFromEachOtherShouldReturnSmallZoom() {

		strategy.onAdd(marker1);
		strategy.onAdd(marker2);

		Assert.assertEquals(2.0f, strategy.getMinZoomLevelNotClustered(marker1), 0.0001f);
		Assert.assertEquals(2.0f, strategy.getMinZoomLevelNotClustered(marker2), 0.0001f);
	}

	@Test
	public void whenAddedMarkersAreCloseToEachOtherShouldReturnBigZoom() {

		strategy.onAdd(marker1);
		strategy.onAdd(marker3);

		Assert.assertEquals(11.0f, strategy.getMinZoomLevelNotClustered(marker1), 0.0001f);
		Assert.assertEquals(11.0f, strategy.getMinZoomLevelNotClustered(marker3), 0.0001f);
	}
}
