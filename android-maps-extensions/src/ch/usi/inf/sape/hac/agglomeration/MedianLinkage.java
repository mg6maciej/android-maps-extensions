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
package ch.usi.inf.sape.hac.agglomeration;


/**
 * The "median", "weighted centroid", "weighted center of mass distance", "Gower",
 * or "Weighted Pair-Group Method using Centroids (WPGMC)" method is a geometric approach.
 * 
 * The size of the clusters is assumed to be equal and 
 * the position of the new centroid is always between the two old centroids.
 * This method preserves the importance of a small cluster when it is merged with a large cluster.
 * [The data analysis handbook. By Ildiko E. Frank, Roberto Todeschini]
 * 
 * Can produce a dendrogram that is not monotonic
 * (it can have so called inversions, which are hard to interpret). 
 * This occurs when the distance from the union of two clusters, r and s, 
 * to a third cluster is less than the distance between r and s.
 * 
 * Used only for Euclidean distance!
 * 
 * The distance between two clusters is the Euclidean distance between their weighted centroids.
 * 
 * The general form of the Lance-Williams matrix-update formula:
 * d[(i,j),k] = ai*d[i,k] + aj*d[j,k] + b*d[i,j] + g*|d[i,k]-d[j,k]|
 *
 * For the "median" method:
 * ai = 0.5
 * aj = 0.5
 * b  = -0.25
 * g  = 0
 * 
 * Thus:
 * d[(i,j),k] = 0.5*d[i,k] + 0.5*d[j,k] - 0.25*d[i,j]
 * 
 * @author Matthias.Hauswirth@usi.ch
 */
public final class MedianLinkage implements AgglomerationMethod {

    public double computeDissimilarity(final double dik, final double djk, final double dij, final int ci, final int cj, final int ck) {
        return 0.5*dik+0.5*djk-0.25*dij;
    }

    public String toString() {
        return "Median";
    }

}
