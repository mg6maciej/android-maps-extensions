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

import android.content.Context;

import com.androidmapsextensions.GoogleMap;

public final class ExtendedMapFactory {

    private ExtendedMapFactory() {
    }

    public static GoogleMap create(com.google.android.gms.maps.GoogleMap real, Context context) {
        return new DelegatingGoogleMap(new GoogleMapWrapper(real, context), context);
    }
}
