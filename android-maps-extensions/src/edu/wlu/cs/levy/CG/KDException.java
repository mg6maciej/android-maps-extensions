// KDException.java : general exception class for KD-Tree library
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


public class KDException extends Exception {
    protected KDException(String s) {
        super(s);
    }
    public static final long serialVersionUID = 1L;
}
