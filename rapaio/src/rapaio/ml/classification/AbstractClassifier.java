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
package rapaio.ml.classification;

import rapaio.data.Frame;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Aurelian Tutuianu <paderati@yahoo.com>
 */
public abstract class AbstractClassifier<T> implements Classifier<T> {

    @Override
    public void learn(Frame df, String classColName) {
        List<Double> weights = new ArrayList<>();
        for (int i = 0; i < df.rowCount(); i++) {
            weights.add(1.);
        }
        learn(df, weights, classColName);
    }

    @Override
    public void learnFurther(Frame df, String classColName, T classifier) {
        List<Double> weights = new ArrayList<>();
        for (int i = 0; i < df.rowCount(); i++) {
            weights.add(1.);
        }
        learnFurther(df, weights, classColName, classifier);
    }

    @Override
    public void learnFurther(Frame df, List<Double> weights, String classColName, T classifier) {
        // default further prediction is not implemented
        throw new NotImplementedException();
    }

    @Override
    public void predictFurther(Frame df, T classifier) {
        // default further prediction is not implemented
        throw new NotImplementedException();
    }
}