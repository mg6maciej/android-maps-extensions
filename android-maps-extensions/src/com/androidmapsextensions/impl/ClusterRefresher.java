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

import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;

import java.util.HashSet;
import java.util.Set;

class ClusterRefresher {

    private Set<ClusterMarker> refreshQueue = new HashSet<ClusterMarker>();
    private boolean refreshPending;
    private Handler refresher = new Handler(new Callback() {
        public boolean handleMessage(Message msg) {
            refreshAll();
            return true;
        }
    });

    void refresh( ClusterMarker cluster ) {
        refreshQueue.add(cluster);
        if ( ! refreshPending ) {
            refresher.sendEmptyMessage(0);
            refreshPending = true;
        }
    }

    void cleanup() {
        refreshQueue.clear();
        refreshPending = false;
        refresher.removeMessages(0);
    }

    void refreshAll() {
        for ( ClusterMarker cluster : refreshQueue ) {
            cluster.refresh();
        }
        cleanup();
    }
}
