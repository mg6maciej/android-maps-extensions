// HPoint.java : Hyper-Point class supporting KDTree class
//
// Copyright (C) Simon D. Levy 2014
//
// This code is free software: you can redistribute it and/or modify
// it under the terms of the GNU Lesser General Public License as 
// published by the Free Software Foundation, either version 3 of the 
// License, or (at your option) any later version.
//
// This code is distributed in the hope that it will be useful,     
// but WITHOUT ANY WARRANTY without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
//  You should have received a copy of the GNU Lesser General Public License 
//  along with this code.  If not, see <http://www.gnu.org/licenses/>.
//  You should also have received a copy of the Parrot Parrot AR.Drone 
//  Development License and Parrot AR.Drone copyright notice and disclaimer 
//  and If not, see 
//   <https://projects.ardrone.org/attachments/277/ParrotLicense.txt> 
// and
//   <https://projects.ardrone.org/attachments/278/ParrotCopyrightAndDisclaimer.txt>.

package edu.wlu.cs.levy.CG;

import java.io.Serializable;

class HPoint implements Serializable{

    protected double [] coord;

    protected HPoint(int n) {
	coord = new double [n];
    }

    protected HPoint(double [] x) {

	coord = new double[x.length];
	for (int i=0; i<x.length; ++i) coord[i] = x[i];
    }

    protected Object clone() {

	return new HPoint(coord);
    }

    protected boolean equals(HPoint p) {

	// seems faster than java.util.Arrays.equals(), which is not 
	// currently supported by Matlab anyway
	for (int i=0; i<coord.length; ++i)
	    if (coord[i] != p.coord[i])
		return false;

	return true;
    }

    protected static double sqrdist(HPoint x, HPoint y) {
	
	return EuclideanDistance.sqrdist(x.coord, y.coord);
    }
    


    public String toString() {
	String s = "";
	for (int i=0; i<coord.length; ++i) {
	    s = s + coord[i] + " ";
	}
	return s;
    }

}
