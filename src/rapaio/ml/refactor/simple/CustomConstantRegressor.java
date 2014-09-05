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

package rapaio.ml.refactor.simple;

import rapaio.data.VarRange;
import rapaio.data.Frame;
import rapaio.data.Numeric;
import rapaio.data.SolidFrame;
import rapaio.data.Var;
import rapaio.ml.regressor.Regressor;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Aurelian Tutuianu <padreati@yahoo.com>
 */
@Deprecated
public class CustomConstantRegressor implements Regressor {

    List<String> targets;
    double customValue;
    List<Var> fitValues;

    @Override
    public Regressor newInstance() {
        return new L2ConstantRegressor();
    }

    public double getCustomValue() {
        return customValue;
    }

    public CustomConstantRegressor setCustomValue(double customValue) {
        this.customValue = customValue;
        return this;
    }

    @Override
    public void learn(Frame df, List<Double> weights, String targetColName) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void learn(Frame df, String targetCols) {
        VarRange varRange = new VarRange(targetCols);
        List<Integer> colIndexes = varRange.parseVarIndexes(df);

        targets = new ArrayList<>();
        for (Integer colIndexe : colIndexes) {
            targets.add(df.varNames()[colIndexe]);
        }
    }

    @Override
    public void predict(Frame df) {
        fitValues = new ArrayList<>();
        targets.stream().forEach(s -> fitValues.add(Numeric.newFill(df.rowCount(), customValue).withName(s)));
    }

    @Override
    public Numeric getFitValues() {
        return (Numeric) fitValues.get(0);
    }

    @Override
    public Frame getAllFitValues() {
        return SolidFrame.newWrapOf(fitValues.get(0).rowCount(), fitValues);
    }
}
