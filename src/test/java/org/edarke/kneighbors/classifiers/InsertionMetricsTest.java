package org.edarke.kneighbors.classifiers;

import org.edarke.kneighbors.metrics.StringMetrics;

/**
 * Created by Evan on 2/5/17.
 */
public class InsertionMetricsTest extends MetricTestHelper {
    public InsertionMetricsTest() {
        super(StringMetrics.INSERTION_DISTANCE);
    }
}
