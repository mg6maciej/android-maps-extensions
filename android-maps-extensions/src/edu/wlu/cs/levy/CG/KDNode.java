// KDNode.java : K-D Tree node class
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
import java.util.List;

class KDNode<T> implements Serializable{

    // these are seen by KDTree
    protected HPoint k;
    T v;
    protected KDNode<T> left, right;
    protected boolean deleted;

    // Method ins translated from 352.ins.c of Gonnet & Baeza-Yates
    protected static <T> int edit(HPoint key, Editor<T> editor, KDNode<T> t, int lev, int K) throws KeyDuplicateException {
        KDNode<T> next_node = null;
        int next_lev = (lev+1) % K;
        synchronized (t) {
            if (key.equals(t.k)) {
                boolean was_deleted = t.deleted;
                t.v = editor.edit(t.deleted ? null : t.v );
                t.deleted = (t.v == null);

                if (t.deleted == was_deleted) {
                	// if I was and still am deleted or was and still am alive
                    return 0;
                } else if (was_deleted) {
                	// if I was deleted => I am now undeleted
                    return 1;
                }
                // I was not deleted, but I am now deleted
                return -1;
            } else if (key.coord[lev] > t.k.coord[lev]) {
                next_node = t.right;
                if (next_node == null) {
                    t.right = create(key, editor);
                    return t.right.deleted ? 0 : 1;
                }                
            }
            else {
                next_node = t.left;
                if (next_node == null) {
                    t.left = create(key, editor);
                    return t.left.deleted ? 0 : 1;
                }                
            }
        }

        return edit(key, editor, next_node, next_lev, K);
    }

    protected static <T> KDNode<T> create(HPoint key, Editor<T> editor) throws KeyDuplicateException {
        KDNode<T> t = new KDNode<T>( key, editor.edit(null) );
        if (t.v == null) {
            t.deleted = true;
        }
        return t;            
    }

    protected static <T> boolean del(KDNode<T> t) {
        synchronized (t) {
            if (!t.deleted) {
                t.deleted = true;
                return true;
            }
        }
        return false;
    }

    // Method srch translated from 352.srch.c of Gonnet & Baeza-Yates
    protected static <T> KDNode<T> srch(HPoint key, KDNode<T> t, int K) {

	for (int lev=0; t!=null; lev=(lev+1)%K) {

	    if (!t.deleted && key.equals(t.k)) {
		return t;
	    }
	    else if (key.coord[lev] > t.k.coord[lev]) {
		t = t.right;
	    }
	    else {
		t = t.left;
	    }
	}

	return null;
    }

    // Method rsearch translated from 352.range.c of Gonnet & Baeza-Yates
    protected static <T> void rsearch(HPoint lowk, HPoint uppk, KDNode<T> t, int lev,
				  int K, List<KDNode<T>> v) {

	if (t == null) return;
	if (lowk.coord[lev] <= t.k.coord[lev]) {
	    rsearch(lowk, uppk, t.left, (lev+1)%K, K, v);
	}
        if (!t.deleted) {
            int j = 0;
            while (j<K && lowk.coord[j]<=t.k.coord[j] && 
                   uppk.coord[j]>=t.k.coord[j]) {
                j++;
            }
            if (j==K) v.add(t);
        }
	if (uppk.coord[lev] > t.k.coord[lev]) {
	    rsearch(lowk, uppk, t.right, (lev+1)%K, K, v);
	}
    }

    // Method Nearest Neighbor from Andrew Moore's thesis. Numbered
    // comments are direct quotes from there.   NearestNeighborList solution
    // courtesy of Bjoern Heckel.
   protected static <T> void nnbr(KDNode<T> kd, HPoint target, HRect hr,
                              double max_dist_sqd, int lev, int K,
                              NearestNeighborList<KDNode<T>> nnl,
                              Checker<T> checker,
                              long timeout) {

       // 1. if kd is empty then set dist-sqd to infinity and exit.
       if (kd == null) {
           return;
       }

       if ((timeout > 0) && (timeout < System.currentTimeMillis())) {
           return;
       }
       // 2. s := split field of kd
       int s = lev % K;

       // 3. pivot := dom-elt field of kd
       HPoint pivot = kd.k;
       double pivot_to_target = HPoint.sqrdist(pivot, target);

       // 4. Cut hr into to sub-hyperrectangles left-hr and right-hr.
       //    The cut plane is through pivot and perpendicular to the s
       //    dimension.
       HRect left_hr = hr; // optimize by not cloning
       HRect right_hr = (HRect) hr.clone();
       left_hr.max.coord[s] = pivot.coord[s];
       right_hr.min.coord[s] = pivot.coord[s];

       // 5. target-in-left := target_s <= pivot_s
       boolean target_in_left = target.coord[s] < pivot.coord[s];

       KDNode<T> nearer_kd;
       HRect nearer_hr;
       KDNode<T> further_kd;
       HRect further_hr;

       // 6. if target-in-left then
       //    6.1. nearer-kd := left field of kd and nearer-hr := left-hr
       //    6.2. further-kd := right field of kd and further-hr := right-hr
       if (target_in_left) {
           nearer_kd = kd.left;
           nearer_hr = left_hr;
           further_kd = kd.right;
           further_hr = right_hr;
       }
       //
       // 7. if not target-in-left then
       //    7.1. nearer-kd := right field of kd and nearer-hr := right-hr
       //    7.2. further-kd := left field of kd and further-hr := left-hr
       else {
           nearer_kd = kd.right;
           nearer_hr = right_hr;
           further_kd = kd.left;
           further_hr = left_hr;
       }

       // 8. Recursively call Nearest Neighbor with paramters
       //    (nearer-kd, target, nearer-hr, max-dist-sqd), storing the
       //    results in nearest and dist-sqd
       nnbr(nearer_kd, target, nearer_hr, max_dist_sqd, lev + 1, K, nnl, checker, timeout);

       KDNode<T> nearest = nnl.getHighest();
       double dist_sqd;

       if (!nnl.isCapacityReached()) {
           dist_sqd = Double.MAX_VALUE;
       }
       else {
           dist_sqd = nnl.getMaxPriority();
       }

       // 9. max-dist-sqd := minimum of max-dist-sqd and dist-sqd
       max_dist_sqd = Math.min(max_dist_sqd, dist_sqd);

       // 10. A nearer point could only lie in further-kd if there were some
       //     part of further-hr within distance max-dist-sqd of
       //     target.  
       HPoint closest = further_hr.closest(target);
       if (HPoint.sqrdist(closest, target) < max_dist_sqd) {

           // 10.1 if (pivot-target)^2 < dist-sqd then
           if (pivot_to_target < dist_sqd) {

               // 10.1.1 nearest := (pivot, range-elt field of kd)
               nearest = kd;

               // 10.1.2 dist-sqd = (pivot-target)^2
               dist_sqd = pivot_to_target;

               // add to nnl
               if (!kd.deleted && ((checker == null) || checker.usable(kd.v))) {
                   nnl.insert(kd, dist_sqd);
               }

               // 10.1.3 max-dist-sqd = dist-sqd
               // max_dist_sqd = dist_sqd;
               if (nnl.isCapacityReached()) {
                   max_dist_sqd = nnl.getMaxPriority();
               }
               else {
                   max_dist_sqd = Double.MAX_VALUE;
               }
           }

           // 10.2 Recursively call Nearest Neighbor with parameters
           //      (further-kd, target, further-hr, max-dist_sqd),
           //      storing results in temp-nearest and temp-dist-sqd
           nnbr(further_kd, target, further_hr, max_dist_sqd, lev + 1, K, nnl, checker, timeout);
       }
   }


    // constructor is used only by class; other methods are static
    private KDNode(HPoint key, T val) {
	
	k = key;
	v = val;
	left = null;
	right = null;
	deleted = false;
    }

    protected String toString(int depth) {
	String s = k + "  " + v + (deleted ? "*" : "");
	if (left != null) {
	    s = s + "\n" + pad(depth) + "L " + left.toString(depth+1);
	}
	if (right != null) {
	    s = s + "\n" + pad(depth) + "R " + right.toString(depth+1);
	}
	return s;
    }

    private static String pad(int n) {
	String s = "";
	for (int i=0; i<n; ++i) {
	    s += " ";
	}
	return s;
    }

    private static void hrcopy(HRect hr_src, HRect hr_dst) {
	hpcopy(hr_src.min, hr_dst.min);
	hpcopy(hr_src.max, hr_dst.max);
    }

    private static void hpcopy(HPoint hp_src, HPoint hp_dst) {
	for (int i=0; i<hp_dst.coord.length; ++i) {
	    hp_dst.coord[i] = hp_src.coord[i];
	}
    }
}
