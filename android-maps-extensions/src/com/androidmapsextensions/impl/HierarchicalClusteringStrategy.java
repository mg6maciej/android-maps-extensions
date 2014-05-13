package com.androidmapsextensions.impl;

import android.util.Log;

import ch.usi.inf.sape.hac.HierarchicalAgglomerativeClusterer;
import ch.usi.inf.sape.hac.agglomeration.AgglomerationMethod;
import ch.usi.inf.sape.hac.agglomeration.CentroidLinkage;
import ch.usi.inf.sape.hac.dendrogram.Dendrogram;
import ch.usi.inf.sape.hac.dendrogram.DendrogramBuilder;
import ch.usi.inf.sape.hac.dendrogram.DendrogramNode;
import ch.usi.inf.sape.hac.dendrogram.MergeNode;
import ch.usi.inf.sape.hac.dendrogram.ObservationNode;
import ch.usi.inf.sape.hac.experiment.DissimilarityMeasure;
import ch.usi.inf.sape.hac.experiment.Experiment;
import com.androidmapsextensions.ClusterOptions;
import com.androidmapsextensions.ClusterOptionsProvider;
import com.androidmapsextensions.ClusteringSettings;
import com.androidmapsextensions.Marker;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


class HierarchicalClusteringStrategy implements ClusteringStrategy {

    private static boolean GOOGLE_PLAY_SERVICES_4_0 = true;

    private static final boolean DEBUG_GRID = false;
    private DebugHelper debugHelper;
    
    private final MarkerOptions markerOptions = new MarkerOptions();
    
    private boolean addMarkersDynamically;
    private IGoogleMap map;
    private Map<DelegatingMarker, ClusterMarker> markers;
    //private double baseClusterSize;
    //private double clusterSize;
    private int oldZoom, zoom;
    //private int[] visibleClusters = new int[4];
    
    private List<ClusterMarker> clusters = new ArrayList<ClusterMarker>();
    
    private ClusterRefresher refresher;
    private ClusterOptionsProvider clusterOptionsProvider;
    
    private List<DelegatingMarker> fullMarkerList;
    private Dendrogram dendrogram;
    private Map<DelegatingMarker, Double> nearestMarkerDistance = new HashMap<DelegatingMarker, Double>();
    
    private void reComputeDendrogram() {
    	Log.e("e","reComputingDendrogram with " + fullMarkerList.size() + " observations");
    	// Only markers within the same clusterGroup are clustered together
    	// TODO - for now, if clusterGroup < 0 => not clustered, otherwise consider all markers as in one group
    	cleanup();
    	// First, Compute the Dendrogram. Recompute it every time a marker is added or removed (or it's visibility changed).
    	Experiment experiment = new Experiment() {
			@Override
			public int getNumberOfObservations() {
				return fullMarkerList.size();
			} 
		};
		DissimilarityMeasure dissimilarityMeasure = new DissimilarityMeasure() {
			private static final double EARTH_RADIUS_MILES = 3958.76;
			private double distanceMiles( double lat1d, double lon1d, double lat2d, double lon2d ) {
				double dLat = Math.toRadians( lat2d - lat1d );
				double dLon = Math.toRadians( lon2d - lon1d );
				double lat1 = Math.toRadians( lat1d );
				double lat2 = Math.toRadians( lat2d );
				
				double a = Math.sin(dLat/2) * Math.sin(dLat/2) + Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(lat1) * Math.cos(lat2); 
				double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
				double d = EARTH_RADIUS_MILES * c;
				
				return d;
			}
			@Override
			public double computeDissimilarity( Experiment experiment, int observation1, int observation2 ) {
				// Distance in miles between the two markers
				DelegatingMarker dm1 = fullMarkerList.get( observation1 );
				DelegatingMarker dm2 = fullMarkerList.get( observation2 );
		        int clusterGroup1 = dm1.getClusterGroup();
		        int clusterGroup2 = dm2.getClusterGroup();
		        if ( clusterGroup1 < 0  ||  clusterGroup2 < 0 ) {
		        	return Double.MAX_VALUE; // clusterGroup < 0 implies this marker is never clustered
		        }
		        
				return distanceMiles( dm1.getPosition().latitude, dm1.getPosition().longitude, dm2.getPosition().latitude, dm2.getPosition().longitude );
			}
		};
		AgglomerationMethod agglomerationMethod = new CentroidLinkage();
		DendrogramBuilder dendrogramBuilder = new DendrogramBuilder( experiment.getNumberOfObservations() );
		HierarchicalAgglomerativeClusterer clusterer = new HierarchicalAgglomerativeClusterer( experiment, dissimilarityMeasure, agglomerationMethod );
		clusterer.cluster( dendrogramBuilder );
		dendrogram = dendrogramBuilder.getDendrogram();
    }
    
    private void addToCluster( ClusterMarker cm, DendrogramNode node ) {
    	if ( node == null ) {
    	}
    	else 
    	if ( node instanceof MergeNode ) {
    		addToCluster( cm, node.getLeft() );
    		addToCluster( cm, node.getRight() );
    	} 
    	else
    	if ( node instanceof ObservationNode ) {
    		DelegatingMarker dm = fullMarkerList.get( ((ObservationNode) node).getObservation() );
    		cm.add( dm );
    		markers.put( dm, cm );
    	}
    }
    private void evaluateNode( DendrogramNode node, double threshold ) {
    	if ( node == null ) {
    	}
    	else
    	if ( node instanceof MergeNode ) {
    		double distance = ((MergeNode) node).getDissimilarity();
    		
    		if ( node.getLeft() instanceof ObservationNode ) {
    			DelegatingMarker dm = fullMarkerList.get( ((ObservationNode) node.getLeft()).getObservation() );
    			nearestMarkerDistance.put( dm, distance );
    		}
    		if ( node.getRight() instanceof ObservationNode ) {
    			DelegatingMarker dm = fullMarkerList.get( ((ObservationNode) node.getRight()).getObservation() );
    			nearestMarkerDistance.put( dm, distance );
    		}    		
    		
    		if ( distance < threshold ) {
    			// Terminate here, create a new cluster containing all the lower ObservationNodes		
    			ClusterMarker cm = new ClusterMarker(this);
    			cm.mergeNode = (MergeNode) node;
    			addToCluster(cm, node);
    		}
    		else {
    			evaluateNode( node, threshold );
    		}
    	}
    	else
    	if ( node instanceof ObservationNode ) {
    		// This marker is not clustered.
    		DelegatingMarker dm = fullMarkerList.get( ((ObservationNode) node).getObservation() );
    		markers.put( dm, null );
    	}
	}    
        
    private double getThreshold( float zoom ) {
    	return 3200.0 / Math.pow( 2, zoom );
    }
    
    public HierarchicalClusteringStrategy(ClusteringSettings settings, IGoogleMap map, List<DelegatingMarker> markers, ClusterRefresher refresher) {
    	this.fullMarkerList = markers;
        this.clusterOptionsProvider = settings.getClusterOptionsProvider();
        this.addMarkersDynamically  = settings.isAddMarkersDynamically();        
        this.map = map;
        this.markers = new HashMap<DelegatingMarker, ClusterMarker>();
        this.refresher = refresher;
        this.zoom = Math.round(map.getCameraPosition().zoom);
        
    	// Need to keep track of distance between nodes in dendrogram. For every zoom level, determine cluster
		// membership for every marker. Compute MinDistanceMiles(zoom). Start at root. If child nodes are more then
		// MDM apart, proceed down the tree, until two child nodes collide. Stop there.
    	// Needed output is: private Map<ClusterKey, ClusterMarker> clusters = new HashMap<ClusterKey, ClusterMarker>();
        reComputeDendrogram();
        if ( dendrogram == null )
        	return;
        
		DendrogramNode node = dendrogram.getRoot();
		dendrogram.dump();
		evaluateNode( node, getThreshold(zoom) );
		        
        //calculateVisibleClusters();
        refresher.refreshAll();
        
        // this.baseClusterSize        = settings.getClusterSize();
        // this.clusterSize = calculateClusterSize(zoom);
        // addVisibleMarkers(markers);
    }
    
    @Override
    public void cleanup() {
        for ( ClusterMarker cluster : clusters ) {
            cluster.cleanup();
        }
        clusters.clear();
        markers.clear();
        refresher.cleanup();
        if (DEBUG_GRID) {
            if (debugHelper != null) {
                debugHelper.cleanup();
            }
        }
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        oldZoom = zoom;
        zoom = Math.round(cameraPosition.zoom);
        
        VisibleRegion visibleRegion = map.getVisibleRegion();
        LatLngBounds bounds = visibleRegion.latLngBounds;
        for ( DelegatingMarker marker : fullMarkerList ) {
        	marker.splitClusterPosition = null; // Reset this, will not be needed any more    
        	if ( marker.isVisible()  &&  marker.getClusterGroup() < 0 ) {
        		if ( cameraPosition.zoom >= marker.getMinZoomLevelVisible()  &&  bounds.contains(marker.getPosition()) ) {
        			marker.changeVisible(true);
        		}
        		else 
        		if ( cameraPosition.zoom < marker.getMinZoomLevelVisible() ) {
        			marker.changeVisible(false);
        		}
        	}
        }
        
        //if ( addMarkersDynamically ) {
        //   calculateVisibleClusters();
        //}
        if ( zoomedIn() ) {
            splitClusters();
            refresher.refreshAll();
        } 
        else
        if ( zoomedOut() ) {
            joinClusters();
            refresher.refreshAll();
        }
        else 
        if ( addMarkersDynamically ) {
            addMarkersInVisibleRegion();
        }
    }

    @Override
    public void onClusterGroupChange( DelegatingMarker marker ) {
        if ( ! marker.isVisible() ) {
            return;
        }
    	// Recalculate everything
    	reComputeDendrogram();
    	refresher.refreshAll();
    }
    
    @Override
    public void onAdd( DelegatingMarker marker ) {
        if ( ! marker.isVisible() ) {
            return;
        }
        addMarker(marker);
    }

    @Override
    public void onBulkAdd( DelegatingMarker marker ) {
    	/*
        if ( ! marker.isVisible() ) {
            return;
        }
        fullMarkerList.add( marker );
        */
    }
    
    private void addMarker( DelegatingMarker marker ) {
    	fullMarkerList.add( marker );
    	
    	// Recalculate everything
    	reComputeDendrogram();
    	refresher.refreshAll();
    }
    
    /*
    private boolean isPositionInVisibleClusters(LatLng position) {
        int y = convLat(position.latitude);
        int x = convLng(position.longitude);
        int[] b = visibleClusters;
        return b[0] <= y && y <= b[2] && (b[1] <= x && x <= b[3] || b[1] > b[3] && (b[1] <= x || x <= b[3]));
    }
*/
    @Override
    public void onRemove( DelegatingMarker marker ) {
        if ( ! marker.isVisible() ) {
            return;
        }
        removeMarker( marker );
    }

    private void removeMarker( DelegatingMarker marker ) {
    	fullMarkerList.remove( marker );
    	// Recalculate everything
    	reComputeDendrogram();
    	refresher.refreshAll();
    }
    
    @Override
    public void onPositionChange( DelegatingMarker marker ) {
        if ( ! marker.isVisible() ) {
            return;
        }
    	// Recalculate everything
    	reComputeDendrogram();
    	refresher.refreshAll();
    }
    
    @Override
    public Marker map(com.google.android.gms.maps.model.Marker original) {
        for ( ClusterMarker cluster : clusters ) {
            if ( original.equals(cluster.getVirtual()) ) {
                return cluster;
            }
        }
        return null;
    }

    @Override
    public List<Marker> getDisplayedMarkers() {
        List<Marker> displayedMarkers = new ArrayList<Marker>();
        for ( ClusterMarker cluster : clusters ) {
            Marker displayedMarker = cluster.getDisplayedMarker();
            if ( displayedMarker != null ) {
                displayedMarkers.add( displayedMarker );
            }
        }
        for ( DelegatingMarker marker : fullMarkerList ) {
            if ( markers.get(marker) == null ) {
                displayedMarkers.add(marker);
            }
        }
        return displayedMarkers;
    }

    @Override
    public float getMinZoomLevelNotClustered( Marker marker ) {
        if ( ! fullMarkerList.contains( marker) ) {
            throw new UnsupportedOperationException( "marker is not visible or is a cluster" );
        }
        int zoom = 0;
        while ( zoom <= 25  &&  hasCollision(marker, zoom) ) {
            zoom++;
        }
        if ( zoom > 25 ) {
            return Float.POSITIVE_INFINITY;
        }
        return zoom;
    }
        
    // Find the parent node of the marker in the dendrogram (which will be a MergeNode, if it has one), then get the 
    // dissimilarity measure and compare to threshold.
    private boolean hasCollision( Marker marker, int zoom ) {
    	Double nmd = nearestMarkerDistance.get( marker );
    	if ( nmd == null ) {
    		return false;
    	}
    	double threshold = getThreshold(zoom);
    	if ( nmd < threshold ) 
    		return true;
    	else
    		return false;
    }
   
    /*
    private ClusterMarker findClusterById( ClusterKey key ) {
        ClusterMarker cluster = clusters.get( key );
        if ( cluster == null ) {
            cluster = new ClusterMarker( this );
            clusters.put( key, cluster );
        }
        return cluster;
    }
    */
    
    @Override
    public void onVisibilityChangeRequest(DelegatingMarker marker, boolean visible) {
        if (visible) {
            addMarker(marker);
        } else {
            removeMarker(marker);
            marker.changeVisible(false);
        }
    }

    @Override
    public void onShowInfoWindow( DelegatingMarker marker ) {
        if ( ! marker.isVisible() ) {
            return;
        }
        ClusterMarker cluster = markers.get(marker);
        if ( cluster == null ) {
            marker.forceShowInfoWindow();
        } 
        else 
        if ( cluster.getMarkersInternal().size() == 1 ) {
            cluster.refresh();
            marker.forceShowInfoWindow();
        }
    }
    
    private void refresh(ClusterMarker cluster) {
        if (cluster != null) {
            refresher.refresh(cluster);
        }
    }

    /*
    private void addVisibleMarkers(List<DelegatingMarker> markers) {
        if (addMarkersDynamically) {
            calculateVisibleClusters();
        }
        for (DelegatingMarker marker : markers) {
            if (marker.isVisible()) {
                addMarker(marker);
            }
        }
        refresher.refreshAll();
    }
    */
    /*
    private void recalculate() {
        if (addMarkersDynamically) {
            calculateVisibleClusters();
        }
        if ( zoomedIn() ) {
            splitClusters();
        } else {
            joinClusters();
        }
        refresher.refreshAll();
    }
	*/
    private boolean zoomedIn() {
        return zoom > oldZoom;
    }
    private boolean zoomedOut() {
        return zoom < oldZoom;
    }
    
    private void splitNode( MergeNode node, List<ClusterMarker> newClusters, List<DelegatingMarker> newlyVisibleMarkers, double threshold ) {
    	if ( node.getDissimilarity() > threshold ) { // split them
    		DendrogramNode left  = node.getLeft();
    		DendrogramNode right = node.getRight();
    		if ( left instanceof MergeNode ) {
    			splitNode( (MergeNode) left, newClusters, newlyVisibleMarkers, threshold );
    		}
    		else
       		if ( left instanceof ObservationNode ) {
       			DelegatingMarker dm = fullMarkerList.get( ((ObservationNode) left).getObservation() );
       			newlyVisibleMarkers.add( dm );
       			markers.put( dm, null );
       		}    			
    		if ( right instanceof MergeNode ) {
    			splitNode( (MergeNode) right, newClusters, newlyVisibleMarkers, threshold );
    		}
    		else
       		if ( right instanceof ObservationNode ) {
       			DelegatingMarker dm = fullMarkerList.get( ((ObservationNode) right).getObservation() );
       			newlyVisibleMarkers.add( dm );
       			markers.put( dm, null );
       		}
    	}
    	else {
    		ClusterMarker cm = new ClusterMarker( this );
    		cm.mergeNode = node;
    	    addToCluster( cm, node );
    		newClusters.add( cm );
    	}
    }
    // The user zoomed in. Evaluate all MergeNodes to see if they should be split up into two or more.
    private void splitClusters() {
    	double threshold = getThreshold( zoom );
    	
    	List<ClusterMarker> allNewClusters = new ArrayList<ClusterMarker>();
    	
        for ( ClusterMarker cluster : clusters ) {
        	List<ClusterMarker> newClusters            = new ArrayList<ClusterMarker>();
        	List<DelegatingMarker> newlyVisibleMarkers = new ArrayList<DelegatingMarker>();
        	
        	MergeNode node = cluster.mergeNode;
        	if ( node.getDissimilarity() > threshold ) {
        		splitNode( node, newClusters, newlyVisibleMarkers, threshold );
        		
        		// Set the slide-away position for the new clusters
        		// If either the old position or new position are visible
                for ( ClusterMarker cm : newClusters ) {
                	if ( isVisible( cm.getPosition() )  ||  isVisible( cluster.getPosition() )  ) {
                		cm.splitClusterPosition = cluster.getPosition();
                		refresh(cm);
                	}
                }
        		// Set the slide-away position for the newly visible markers
                // If either the newly visible marker or old cluster position are visible
                for ( DelegatingMarker dm : newlyVisibleMarkers ) {
                	if ( isVisible( dm.getPosition() )  ||  isVisible( cluster.getPosition() )  ) {
                		dm.splitClusterPosition = cluster.getPosition();
                	}
                }
                
                // Remove the old cluster
                cluster.removeVirtual();
        	}
        	allNewClusters.addAll( newClusters );
        }
        
        clusters.addAll( allNewClusters );
    }

    
    private void joinClusters() {
    	/*
        Map<ClusterKey, ClusterMarker> newClusters = new HashMap<ClusterKey, ClusterMarker>();
        Map<ClusterKey, List<ClusterMarker>> oldClusters = new HashMap<ClusterKey, List<ClusterMarker>>();
        for (ClusterMarker cluster : clusters.values()) {
            List<DelegatingMarker> ms = cluster.getMarkersInternal();
            if (ms.isEmpty()) {
                cluster.removeVirtual();
                continue;
            }
            ClusterKey clusterId = calculateClusterKey(ms.get(0).getClusterGroup(), ms.get(0).getPosition());
            List<ClusterMarker> clusterList = oldClusters.get(clusterId);
            if (clusterList == null) {
                clusterList = new ArrayList<ClusterMarker>();
                oldClusters.put(clusterId, clusterList);
            }
            clusterList.add(cluster);
        }
        for (ClusterKey key : oldClusters.keySet()) {
            List<ClusterMarker> clusterList = oldClusters.get(key);
            if (clusterList.size() == 1) {
                ClusterMarker cluster = clusterList.get(0);
                newClusters.put(key, cluster);
                if (addMarkersDynamically && isPositionInVisibleClusters(cluster.getMarkersInternal().get(0).getPosition())) {
                    refresh(cluster);
                }
            } else {
                ClusterMarker cluster = new ClusterMarker(this);
                newClusters.put(key, cluster);
                if (!addMarkersDynamically || isPositionInVisibleClusters(clusterList.get(0).getMarkersInternal().get(0).getPosition())) {
                    refresh(cluster);
                }
                for (ClusterMarker old : clusterList) {
                    old.removeVirtual();
                    //old.removeVirtual( cluster.getPosition() ); // VH - new position after join
                    List<DelegatingMarker> ms = old.getMarkersInternal();
                    for (DelegatingMarker m : ms) {
                        cluster.add(m);
                        markers.put(m, cluster);
                    }
                }
            }
        }
        clusters = newClusters;
        */
    }

    private void addMarkersInVisibleRegion() {
        //calculateVisibleClusters();    	
        for ( DelegatingMarker marker : markers.keySet() ) {
            LatLng position = marker.getPosition();
            if ( isVisible( position ) ) {
//            if ( isPositionInVisibleClusters(position) ) {
                ClusterMarker cluster = markers.get(marker);
                refresh(cluster);
            }
        }
        refresher.refreshAll();
    }

    private boolean isVisible( LatLng pos ) {
    	 VisibleRegion visibleRegion = map.getVisibleRegion();
         LatLngBounds bounds = visibleRegion.latLngBounds;
         if ( bounds.contains( pos ) ) 
        	 return true;
         else
        	 return false;
    }
    
    /*
    private void calculateVisibleClusters() {
        
        VisibleRegion visibleRegion = map.getVisibleRegion();
        LatLngBounds bounds = visibleRegion.latLngBounds;
        visibleClusters[0] = convLat(bounds.southwest.latitude);
        visibleClusters[1] = convLng(bounds.southwest.longitude);
        visibleClusters[2] = convLat(bounds.northeast.latitude);
        visibleClusters[3] = convLng(bounds.northeast.longitude);
    }
    */
    /*
    private ClusterKey calculateClusterKey(int group, LatLng position) {
        int y = convLat(position.latitude);
        int x = convLng(position.longitude);
        return new ClusterKey(group, y, x);
    }
    */
/*
    private int convLat(double lat) {
        return (int) (SphericalMercator.scaleLatitude(lat) / clusterSize);
    }

    private int convLng(double lng) {
        return (int) (SphericalMercator.scaleLongitude(lng) / clusterSize);
    }
*/
    /*
    private double calculateClusterSize(int zoom) {
        return baseClusterSize / (1 << zoom);
    }
    */
    
    com.google.android.gms.maps.model.Marker createMarker(List<Marker> markers, LatLng position) {
        markerOptions.position(position);
        ClusterOptions opts = clusterOptionsProvider.getClusterOptions(markers);
        markerOptions.icon(opts.getIcon());
        if (GOOGLE_PLAY_SERVICES_4_0) {
            try {
                markerOptions.alpha(opts.getAlpha());
            } catch (NoSuchMethodError error) {
                // not the cutest way to handle backward compatibility
                GOOGLE_PLAY_SERVICES_4_0 = false;
            }
        }
        markerOptions.anchor(opts.getAnchorU(), opts.getAnchorV());
        markerOptions.flat(opts.isFlat());
        markerOptions.infoWindowAnchor(opts.getInfoWindowAnchorU(), opts.getInfoWindowAnchorV());
        markerOptions.rotation(opts.getRotation());
        return map.addMarker(markerOptions);
    }
/*
    private static class ClusterKey {
        private final int group;
        private final int latitudeId;
        private final int longitudeId;

        public ClusterKey(int group, int latitudeId, int longitudeId) {
            this.group       = group;
            this.latitudeId  = latitudeId;
            this.longitudeId = longitudeId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            ClusterKey that = (ClusterKey) o;

            if (group != that.group) {
                return false;
            }
            if (latitudeId != that.latitudeId) {
                return false;
            }
            if (longitudeId != that.longitudeId) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int result = group;
            result = 31 * result + latitudeId;
            result = 31 * result + longitudeId;
            return result;
        }
    }
*/
	@Override
	public void refreshAll() {
		refresher.refreshAll();		
	}
}
