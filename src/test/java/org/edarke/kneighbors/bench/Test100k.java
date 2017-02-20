package org.edarke.kneighbors.bench;

import com.eatthepath.jvptree.DistanceFunction;
import com.eatthepath.jvptree.VPTree;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import org.edarke.kneighbors.classifiers.BkTree;
import org.edarke.kneighbors.classifiers.VpTree;
import org.edarke.kneighbors.metrics.StringMetrics;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;


/**
 * Created by Evan on 2/8/17.
 */
public class Test100k {


    @State(Scope.Benchmark)
    public static class MyState {

        public List<String> dataset;
        public List<String> testset = Arrays.asList("cat", "univrse", "benchmarks", "Optimisations");


        VpTree<String> mine;
        VPTree<String> jvpt;
        BkTree<String> bk;

        @Setup(Level.Invocation)
        public void doSetup(){
            try {
                dataset = Files.readAllLines(Paths.get("/usr/share/dict/words"), Charset.defaultCharset());
                dataset = new ArrayList<>(new HashSet<>(dataset));
                mine = VpTree.create(dataset, StringMetrics.LEVENSHTEIN_DISTANCE);
                jvpt = new VPTree<>(new JvpDistance(), dataset);
                bk = new BkTree<>(StringMetrics.LEVENSHTEIN_DISTANCE, dataset.get(0));
                dataset.forEach(bk::add);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    public void mineRadius3(MyState state) {
        for (String s : state.testset)
            state.mine.getAllWithinRadius(s, 3);
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    public void jvptreeRadius3(MyState state) {
        for (String s : state.testset)
            state.jvpt.getAllWithinDistance(s, 3.0);
    }


    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    public void bktreeRadius3(MyState state) {
        for (String s : state.testset)
            state.bk.getAllWithinRadius(s, 3);
    }


    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    public void mineRadius7(MyState state) {
        for (String s : state.testset)
            state.mine.getAllWithinRadius(s, 7);
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    public void jvptreeRadius7(MyState state) {
        for (String s : state.testset)
            state.jvpt.getAllWithinDistance(s, 7.0);
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    public void bktreeRadius7(MyState state) {
        for (String s : state.testset)
            state.bk.getAllWithinRadius(s, 7);
    }


    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    public void newVpTree(MyState state) {
        VpTree<String> vptree = VpTree.create(state.dataset, StringMetrics.LEVENSHTEIN_DISTANCE);
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    public void newJVPTree(MyState state) {
        VPTree<String> vptree = new VPTree<String>(new JvpDistance(), state.dataset);
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    public void newBkTree(MyState state) {
        BkTree<String> tree = new BkTree<>(StringMetrics.LEVENSHTEIN_DISTANCE, state.dataset.get(0));
        for (String s: state.dataset) {
            tree.add(s);
        }
    }

    public static class JvpDistance implements DistanceFunction<String> {

        @Override
        public double getDistance(String s, String t1) {
            return StringMetrics.LEVENSHTEIN_DISTANCE.distance(s, t1);
        }
    }
}
