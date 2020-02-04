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
package com.androidmapsextensions.impl

import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.Marker

internal interface ClusteringStrategy {
    fun cleanup()
    fun onCameraChange(cameraPosition: CameraPosition?)
    fun onClusterGroupChange(marker: DelegatingMarker?)
    fun onAdd(marker: DelegatingMarker?)
    fun onRemove(marker: DelegatingMarker?)
    fun onPositionChange(marker: DelegatingMarker?)
    fun onVisibilityChangeRequest(marker: DelegatingMarker?, visible: Boolean)
    fun onShowInfoWindow(marker: DelegatingMarker?)
    fun map(original: Marker?): com.androidmapsextensions.Marker?
    val displayedMarkers: List<com.androidmapsextensions.Marker?>?
    fun getMinZoomLevelNotClustered(marker: com.androidmapsextensions.Marker?): Float
}