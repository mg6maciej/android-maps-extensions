package com.androidmapsextensions;

import android.content.Context;

import com.androidmapsextensions.impl.ExtendedMapFactory;

import java.lang.reflect.Method;

final class MapHolder {

    private static final String ERROR_MESSAGE = "The version of Google Play Services you are using does not have `getMap` method. Please use `getExtendedMapAsync(OnMapReadyCallback)` instead of `getExtendedMap()`.";

    public interface Delegate {

        // This function no longer exist in new versions of Google Maps Android API v2,
        // so we get access to it via reflection whenever possible
        //com.google.android.gms.maps.GoogleMap getMap();

        void getMapAsync(com.google.android.gms.maps.OnMapReadyCallback callback);

        Context getContext();
    }

    private final Delegate delegate;
    private GoogleMap map;

    public MapHolder(Delegate delegate) {
        this.delegate = delegate;
    }

    public GoogleMap getExtendedMap() {
        if (map == null) {
            try {
                Method getMapMethod = delegate.getClass().getMethod("getMap");
                Object obj = getMapMethod.invoke(delegate);
                if (obj != null) {
                    com.google.android.gms.maps.GoogleMap realMap = (com.google.android.gms.maps.GoogleMap) obj;
                    map = ExtendedMapFactory.create(realMap, delegate.getContext());
                }
            } catch (Exception e) {
                throw new RuntimeException(ERROR_MESSAGE, e);
            }
        }
        return map;
    }

    public void getExtendedMapAsync(final OnMapReadyCallback callback) {
        if (map != null) {
            callback.onMapReady(map);
        } else {
            delegate.getMapAsync(new com.google.android.gms.maps.OnMapReadyCallback() {
                @Override
                public void onMapReady(com.google.android.gms.maps.GoogleMap realMap) {
                    if (map == null) {
                        map = ExtendedMapFactory.create(realMap, delegate.getContext());
                    }
                    callback.onMapReady(map);
                }
            });
        }
    }
}
