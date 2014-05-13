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
 * The "weighted average", "McQuitty", or 
 * "Weighted Pair-Group Method using Arithmetic averages, or WPGMA)" method.
 * 
 * Average linkage where the sizes of the clusters are assumed to be equal.
 * This method, similar to "Median", weights small and large clusters equally. 
 * [The data analysis handbook. By Ildiko E. Frank, Roberto Todeschini]
 * 
 * The general form of the Lance-Williams matrix-update formula:
 * d[(i,j),k] = ai*d[i,k] + aj*d[j,k] + b*d[i,j] + g*|d[i,k]-d[j,k]|
 *
 * For the "McQuitty" method:
 * ai = 0.5
 * aj = 0.5
 * b  = 0
 * g  = 0
 * 
 * Thus:
 * d[(i,j),k] = 0.5*d[i,k] + 0.5*d[j,k]
 * 
 * @author Matthias.Hauswirth@usi.ch
 */
public final class WeightedAverageLinkage implements AgglomerationMethod {

    public double computeDissimilarity(final double dik, final double djk, final double dij, final int ci, final int cj, final int ck) {
        return 0.5*dik+0.5*djk;
    }

    public String toString() {
        return "Weighted average";
    }

}
