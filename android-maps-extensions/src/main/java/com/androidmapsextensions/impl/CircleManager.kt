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

import com.androidmapsextensions.CircleOptions
import com.androidmapsextensions.GoogleMap
import com.google.android.gms.maps.model.Circle
import java.util.*

internal class CircleManager(private val factory: IGoogleMap) {
    private val circles: MutableMap<Circle?, com.androidmapsextensions.Circle>
    fun addCircle(circleOptions: CircleOptions): com.androidmapsextensions.Circle {
        val circle = createCircle(circleOptions.real)
        circle.setData(circleOptions.data)
        return circle
    }

    private fun createCircle(circleOptions: com.google.android.gms.maps.model.CircleOptions): com.androidmapsextensions.Circle {
        val real = factory.addCircle(circleOptions)
        val circle: com.androidmapsextensions.Circle = DelegatingCircle(real, this)
        circles[real] = circle
        return circle
    }

    fun clear() {
        circles.clear()
    }

    fun getCircles(): List<com.androidmapsextensions.Circle> {
        return ArrayList(circles.values)
    }

    fun onRemove(real: Circle?) {
        circles.remove(real)
    }

    fun setOnCircleClickListener(onCircleClickListener: GoogleMap.OnCircleClickListener?) {
        var realOnCircleClickListener: com.google.android.gms.maps.GoogleMap.OnCircleClickListener? = null
        if (onCircleClickListener != null) {
            realOnCircleClickListener = DelegatingOnCircleClickListener(onCircleClickListener)
        }
        factory.setOnCircleClickListener(realOnCircleClickListener)
    }

    private inner class DelegatingOnCircleClickListener(private val onCircleClickListener: GoogleMap.OnCircleClickListener) : com.google.android.gms.maps.GoogleMap.OnCircleClickListener {
        override fun onCircleClick(circle: Circle) {
            onCircleClickListener.onCircleClick(circles[circle])
        }

    }

    init {
        circles = HashMap()
    }
}