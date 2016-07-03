package com.androidmapsextensions;

import android.content.Context;

import com.androidmapsextensions.impl.ExtendedMapFactory;

final class MapHolder {

    public interface Delegate {

        void getMapAsync(com.google.android.gms.maps.OnMapReadyCallback callback);

        Context getContext();
    }

    private final Delegate delegate;
    private GoogleMap map;

    public MapHolder(Delegate delegate) {
        this.delegate = delegate;
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
