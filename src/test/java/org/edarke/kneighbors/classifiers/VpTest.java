package org.edarke.kneighbors.classifiers;

import java.util.Collections;
import java.util.List;
import org.edarke.kneighbors.metrics.StringMetrics;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by Evan on 2/5/17.
 */
public class VpTest {



    @Test
    public void test3NN_empty(){
        VpTree<String> empty = VpTree.create(Collections.emptyList(), StringMetrics.HAMMING_DISTANCE);

        List<Match<String>> matches = empty.getNearestNeighbors("impossiblw", 3);
        assertTrue(matches.isEmpty());
    }


}
