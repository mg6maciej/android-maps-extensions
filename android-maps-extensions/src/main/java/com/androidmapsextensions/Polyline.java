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

public interface Polyline {

    int getColor();

    <T> T getData();

    Cap getEndCap();

    @Deprecated
    String getId();

    int getJointType();

    List<PatternItem> getPattern();

    List<LatLng> getPoints();

    Cap getStartCap();

    Object getTag();

    float getWidth();

    float getZIndex();

    boolean isClickable();

    boolean isGeodesic();

    boolean isVisible();

    void remove();

    void setClickable(boolean clickable);

    void setColor(int color);

    void setData(Object data);

    void setEndCap(Cap endCap);

    void setGeodesic(boolean geodesic);

    void setJointType(int jointType);

    void setPattern(List<PatternItem> pattern);

    void setPoints(List<LatLng> points);

    void setStartCap(Cap startCap);

    void setTag(Object tag);

    void setVisible(boolean visible);

    void setWidth(float width);

    void setZIndex(float zIndex);
}
