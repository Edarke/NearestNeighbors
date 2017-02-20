package org.edarke.kneighbors;

import org.edarke.kneighbors.classifiers.BkTree;
import org.edarke.kneighbors.classifiers.Match;
import org.edarke.kneighbors.metrics.StringMetrics;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Evan on 2/5/17.
 */
public class BkTreeTest extends TestHelper {

    BkTree<String> bkt = new BkTree<>(StringMetrics.HAMMING_DISTANCE, dataset.get(0));
    {
        dataset.subList(1, dataset.size()).forEach(bkt::add);
    }




    @Test
    public void test3NN(){
        Queue<Match<String>> matches = bkt.getAllWithinRadius("impossiblw", 3);
        System.out.println(matches);

        assertTrue(matches.removeIf(m -> m.getValue().equals("impossible")));
        assertTrue(matches.removeIf(m -> m.getValue().equals("impossibly")));
    }



    @Test
    public void test_cat(){
        Queue<Match<String>> matches = bkt.getAllWithinRadius("cat", 150);

        System.out.println(matches);

        assertFalse(matches.isEmpty());
        assertEquals(matches.peek().getValue(), "cat");
        assertEquals(matches.peek().getDistance(), 0);


        List<String> values = matches.stream().map(Match::getValue).collect(Collectors.toList());
        assertTrue(values.contains("sat"));
        assertTrue(values.contains("bat"));
        assertTrue(values.contains("Cat"));
        assertTrue(values.contains("pat"));
        assertTrue(values.contains("fat"));
        assertTrue(values.contains("hat"));
        assertTrue(values.contains("mat"));
        assertTrue(values.contains("rat"));
        assertTrue(values.contains("vat"));
        assertTrue(values.contains("cut"));
        assertTrue(values.contains("cab"));
        assertTrue(values.contains("cot"));
        assertTrue(values.contains("car"));
        assertTrue(values.contains("cap"));
    }



}
