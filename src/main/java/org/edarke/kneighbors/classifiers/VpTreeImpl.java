package org.edarke.kneighbors.classifiers;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import org.eclipse.jdt.annotation.Nullable;
import org.edarke.kneighbors.metrics.Metric;

/**
 * Created by Evan on 2/5/17.
 */

public class VpTreeImpl<T> implements VpTree<T> {

    private final T vantagePoint;
    private final int threshold;
    private final Metric<? super T> metric;
    private final @Nullable VpTreeImpl<T> left, right;
    private final int size;

    private VpTreeImpl(int threshold, @Nullable VpTreeImpl<T> left, @Nullable VpTreeImpl<T> right, Metric<? super T> metric, T vp) {
        this.threshold = threshold;
        this.left = left;
        this.right = right;
        this.metric = metric;
        this.vantagePoint = vp;
        this.size = 1 + (left != null? left.size(): 0) + (right != null? right.size(): 0);
    }

    @Override
    public int size(){
        return size;
    }


    public List<Match<T>> getNearestNeighbors(T sample, int k) {
        return getNearestNeighbors(sample, k, Integer.MAX_VALUE);
    }


    public List<Match<T>> getAllWithinRadius(T sample, int maxDistance) {
        List<Match<T>> neighbors = new ArrayList<>();

        Deque<VpTree<T>> nodesToVisit = new ArrayDeque<>();
        nodesToVisit.addLast(this);
        do {
            VpTree<T> next1 = nodesToVisit.removeLast();
            if (!(next1 instanceof VpTreeImpl)) continue;
            VpTreeImpl<T> next = (VpTreeImpl<T>) next1;
            final int dist = metric.distance(next.vantagePoint, sample);


            if (dist <= maxDistance) {
                neighbors.add(new Match<>(next.vantagePoint, dist));
            }

            if (dist - next.threshold <= maxDistance) {
                if (next.left != null)
                    nodesToVisit.addLast(next.left);
            }
            if (next.threshold - dist < maxDistance) {
                if (next.right != null)
                    nodesToVisit.addLast(next.right);
            }

        } while (!nodesToVisit.isEmpty());
        neighbors.sort(Comparator.naturalOrder());
        return neighbors;
    }



    public List<Match<T>> getNearestNeighbors(T sample, int k, int maxDistance) {
        List<Match<T>> neighbors = new ArrayList<Match<T>>() {
            @Override
            public boolean add(Match<T> match) {
                if (this.size() < k) {
                    return super.add(match);
                } else if (this.get(this.size() - 1).getDistance() > match.getDistance()) {
                    this.set(this.size() - 1, match);
                    Collections.sort(this);
                    return true;
                } else {
                    return false;
                }
            }
        };

        Deque<VpTreeImpl<T>> nodesToVisit = new ArrayDeque<>();
        nodesToVisit.addLast(this);
        while (!nodesToVisit.isEmpty()) {
            VpTreeImpl<T> next = nodesToVisit.removeLast();
            int dist = metric.distance(next.vantagePoint, sample);


            if (dist < maxDistance) {
                if(neighbors.add(new Match<>(next.vantagePoint, dist)) && neighbors.size() == k)
                    maxDistance = neighbors.get(neighbors.size() - 1).getDistance();
            }

            if (dist - next.threshold <= maxDistance) {
                if (next.left != null)
                    nodesToVisit.addLast(next.left);
            }
            if (next.threshold - dist <= maxDistance) {
                if(next.right != null)
                    nodesToVisit.addLast(next.right);
            }
        }
        neighbors.sort(Comparator.naturalOrder());
        return neighbors;
    }



    static <T> VpTreeImpl<T> createTree(List<T> samples, Metric<? super T> metric) {
        if(samples.size() == 0) {
            return null;
        }

        final int pivotIndex = ThreadLocalRandom.current().nextInt(samples.size());
        final T pivot = samples.get(pivotIndex);

        List<Match<T>> measures = samples.stream().map(s -> new Match<>(s, metric.distance(pivot, s))).sorted().collect(Collectors.toList());
        int medianDistance;


        int lastDistance = measures.get(0).getDistance();
        int bestSplitIndex = 0;
        for (int i = 1; i < measures.size(); ++i) {
            if (measures.get(i).getDistance() != lastDistance) {
                if (Math.abs(measures.size()/2 - bestSplitIndex) >= Math.abs(measures.size()/2 - (i-1))) {
                    bestSplitIndex = i - 1;
                } else {
                    break;
                }
                lastDistance = measures.get(i).getDistance();
            }
        }
        medianDistance = measures.get(bestSplitIndex).getDistance();


        List<T> left = new ArrayList<>(bestSplitIndex + 1);
        List<T> right = new ArrayList<>(measures.size() - (bestSplitIndex + 1));

        for (Match<T> m: measures) {
            if (m.getDistance() > medianDistance) {
                right.add(m.getValue());
            } else if (m.getValue() != pivot){
                left.add(m.getValue());
            }
        }

        return new VpTreeImpl<>(medianDistance, createTree(left, metric), createTree(right, metric), metric, pivot);
    }



    static class EmptyVpTree implements VpTree {

        @Override
        public List<Match> getNearestNeighbors(Object sample, int k) {
            return Collections.emptyList();
        }

        @Override
        public List<Match> getNearestNeighbors(Object sample, int k, int maxDistance) {
            return Collections.emptyList();
        }

        @Override
        public List<Match> getAllWithinRadius(Object sample, int maxDistance) {
            return Collections.emptyList();
        }

        @Override
        public int size() {
            return 0;
        }
    }

}
