package org.edarke.kneighbors.classifiers;

import org.edarke.kneighbors.metrics.Metric;
import org.edarke.kneighbors.metrics.StringMetrics;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Evan on 2/5/17.
 */
public class DamareauTest extends MetricTestHelper {
    public DamareauTest() {
        super(StringMetrics.createDamerauLevenshteinMetric(1,1,1,1));
    }



    @Test
    public void test_distance() {
        Metric<CharSequence> m = StringMetrics.createDamerauLevenshteinMetric(1,1,1,1);
        assertEquals(1, m.distance("impossiblw", "impossibly"));
    }

    @Test
    public void test_distance4() {
        Metric<CharSequence> m = StringMetrics.createDamerauLevenshteinMetric(1,1,1,1);
        assertEquals(1, m.distance("impossiblw", "impossible"));
    }

    @Test
    public void test_distance2() {
        Metric<CharSequence> m = StringMetrics.createDamerauLevenshteinMetric(1,1,1,1);
        assertEquals(1, m.distance("cat", "Lat"));
    }

    @Test
    public void test_distance3() {
        Metric<CharSequence> m = StringMetrics.createDamerauLevenshteinMetric(1,1,1,1);
        assertEquals(1, m.distance("cat", "1cat"));
    }



    @Test
    public void test_cap() {
        Metric<CharSequence> m = StringMetrics.createDamerauLevenshteinMetric(1,2,1,1);
        assertEquals(0, m.distance("Cat", "Cat"));
        assertEquals(1, m.distance("Cat", "cat"));
        assertEquals(2, m.distance("cat", "bat"));
    }
}
