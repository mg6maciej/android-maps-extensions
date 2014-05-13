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
 * The "average", "group average", "unweighted average", or 
 * "Unweighted Pair Group Method using Arithmetic averages (UPGMA)",
 * is a graph-based approach.
 * 
 * The distance between two clusters is calculated as the average 
 * of the distances between all pairs of objects in opposite clusters.
 * This method tends to produce small clusters of outliers,
 * but does not deform the cluster space.
 * [The data analysis handbook. By Ildiko E. Frank, Roberto Todeschini]
 * 
 * The general form of the Lance-Williams matrix-update formula:
 * d[(i,j),k] = ai*d[i,k] + aj*d[j,k] + b*d[i,j] + g*|d[i,k]-d[j,k]|
 *
 * For the "group average" method:
 * ai = ci/(ci+cj)
 * aj = cj/(ci+cj)
 * b  = 0
 * g  = 0
 * 
 * Thus:
 * d[(i,j),k] = ci/(ci+cj)*d[i,k] + cj/(ci+cj)*d[j,k]
 *            = ( ci*d[i,k] + cj*d[j,k] ) / (ci+cj)
 * 
 * @author Matthias.Hauswirth@usi.ch
 */
public final class AverageLinkage implements AgglomerationMethod {

    public double computeDissimilarity(final double dik, final double djk, final double dij, final int ci, final int cj, final int ck) {
        return (ci*dik+cj*djk)/(ci+cj);
    }

    public String toString() {
        return "Average";
    }

}
