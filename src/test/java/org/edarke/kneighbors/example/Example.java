package org.edarke.kneighbors.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import org.edarke.kneighbors.classifiers.Match;
import org.edarke.kneighbors.classifiers.TestHelper;
import org.edarke.kneighbors.metrics.MoreMetrics;
import org.edarke.kneighbors.classifiers.VpTree;
import org.edarke.kneighbors.metrics.Metric;
import org.edarke.kneighbors.metrics.StringMetrics;

/**
 * Created by Evan on 2/14/17.
 */
public class Example {

    public static class Student {
        private final String firstName;
        private final int year;

        public Student(String first, int year) {
            firstName = first;
            this.year = year;
        }


        public static Metric<Student> metric =
                (s,t) -> StringMetrics.INSERTION_DISTANCE.distance(s.firstName, t.firstName)
                        + Math.abs(s.year - t.year);

        public static class StudentMetric implements Metric<Student> {

            @Override
            public int distance(Student x, Student y) {
                return 0;
            }
        }

    }


    public static void main(String[] args) {
        SpellChecker spellCheck = new SpellChecker(TestHelper.dataset);
        System.out.println(spellCheck.suggestCorrections("sujestion"));
        System.out.println(spellCheck.suggestCorrections("antydisestableshmunterianism"));

        List<DataPoint> points = new ArrayList<>();
        for(int i = 0; i < 100; i += 3) {
            for(int j = 1; j < 100; j += 2) {
                points.add(new DataPoint(i, j, ThreadLocalRandom.current().nextInt(10)));
            }
        }

        VpTree<DataPoint> tree = VpTree.create(points, (p1, p2) -> MoreMetrics.MANHATTAN_DISTANCE.distance(p1.point, p2.point));
        System.out.println(tree.getAllWithinRadius(new DataPoint(50, 49), 3));
        System.out.println(tree.getNearestNeighbors(new DataPoint(75, 50), 3));
    }


    public static class DataPoint {
        public static final int UNKNOWN = -1;
        private int[] point;
        private final int category;

        public DataPoint(int x, int y) {
            this(x,y,UNKNOWN);
        }

        public  DataPoint(int x, int y, int category) {
            point = new int[] {x, y};
            this.category = category;
        }

        @Override
        public String toString(){
            return String.format("DataPoint{x=%d, y=%d, category=%d}", point[0], point[1], category);
        }
    }



    public static class SpellChecker {
        private VpTree<String> words;

        public SpellChecker(List<String> dictionary) {
            this(dictionary, StringMetrics.LEVENSHTEIN_DISTANCE);
        }

        public SpellChecker(List<String> dictionary, Metric<? super String> metric) {
            words = VpTree.create(TestHelper.dataset, metric);

        }

        public boolean isWord(String word) {
            return words.getNearestNeighbors(word, 1).get(0).getValue().equals(word);
        }

        public List<String> suggestCorrections(String word) {
            return words.getNearestNeighbors(word, 3).stream().map(Match::getValue).collect(Collectors.toList());
        }

    }

}
