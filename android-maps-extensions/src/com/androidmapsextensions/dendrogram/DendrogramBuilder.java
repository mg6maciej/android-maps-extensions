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



/**
 * A DendrogramBuilder creates a Dendrogram consisting of ObservationNodes and
 * MergeNodes.
 * 
 * @author Matthias.Hauswirth@usi.ch
 */
public final class DendrogramBuilder implements ClusteringBuilder {
	
    private MergeNode lastMergeNode;
    private Experiment experiment;
    
    public DendrogramBuilder( Experiment experiment ) {
    	this.experiment = experiment;    
    }
    public final MergeNode merge(final DendrogramNode i, final DendrogramNode j, double dist) {
        final MergeNode node = new MergeNode( i, j, dist );
        lastMergeNode = node;
        i.setParent(node);
        j.setParent(node);
        return node;
    }

    public final Dendrogram getDendrogram() {
        if ( experiment.getNumberOfObservations() == 1 ) {
            return new Dendrogram( new ObservationNode(0, experiment.getPosition( 0 ) ) );
        } else {
            return new Dendrogram( lastMergeNode );
        }
    }

}
