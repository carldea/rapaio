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

package rapaio.experiment.ml.classifier.meta;

import rapaio.data.*;
import rapaio.ml.classifier.*;
import rapaio.ml.common.*;
import rapaio.ml.eval.metric.*;
import rapaio.printer.*;
import rapaio.printer.format.*;
import rapaio.sys.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * Created by <a href="mailto:padreati@yahoo.com">Aurelian Tutuianu</a> on 11/11/15.
 */
public class CStepwiseSelection
        extends AbstractClassifierModel<CStepwiseSelection, ClassifierResult<CStepwiseSelection>>
        implements DefaultPrintable {

    private static final long serialVersionUID = 2642562123626893974L;
    ClassifierModel best;
    private ClassifierModel c;
    private int minVars = 1;
    private int maxVars = 1;
    private String[] startSelection = new String[]{};
    private int restartAfter = 2;
    private int maxSearch = Integer.MAX_VALUE;
    private Frame test;
    // training artifacts
    private List<String> selection;

    @Override
    public String name() {
        return "CStepwiseSelection";
    }

    @Override
    public String fullName() {
        return null;
    }

    @Override
    public CStepwiseSelection newInstance() {
        return newInstanceDecoration(new CStepwiseSelection())
                .withRestartAfter(restartAfter)
                .withClassifier(c)
                .withMaxVars(maxVars)
                .withMinVars(minVars)
                .withStartSelection(startSelection)
                .withTestFrame(test);
    }

    @Override
    public Capabilities capabilities() {
        return c.capabilities();
    }

    public CStepwiseSelection withClassifier(ClassifierModel c) {
        this.c = c;
        return this;
    }

    public CStepwiseSelection withMinVars(int minVars) {
        this.minVars = minVars;
        return this;
    }

    public CStepwiseSelection withMaxVars(int maxVars) {
        this.maxVars = maxVars;
        return this;
    }

    public CStepwiseSelection withStartSelection(String... startSelection) {
        this.startSelection = startSelection;
        return this;
    }

    public CStepwiseSelection withRestartAfter(int restartAfter) {
        this.restartAfter = restartAfter;
        return this;
    }

    public CStepwiseSelection withMaxSearch(int maxSearch) {
        this.maxSearch = maxSearch;
        return this;
    }

    public CStepwiseSelection withTestFrame(Frame test) {
        this.test = test;
        return this;
    }

    @Override
    protected boolean coreFit(Frame df, Var weights) {

        selection = VRange.of(startSelection).parseVarNames(df);
        Frame testFrame = test != null ? test : df;

        List<String> bestSelection = new ArrayList<>(selection);
        ClassifierModel bestClassifierModel = null;
        double bestAcc = 0.0;
        String forwardNext = null;
        String backwardNext = null;

        for (int r = 0; r < runs(); r++) {
            boolean found = false;

            Set<String> inSet = new HashSet<>(selection);
            if (selection.size() < maxVars) {
                // do forward selection
                List<String> in = Arrays.stream(inputNames()).collect(Collectors.toList());
                Collections.shuffle(in);
                int restart = 0;
                for (int i = 0; i < in.size() && i < maxSearch; i++) {
                    String test = in.get(i);
                    if (inSet.contains(test))
                        continue;

                    List<String> next = new ArrayList<>(selection);
                    next.add(test);
                    next.add(firstTargetName());

                    ClassifierModel cNext = c.newInstance();
                    cNext.fit(df.mapVars(next), firstTargetName());
                    Confusion cm = Confusion.from(testFrame.rvar(firstTargetName()), cNext.predict(testFrame).firstClasses());

                    double acc = cm.accuracy();
                    if (acc > bestAcc) {

                        WS.println(Format.floatFlex(acc));
                        bestAcc = acc;
                        bestClassifierModel = cNext;
                        forwardNext = test;
                        backwardNext = null;
                        found = true;
                        restart++;
                        if (restart >= restartAfter) {
                            break;
                        }
                    }
                }
            }
            if (!found && selection.size() > minVars) {
                // do backward selection
                int restart = 0;
                Collections.shuffle(selection);
                for (int i = 0; i < selection.size() && i < maxSearch; i++) {

                    String test = selection.get(i);
                    List<String> next = selection.stream().filter(n -> !test.equals(n)).collect(toList());
                    next.add(firstTargetName());

                    ClassifierModel cNext = c.newInstance();
                    cNext.fit(df.mapVars(next), firstTargetName());
                    Confusion cm = Confusion.from(testFrame.rvar(firstTargetName()), cNext.predict(testFrame).firstClasses());

                    double acc = cm.accuracy();

                    if (acc > bestAcc) {
                        WS.println(Format.floatFlex(acc));
                        bestAcc = acc;
                        bestClassifierModel = cNext;
                        forwardNext = null;
                        backwardNext = test;
                        found = true;
                        restart++;
                        if (restart >= restartAfter)
                            break;
                    }
                }
            }
            if (!found)
                break;

            best = bestClassifierModel;

            String testNext = (forwardNext == null) ? backwardNext : forwardNext;
            if (forwardNext != null) {
                selection.add(testNext);
            }
            if (backwardNext != null) {
                selection = selection.stream().filter(n -> !n.equals(testNext)).collect(toList());
            }
        }
        return true;
    }

    @Override
    protected ClassifierResult<CStepwiseSelection> corePredict(Frame df, boolean withClasses, boolean withDistributions) {
        return ClassifierResult.copy(this, df, withClasses, withDistributions, best.predict(df, withClasses, withDistributions));
    }

}
