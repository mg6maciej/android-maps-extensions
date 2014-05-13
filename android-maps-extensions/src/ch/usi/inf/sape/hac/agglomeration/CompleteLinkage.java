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
 * The "complete", "maximum", "clique", 
 * "furthest neighbor", or "furthest distance" method is a graph-based approach.
 * 
 * The distance between two clusters is calculated as the largest distance 
 * between two objects in opposite clusters.
 * This method tends to produce well separated, small, compact spherical clusters.
 * The cluster space is dilated. 
 * [The data analysis handbook. By Ildiko E. Frank, Roberto Todeschini]
 * 
 * This method tends to produce compact clusters. Outliers are given more weight with this method.
 * It is generally a good choice if the clusters are far apart in feature space, but not good if the data are noisy.
 * @see http://www.stanford.edu/~maureenh/quals/html/ml/node75.html
 *  
 * The general form of the Lance-Williams matrix-update formula:
 * d[(i,j),k] = ai*d[i,k] + aj*d[j,k] + b*d[i,j] + g*|d[i,k]-d[j,k]|
 *
 * For the "single linkage" method:
 * ai = 0.5
 * aj = 0.5
 * b  = 0
 * g  = 0.5
 * 
 * Thus:
 * d[(i,j),k] = 0.5*d[i,k] + 0.5*d[j,k] + 0.5*|d[i,k]-d[j,k]|
 *            = 0.5*d[i,k] + 0.5*d[j,k] + | 0.5*d[i,k] - 0.5*d[j,k] |
 *            = d[i,j]<d[j,k]  ?  0.5*d[i,k] + 0.5*d[j,k] - 0.5*d[i,k] + 0.5*d[j,k]  :  0.5*d[i,k] + 0.5*d[j,k] + 0.5*d[i,k] - 0.5*d[j,k]
 *            = d[i,j]<d[j,k]  ?  0.5*d[j,k] + 0.5*d[j,k]  :  0.5*d[i,k] + 0.5*d[i,k]
 *            = d[i,j]<d[j,k]  ?  d[j,k]  :  d[i,k]
 *            = max( d[i,k] , d[j,k] )
 *            
 * @author Matthias.Hauswirth@usi.ch
 */
public final class CompleteLinkage implements AgglomerationMethod {

    public double computeDissimilarity(final double dik, final double djk, final double dij, final int ci, final int cj, final int ck) {
        return Math.max(dik, djk);
    }
    
    public String toString() {
        return "Complete";
    }

}
