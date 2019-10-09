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

package rapaio.ml.classifier.bayes.estimator;

import rapaio.core.distributions.*;
import rapaio.core.stat.*;
import rapaio.data.*;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Numeric probability estimator, using pdf of gaussian distribution.
 * <p>
 * Created by <a href="mailto:padreati@yahoo.com">Aurelian Tutuianu</a> on 5/18/15.
 */
public class GaussianPdf implements NumEstimator {

    private static final long serialVersionUID = -5974296887792054267L;

    private final Map<String, Normal> normals = new HashMap<>();

    @Override
    public String name() {
        return "GaussianPdf";
    }

    @Override
    public void learn(Frame df, String targetVar, String testVar) {
        normals.clear();
        Map<String, OnlineStat> onlineStatMap = new HashMap<>();
        for (String label : df.levels(targetVar)) {
            onlineStatMap.put(label, OnlineStat.empty());
        }
        df.stream().forEach(s -> {
            String label = s.getLabel(targetVar);
            onlineStatMap.get(label).update(s.getDouble(testVar));
        });
        for (String label : df.levels(targetVar)) {
            if ("?".equals(label)) {
                continue;
            }
            double mu = onlineStatMap.get(label).mean();
            double sd = onlineStatMap.get(label).sd();
            normals.put(label, Normal.of(mu, sd));
        }
    }

    @Override
    public double computeProbability(double testValue, String targetLabel) {
        Distribution normal = normals.get(targetLabel);
        if (Math.abs(normal.var()) < 1e-20) {
            return (Math.abs(normal.mean() - testValue) < 1e-20) ? 1.0 : 0.0;
        }
        return normals.get(targetLabel).pdf(testValue);
    }

    @Override
    public NumEstimator newInstance() {
        return new GaussianPdf();
    }

    @Override
    public String learningInfo() {
        return "GaussianPdf {" + normals.entrySet().stream().map(e -> e.getKey() + "~" + e.getValue().name()).collect(Collectors.joining(", ")) + '}';
    }
}
