package org.edarke.kneighbors.classifiers;

import org.edarke.kneighbors.metrics.StringMetrics;

/**
 * Created by Evan on 2/5/17.
 */
public class LevenshteinTest extends MetricTestHelper {

    public LevenshteinTest() {
        super(StringMetrics.LEVENSHTEIN_DISTANCE);
    }

}
