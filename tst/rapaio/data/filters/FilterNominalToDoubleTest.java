/*
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 *
 *    Copyright 2013 Aurelian Tutuianu
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
 */

package rapaio.data.filters;

import org.junit.Test;
import rapaio.data.Nominal;
import rapaio.data.Vector;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static rapaio.data.filters.BaseFilters.sort;
import static rapaio.data.filters.BaseFilters.toNumeric;

/**
 * @author <a href="mailto:padreati@yahoo.com">Aurelian Tutuianu</a>
 */
public class FilterNominalToDoubleTest {

    @Test
    public void testNormalCase() {
        int n = 10;
        HashSet<String> dict = new HashSet<>();
        for (int i = 0; i < n; i++) {
            dict.add(String.valueOf(Math.pow(i, 1.5)));
        }
        Vector v = new Nominal(10, dict);
        for (int i = 0; i < v.rowCount(); i++) {
            String value = String.valueOf(Math.pow(i, 1.5));
            v.setLabel(i, value);
        }
        Vector filtered = toNumeric(v);
        for (int i = 0; i < v.rowCount(); i++) {
            double value = Math.pow(i, 1.5);
            assertEquals(value, filtered.value(i), 1e-10);
        }
    }

    @Test
    public void testNullVector() {
        try {
            sort(null);
        } catch (Exception ex) {
            assertTrue(true);
        }
    }

    @Test
    public void testNFE() {
        Vector filtered = new Nominal(1, Arrays.asList(new String[]{"abc"}));
        filtered.setLabel(0, "abc");
        Vector numeric = toNumeric(filtered);
        assertEquals(numeric.value(0), numeric.value(0), 1e-10);
        assertTrue(numeric.missing(0));
    }
}
