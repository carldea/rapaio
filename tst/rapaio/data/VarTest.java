/*
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 *
 *    Copyright 2013 Aurelian Tutuianu
 *    Copyright 2014 Aurelian Tutuianu
 *    Copyright 2015 Aurelian Tutuianu
 *    Copyright 2016 Aurelian Tutuianu
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 */

package rapaio.data;

import org.junit.Test;
import rapaio.data.filter.var.VFSort;
import rapaio.data.solid.SolidVarDouble;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

/**
 * Created by <a href="mailto:padreati@yahoo.com">Aurelian Tutuianu</a>.
 */
public class VarTest {

    @Test
    public void testDictionary() {
        Var x = VarNominal.copy("x", "y", "x", "z");
        Var y = VarNominal.copy("x", "y", "x", "z");

        x.setLevels("a", "b", "c");
        List<String> dict = new ArrayList<>();
        dict.add("a");
        dict.add("b");
        dict.add("c");
        y.setLevels(dict);

        assertEquals(4, x.rowCount());
        assertEquals(4, y.rowCount());

        for (int i = 0; i < 4; i++) {
            assertEquals(x.getLabel(i), y.getLabel(i));
        }
    }

    @Test
    public void testNumericCollector() {
        double[] src = IntStream.range(0, 100_000).mapToDouble(x -> x).toArray();
        Var x = SolidVarDouble.wrap(src);
        Var y = Arrays.stream(src).boxed().parallel().collect(SolidVarDouble.collector());
        y = new VFSort().fitApply(y);

        assertTrue(x.deepEquals(y));
    }

    @Test
    public void testIndexCollector() {
        int[] src = IntStream.range(0, 100_000).toArray();
        Var x = VarInt.wrap(src);
        Var y = Arrays.stream(src).boxed().parallel().collect(VarInt.collector());
        y = new VFSort().fitApply(y);

        assertTrue(x.deepEquals(y));
    }

    @Test
    public void solidCopyNameTest() {
        VarDouble num = SolidVarDouble.seq(1, 10, 0.5).withName("num");
        assertEquals(num.name(), num.solidCopy().name());
        assertEquals(num.name(), num.mapRows(2,5).solidCopy().name());

        VarInt idx = VarInt.seq(1, 10).withName("idx");
        assertEquals(idx.name(), idx.solidCopy().name());
        assertEquals(idx.name(), idx.mapRows(2,5).solidCopy().name());

        VarBoolean bin = VarBoolean.copy(true, false, true, false, true).withName("bin");
        assertEquals(bin.name(), bin.solidCopy().name());
        assertEquals(bin.name(), bin.mapRows(2,5).solidCopy().name());

        VarOrdinal ord = VarOrdinal.empty(0, "a", "b", "c").withName("ord");
        ord.addLabel("a");
        ord.addLabel("b");
        ord.addLabel("a");
        ord.addLabel("c");
        ord.addLabel("a");
        assertEquals(ord.name(), ord.solidCopy().name());
        assertEquals(ord.name(), ord.mapRows(2,4).solidCopy().name());

        VarNominal nom = VarNominal.copy("a", "b", "a", "c", "a").withName("nom");
        assertEquals(nom.name(), nom.solidCopy().name());
        assertEquals(nom.name(), nom.mapRows(2,4).solidCopy().name());

        VarLong stp = VarLong.seq(1, 10).withName("stamp");
        assertEquals(stp.name(), stp.solidCopy().name());
        assertEquals(stp.name(), stp.mapRows(2,5).solidCopy().name());

    }
}
