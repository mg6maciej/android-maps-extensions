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
 * The "Ward", "inner squared distance", "sum of squares", "error sum of squares", 
 * or "minimum variance" method.
 * 
 * This method fuses those two clusters that result in the smallest increase 
 * in the total within-group error sum of squares.
 * This quantity is defined as the sum of squared deviation 
 * of each object from the centroid of its own cluster.
 * In contrast to the other methods that use prior criteria,
 * this method is based on a posterior fusion criterion.
 * [The data analysis handbook. By Ildiko E. Frank, Roberto Todeschini]
 *  
 * Used only for Euclidean distance!
 * 
 * The general form of the Lance-Williams matrix-update formula:
 * d[(i,j),k] = ai*d[i,k] + aj*d[j,k] + b*d[i,j] + g*|d[i,k]-d[j,k]|
 *
 * For the "Ward" method:
 * ai = (ci+ck)/(ci+cj+ck)
 * aj = (cj+ck)/(ci+cj+ck)
 * b  = -ck/(ci+cj+ck)
 * g  = 0
 * 
 * Thus:
 * d[(i,j),k] = (ci+ck)/(ci+cj+ck)*d[i,k] + (cj+ck)/(ci+cj+ck)*d[j,k] - ck/(ci+cj+ck)*d[i,j]
 *            = ( (ci+ck)*d[i,k] + (cj+ck)*d[j,k] - ck*d[i,j] ) / (ci+cj+ck)
 * 
 * @author Matthias.Hauswirth@usi.ch
 */
public final class WardLinkage implements AgglomerationMethod {

    public double computeDissimilarity(final double dik, final double djk, final double dij, final int ci, final int cj, final int ck) {
        return ((ci+ck)*dik+(cj+ck)*djk-ck*dij)/(ci+cj+ck);
    }

    public String toString() {
        return "Ward";
    }

}
