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
package com.androidmapsextensions

import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds

interface GroundOverlay {
    var bearing: Float
    val bounds: LatLngBounds?
    fun <T> getData(): T
    val height: Float
    @get:Deprecated("")
    val id: String?

    var position: LatLng?
    var tag: Any?
    var transparency: Float
    val width: Float
    var zIndex: Float
    var isClickable: Boolean
    var isVisible: Boolean
    fun remove()
    fun setData(data: Any?)
    fun setDimensions(width: Float, height: Float)
    fun setDimensions(width: Float)
    fun setImage(image: BitmapDescriptor?)
    fun setPositionFromBounds(bounds: LatLngBounds?)
}