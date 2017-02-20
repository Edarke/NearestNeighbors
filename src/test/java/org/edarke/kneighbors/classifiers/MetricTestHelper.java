package org.edarke.kneighbors.classifiers;

import java.util.List;
import java.util.stream.Collectors;
import org.edarke.kneighbors.metrics.Metric;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Evan on 2/5/17.
 */
public abstract class MetricTestHelper extends TestHelper {


    VpTree<String> vpt;

    protected MetricTestHelper(Metric<? super String> metric) {
        vpt = VpTree.create(dataset, metric);
        System.out.println(this.getClass() + " size: " + vpt.size());
    }


    @Test
    public void test_dataset_size_preserved(){
        assertEquals(dataset.size(), vpt.size());
    }


    @Test
    public void test3NN(){
        List<Match<String>> matches = vpt.getNearestNeighbors("impossiblw", 3);
        assertEquals(3, matches.size());

        assertTrue(matches.removeIf(m -> m.getValue().equals("impossible")));
        assertTrue(matches.removeIf(m -> m.getValue().equals("impossibly")));
    }



    @Test
    public void test_cat(){
        List<Match<String>> matches = vpt.getNearestNeighbors("cat", 500);
        assertFalse(matches.isEmpty());
        assertEquals("cat", matches.get(0).getValue());
        assertEquals(0, matches.get(0).getDistance());


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
