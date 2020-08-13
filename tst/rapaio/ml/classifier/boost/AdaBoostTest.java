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

package rapaio.ml.classifier.boost;

import org.junit.jupiter.api.Test;
import rapaio.datasets.Datasets;
import rapaio.ml.classifier.tree.CTree;
import rapaio.ml.common.VarSelector;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class AdaBoostTest {

    @Test
    void buildTest() throws IOException {

        var spam = Datasets.loadSpamBase();
        var model = AdaBoost.newModel()
                .model.set(CTree.newC45().minCount.set(4).maxDepth.set(5).varSelector.set(VarSelector.fixed(10)))
                .runs.set(10);

        String target = "spam";
        model.fit(spam, target);

        assertEquals(10, model.getLearners().size());
        assertEquals(10, model.getAlphas().size());

        assertEquals("AdaBoost{model=CTree,runs=10}; fitted=true, fitted trees=10", model.toString());

        var result = model.predict(spam, true, true);

        assertNotNull(result);
        assertEquals(spam.rvar("spam").levels().size(), result.firstDensity().varCount());
    }

    @Test
    void newInstanceTest() {
        var model = AdaBoost.newModel()
                .model.set(CTree.newC45().minCount.set(4).maxDepth.set(5).varSelector.set(VarSelector.fixed(10)))
                .runs.set(10);

        var copy = model.newInstance();

        assertEquals(model.toString(), copy.toString());
        assertEquals(model.toSummary(), copy.toSummary());
        assertEquals(model.toContent(), copy.toContent());
        assertEquals(model.toFullContent(), copy.toFullContent());
    }
}
