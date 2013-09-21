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
package pl.mg6.android.maps.extensions;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.v4.util.LruCache;

import com.google.android.gms.R;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.util.List;

public class DefaultClusterOptionsProvider implements ClusterOptionsProvider {

	private final LruCache<Integer, BitmapDescriptor> cache = new LruCache<Integer, BitmapDescriptor>(128);
	private final ClusterOptions clusterOptions = new ClusterOptions().anchor(0.5f, 0.5f);
	private final int[] colors;
	private final Paint circlePaint;
	private final Paint circleShadowPaint;
	private final Paint textPaint;
	private final Rect bounds = new Rect();
	private float blurRadius;
	private float textPadding;
	private float shadowBlurRadius;
	private float shadowOffsetX;
	private float shadowOffsetY;

	public DefaultClusterOptionsProvider(Resources resources) {
		colors = new int[] {
				resources.getColor(R.color.ame_default_cluster_circle_color_small),
				resources.getColor(R.color.ame_default_cluster_circle_color_medium),
				resources.getColor(R.color.ame_default_cluster_circle_color_large),
				resources.getColor(R.color.ame_default_cluster_circle_color_extra_large),
		};
		circlePaint = createCirclePaint(resources);
		circleShadowPaint = createCircleShadowPaint(resources);
		textPaint = createTextPaint(resources);
		textPadding = resources.getDimension(R.dimen.ame_default_cluster_text_padding);
	}

	private Paint createCirclePaint(Resources resources) {
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		blurRadius = resources.getDimension(R.dimen.ame_default_cluster_circle_blur_radius);
		if (blurRadius > 0.0f) {
			BlurMaskFilter maskFilter = new BlurMaskFilter(blurRadius, BlurMaskFilter.Blur.SOLID);
			paint.setMaskFilter(maskFilter);
		}
		return paint;
	}

	private Paint createCircleShadowPaint(Resources resources) {
		Paint paint = null;
		float circleShadowBlurRadius = resources.getDimension(R.dimen.ame_default_cluster_circle_shadow_blur_radius);
		if (circleShadowBlurRadius > 0.0f) {
			paint = new Paint(Paint.ANTI_ALIAS_FLAG);
			float offsetX = resources.getDimension(R.dimen.ame_default_cluster_circle_shadow_offset_x);
			float offsetY = resources.getDimension(R.dimen.ame_default_cluster_circle_shadow_offset_y);
			int color = resources.getColor(R.color.ame_default_cluster_circle_shadow_color);
			paint.setShadowLayer(circleShadowBlurRadius, offsetX, offsetY, color);
		}
		return paint;
	}

	private Paint createTextPaint(Resources resources) {
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(resources.getColor(R.color.ame_default_cluster_text_color));
		shadowBlurRadius = resources.getDimension(R.dimen.ame_default_cluster_text_shadow_blur_radius);
		if (shadowBlurRadius > 0.0f) {
			shadowOffsetX = resources.getDimension(R.dimen.ame_default_cluster_text_shadow_offset_x);
			shadowOffsetY = resources.getDimension(R.dimen.ame_default_cluster_text_shadow_offset_y);
			int shadowColor = resources.getColor(R.color.ame_default_cluster_text_shadow_color);
			paint.setShadowLayer(shadowBlurRadius, shadowOffsetX, shadowOffsetY, shadowColor);
		}
		paint.setTextSize(resources.getDimension(R.dimen.ame_default_cluster_text_size));
		paint.setTypeface(Typeface.DEFAULT_BOLD);
		return paint;
	}

	@Override
	public ClusterOptions getClusterOptions(List<Marker> markers) {
		int count = markers.size();
		BitmapDescriptor icon = cache.get(count);
		if (icon == null) {
			icon = createIcon(count);
			cache.put(count, icon);
		}
		clusterOptions.icon(icon);
		return clusterOptions;
	}

	private BitmapDescriptor createIcon(int count) {
		String text = String.valueOf(count);
		calculateTextSize(text);
		int iconSize = calculateIconSize();
		Bitmap bitmap = Bitmap.createBitmap(iconSize, iconSize, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		drawCircle(canvas, count, iconSize);
		drawText(canvas, text, iconSize);
		return BitmapDescriptorFactory.fromBitmap(bitmap);
	}

	private void calculateTextSize(String text) {
		textPaint.getTextBounds(text, 0, text.length(), bounds);
	}

	private int calculateIconSize() {
		int w = bounds.width();
		int h = bounds.height();
		return (int) Math.ceil(2 * (textPadding + blurRadius) + Math.sqrt(w * w + h * h));
	}

	private void drawCircle(Canvas canvas, int count, float iconSize) {
		canvas.drawCircle(iconSize / 2, iconSize / 2, iconSize / 2 - blurRadius, circleShadowPaint);
		for (int i = colors.length - 1; i >= 0; i--) {
			if (count >= Math.pow(10, i)) {
				circlePaint.setColor(colors[i]);
				break;
			}
		}
		canvas.drawCircle(iconSize / 2, iconSize / 2, iconSize / 2 - blurRadius, circlePaint);
	}

	private void drawText(Canvas canvas, String text, int iconSize) {
		int x = Math.round((iconSize - bounds.width()) / 2 - bounds.left - shadowOffsetX / 2);
		int y = Math.round((iconSize - bounds.height()) / 2 - bounds.top - shadowOffsetY / 2);
		canvas.drawText(text, x, y, textPaint);
	}
}
