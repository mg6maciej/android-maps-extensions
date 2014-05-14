package ch.usi.inf.sape.hac.experiment;

import ch.usi.inf.sape.hac.dendrogram.ObservationNode;
import com.androidmapsextensions.impl.DelegatingMarker;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;


public class MapDissimilarityMeasure implements DissimilarityMeasure {
	// Cache previously computed dissimilarities
	static List<DelegatingMarker> mFullMarkerList;
	
	private static final double EARTH_RADIUS_MILES = 3958.76;	
	private double distanceMiles( double lat1d, double lon1d, double lat2d, double lon2d ) {
		double avgLat = Math.toRadians( (lat1d+lat2d)/2 );
		
		double dx = Math.toRadians( lon2d - lon1d ) * Math.cos( avgLat );
		double dy = Math.toRadians( lat2d - lat1d );
		
		double d = EARTH_RADIUS_MILES * Math.sqrt( dx*dx + dy*dy ); 
		
		return d;
	}
	
    private static final class Pair {
        private int cluster1; // smaller
        private int cluster2; // larger
        
        public final void set(final int cluster1, final int cluster2) {
            this.cluster1 = Math.min(cluster1, cluster2);
            this.cluster2 = Math.max(cluster1, cluster2);
        }
        public final int getSmaller() {
            return cluster1;
        }
        public final int getLarger() {
            return cluster2;
        }        
    }
    
    // Cluster the markers in steps of 0.5zoom (or whatever), rather than finding most similar pair every time
    private static Pair findMostSimilarClusters( final boolean[] indexUsed ) {
        final Pair mostSimilarPair = new Pair();
        double smallestDissimilarity = Double.POSITIVE_INFINITY;
        for ( int cluster = 0; cluster < mFullMarkerList.size(); cluster++ ) {
            if ( ! indexUsed[cluster] )
            	continue;
            LatLng clusterPos = mFullMarkerList.get(cluster).getPosition(); 
            
            for ( int neighbor = 0; neighbor < mFullMarkerList.size(); neighbor++ ) {
            	if ( indexUsed[neighbor] )
            		continue;
            	if ( cluster == neighbor )
            		continue;
            	LatLng neighborPos = mFullMarkerList.get(neighbor).getPosition();
            	if ( )
            			
            			
            		&&  dissimilarityMatrix[cluster][neighbor] < smallestDissimilarity  &&  cluster != neighbor ) {
                        smallestDissimilarity = dissimilarityMatrix[cluster][neighbor];
                        mostSimilarPair.set(cluster, neighbor);
                    }
                }
            }
        }
        return mostSimilarPair;
    }
    
	// Approximation for small distances, but good enough
	@Override
	public double computeDissimilarity( Experiment experiment, int observation1, int observation2 ) {
		DelegatingMarker dm1 = fullMarkerList.get( observation1 );
		DelegatingMarker dm2 = fullMarkerList.get( observation2 );
        int clusterGroup1 = dm1.getClusterGroup();
        int clusterGroup2 = dm2.getClusterGroup();
        if ( clusterGroup1 < 0  ||  clusterGroup2 < 0 ) {
        	return Double.MAX_VALUE; // clusterGroup < 0 implies this marker is never clustered
        }
        
		return distanceMiles( dm1.getPosition().latitude, dm1.getPosition().longitude, dm2.getPosition().latitude, dm2.getPosition().longitude );
	}

}
