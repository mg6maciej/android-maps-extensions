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
package ch.usi.inf.sape.hac.dendrogram;

import ch.usi.inf.sape.hac.ClusteringBuilder;


/**
 * A DendrogramBuilder creates a Dendrogram consisting of ObservationNodes and
 * MergeNodes.
 * 
 * @author Matthias.Hauswirth@usi.ch
 */
public final class DendrogramBuilder implements ClusteringBuilder {

    private final DendrogramNode[] nodes;
    private MergeNode lastMergeNode;


    public DendrogramBuilder(final int nObservations) {
        nodes = new DendrogramNode[nObservations];
        for (int i = 0; i<nObservations; i++) {
            nodes[i] = new ObservationNode(i);
        }
    }

    public final void merge(final int i, final int j, final double dissimilarity) {
        final MergeNode node = new MergeNode(nodes[i], nodes[j], dissimilarity);
        nodes[i] = node;
        lastMergeNode = node;
    }

    public final Dendrogram getDendrogram() {
        if (nodes.length==1) {
            return new Dendrogram(nodes[0]);
        } else {
            return new Dendrogram(lastMergeNode);
        }
    }

}
