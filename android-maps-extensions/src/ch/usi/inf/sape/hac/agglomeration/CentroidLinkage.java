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
 * The "centroid" or "Unweighted Pair-Group Method using Centroids (UPGMC)" 
 * method is a geometric approach that links the centroids of clusters.
 * 
 * Each cluster is represented by its centroid.
 * The distance between two clusters is calculated as the distance between their centriods.
 * This method does not distort the cluster space.
 * [The data analysis handbook. By Ildiko E. Frank, Roberto Todeschini]
 *  
 * Can produce a dendrogram that is not monotonic
 * (it can have so called inversions, which are hard to interpret). 
 * This occurs when the distance from the union of two clusters, r and s, 
 * to a third cluster is less than the distance between r and s.
 * 
 * Used only for Euclidean distance!
 * 
 * The general form of the Lance-Williams matrix-update formula:
 * d[(i,j),k] = ai*d[i,k] + aj*d[j,k] + b*d[i,j] + g*|d[i,k]-d[j,k]|
 *
 * For the "centroid" method:
 * ai = ci/(ci+cj)
 * aj = cj/(ci+cj)
 * b  = -ci*cj/((ci+cj)*(ci+cj))
 * g  = 0
 * 
 * Thus:
 * d[(i,j),k] = ci/(ci+cj)*d[i,k] + cj/(ci+cj)*d[j,k] - ci*cj/((ci+cj)*(ci+cj))*d[i,j]
 *            = ( ci*d[i,k] + cj*d[j,k] - ci*cj/(ci+cj)*d[i,j] ) / (ci+cj)
 * 
 * @author Matthias.Hauswirth@usi.ch
 */
public final class CentroidLinkage implements AgglomerationMethod {

    public double computeDissimilarity(final double dik, final double djk, final double dij, final int ci, final int cj, final int ck) {
        return (ci*dik+cj*djk-ci*cj*dij/(ci+cj))/(ci+cj);
    }

    public String toString() {
        return "Centroid";
    }

}
