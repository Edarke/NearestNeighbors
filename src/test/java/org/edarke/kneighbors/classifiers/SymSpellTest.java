package org.edarke.kneighbors.classifiers;

import org.edarke.kneighbors.metrics.StringMetrics;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by Evan on 2/16/17.
 */
public class SymSpellTest {

    SymSpell<String> sym = new SymSpell<>(3, StringMetrics.DAMERAU_LEVENSHTEIN, SymSpell.LookupType.ALL_WITHIN_RANGE);


    public SymSpellTest() {
        for (String s: TestHelper.smallDataset) {
           sym.put(s, s);
        }
    }

    @Test
    public void testSymSpell(){
        assertTrue(sym.lookup("sujestion", 3).stream().anyMatch(m -> m.getValue().equals("suggestion") && m.getDistance() == 2));
        assertTrue(sym.lookup("conputer", 3).stream().anyMatch(m -> m.getValue().equals("computer") && m.getDistance() == 1));
        assertTrue(sym.lookup("wosh", 1).stream().anyMatch(m -> m.getValue().equals("wash") && m.getDistance() == 1));
        assertTrue(sym.lookup("antidisestableshmentarianysm", 3).stream().anyMatch(m -> m.getValue().equals("antidisestablishmentarianism") && m.getDistance() == 2));
    }


}
