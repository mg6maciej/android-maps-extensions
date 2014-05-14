package ch.usi.inf.sape.hac;

import ch.usi.inf.sape.hac.dendrogram.Dendrogram;
import ch.usi.inf.sape.hac.dendrogram.DendrogramBuilder;
import ch.usi.inf.sape.hac.experiment.Experiment;

public class Test {
	public static void main( String[] args ) {
		Experiment experiment = new Experiment() {
			@Override
			public int getNumberOfObservations() {
				return 3;
			}

			@Override
			public double[] getPosition( int observation ) {
				return null;
			}
		};
		MyDissimilarityMeasure dissimilarityMeasure = new MyDissimilarityMeasure();
		DendrogramBuilder dendrogramBuilder = new DendrogramBuilder( experiment );
		
		HierarchicalAgglomerativeClusterer clusterer = new HierarchicalAgglomerativeClusterer(experiment, dissimilarityMeasure );
		clusterer.cluster(dendrogramBuilder);
		Dendrogram dendrogram = dendrogramBuilder.getDendrogram();
		dendrogram.dump();
	}		
}
