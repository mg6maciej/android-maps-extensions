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

import ch.usi.inf.sape.hac.agglomeration.AgglomerationMethod;
import ch.usi.inf.sape.hac.experiment.DissimilarityMeasure;
import ch.usi.inf.sape.hac.experiment.Experiment;


/**
 * The HierarchicalAgglomerativeClusterer creates a hierarchical agglomerative clustering.
 * 
 * <pre>
 * Experiment experiment = ...;
 * DissimilarityMeasure dissimilarityMeasure = ...;
 * AgglomerationMethod agglomerationMethod = ...;
 * DendrogramBuilder dendrogramBuilder = new DendrogramBuilder(experiment.getNumberOfObservations());
 * HierarchicalAgglomerativeClusterer clusterer = new HierarchicalAgglomerativeClusterer(experiment, dissimilarityMeasure, agglomerationMethod);
 * clusterer.cluster(dendrogramBuilder);
 * Dendrogram dendrogram = dendrogramBuilder.getDendrogram();
 * </pre>
 * 
 * @author Matthias.Hauswirth@usi.ch
 */
public final class HierarchicalAgglomerativeClusterer {

    private Experiment experiment;
    private DissimilarityMeasure dissimilarityMeasure;
    private AgglomerationMethod agglomerationMethod;
    
    
    public HierarchicalAgglomerativeClusterer(final Experiment experiment, final DissimilarityMeasure dissimilarityMeasure, final AgglomerationMethod agglomerationMethod) {
        this.experiment = experiment;
        this.dissimilarityMeasure = dissimilarityMeasure;
        this.agglomerationMethod = agglomerationMethod;
    }
    
    public void setExperiment(final Experiment experiment) {
        this.experiment = experiment;
    }
    
    public Experiment getExperiment() {
        return experiment;
    }
    
    public void setDissimilarityMeasure(final DissimilarityMeasure dissimilarityMeasure) {
        this.dissimilarityMeasure = dissimilarityMeasure;
    }

    public DissimilarityMeasure getDissimilarityMeasure() {
        return dissimilarityMeasure;
    }
    
    public void setAgglomerationMethod(final AgglomerationMethod agglomerationMethod) {
        this.agglomerationMethod = agglomerationMethod;
    }
    
    public AgglomerationMethod getAgglomerationMethod() {
        return agglomerationMethod;
    }    
    
    public void cluster(final ClusteringBuilder clusteringBuilder) {
        final double[][] dissimilarityMatrix = computeDissimilarityMatrix();
        final int nObservations = dissimilarityMatrix.length;
        
        final boolean[] indexUsed = new boolean[nObservations];
        final int[] clusterCardinalities = new int[nObservations];
        for (int i = 0; i<nObservations; i++) {
            indexUsed[i] = true;
            clusterCardinalities[i] = 1;
        }
        
        // Perform nObservations-1 agglomerations
        for (int a = 1; a<nObservations; a++) {
            // Determine the two most similar clusters, i and j (such that i<j)
            final Pair pair = findMostSimilarClusters(dissimilarityMatrix, indexUsed);
            final int i = pair.getSmaller();
            final int j = pair.getLarger();
            final double d = dissimilarityMatrix[i][j];
            
            /**
            System.out.println("Agglomeration #"+a+
                    ": merging clusters "+i+
                    " (cardinality "+(clusterCardinalities[i])+") and "+j+
                    " (cardinality "+(clusterCardinalities[j])+") with dissimilarity "+d);
            **/
            
            // cluster i becomes new cluster
            // (by agglomerating former clusters i and j)
            // update dissimilarityMatrix[i][*] and dissimilarityMatrix[*][i]
            for (int k = 0; k<nObservations; k++) {
                if ((k!=i)&&(k!=j)&&indexUsed[k]) {
                    final double dissimilarity = agglomerationMethod.computeDissimilarity(dissimilarityMatrix[i][k], dissimilarityMatrix[j][k],
                            dissimilarityMatrix[i][j], clusterCardinalities[i], clusterCardinalities[j], clusterCardinalities[k]);
                    dissimilarityMatrix[i][k] = dissimilarity;
                    dissimilarityMatrix[k][i] = dissimilarity;
                }
            }
            clusterCardinalities[i] = clusterCardinalities[i]+clusterCardinalities[j];
            
            // erase cluster j
            indexUsed[j] = false;
            for (int k = 0; k<nObservations; k++) {
                dissimilarityMatrix[j][k] = Double.POSITIVE_INFINITY;
                dissimilarityMatrix[k][j] = Double.POSITIVE_INFINITY;
            }
            
            // update clustering
            clusteringBuilder.merge(i, j, d);
        }
    }
    
    private double[][] computeDissimilarityMatrix() {
        final double[][] dissimilarityMatrix = new double[experiment.getNumberOfObservations()][experiment.getNumberOfObservations()];
        // fill diagonal
        for (int o = 0; o<dissimilarityMatrix.length; o++) {
            dissimilarityMatrix[o][o] = 0.0;
        }
        // fill rest (only compute half, then mirror accross diagonal, assuming
        // a symmetric dissimilarity measure)
        for (int o1 = 0; o1<dissimilarityMatrix.length; o1++) {
            for (int o2 = 0; o2<o1; o2++) {
                final double dissimilarity = dissimilarityMeasure.computeDissimilarity(experiment, o1, o2);
                dissimilarityMatrix[o1][o2] = dissimilarity;
                dissimilarityMatrix[o2][o1] = dissimilarity;
            }
        }
        return dissimilarityMatrix;
    }

    private static Pair findMostSimilarClusters(final double[][] dissimilarityMatrix, final boolean[] indexUsed) {
        final Pair mostSimilarPair = new Pair();
        double smallestDissimilarity = Double.POSITIVE_INFINITY;
        for (int cluster = 0; cluster<dissimilarityMatrix.length; cluster++) {
            if (indexUsed[cluster]) {
                for (int neighbor = 0; neighbor<dissimilarityMatrix.length; neighbor++) {
                    if (indexUsed[neighbor]&&dissimilarityMatrix[cluster][neighbor]<smallestDissimilarity&&cluster!=neighbor) {
                        smallestDissimilarity = dissimilarityMatrix[cluster][neighbor];
                        mostSimilarPair.set(cluster, neighbor);
                    }
                }
            }
        }
        return mostSimilarPair;
    }


    private static final class Pair {

        private int cluster1;
        private int cluster2;


        public final void set(final int cluster1, final int cluster2) {
            this.cluster1 = cluster1;
            this.cluster2 = cluster2;
        }

        public final int getLarger() {
            return Math.max(cluster1, cluster2);
        }

        public final int getSmaller() {
            return Math.min(cluster1, cluster2);
        }

    }

}
