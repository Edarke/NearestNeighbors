package org.edarke.kneighbors.classifiers;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


/**
 * Created by Evan on 2/5/17.
 */
public abstract class TestHelper {

    public static List<String> dataset;
    public static List<String> smallDataset;

    static {
        try {
            dataset = Files.readAllLines(Paths.get("/usr/share/dict/words"), Charset.defaultCharset());
            dataset = new ArrayList<>(new HashSet<>(dataset));
            smallDataset = Files.readAllLines(Paths.get(TestHelper.class.getResource("/uniq.txt").toURI()));
            System.out.println("File Size is " + dataset.size());
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }



}
