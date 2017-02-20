package org.edarke.kneighbors.classifiers;

import java.util.List;
import org.edarke.kneighbors.metrics.Metric;

/**
 * Created by Evan on 2/5/17.
 */
public interface VpTree<T> {

    @SuppressWarnings("unchecked")
    static <T> VpTree<T> create(List<T> examples, Metric<? super T> metric) {
        if (examples.isEmpty()) {
            return new VpTreeImpl.EmptyVpTree();
        } else {
            return VpTreeImpl.createTree(examples, metric);
        }
    }

    List<Match<T>> getNearestNeighbors(T sample, int k);

    List<Match<T>> getNearestNeighbors(T sample, int k, int maxDistance) ;

    List<Match<T>> getAllWithinRadius(T sample, int maxDistance);

    int size();

}
