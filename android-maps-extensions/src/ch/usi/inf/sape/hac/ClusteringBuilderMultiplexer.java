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
 * A ClusteringBuilderMultiplexer is a ClusteringBuilder that forwards calls to two other ClusteringBuilders.
 * 
 * @author Matthias.Hauswirth@usi.ch
 */
public final class ClusteringBuilderMultiplexer implements ClusteringBuilder {

    private final ClusteringBuilder a;
    private final ClusteringBuilder b;


    public ClusteringBuilderMultiplexer(final ClusteringBuilder a, final ClusteringBuilder b) {
        this.a = a;
        this.b = b;
    }

    public void merge(final int i, final int j, final double dissimilarity) {
        a.merge(i, j, dissimilarity);
        b.merge(i, j, dissimilarity);
    }

}
