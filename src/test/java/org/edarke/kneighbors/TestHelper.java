package org.edarke.kneighbors;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static com.google.common.io.Files.readLines;

/**
 * Created by Evan on 2/5/17.
 */
public abstract class TestHelper {

    public static List<String> dataset;

    static {
        try {
            dataset = readLines(new File("/usr/share/dict/words"), Charset.defaultCharset());
            dataset = new ArrayList<>(new HashSet<>(dataset));
            System.out.println("File Size is " + dataset.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
