package ch.usi.inf.sape.hac;

import ch.usi.inf.sape.hac.experiment.DissimilarityMeasure;
import ch.usi.inf.sape.hac.experiment.Experiment;


public class MyDissimilarityMeasure implements DissimilarityMeasure {
	
	@Override
    public double computeDissimilarity(Experiment experiment, int observation1, int observation2) {		
		double [] pos1 = experiment.getPosition( observation1 );
		double [] pos2 = experiment.getPosition( observation2 );
		
		return distanceMiles( pos1, pos2 );
	}

	public double distanceMiles( double[] pos1, double[] pos2 ) {
		
		double EARTH_RADIUS_MILES = 3200; // TODO
		
		double avgLat = Math.toRadians( (pos1[0] + pos2[0])/2 );
		
		double dx = Math.toRadians( pos2[1] - pos1[1] ) * Math.cos( avgLat );
		double dy = Math.toRadians( pos2[0] - pos1[0] );
		
		double d = EARTH_RADIUS_MILES * Math.sqrt( dx*dx + dy*dy ); 
		
		return d;
	}

}