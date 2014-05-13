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
 * The "single linkage", "minimum", "shortest distance", or "nearest neighbor" method is a graph-based approach.
 * 
 * The distance between two clusters is calculated as 
 * the smallest distance between two objects in opposite clusters.
 * This method tends to produce loosely bound large clusters with little internal cohesion.
 * Linear, elongated clusters are formed as opposed to the more usual spherical clusters.
 * This pheonomenon is called chaining.
 * [The data analysis handbook. By Ildiko E. Frank, Roberto Todeschini]
 *
 * This method can cause "chaining" of clusters. 
 * @see http://www.stanford.edu/~maureenh/quals/html/ml/node74.html
 * 
 * The general form of the Lance-Williams matrix-update formula:
 * d[(i,j),k] = ai*d[i,k] + aj*d[j,k] + b*d[i,j] + g*|d[i,k]-d[j,k]|
 *
 * For the "single linkage" method:
 * ai = 0.5
 * aj = 0.5
 * b  = 0
 * g  = -0.5
 * 
 * Thus:
 * d[(i,j),k] = 0.5*d[i,k] + 0.5*d[j,k] - 0.5*|d[i,k]-d[j,k]|
 *            = 0.5*d[i,k] + 0.5*d[j,k] - | 0.5*d[i,k] - 0.5*d[j,k] |
 *            = d[i,j]<d[j,k]  ?  0.5*d[i,k] + 0.5*d[j,k] + 0.5*d[i,k] - 0.5*d[j,k]  :  0.5*d[i,k] + 0.5*d[j,k] - 0.5*d[i,k] + 0.5*d[j,k]
 *            = d[i,j]<d[j,k]  ?  0.5*d[i,k] + 0.5*d[i,k]  :  0.5*d[j,k] + 0.5*d[j,k]
 *            = d[i,j]<d[j,k]  ?  d[i,k]  :  d[j,k]
 *            = min( d[i,k] , d[j,k] )
 *            
 * @author Matthias.Hauswirth@usi.ch
 */
public final class SingleLinkage implements AgglomerationMethod {

	public double computeDissimilarity(final double dik, final double djk, final double dij, final int ci, final int cj, final int ck) {
		return Math.min(dik, djk);
	}
	
    public String toString() {
		return "Single";
	}

}
