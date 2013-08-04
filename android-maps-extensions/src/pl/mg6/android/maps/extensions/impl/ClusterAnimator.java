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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import pl.mg6.android.maps.extensions.AnimationSettings;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.os.SystemClock;
import android.view.animation.Interpolator;

import com.google.android.gms.maps.model.LatLng;

class ClusterAnimator {

	private Handler handler = new Handler(new Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			calculatePositions();
			return true;
		}
	});

	private Map<ClusterMarker, AnimationData> queue = new HashMap<ClusterMarker, AnimationData>();

	private void calculatePositions() {
		long now = SystemClock.uptimeMillis();
		Iterator<ClusterMarker> iterator = queue.keySet().iterator();
		while (iterator.hasNext()) {
			ClusterMarker marker = iterator.next();
			AnimationData data = queue.get(marker);
			long time = now - data.start;
			if (time <= 0) {
				marker.setVirtualPosition(data.from);
			} else if (time >= data.duration) {
				marker.removeVirtual();
				iterator.remove();
			} else {
				float t = ((float) time) / data.duration;
				t = data.interpolator.getInterpolation(t);
				double lat = (1.0f - t) * data.from.latitude + t * data.to.latitude;
				double lng = (1.0f - t) * data.from.longitude + t * data.to.longitude;
				marker.setVirtualPosition(new LatLng(lat, lng));
			}
		}
		if (queue.size() > 0) {
			handler.sendEmptyMessage(0);
		}
	}

	public void animate(ClusterMarker marker, LatLng from, LatLng to, long start, AnimationSettings settings) {
		AnimationData data = new AnimationData();
		data.from = from;
		data.to = to;
		data.start = start;
		data.duration = settings.getDuration();
		data.interpolator = settings.getInterpolator();
		queue.put(marker, data);
		handler.removeMessages(0);
		handler.sendEmptyMessage(0);
	}

	private static class AnimationData {

		private LatLng from;

		private LatLng to;

		private long start;

		private long duration;

		private Interpolator interpolator;
	}
}
