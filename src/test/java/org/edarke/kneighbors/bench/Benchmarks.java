package org.edarke.kneighbors.bench;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 * Created by Evan on 2/8/17.
 */
public class Benchmarks {

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(TestMetrics.class.getSimpleName())
            //    .include(Test100k.class.getSimpleName())
                .forks(1)
                .build();
        new Runner(opt).run();
    }



}
