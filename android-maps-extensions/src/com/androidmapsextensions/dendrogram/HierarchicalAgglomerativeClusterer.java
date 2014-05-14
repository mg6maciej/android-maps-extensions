/*
 * This file is licensed to You under the "Simplified BSD License".
 * You may not use this software except in compliance with the License. 
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/bsd-license.php
 * 
 * See the COPYRIGHT file distributed with this work for information
 * regarding copyright ownership.
 */
package com.androidmapsextensions.dendrogram;

import android.util.Log;

import com.androidmapsextensions.impl.DelegatingMarker;


import edu.wlu.cs.levy.CG.KDTree;
import edu.wlu.cs.levy.CG.KeyDuplicateException;
import edu.wlu.cs.levy.CG.KeyMissingException;
import edu.wlu.cs.levy.CG.KeySizeException;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;


/**
 * The HierarchicalAgglomerativeClusterer creates a hierarchical agglomerative clustering.
 * 
 * <pre>
 * Experiment experiment = ...;
 * DissimilarityMeasure dissimilarityMeasure = ...;
 * AgglomerationMethod agglomerationMethod = ...;
 * DendrogramBuilder dendrogramBuilder = new DendrogramBuilder(experiment.getNumberOfObservations());
 * HierarchicalAgglomerativeClusterer clusterer = new HierarchicalAgglomerativeClusterer(experiment, dissimilarityMeasure, agglomerationMethod);
 * clusterer.cluster(dendrogramBuilder);
 * Dendrogram dendrogram = dendrogramBuilder.getDendrogram();
 * </pre>
 * 
 * @author Matthias.Hauswirth@usi.ch
 */
public final class HierarchicalAgglomerativeClusterer {

    private Experiment experiment;
    private DissimilarityMeasure dissimilarityMeasure;    
        
    public HierarchicalAgglomerativeClusterer(final Experiment experiment, final DissimilarityMeasure dissimilarityMeasure ) {
        this.experiment = experiment;
        this.dissimilarityMeasure = dissimilarityMeasure;
    }
    
    public void setExperiment(final Experiment experiment) {
        this.experiment = experiment;
    }
    
    public Experiment getExperiment() {
        return experiment;
    }
    
    public void setDissimilarityMeasure( final DissimilarityMeasure dissimilarityMeasure ) {
        this.dissimilarityMeasure = dissimilarityMeasure;
    }

    public DissimilarityMeasure getDissimilarityMeasure() {
        return dissimilarityMeasure;
    }
    
    private KDTree<DendrogramNode> kd;
    public void cluster(final DendrogramBuilder clusteringBuilder) {
    	// TODO - We cannot have duplicate keys in kdtree. Hence, first cluster all dupes.
    	
    	// Initialize the KD-tree
    	kd = new KDTree<DendrogramNode>(2);
    	try {
    		for ( int i = 0; i < experiment.getNumberOfObservations(); ++i ) {    			
    			double [] xyCoord = experiment.getPosition( i );
    			Log.e( "e", "adding i=" + i + " x=" + xyCoord[0] + " y=" + xyCoord[1] );
    			kd.insert( xyCoord, new ObservationNode(i, xyCoord) );
    		}
    	}    	
		catch ( KeyDuplicateException e ) {			
			e.printStackTrace();
		}
		catch ( KeySizeException e ) {
			e.printStackTrace();
		}
    	
    	// Initialize the min-heap    	
    	SortedMap<Double,Pair> minHeap = new TreeMap<Double,Pair>();
    	try {
    		for ( int i = 0; i < experiment.getNumberOfObservations(); ++i ) {    			
    			double [] pos = experiment.getPosition( i );
    			DendrogramNode target = kd.search( pos );
    			
    			// Find the nearest observation to this observation, excluding itself
    			DendrogramNode nearest = findNearest( target );    			
    			
    			double dist = dissimilarityMeasure.distanceMiles( pos, nearest.getPosition() );
    			minHeap.put( dist, new Pair(target,nearest) );
    		}
    		//System.out.println( "MinHeap=" + minHeap );
    	}
    	catch ( KeySizeException e ) {
    		e.printStackTrace();
    	}
    	
    	try {
    		while( kd.size() > 1 ) {
    			Pair pair = minHeap.remove( minHeap.firstKey() );
    			// Does kd contain pair.A ?
    			double [] pos1 = pair.cluster1.getPosition();
    			double [] pos2 = pair.cluster2.getPosition();
    			DendrogramNode node1 = kd.search( pos1 );
    			DendrogramNode node2 = kd.search( pos2 );
    			if ( node1 == null ) { 
    				// A was already clustered with somebody
    			}
    			else
    			if ( node2 == null ) {
    				// B is invalid, find new best match for A
    				DendrogramNode nearest = findNearest( node1 );
        			double dist = dissimilarityMeasure.distanceMiles( pos1, nearest.getPosition() );
        			minHeap.put( dist, new Pair(node1, nearest) );
    			} else {
    				double dist = dissimilarityMeasure.distanceMiles( pos1, pos2 );
    				MergeNode cluster = clusteringBuilder.merge( pair.cluster1, pair.cluster2, dist );
    	    		//System.out.println( "Deleting keys " + pos1[0] + " " + pos1[1] );
    				kd.delete( pos1 );
    				//System.out.println( "TRee now " + kd.toString() );
    				//System.out.println( "Deleting keys " + pos2[0] + " " + pos2[1] );
    				kd.delete( pos2 );
    				//System.out.println( "TRee now " + kd.toString() );
        			kd.insert( cluster.getPosition(), cluster);
        			
        			if ( kd.size() <= 1 )
        				break;
        			
    				DendrogramNode nearest = findNearest( cluster );
        			double dist2 = dissimilarityMeasure.distanceMiles( cluster.getPosition(), nearest.getPosition() );
        			minHeap.put( dist2, new Pair(cluster, nearest) );
    			}
    		}
    	}
		catch ( KeySizeException e ) {
			e.printStackTrace();
		}
		catch ( KeyMissingException e ) {
			e.printStackTrace();
		}
		catch ( KeyDuplicateException e ) {
			e.printStackTrace();
		}    
    }

    // Find the nearest observation to this observation, excluding self
    private DendrogramNode findNearest( DendrogramNode A ) {    	
		try {
			List<DendrogramNode> nearestList = kd.nearest( A.getPosition(), 2 );
			
			if ( nearestList.size() == 0 ) {
				throw new IllegalStateException();
			}
			else
			if ( nearestList.size() == 1 ) {
				return nearestList.get(0);
			}
			
	    	if ( nearestList.get(0).equals( A ) ) {
	    		return nearestList.get(1);
	    	} else {
	    		return nearestList.get(0);
	    	}
		}
		catch ( KeySizeException e ) {
			e.printStackTrace();
			return null;
		}    			
    }
	
    private static final class Pair {

        private DendrogramNode cluster1;
        private DendrogramNode cluster2;

        public Pair (final DendrogramNode cluster1, final DendrogramNode cluster2) {
            this.cluster1 = cluster1;
            this.cluster2 = cluster2;
        }
    }

}
