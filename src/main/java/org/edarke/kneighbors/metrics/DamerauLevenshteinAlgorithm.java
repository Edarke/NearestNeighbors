package org.edarke.kneighbors.metrics;

import net.openhft.koloboke.collect.map.CharIntMap;
import net.openhft.koloboke.collect.map.hash.HashCharIntMaps;

/* Copyright (c) 2012 Kevin L. Stern
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

/**
 * The Damerau-Levenshtein Algorithm is an extension to the Levenshtein
 * Algorithm which solves the edit distance problem between a source string and
 * a target string with the following operations:
 *
 * <ul>
 * <li>Character Insertion</li>
 * <li>Character Deletion</li>
 * <li>Character Replacement</li>
 * <li>Adjacent Character Swap</li>
 * </ul>
 *
 * Note that the adjacent character swap operation is an edit that may be
 * applied when two adjacent characters in the source string match two adjacent
 * characters in the target string, but in reverse order, rather than a general
 * allowance for adjacent character swaps.
 * <p>
 *
 * This implementation allows the client to specify the costs of the various
 * edit operations with the restriction that the cost of two swap operations
 * must not be less than the cost of a delete operation followed by an insert
 * operation. This restriction is required to preclude two swaps involving the
 * same character being required for optimality which, in turn, enables a fast
 * dynamic programming solution.
 * <p>
 *
 * The running time of the Damerau-Levenshtein algorithm is O(n*m) where n is
 * the length of the source string and m is the length of the target string.
 * This implementation consumes O(n*m) space.
 *
 * @author Kevin L. Stern
 */
public class DamerauLevenshteinAlgorithm implements Metric<CharSequence> {
    private final int insertCost, replaceCost, capitalizeCost, swapCost;

    /**
     * Constructor.
     *
     * @param insertCost
     *          the cost of inserting a character.
     * @param replaceCost
     *          the cost of replacing a character.
     * @param swapCost
     *          the cost of swapping two adjacent characters.
     */
    public DamerauLevenshteinAlgorithm(int insertCost,
                                       int replaceCost, int capitalizeCost,
                                       int swapCost) {
    /*
     * Required to facilitate the premise to the algorithm that two swaps of the
     * same character are never required for optimality.
     */
        if (swapCost < insertCost) {
            throw new IllegalArgumentException("Unsupported cost assignment");
        }
        this.insertCost = insertCost;
        this.replaceCost = replaceCost;
        this.swapCost = swapCost;
        this.capitalizeCost = capitalizeCost;
    }



    /**
     * Compute the Damerau-Levenshtein distance between the specified source
     * string and the specified target string.
     */
    public int distance(CharSequence s1, CharSequence s2) {
        if (s1.length() == 0) {
            return s2.length() * insertCost;
        }
        if (s2.length() == 0) {
            return s1.length() * insertCost;
        }

        // INFinite distance is the max possible distance
        final int inf = s1.length() + s2.length();

        // Create and initialize the character array indices
        final CharIntMap da = HashCharIntMaps.newMutableMap();

        for (int d = 0; d < s1.length(); ++d) {
            da.put(s1.charAt(d), Integer.MIN_VALUE);
        }

        for (int d = 0; d < s2.length(); ++d) {
            da.put(s2.charAt(d), Integer.MIN_VALUE);
        }

        // Create the distance matrix H[0 .. s1.length+1][0 .. s2.length+1]
        int[][] h = new int[s1.length() + 2][s2.length() + 2];

        // initialize the left and top edges of H
        for (int i = 0; i <= s1.length(); ++i) {
            h[i + 1][0] = inf;
            h[i + 1][1] = i*insertCost;
        }


        for (int j = 0; j <= s2.length(); ++j) {
            h[0][j + 1] = inf;
            h[1][j + 1] = j*insertCost;
        }

        // fill in the distance matrix H
        // look at each character in s1
        for (int i = 1; i <= s1.length(); ++i) {
            final char char1 = s1.charAt(i - 1);
            int db = 0;

            // look at each character in b
            for (int j = 1; j <= s2.length(); ++j) {
                final char char2 = s2.charAt(j - 1);
                final int i1 = da.get(char2);
                final int j1 = db;
                final int swap = i1 < 0 ? Integer.MAX_VALUE : h[i1][j1] + (i - i1 - 1) + swapCost + (j - j1 - 1);


                final int cost;
                if (char1 == char2) {
                    cost = 0;
                    db = j;
                } else if (Character.toLowerCase(char1) == Character.toLowerCase(char2)) {
                    cost = capitalizeCost;
                } else {
                    cost = replaceCost;
                }


                h[i + 1][j + 1] = min(
                        h[i][j] + cost,           // substitution
                        h[i + 1][j] + insertCost, // insertion
                        h[i][j + 1] + insertCost, // deletion
                        swap);
            }

            da.put(char1, i);
        }

        return h[s1.length() + 1][s2.length() + 1];
    }

    private static int min(
            final int a, final int b, final int c, final int d) {
        return Math.min(a, Math.min(b, Math.min(c, d)));
    }

    private int replaceCost(char source, char target) {
        if (source == target) return 0;
        if (Character.toLowerCase(source) == Character.toLowerCase(target)) return capitalizeCost;
        return replaceCost;
    }
}
