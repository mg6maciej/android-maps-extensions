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


/**
 * A MergeNode represents an interior node in a Dendrogram.
 * It corresponds to a (non-singleton) cluster of observations.
 * 
 * @author Matthias.Hauswirth@usi.ch
 */
public final class MergeNode implements DendrogramNode {
	
	private final DendrogramNode left;
	private final DendrogramNode right;
	private final double dissimilarity;
	private final int observationCount;
	
	
	public MergeNode( final DendrogramNode left, final DendrogramNode right, final double dissimilarity ) {
		this.left  = left;
		this.right = right;
		this.dissimilarity = dissimilarity;
		observationCount = left.getObservationCount() + right.getObservationCount();
	}
	
	public int getObservationCount() {
		return observationCount;
	}
	
	public final DendrogramNode getLeft() {
		return left;
	}
	
	public final DendrogramNode getRight() {
		return right;
	}
	
	public final double getDissimilarity() {
		return dissimilarity;
	}

}