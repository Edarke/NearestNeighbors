package org.edarke.kneighbors.bench;

import org.edarke.kneighbors.metrics.Metric;
import org.edarke.kneighbors.metrics.StringMetrics;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.annotations.Mode;

/**
 * Created by Evan on 2/19/17.
 */
public class TestMetrics {

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public void mine() {
        Metric<CharSequence> metric = StringMetrics.createDamerauLevenshteinMetric(1,1,1,1);
        metric.distance("abcdefghijklmnopqrstuvwxyz1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789",
                "abcdefghijklmonpqrstuvwxyz012345789AbcdefghijklmnoqprstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890");
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public void myShort() {
        Metric<CharSequence> metric = StringMetrics.createDamerauLevenshteinMetric(1,1,1,1);
        metric.distance("hello", "jello");
    }
}
