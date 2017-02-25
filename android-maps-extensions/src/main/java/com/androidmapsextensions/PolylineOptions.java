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
package com.androidmapsextensions;

import com.google.android.gms.maps.model.Cap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PatternItem;

import java.util.List;

public class PolylineOptions {

    public final com.google.android.gms.maps.model.PolylineOptions real = new com.google.android.gms.maps.model.PolylineOptions();
    private Object data;

    public PolylineOptions add(LatLng point) {
        real.add(point);
        return this;
    }

    public PolylineOptions add(LatLng... points) {
        real.add(points);
        return this;
    }

    public PolylineOptions addAll(Iterable<LatLng> points) {
        real.addAll(points);
        return this;
    }

    public PolylineOptions clickable(boolean clickable) {
        real.clickable(clickable);
        return this;
    }

    public PolylineOptions color(int color) {
        real.color(color);
        return this;
    }

    public PolylineOptions data(Object data) {
        this.data = data;
        return this;
    }

    public PolylineOptions endCap(Cap cap) {
        real.endCap(cap);
        return this;
    }

    public PolylineOptions geodesic(boolean geodesic) {
        real.geodesic(geodesic);
        return this;
    }

    public int getColor() {
        return real.getColor();
    }

    public Object getData() {
        return data;
    }

    public Cap getEndCap() {
        return real.getEndCap();
    }

    public int getJointType() {
        return real.getJointType();
    }

    public List<PatternItem> getPattern() {
        return real.getPattern();
    }

    public List<LatLng> getPoints() {
        return real.getPoints();
    }

    public Cap getStartCap() {
        return real.getStartCap();
    }

    public float getWidth() {
        return real.getWidth();
    }

    public float getZIndex() {
        return real.getZIndex();
    }

    public boolean isClickable() {
        return real.isClickable();
    }

    public boolean isGeodesic() {
        return real.isGeodesic();
    }

    public boolean isVisible() {
        return real.isVisible();
    }

    public PolylineOptions jointType(int type) {
        real.jointType(type);
        return this;
    }

    public PolylineOptions pattern(List<PatternItem> pattern) {
        real.pattern(pattern);
        return this;
    }

    public PolylineOptions startCap(Cap cap) {
        real.startCap(cap);
        return this;
    }

    public PolylineOptions visible(boolean visible) {
        real.visible(visible);
        return this;
    }

    public PolylineOptions width(float width) {
        real.width(width);
        return this;
    }

    public PolylineOptions zIndex(float zIndex) {
        real.zIndex(zIndex);
        return this;
    }
}
