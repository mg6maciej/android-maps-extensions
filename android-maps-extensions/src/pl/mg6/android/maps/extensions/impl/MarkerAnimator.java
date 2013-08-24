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
import pl.mg6.android.maps.extensions.Marker;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.os.SystemClock;
import android.view.animation.Interpolator;

import com.google.android.gms.maps.model.LatLng;

class MarkerAnimator {

	private Handler handler = new Handler(new Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			calculatePositions();
			return true;
		}
	});

	private Map<DelegatingMarker, AnimationData> queue = new HashMap<DelegatingMarker, AnimationData>();

	private void calculatePositions() {
		long now = SystemClock.uptimeMillis();
		Iterator<DelegatingMarker> iterator = queue.keySet().iterator();
		while (iterator.hasNext()) {
			DelegatingMarker marker = iterator.next();
			AnimationData data = queue.get(marker);
			long time = now - data.start;
			if (time <= 0) {
				marker.setPositionDuringAnimation(data.from);
			} else if (time >= data.duration) {
				marker.setPositionDuringAnimation(data.to);
				if (data.callback != null) {
					data.callback.onFinish(marker);
				}
				iterator.remove();
			} else {
				float t = ((float) time) / data.duration;
				t = data.interpolator.getInterpolation(t);
				double lat = (1.0f - t) * data.from.latitude + t * data.to.latitude;
				double lng = (1.0f - t) * data.from.longitude + t * data.to.longitude;
				marker.setPositionDuringAnimation(new LatLng(lat, lng));
			}
		}
		if (queue.size() > 0) {
			handler.sendEmptyMessage(0);
		}
	}

	public void animate(DelegatingMarker marker, LatLng from, LatLng to, long start, AnimationSettings settings, Marker.AnimationCallback callback) {
		AnimationData data = new AnimationData();
		data.from = from;
		data.to = to;
		data.start = start;
		data.duration = settings.getDuration();
		data.interpolator = settings.getInterpolator();
		data.callback = callback;
		queue.put(marker, data);
		handler.removeMessages(0);
		handler.sendEmptyMessage(0);
	}

	public void cancelAnimation(DelegatingMarker marker, Marker.AnimationCallback.CancelReason reason) {
		AnimationData data = queue.remove(marker);
		if (data != null && data.callback != null) {
			data.callback.onCancel(marker, reason);
		}
	}

	private static class AnimationData {

		private LatLng from;

		private LatLng to;

		private long start;

		private long duration;

		private Interpolator interpolator;

		private Marker.AnimationCallback callback;
	}
}
