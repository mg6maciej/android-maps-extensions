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

import com.androidmapsextensions.AnimationSettings;
import com.androidmapsextensions.Marker;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class ClusterMarker implements Marker {

    private int lastCount = -1;

    private GridClusteringStrategy strategy;

    private com.google.android.gms.maps.model.Marker virtual;

    private List<DelegatingMarker> markers = new ArrayList<DelegatingMarker>();

    public ClusterMarker(GridClusteringStrategy strategy) {
        this.strategy = strategy;
    }

    com.google.android.gms.maps.model.Marker getVirtual() {
        return virtual;
    }

    void add(DelegatingMarker marker) {
        markers.add(marker);
    }

    void remove(DelegatingMarker marker) {
        markers.remove(marker);
    }

    void refresh() {
        int count = markers.size();
        if (count == 0) {
            removeVirtual();
        } else if (count < strategy.getMinMarkersCount()) {
            removeVirtual();
            for (DelegatingMarker m : markers) {
                m.changeVisible(true);
            }
        } else {
            for (DelegatingMarker m : markers) {
                m.changeVisible(false);
            }
            LatLng position = calculateClusterPosition();
            if (virtual == null || lastCount != count) {
                removeVirtual();
                lastCount = count;
                virtual = strategy.createMarker(new ArrayList<Marker>(markers), position);
            } else {
                virtual.setPosition(position);
            }
        }
    }

    private LatLng calculateClusterPosition() {
        LatLngBounds.Builder builder = LatLngBounds.builder();
        for (DelegatingMarker m : markers) {
            builder.include(m.getPosition());
        }
        return builder.build().getCenter();
    }

    List<? extends Marker> getDisplayedMarkers() {
        int count = markers.size();
        if (count == 0) {
            return null;
        } else if (count < strategy.getMinMarkersCount()) {
            return markers;
        } else {
            return Collections.singletonList(this);
        }
    }

    void removeVirtual() {
        if (virtual != null) {
            virtual.remove();
            virtual = null;
        }
    }

    void cleanup() {
        if (virtual != null) {
            virtual.remove();
        }
    }

    List<DelegatingMarker> getMarkersInternal() {
        return new ArrayList<DelegatingMarker>(markers);
    }

    @Override
    public void animatePosition(LatLng target) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void animatePosition(LatLng target, AnimationSettings settings) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void animatePosition(LatLng target, AnimationCallback callback) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void animatePosition(LatLng target, AnimationSettings settings, AnimationCallback callback) {
        throw new UnsupportedOperationException();
    }

    @Override
    public float getAlpha() {
        if (virtual != null) {
            return virtual.getAlpha();
        }
        return 1.0f;
    }

    @Override
    public int getClusterGroup() {
        if (markers.size() > 0) {
            return markers.get(0).getClusterGroup();
        }
        throw new IllegalStateException();
    }

    @Override
    public Object getData() {
        return null;
    }

    @Deprecated
    @Override
    public String getId() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Marker> getMarkers() {
        return new ArrayList<Marker>(markers);
    }

    @Override
    public LatLng getPosition() {
        if (virtual != null) {
            return virtual.getPosition();
        }
        LatLng position = calculateClusterPosition();
        return position;
    }

    @Override
    public float getRotation() {
        if (virtual != null) {
            return virtual.getRotation();
        }
        return 0.0f;
    }

    @Override
    public String getSnippet() {
        return null;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public float getZIndex() {
        return 0;
    }

    @Override
    public void hideInfoWindow() {
        if (virtual != null) {
            virtual.hideInfoWindow();
        }
    }

    @Override
    public boolean isCluster() {
        return true;
    }

    @Override
    public boolean isDraggable() {
        return false;
    }

    @Override
    public boolean isFlat() {
        if (virtual != null) {
            return virtual.isFlat();
        }
        return false;
    }

    @Override
    public boolean isInfoWindowShown() {
        if (virtual != null) {
            return virtual.isInfoWindowShown();
        }
        return false;
    }

    @Override
    public boolean isVisible() {
        if (virtual != null) {
            return virtual.isVisible();
        }
        return false;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setAlpha(float alpha) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setAnchor(float anchorU, float anchorV) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setClusterGroup(int clusterGroup) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setData(Object data) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setDraggable(boolean draggable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setFlat(boolean flat) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setIcon(BitmapDescriptor icon) {
        if (virtual != null) {
            virtual.setIcon(icon);
        }
    }

    @Override
    public void setInfoWindowAnchor(float anchorU, float anchorV) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setPosition(LatLng position) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setRotation(float rotation) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setSnippet(String snippet) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setTitle(String title) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setVisible(boolean visible) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setZIndex(float zIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void showInfoWindow() {
        if (virtual == null && markers.size() > 1) {
            refresh();
        }
        if (virtual != null) {
            virtual.showInfoWindow();
        }
    }
}
