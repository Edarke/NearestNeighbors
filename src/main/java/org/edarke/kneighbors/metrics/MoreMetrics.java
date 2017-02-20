package org.edarke.kneighbors.metrics;

import org.edarke.kneighbors.metrics.Metric;

/**
 * Created by Evan on 2/15/17.
 */
public enum MoreMetrics {
    ;
    /** Manhattan distance (aka Taxi-cab distance, rectilinear distance, or L1 Norm)*/
    public static Metric<int[]> MANHATTAN_DISTANCE = new Metric<int[]>() {
        @Override
        public int distance(int[] x, int[] y) {
            int sum = 0;
            for (int i = 0; i < x.length; ++i){
                sum += Math.abs(x[i] - y[i]);
            }
            return sum;
        }
    };

    /**The Chebyshev, or infinity-norm, distance between two points. */
    public static Metric<int[]> CHEBYSHEV_DISTANCE = new Metric<int[]>() {
        @Override
        public int distance(int[] x, int[] y) {
            int max = 0;
            for (int i = 0; i < x.length; ++i){
                max = Math.max(max, Math.abs(x[i] - y[i]));
            }
            return max;
        }
    };
}
