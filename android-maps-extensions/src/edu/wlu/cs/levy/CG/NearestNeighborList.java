// NearestNeighborList.java : A solution to the KD-Tree n-nearest-neighbor problem
//
// Copyright (C) Bjoern Heckel and Simon D. Levy 2014
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
// 

package edu.wlu.cs.levy.CG;

import java.util.*;


class NearestNeighborList<T> {

    static class NeighborEntry<T> implements Comparable<NeighborEntry<T>> {
        final T data;
        final double value;

        public NeighborEntry(final T data,
                             final double value) {
            this.data = data;
            this.value = value;
        }

        public int compareTo(NeighborEntry<T> t) {
            // note that the positions are reversed!
            return Double.compare(t.value, this.value);
        }
    };

    java.util.PriorityQueue<NeighborEntry<T>> m_Queue;
    int m_Capacity = 0;

    // constructor
    public NearestNeighborList(int capacity) {
        m_Capacity = capacity;
        m_Queue = new java.util.PriorityQueue<NeighborEntry<T>>(m_Capacity);
    }

    public double getMaxPriority() {
        NeighborEntry p = m_Queue.peek();
        return (p == null) ? Double.POSITIVE_INFINITY : p.value ;
    }

    public boolean insert(T object, double priority) {
        if (isCapacityReached()) {
            if (priority > getMaxPriority()) {
                // do not insert - all elements in queue have lower priority
                return false;
            }
            m_Queue.add(new NeighborEntry<T>(object, priority));
            // remove object with highest priority
            m_Queue.poll();
        } else {
            m_Queue.add(new NeighborEntry<T>(object, priority));
        }
        return true;
    }

    public boolean isCapacityReached() {
        return m_Queue.size()>=m_Capacity;
    }

    public T getHighest() {
        NeighborEntry<T> p = m_Queue.peek();
        return (p == null) ?  null : p.data ;
    }

    public boolean isEmpty() {
        return m_Queue.size()==0;
    }

    public int getSize() {
        return m_Queue.size();
    }

    public T removeHighest() {
        // remove object with highest priority
        NeighborEntry<T> p = m_Queue.poll();
        return (p == null) ?  null : p.data ;
    }
}
