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
package com.androidmapsextensions.impl;

import com.androidmapsextensions.Polyline;
import com.google.android.gms.maps.model.Cap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PatternItem;

import java.util.List;

class DelegatingPolyline implements Polyline {

    private com.google.android.gms.maps.model.Polyline real;
    private PolylineManager manager;

    private Object data;

    DelegatingPolyline(com.google.android.gms.maps.model.Polyline real, PolylineManager manager) {
        this.real = real;
        this.manager = manager;
    }

    @Override
    public int getColor() {
        return real.getColor();
    }

    @Override
    public Object getData() {
        return data;
    }

    @Override
    public Cap getEndCap() {
        return real.getEndCap();
    }

    @Deprecated
    @Override
    public String getId() {
        return real.getId();
    }

    @Override
    public int getJointType() {
        return real.getJointType();
    }

    @Override
    public List<PatternItem> getPattern() {
        return real.getPattern();
    }

    @Override
    public List<LatLng> getPoints() {
        return real.getPoints();
    }

    @Override
    public Cap getStartCap() {
        return real.getStartCap();
    }

    @Override
    public Object getTag() {
        return real.getTag();
    }

    @Override
    public float getWidth() {
        return real.getWidth();
    }

    @Override
    public float getZIndex() {
        return real.getZIndex();
    }

    @Override
    public boolean isClickable() {
        return real.isClickable();
    }

    @Override
    public boolean isGeodesic() {
        return real.isGeodesic();
    }

    @Override
    public boolean isVisible() {
        return real.isVisible();
    }

    @Override
    public void remove() {
        manager.onRemove(real);
        real.remove();
    }

    @Override
    public void setClickable(boolean clickable) {
        real.setClickable(clickable);
    }

    @Override
    public void setColor(int color) {
        real.setColor(color);
    }

    @Override
    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public void setEndCap(Cap endCap) {
        real.setEndCap(endCap);
    }

    @Override
    public void setGeodesic(boolean geodesic) {
        real.setGeodesic(geodesic);
    }

    @Override
    public void setJointType(int jointType) {
        real.setJointType(jointType);
    }

    @Override
    public void setPattern(List<? extends PatternItem> pattern) {
        real.setPattern((List<PatternItem>) pattern);
    }

    @Override
    public void setPoints(List<LatLng> points) {
        real.setPoints(points);
    }

    @Override
    public void setStartCap(Cap startCap) {
        real.setStartCap(startCap);
    }

    @Override
    public void setTag(Object tag) {
        real.setTag(tag);
    }

    @Override
    public void setVisible(boolean visible) {
        real.setVisible(visible);
    }

    @Override
    public void setWidth(float width) {
        real.setWidth(width);
    }

    @Override
    public void setZIndex(float zIndex) {
        real.setZIndex(zIndex);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DelegatingPolyline)) {
            return false;
        }
        DelegatingPolyline other = (DelegatingPolyline) o;
        return real.equals(other.real);
    }

    @Override
    public int hashCode() {
        return real.hashCode();
    }

    @Override
    public String toString() {
        return real.toString();
    }
}
