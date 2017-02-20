package org.edarke.kneighbors.metrics;

/**
 * A utility class containing various definitions of edit distance between strings.
 * These can be used directly in KNN classifiers for Strings, wrapped in a new Metric
 * for objects with String fields.
 *
 * Created by Evan on 1/29/17.
 */
public enum StringMetrics {
    ;

    /** The number of indices at which the two strings differ. */
    public static Metric<CharSequence> HAMMING_DISTANCE = new Metric<CharSequence>() {
        @Override
        public int distance(CharSequence s1, CharSequence s2) {
            final int shorter = Math.min(s1.length(), s2.length());
            final int longest = Math.max(s1.length(), s2.length());

            int result = longest - shorter;
            for (int i = 0; i < shorter; ++i) {
                if (s1.charAt(i) != s2.charAt(i)) ++result;
            }

            return result;
        }
    };


    /**
     * Computes the minimum number of edits needed to change string s into t
     * where each edit is either an insert or deletion. Similar to Levenshtein distance
     * except replacements have a cost of 2 (ie 1 insert + 1 delete).
     */
    public static Metric<CharSequence> INSERTION_DISTANCE_WITH_CAPITALIZATION = new Metric<CharSequence>() {
        @Override
        public int distance(CharSequence s, CharSequence t) {
            // degenerate cases
            if (s.equals(t)) return 0;
            if (s.length() == 0) return t.length();
            if (t.length() == 0) return s.length();

            // create two work vectors of integer distances
            int[] v0 = new int[t.length() + 1];
            int[] v1 = new int[t.length() + 1];

            // initialize v0 (the previous row of distances)
            // this row is A[0][i]: edit distance for an empty s
            // the distance is just the number of characters to delete from t
            for (int i = 0; i < v0.length; ++i)
                v0[i] = i;

            for (int i = 0; i < s.length(); ++i) {
                // calculate v1 (current row distances) from the previous row v0

                // first element of v1 is A[i+1][0]
                //   edit distance is delete (i+1) chars from s to match empty t
                v1[0] = i + 1;

                // use formula to fill in the rest of the row
                for (int j = 0; j < t.length(); ++j) {
                    final int cost = (s.charAt(i) == t.charAt(j)) ? v0[j] :
                            (Character.toLowerCase(s.charAt(i)) == Character.toLowerCase(t.charAt(j)))? v0[j]+1: Integer.MAX_VALUE;
                    v1[j + 1] = Math.min(Math.min(v1[j] + 1, v0[j + 1] + 1), cost);
                }

                // copy v1 (current row) to v0 (previous row) for next iteration
                System.arraycopy(v1, 0, v0, 0, v0.length);
            }

            return v1[t.length()];
        }
    };




    /**
     * Computes the minimum number of edits needed to change string s into t
     * where each edit is either an insert or deletion. Similar to Levenshtein distance
     * except replacements have a cost of 2 (ie 1 insert + 1 delete).
     */
    public static Metric<CharSequence> INSERTION_DISTANCE = new Metric<CharSequence>() {
        @Override
        public int distance(CharSequence s, CharSequence t) {
            // degenerate cases
            if (s.equals(t)) return 0;
            if (s.length() == 0) return t.length();
            if (t.length() == 0) return s.length();

            // create two work vectors of integer distances
            int[] v0 = new int[t.length() + 1];
            int[] v1 = new int[t.length() + 1];

            // initialize v0 (the previous row of distances)
            // this row is A[0][i]: edit distance for an empty s
            // the distance is just the number of characters to delete from t
            for (int i = 0; i < v0.length; ++i)
                v0[i] = i;

            for (int i = 0; i < s.length(); ++i) {
                // calculate v1 (current row distances) from the previous row v0

                // first element of v1 is A[i+1][0]
                //   edit distance is delete (i+1) chars from s to match empty t
                v1[0] = i + 1;

                // use formula to fill in the rest of the row
                for (int j = 0; j < t.length(); ++j) {
                    final int cost = (s.charAt(i) == t.charAt(j)) ? v0[j] : Integer.MAX_VALUE;
                    v1[j + 1] = Math.min(Math.min(v1[j] + 1, v0[j + 1] + 1), cost);
                }

                // copy v1 (current row) to v0 (previous row) for next iteration
                System.arraycopy(v1, 0, v0, 0, v0.length);
            }

            return v1[t.length()];
        }
    };



    /**
     * Computes the minimum number of edits needed to change string s into t
     * where each edit is either an insert, deletion, or replacement.
     * Implementation is based on https://en.wikipedia.org/wiki/Levenshtein_distance#Iterative_with_two_matrix_rows
     */
    public static Metric<CharSequence> LEVENSHTEIN_DISTANCE = new Metric<CharSequence>() {
        @Override
        public int distance(CharSequence s, CharSequence t) {
            // degenerate cases
            if (s.equals(t)) return 0;
            if (s.length() == 0) return t.length();
            if (t.length() == 0) return s.length();

            // create two work vectors of integer distances
            int[] v0 = new int[t.length() + 1];
            int[] v1 = new int[t.length() + 1];

            // initialize v0 (the previous row of distances)
            // this row is A[0][i]: edit distance for an empty s
            // the distance is just the number of characters to delete from t
            for (int i = 0; i < v0.length; ++i)
                v0[i] = i;

            for (int i = 0; i < s.length(); ++i) {
                // calculate v1 (current row distances) from the previous row v0

                // first element of v1 is A[i+1][0]
                //   edit distance is delete (i+1) chars from s to match empty t
                v1[0] = i + 1;

                // use formula to fill in the rest of the row
                for (int j = 0; j < t.length(); ++j) {
                    final int cost = (s.charAt(i) == t.charAt(j)) ? 0 : 1;
                    v1[j + 1] = Math.min(Math.min(v1[j] + 1, v0[j + 1] + 1), v0[j] + cost);
                }

                // copy v1 (current row) to v0 (previous row) for next iteration
                System.arraycopy(v1, 0, v0, 0, v0.length);
            }

            return v1[t.length()];
        }
    };


    /** Similar to Levenshtein distance, but allows two adjacent characters to be swapped with a cost of 1. */
    public static Metric<CharSequence> DAMERAU_LEVENSHTEIN = createDamerauLevenshteinMetric(1,1,1,1);

    /** Creates a version of Damerau-Levenshtein distance with different weights for insert, replace, capitalization, and transposition*/
    public static Metric<CharSequence> createDamerauLevenshteinMetric(int insertCost, int replaceCost, int capitalizeCost, int transpose) {
        return new DamerauLevenshteinAlgorithm(insertCost, replaceCost, capitalizeCost, transpose);
    }
}
