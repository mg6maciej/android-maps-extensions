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
 * An AgglomerationMethod represents the Lance-Williams dissimilarity update formula
 * used for hierarchical agglomerative clustering.
 * 
 * The general form of the Lance-Williams matrix-update formula:
 * d[(i,j),k] = ai*d[i,k] + aj*d[j,k] + b*d[i,j] + g*|d[i,k]-d[j,k]|
 *
 * Parameters ai, aj, b, and g are defined differently for different methods:
 * 
 * Method          ai                   aj                   b                          g
 * -------------   ------------------   ------------------   ------------------------   -----
 * Single          0.5                  0.5                  0                          -0.5
 * Complete        0.5                  0.5                  0                          0.5
 * Average         ci/(ci+cj)           cj/(ci+cj)           0                          0
 * 
 * Centroid        ci/(ci+cj)           cj/(ci+cj)           -ci*cj/((ci+cj)*(ci+cj))   0
 * Median          0.5                  0.5                  -0.25                      0
 * Ward            (ci+ck)/(ci+cj+ck)   (cj+ck)/(ci+cj+ck)   -ck/(ci+cj+ck)             0
 * 
 * WeightedAverage 0.5                  0.5                  0                          0
 * 
 * (ci, cj, ck are cluster cardinalities)
 *
 * @see http://www.mathworks.com/help/toolbox/stats/linkage.html
 * @see http://www.stanford.edu/~maureenh/quals/html/ml/node73.html
 * @see [The data analysis handbook. By Ildiko E. Frank, Roberto Todeschini. Pages 152-155]
 * 
 * @author Matthias.Hauswirth@usi.ch
 */
public interface AgglomerationMethod {
	
	/**
	 * Compute the dissimilarity between the 
	 * newly formed cluster (i,j) and the existing cluster k.
	 * 
	 * @param dik dissimilarity between clusters i and k
	 * @param djk dissimilarity between clusters j and k
	 * @param dij dissimilarity between clusters i and j
	 * @param ci cardinality of cluster i
	 * @param cj cardinality of cluster j
	 * @param ck cardinality of cluster k
	 * 
	 * @return dissimilarity between cluster (i,j) and cluster k.
	 */
	public double computeDissimilarity(double dik, double djk, double dij, int ci, int cj, int ck);

}
