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
package com.androidmapsextensions.dendrogram;


/**
 * An experiment consists of a number of observations.
 * 
 * @author Matthias.Hauswirth@usi.ch
 */
public interface Experiment {

    public int getNumberOfObservations();
    public double[] getPosition(int observation);
}
