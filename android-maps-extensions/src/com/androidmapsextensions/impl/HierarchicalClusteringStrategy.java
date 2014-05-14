package com.androidmapsextensions.impl;

import android.util.Log;

import com.androidmapsextensions.ClusterOptions;
import com.androidmapsextensions.ClusterOptionsProvider;
import com.androidmapsextensions.ClusteringSettings;
import com.androidmapsextensions.Marker;
import com.androidmapsextensions.dendrogram.Dendrogram;
import com.androidmapsextensions.dendrogram.DendrogramBuilder;
import com.androidmapsextensions.dendrogram.DendrogramNode;
import com.androidmapsextensions.dendrogram.DissimilarityMeasure;
import com.androidmapsextensions.dendrogram.Experiment;
import com.androidmapsextensions.dendrogram.HierarchicalAgglomerativeClusterer;
import com.androidmapsextensions.dendrogram.MergeNode;
import com.androidmapsextensions.dendrogram.ObservationNode;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


class HierarchicalClusteringStrategy implements ClusteringStrategy {

    private static boolean GOOGLE_PLAY_SERVICES_4_0 = true;

    private static final boolean DEBUG_GRID = false;
    private DebugHelper debugHelper;
    
    private final MarkerOptions markerOptions = new MarkerOptions();
    
    private IGoogleMap map;
    private Map<DelegatingMarker, ClusterMarker> markers;
    //private double baseClusterSize;
    //private double clusterSize;
    private int oldZoom, zoom;
    //private int[] visibleClusters = new int[4];
    
    private Set<ClusterMarker> clusters = new HashSet<ClusterMarker>();
    
    private ClusterRefresher refresher;
    private ClusterOptionsProvider clusterOptionsProvider;
    
    private List<DelegatingMarker> fullMarkerList;
    private Set<DelegatingMarker> renderedMarkerList = new HashSet<DelegatingMarker>();
    
    private Dendrogram dendrogram;
    
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
		    public double[] getPosition( int observation ) {
		    	DelegatingMarker pos = fullMarkerList.get( observation );
		    	LatLng ll = pos.getPosition();
		    	return new double[]{ ll.latitude, ll.longitude };
			}
		};
		DissimilarityMeasure dissimilarityMeasure = new DissimilarityMeasure() {
			private static final double EARTH_RADIUS_MILES = 3958.76;
			// Approximation for small distances, but good enough
			@Override
		    public double computeDissimilarity(Experiment experiment, int observation1, int observation2) {				
				double [] pos1 = experiment.getPosition( observation1 );
				double [] pos2 = experiment.getPosition( observation2 );
				
				return distanceMiles( pos1, pos2 );
			}
			public double distanceMiles( double[] pos1, double[] pos2 ) {
				double avgLat = Math.toRadians( (pos1[0] + pos2[0])/2 );
				
				double dx = Math.toRadians( pos2[1] - pos1[1] ) * Math.cos( avgLat );
				double dy = Math.toRadians( pos2[0] - pos1[0] );
				
				double d = EARTH_RADIUS_MILES * Math.sqrt( dx*dx + dy*dy ); 
				
				return d;
			}
		};
		DendrogramBuilder dendrogramBuilder = new DendrogramBuilder( experiment );
		
		HierarchicalAgglomerativeClusterer clusterer = new HierarchicalAgglomerativeClusterer( experiment, dissimilarityMeasure );
		clusterer.cluster( dendrogramBuilder );
		dendrogram = dendrogramBuilder.getDendrogram();
		
        if ( dendrogram == null )
        	return;
        dendrogram.dump();
        
		DendrogramNode rootNode = dendrogram.getRoot();	
		evaluateNode( rootNode, getThreshold(zoom) );
        refresher.refreshAll();
		
		Log.e("e","reComputingDendrogram DONE");
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
    		if ( dm.isVisible() ) {
    			cm.add( dm );
    			markers.put( dm, cm );
    		}
    	}
    }
    
    private void slideOutSmallerClustersToMerge( DendrogramNode node, DendrogramNode targetNode ) {
    	if ( node == null ) {
    		return;
    	}
    	if ( node.getClusterMarker() != null ) {
    		ClusterMarker cm = node.getClusterMarker();
			cm.removeVirtual();
			clusters.remove( cm ); // TODO - slide
    	}
    	slideOutSmallerClustersToMerge( node.getLeft(), targetNode );
    	slideOutSmallerClustersToMerge( node.getRight(), targetNode );
    }
    
    // After a zoom change, traverse the dendrogram and assign markers to new clusters. The dendrogram is not modified.
    private void evaluateNode( DendrogramNode node, double threshold ) {
    	if ( node == null ) {
    	}
    	else
    	if ( node instanceof MergeNode ) {
    		double distance = ((MergeNode) node).getDissimilarity();
    		// If we zoomed in, do we need to remove any cluster?
    		// It will be removed immediately
    		if ( distance >= threshold ) {
    			ClusterMarker cm = ((MergeNode)node).getClusterMarker();
    			if ( cm != null ) {
    				cm.removeVirtual();
    			}
    			clusters.remove( cm );
    			
    			evaluateNode( node.getLeft(),  threshold );
    			evaluateNode( node.getRight(), threshold );
    		}
    		else
    		if ( distance < threshold ) {
    			// Terminate here, create a new hidden cluster containing all the lower MergeNodes and ObservationNodes    			
    			ClusterMarker cm = new ClusterMarker(this);
    			cm.dendrogramNode = node;
    			addToCluster(cm, node);
    			clusters.add( cm );
    			cm.changeVisible( isVisible( cm.getPosition() ) );
    			Log.e("e","Showing Cluster with size " + cm.getMarkersInternal().size() );
    			
    			// Remove all smaller clusters while sliding
    			slideOutSmallerClustersToMerge( node.getLeft(), node );
    			slideOutSmallerClustersToMerge( node.getRight(), node );
    		}
    	}
    	else // This marker is not clustered.
    	if ( node instanceof ObservationNode ) {
			ClusterMarker cm = new ClusterMarker(this);
			cm.dendrogramNode = node;
			addToCluster(cm, node);
			clusters.add( cm );
			cm.changeVisible( isVisible( cm.getPosition() ) );
			Log.e("e","Showing Cluster with size " + cm.getMarkersInternal().size() );    			
    	}
	}
    
    private double getThreshold( float zoom ) {
    	return 3200.0 / Math.pow( 2, zoom );
    }
    
    public HierarchicalClusteringStrategy(ClusteringSettings settings, IGoogleMap map, List<DelegatingMarker> fullMarkerList, ClusterRefresher refresher) {
    	this.fullMarkerList = fullMarkerList;
        this.clusterOptionsProvider = settings.getClusterOptionsProvider();
        this.map = map;
        this.markers = new HashMap<DelegatingMarker, ClusterMarker>();
        this.refresher = refresher;
        this.zoom = Math.round(map.getCameraPosition().zoom);
        
        reComputeDendrogram();
    }
    
    @Override
    public void cleanup() {
        for ( ClusterMarker cluster : clusters ) {
            cluster.cleanup();
        }
        clusters.clear();
        renderedMarkerList.clear();
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
        
        addMarkersAndClustersInVisibleRegion();
        
        if ( zoomedIn() ) {
        	// Handle special markers with minZoomLevelVisible
        	for ( DelegatingMarker marker : fullMarkerList ) {
        		if ( marker.isVisible()  &&
        			 marker.getClusterGroup() < 0   &&
        			 cameraPosition.zoom >= marker.getMinZoomLevelVisible()  &&
        			 bounds.contains(marker.getPosition()) ) {
        			marker.changeVisible(true);
        		}
        	}
            splitClusters();
            refresher.refreshAll();
        } 
        else
        if ( zoomedOut() ) {
            for ( DelegatingMarker marker : fullMarkerList ) {               
            	if ( marker.isVisible()  &&  
            		 marker.getClusterGroup() < 0  &&
            		 cameraPosition.zoom < marker.getMinZoomLevelVisible() ) {
            			marker.changeVisible(false);
            	}
            }        
            
            mergeClusters();
            refresher.refreshAll();
        }
    }
    
    // This is used when e.g. a cluster is declusterified by user
    @Override
    public void onClusterGroupChange( DelegatingMarker marker ) {
        if ( ! marker.isVisible() ) {
            return;
        }
        ClusterMarker oldCluster = markers.get(marker);
        if ( oldCluster != null ) {
            oldCluster.remove( marker );
            refresh(oldCluster);
        }
        
        int clusterGroup = marker.getClusterGroup();
        if ( clusterGroup < 0 ) {
            markers.put(marker, null);
            if ( map.getCameraPosition().zoom >= marker.getMinZoomLevelVisible() ) {
            	marker.changeVisible(true);
            }
            else {
            	marker.changeVisible(false);
            }
        } else {
        	reComputeDendrogram();
        }        
    }
    
    @Override
    public void onAdd( DelegatingMarker marker ) {
        if ( ! marker.isVisible() ) {
            return;
        }
        addMarker(marker);
    }
    
    @Override
    public void onBulkAdd( List<DelegatingMarker> marker ) {
    	Log.e("e","Hierarchical onBulkAdd");
    	for ( DelegatingMarker m : marker ) {
    		if ( m.isVisible() ) {    	
    			fullMarkerList.add( m );
    		}
    	}
        reComputeDendrogram();
    }
    
    private void addMarker( DelegatingMarker marker ) {
    	fullMarkerList.add( marker );
    	
    	// Recalculate everything
    	reComputeDendrogram();
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
    }
    
    @Override
    public void onPositionChange( DelegatingMarker marker ) {
        if ( ! marker.isVisible() ) {
            return;
        }
    	// Recalculate everything
    	reComputeDendrogram();
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
        DelegatingMarker dm = (DelegatingMarker)marker;
        double dissimilarity = dm.parentNode.getDissimilarity();
                
        int zoom = 25;
        while ( zoom >= 0 ) {
        	double threshold = getThreshold(zoom);
        	if ( dissimilarity < threshold ) {
        		break;
        	}
            zoom--;
        }
        
        return zoom;
    }
    
    // Find the parent node of the marker in the dendrogram (which will be a MergeNode, if it has one), then get the 
    // dissimilarity measure and compare to threshold.
    
    @Override
    public void onVisibilityChangeRequest(DelegatingMarker marker, boolean visible) {
        if ( visible ) {
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
    
    private void refresh( ClusterMarker cluster ) {
        if ( cluster != null ) {
            refresher.refresh( cluster );
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
    
    private void splitNode( MergeNode node, Set<ClusterMarker> newClusters, Set<DelegatingMarker> newlyVisibleMarkers, double threshold ) {
    	if ( node.getDissimilarity() > threshold ) { // split them
    		DendrogramNode left  = node.getLeft();
    		DendrogramNode right = node.getRight();
    		if ( left instanceof MergeNode ) {
    			splitNode( (MergeNode) left, newClusters, newlyVisibleMarkers, threshold );
    		}
    		else
       		if ( left instanceof ObservationNode ) {
       			DelegatingMarker dm = fullMarkerList.get( ((ObservationNode) left).getObservation() );
       			dm.parentNode = node;
       			newlyVisibleMarkers.add( dm );
       		}    			
    		if ( right instanceof MergeNode ) {
    			splitNode( (MergeNode) right, newClusters, newlyVisibleMarkers, threshold );
    		}
    		else
       		if ( right instanceof ObservationNode ) {
       			DelegatingMarker dm = fullMarkerList.get( ((ObservationNode) right).getObservation() );
       			dm.parentNode = node;
       			newlyVisibleMarkers.add( dm );
       		}
    	}
    	else {
    		Log.e("e","Creating a new cluster after split");
    		ClusterMarker cm = new ClusterMarker( this );
    		cm.dendrogramNode = node;
    	    addToCluster( cm, node );
    		newClusters.add( cm );
    		cm.changeVisible( isVisible( cm.getPosition() ) );
    		//refresh(cm);
    	}
    }
    
    // The user zoomed in. Evaluate all MergeNodes to see if they should be split up into two or more.
    private void splitClusters() {    	
    	double threshold = getThreshold( zoom );
    	
    	Set<ClusterMarker> newClusters            = new HashSet<ClusterMarker>();
    	Set<DelegatingMarker> newlyVisibleMarkers = new HashSet<DelegatingMarker>();
    	
    	Iterator<ClusterMarker> it = clusters.iterator();
    	while( it.hasNext() ) {
    		ClusterMarker cluster = it.next();
    		
        	MergeNode node = cluster.mergeNode;
        	Log.e("e","Spliting Clusters! MergeNode=" + node + " distance=" + node.getDissimilarity() );
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
                it.remove();
        	}
        }
    	
        clusters.addAll( newClusters );
        for ( DelegatingMarker marker : newlyVisibleMarkers ) {
            markers.put( marker, null );   			
   			renderedMarkerList.add( marker );
   			
            if ( map.getCameraPosition().zoom >= marker.getMinZoomLevelVisible() ) {
            	marker.changeVisible(true);
            	//marker.animateScreenPosition( marker.splitClusterPosition, marker.getPosition(), null );
        		marker.splitClusterPosition = null;
            }
            else {
            	marker.changeVisible(false);
            }
        }
    }
        
    private void mergeNode( MergeNode parent, Set<ClusterMarker> newClusters, Set<ClusterMarker> oldClusters, Set<DelegatingMarker> newlyHiddenMarkers, double threshold ) {
    	if ( parent.getDissimilarity() < threshold ) { // merge the siblings
    		DendrogramNode left  = parent.getLeft();
    		DendrogramNode right = parent.getRight();
    		
    		if ( left instanceof MergeNode ) {
    			ClusterMarker oldcm = ((MergeNode)left).getClusterMarker();
    			if ( oldcm != null ) {
    				oldClusters.remove( oldcm );
    				oldcm.remove();
    			}
    		}
    		else
       		if ( left instanceof ObservationNode ) {
       			DelegatingMarker dm = fullMarkerList.get( ((ObservationNode) left).getObservation() );
       			newlyHiddenMarkers.add( dm );
       		}
    		
    		if ( right instanceof MergeNode ) {
    			ClusterMarker oldcm = ((MergeNode)right).getClusterMarker();
    			if ( oldcm != null ) {
    				oldClusters.remove( oldcm );
    				oldcm.remove();
    			}
    		}
    		else
       		if ( right instanceof ObservationNode ) {
       			DelegatingMarker dm = fullMarkerList.get( ((ObservationNode) right).getObservation() );
       			newlyHiddenMarkers.add( dm );
       		}
    		
    		if ( parent.getParent() != null ) {
    			mergeNode( parent.getParent(), newClusters, oldClusters, newlyHiddenMarkers, threshold );
    		}
    	}
    	else {
    		// No more merging to do, create a new cluster
    		Log.e("e", "Creating a new cluster after mergem old cl size=" + oldClusters.size() );
    		ClusterMarker cm = new ClusterMarker( this );
    		cm.mergeNode = parent;
    	    addToCluster( cm, parent );
    		newClusters.add( cm );
    		cm.changeVisible( isVisible( cm.getPosition() ) );
    	}
    }

    // See if any markers or clusters should be joined
    private void mergeClusters() {
    	double threshold = getThreshold( zoom );
    	
    	Set<ClusterMarker> newClusters           = new HashSet<ClusterMarker>();
    	Set<ClusterMarker> removedClusters       = new HashSet<ClusterMarker>();
    	Set<DelegatingMarker> newlyHiddenMarkers = new HashSet<DelegatingMarker>();
    	
    	// First check if any clusters need merging
    	Iterator<ClusterMarker> it = clusters.iterator();
    	while ( it.hasNext() ) {
    		ClusterMarker cluster = it.next();
    		MergeNode parent = cluster.mergeNode.getParent();
    		if ( parent.getDissimilarity() < threshold ) {
    			// Remove it
    			removedClusters.add( cluster );
    			// Find location of merged cluster
    			MergeNode newLocation = parent;
    			while ( newLocation.getParent() != null  &&  newLocation.getParent().getDissimilarity() >= threshold ) {
    				newLocation = newLocation.getParent();
    			}
        		ClusterMarker cm = new ClusterMarker( this );
        		cm.mergeNode = newLocation;
        	    addToCluster( cm, newLocation );
        		newClusters.add( cm );
        		cm.changeVisible( isVisible( cm.getPosition() ) );
    		}
    	}
    	
    		ClusterMarker sibling = parent.getLeft().getClusterMarker();
    		
    
        	
        	// Does this cluster have a nearby sibling?
        	// Proceed up the tree until we reach a parent with siblings far enough
        	
        	if ( parent != null  &&  parent.getDissimilarity() < threshold ) { // to handle root cluster
        		mergeNode( parent, newClusters, oldClusters, newlyHiddenMarkers, threshold );
        	}
    	}
    	// Next check if any single markers need merging
    	Log.e("e", "renderedMarkerList size=" + renderedMarkerList.size() );
    	for ( DelegatingMarker dm : renderedMarkerList ) {
    		MergeNode parent = dm.parentNode;
    		mergeNode( parent, newClusters, removedClusters, newlyHiddenMarkers, threshold );
    	}
    	
    	for ( ClusterMarker cm : removedClusters ) {
    		if ( cm != null ) {
    			cm.removeVirtual();
    			clusters.remove( cm );
    		}
    	}
    	
        clusters.addAll( newClusters );
        Log.e("e","MergeClusters newlyHidden size=" + newlyHiddenMarkers.size() );
        for ( DelegatingMarker marker : newlyHiddenMarkers ) {
        	marker.changeVisible( false );
        	renderedMarkerList.remove( marker );
        }
    }
    
    // Add single markers and clusters in visible region, upon a pan or zoom out for example
    private void addMarkersAndClustersInVisibleRegion() {
    	for ( ClusterMarker cm : clusters ) {
    		if ( isVisible( cm.getPosition() ) ) {
    			cm.changeVisible( true );
    		}
    		else {
    			cm.changeVisible( false );    			
    		}
    	}
    	
        for ( DelegatingMarker dm : markers.keySet() ) {
        	if ( markers.get( dm ) == null ) { // not clustered
        		LatLng position = dm.getPosition();
        		if ( isVisible( position ) ) {
        			dm.changeVisible( true );
        		} 
        		else {
        			dm.changeVisible( false );
        		}
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
    
    
    com.google.android.gms.maps.model.Marker createClusterMarker(List<Marker> markers, LatLng position) {
        markerOptions.position(position);
        ClusterOptions opts = clusterOptionsProvider.getClusterOptions( markers );
        markerOptions.icon(opts.getIcon());
        if ( GOOGLE_PLAY_SERVICES_4_0 ) {
            try {
                markerOptions.alpha( opts.getAlpha() );
            } catch ( NoSuchMethodError error ) {
                // not the cutest way to handle backward compatibility
                GOOGLE_PLAY_SERVICES_4_0 = false;
            }
        }
        markerOptions.anchor( opts.getAnchorU(), opts.getAnchorV() );
        markerOptions.flat(opts.isFlat());
        markerOptions.infoWindowAnchor( opts.getInfoWindowAnchorU(), opts.getInfoWindowAnchorV() );
        markerOptions.rotation( opts.getRotation() );
        return map.addMarker( markerOptions );
    }
    
	@Override
	public void refreshAll() {
		refresher.refreshAll();		
	}
}
