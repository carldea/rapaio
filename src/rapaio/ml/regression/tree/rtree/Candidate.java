/*
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 *
 *    Copyright 2013 - 2021 Aurelian Tutuianu
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

package rapaio.ml.regression.tree.rtree;

import rapaio.experiment.ml.common.predicate.RowPredicate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by <a href="mailto:padreati@yahoo.com">Aurelian Tutuianu</a> on 11/1/17.
 */
public class Candidate implements Serializable {

    private static final long serialVersionUID = 6698766675237089849L;
    private final double score;
    private final String testName;
    private final List<String> groupNames = new ArrayList<>();
    private final List<RowPredicate> groupPredicates = new ArrayList<>();

    public Candidate(double score, String testName) {
        this.score = score;
        this.testName = testName;
    }

    public void addGroup(RowPredicate predicate) {
        String name = predicate.toString();
        if (groupNames.contains(name)) {
            throw new IllegalArgumentException("group name already defined");
        }
        groupNames.add(name);
        groupPredicates.add(predicate);
    }

    public List<String> getGroupNames() {
        return groupNames;
    }

    public List<RowPredicate> getGroupPredicates() {
        return groupPredicates;
    }

    public double getScore() {
        return score;
    }

    public String getTestName() {
        return testName;
    }

    @Override
    public String toString() {
        return "Candidate{" +
                "score=" + score +
                ", testName='" + testName + '\'' +
                ", groupNames=" + groupNames +
                '}';
    }
}