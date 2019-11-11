/*
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 *
 *    Copyright 2013 Aurelian Tutuianu
 *    Copyright 2014 Aurelian Tutuianu
 *    Copyright 2015 Aurelian Tutuianu
 *    Copyright 2016 Aurelian Tutuianu
 *    Copyright 2017 Aurelian Tutuianu
 *    Copyright 2018 Aurelian Tutuianu
 *    Copyright 2019 Aurelian Tutuianu
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

package rapaio.experiment.ml.classifier.svm.frame;

import rapaio.data.Frame;
import rapaio.data.VarDouble;
import rapaio.data.VarInt;
import rapaio.util.collection.IntIterator;

import java.util.HashMap;

/**
 * Created by <a href="mailto:padreati@yahoo.com">Aurelian Tutuianu</a> on 7/16/18.
 */
public class SparseRowValues implements RowValues {

    private final VarDouble values = VarDouble.empty();
    private final VarInt indexes = VarInt.empty();
    final HashMap<Integer, Integer> reverseMap = new HashMap<>();

    public SparseRowValues(Frame df, int row, String[] inputVarNames) {
        int pos = 0;
        for (int i=0; i<inputVarNames.length; i++) {
            double value = df.getDouble(row, i);
            if (Double.isNaN(value)) {
                throw new IllegalArgumentException("Does not allow missing values.");
            }
            if (value == 0) {
                continue;
            }
            values.addDouble(value);
            indexes.addInt(pos);
            reverseMap.put(i, pos);
            pos++;
        }
    }

    @Override
    public double get(int i) {
        if(!reverseMap.containsValue(i)) {
            return 0;
        }
        return values.getDouble(reverseMap.get(i));
    }

    @Override
    public IntIterator iterator() {
        return indexes.iterator();
    }
}
