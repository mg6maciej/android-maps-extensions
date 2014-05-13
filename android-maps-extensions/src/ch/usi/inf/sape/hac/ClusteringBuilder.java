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
package ch.usi.inf.sape.hac;


/**
 * HierarchicalAgglomerativeClusterer.cluster() takes a ClusteringBuilder as its argument,
 * calling its merge() method whenever it merges two clusters.
 * 
 * @author Matthias.Hauswirth@usi.ch
 */
public interface ClusteringBuilder {

    /**
     * Merge two clusters.
     * @param i the smaller of the two cluster indices
     * @param j the larger of the two cluster indices
     * @param dissimilarity between the two merged clusters
     */
    public void merge(int i, int j, double dissimilarity);

}
