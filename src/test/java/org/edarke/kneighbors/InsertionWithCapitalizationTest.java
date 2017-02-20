package org.edarke.kneighbors;

import org.edarke.kneighbors.metrics.StringMetrics;

/**
 * Created by Evan on 2/5/17.
 */
public class InsertionWithCapitalizationTest extends MetricTestHelper {
    public InsertionWithCapitalizationTest() {
        super(StringMetrics.INSERTION_DISTANCE_WITH_CAPITALIZATION);
    }
}
