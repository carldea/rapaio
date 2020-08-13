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

package rapaio.experiment.ml.classifier.ensemble;

import rapaio.core.distributions.Distribution;
import rapaio.core.distributions.Normal;
import rapaio.core.stat.Maximum;
import rapaio.core.stat.Mean;
import rapaio.core.stat.Variance;
import rapaio.core.tools.DensityVector;
import rapaio.data.Frame;
import rapaio.data.Mapping;
import rapaio.data.SolidFrame;
import rapaio.data.VRange;
import rapaio.data.VType;
import rapaio.data.Var;
import rapaio.data.VarDouble;
import rapaio.data.VarInt;
import rapaio.data.VarNominal;
import rapaio.data.filter.FRefSort;
import rapaio.data.filter.VShuffle;
import rapaio.data.sample.RowSampler;
import rapaio.data.sample.Sample;
import rapaio.ml.classifier.AbstractClassifierModel;
import rapaio.ml.classifier.ClassifierModel;
import rapaio.ml.classifier.ClassifierResult;
import rapaio.ml.classifier.tree.CTree;
import rapaio.ml.classifier.tree.ctree.Node;
import rapaio.ml.common.Capabilities;
import rapaio.ml.common.VarSelector;
import rapaio.ml.eval.metric.Confusion;
import rapaio.printer.Printer;
import rapaio.printer.opt.POption;
import rapaio.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toSet;

/**
 * Breiman random forest implementation.
 * <p>
 * Created by <a href="mailto:padreati@yahoo.com">Aurelian Tutuianu</a> on 4/16/15.
 */
public class CForest extends AbstractClassifierModel<CForest, ClassifierResult> {

    private static final long serialVersionUID = -145958939373105497L;

    private boolean oobComp;
    private boolean freqVIComp = false;
    private boolean gainVIComp = false;
    private boolean permVIComp = false;

    private ClassifierModel c;
    private BaggingMode baggingMode;

    // learning artifacts
    private double oobError = Double.NaN;
    private List<ClassifierModel> predictors = new ArrayList<>();
    private Map<Integer, DensityVector<String>> oobDensities;
    private Var oobFit;
    private Var oobTrueClass;
    private final Map<String, List<Double>> freqVIMap = new HashMap<>();
    private final Map<String, List<Double>> gainVIMap = new HashMap<>();
    private final Map<String, List<Double>> permVIMap = new HashMap<>();

    private CForest() {
        this.baggingMode = BaggingMode.DISTRIBUTION;
        this.c = CTree.newCART().varSelector.set(VarSelector.auto());
        this.oobComp = false;
        this.rowSampler.set(RowSampler.bootstrap());
    }

    public static CForest newRF() {
        return new CForest();
    }

    @Override
    public String name() {
        return "CForest";
    }

    @Override
    public String fullName() {
        StringBuilder sb = new StringBuilder();
        sb.append(name());
        sb.append("{");
        sb.append("runs:").append(runs.get()).append(";");
        sb.append("baggingMode:").append(baggingMode.name()).append(";");
        sb.append("oob:").append(oobComp).append(";");
        sb.append("sampler:").append(rowSampler.get().name()).append(";");
        sb.append("tree:").append(c.fullName());
        sb.append("}");
        return sb.toString();
    }

    @Override
    public CForest newInstance() {
        return new CForest().copyParameterValues(this)
                .withBaggingMode(baggingMode)
                .withOobComp(oobComp)
                .withFreqVIComp(freqVIComp)
                .withGainVIComp(gainVIComp)
                .withPermVIComp(permVIComp)
                .withClassifier(c.newInstance());
    }

    public CForest withFreqVIComp(boolean freqVIComp) {
        this.freqVIComp = freqVIComp;
        return this;
    }

    public CForest withGainVIComp(boolean gainVIComp) {
        this.gainVIComp = gainVIComp;
        return this;
    }

    public CForest withPermVIComp(boolean permVIComp) {
        this.permVIComp = permVIComp;
        return this;
    }

    public CForest withOobComp(boolean oobCompute) {
        this.oobComp = oobCompute;
        return this;
    }

    public CForest withBaggingMode(BaggingMode baggingMode) {
        this.baggingMode = baggingMode;
        return this;
    }

    public CForest withClassifier(ClassifierModel c) {
        this.c = c;
        return this;
    }

    public CForest withVarSelector(VarSelector varSelector) {
        if (c instanceof CTree) {
            ((CTree) c).varSelector.set(varSelector);
        }
        return this;
    }

    @Override
    public Capabilities capabilities() {
        Capabilities cc = c.capabilities();
        return Capabilities.builder()
                .minInputCount(cc.getMinInputCount()).maxInputCount(cc.getMaxInputCount())
                .inputTypes(Arrays.asList(cc.getInputTypes().toArray(VType[]::new)))
                .allowMissingInputValues(cc.getAllowMissingInputValues())
                .minTargetCount(1).maxTargetCount(1)
                .targetType(VType.NOMINAL)
                .allowMissingTargetValues(false)
                .build();
    }

    public List<ClassifierModel> getClassifiers() {
        return predictors;
    }

    public double getOobError() {
        return oobError;
    }

    public Confusion getOobInfo() {
        return Confusion.from(oobTrueClass, oobFit);
    }

    private Frame getVIInfo(Map<String, List<Double>> viMap) {
        Var name = VarNominal.empty().withName("name");
        Var score = VarDouble.empty().withName("score mean");
        Var sd = VarDouble.empty().withName("score sd");
        for (Map.Entry<String, List<Double>> e : viMap.entrySet()) {
            name.addLabel(e.getKey());
            VarDouble scores = VarDouble.copy(e.getValue());
            sd.addDouble(Variance.of(scores).sdValue());
            score.addDouble(Mean.of(scores).value());
        }
        double maxScore = Maximum.of(score).value();
        Var scaled = VarDouble.from(score.rowCount(), row -> 100.0 * score.getDouble(row) / maxScore).withName("scaled score");
        return SolidFrame.byVars(name, score, sd, scaled).fapply(FRefSort.by(score.refComparator(false))).copy();
    }

    public Frame getFreqVInfo() {
        return getVIInfo(freqVIMap);
    }

    public Frame getGainVIInfo() {
        return getVIInfo(gainVIMap);
    }

    public Frame getPermVIInfo() {
        Var name = VarNominal.empty().withName("name");
        Var score = VarDouble.empty().withName("score mean");
        Var sds = VarDouble.empty().withName("score sd");
        Var zscores = VarDouble.empty().withName("z-score");
        Var pvalues = VarDouble.empty().withName("p-value");
        Distribution normal = Normal.std();
        for (Map.Entry<String, List<Double>> e : permVIMap.entrySet()) {
            name.addLabel(e.getKey());
            VarDouble scores = VarDouble.copy(e.getValue());
            double mean = Mean.of(scores).value();
            double sd = Variance.of(scores).sdValue();
            double zscore = mean / (sd);
            double pvalue = normal.cdf(2 * normal.cdf(-Math.abs(zscore)));
            score.addDouble(Math.abs(mean));
            sds.addDouble(sd);
            zscores.addDouble(Math.abs(zscore));
            pvalues.addDouble(pvalue);
        }
        return SolidFrame.byVars(name, score, sds, zscores, pvalues).fapply(FRefSort.by(zscores.refComparator(false))).copy();
    }

    @Override
    protected boolean coreFit(Frame df, Var weights) {

        double totalOobInstances = 0;
        double totalOobError = 0;
        if (oobComp) {
            oobDensities = new HashMap<>();
            oobTrueClass = df.rvar(firstTargetName()).copy();
            oobFit = VarNominal.empty(df.rowCount(), firstTargetLevels());
            for (int i = 0; i < df.rowCount(); i++) {
                oobDensities.put(i, DensityVector.emptyByLabels(false, firstTargetLevels()));
            }
        }
        if (freqVIComp && c instanceof CTree) {
            freqVIMap.clear();
        }
        if (gainVIComp && c instanceof CTree) {
            gainVIMap.clear();
        }
        if (permVIComp) {
            permVIMap.clear();
        }

        if (poolSize.get() == 0) {
            predictors = new ArrayList<>();
            for (int i = 0; i < runs.get(); i++) {
                Pair<ClassifierModel, VarInt> weak = buildWeakPredictor(df, weights);
                predictors.add(weak._1);
                if (oobComp) {
                    oobCompute(df, weak);
                }
                if (freqVIComp && c instanceof CTree) {
                    freqVICompute(weak);
                }
                if (gainVIComp && c instanceof CTree) {
                    gainVICompute(weak);
                }
                if (permVIComp) {
                    permVICompute(df, weak);
                }
                runningHook.get().accept(this, i + 1);
            }
        } else {
            // build in parallel the trees, than oob and running hook cannot run at the
            // same moment when weak tree was built
            // for a real running hook behavior run without threading
            predictors = new ArrayList<>();
            IntStream intStream = IntStream.range(0, runs.get());
            if (poolSize.get() > 0) {
                intStream = intStream.parallel();
            }
            List<Pair<ClassifierModel, VarInt>> list = intStream
                    .boxed()
                    .map(s -> buildWeakPredictor(df, weights))
                    .collect(Collectors.toList());
            for (int i = 0; i < list.size(); i++) {
                Pair<ClassifierModel, VarInt> weak = list.get(i);
                predictors.add(weak._1);
                if (oobComp) {
                    oobCompute(df, weak);
                }
                if (freqVIComp && c instanceof CTree) {
                    freqVICompute(weak);
                }
                if (gainVIComp && c instanceof CTree) {
                    gainVICompute(weak);
                }
                if (permVIComp) {
                    permVICompute(df, weak);
                }
                runningHook.get().accept(this, i + 1);
            }
        }
        return true;
    }

    private void permVICompute(Frame df, Pair<ClassifierModel, VarInt> weak) {
        ClassifierModel c = weak._1;
        VarInt oobIndexes = weak._2;

        // build oob data frame
        Frame oobFrame = df.mapRows(Mapping.wrap(oobIndexes));

        // build accuracy on oob data frame
        ClassifierResult fit = c.predict(oobFrame);
        double refScore = Confusion.from(
                oobFrame.rvar(firstTargetName()),
                fit.firstClasses())
                .acceptedCases();

        // now for each input variable do computation
        for (String varName : inputNames()) {

            // shuffle values from variable
            Var shuffled = oobFrame.rvar(varName).fapply(VShuffle.filter());

            // build oob frame with shuffled variable
            Frame oobReduced = oobFrame.removeVars(VRange.of(varName)).bindVars(shuffled);

            // compute accuracy on oob shuffled frame

            ClassifierResult pfit = c.predict(oobReduced);
            double acc = Confusion.from(
                    oobReduced.rvar(firstTargetName()),
                    pfit.firstClasses()
            ).acceptedCases();

            if (!permVIMap.containsKey(varName)) {
                permVIMap.put(varName, new ArrayList<>());
            }
            permVIMap.get(varName).add(refScore - acc);
        }
    }

    private void gainVICompute(Pair<ClassifierModel, VarInt> weak) {
        CTree weakTree = (CTree) weak._1;
        var scores = DensityVector.emptyByLabels(false, inputNames());
        collectGainVI(weakTree.getRoot(), scores);
        for (int j = 0; j < inputNames().length; j++) {
            String varName = inputName(j);
            if (!gainVIMap.containsKey(varName)) {
                gainVIMap.put(varName, new ArrayList<>());
            }
            gainVIMap.get(varName).add(scores.get(varName));
        }
    }

    private void collectGainVI(Node node, DensityVector<String> dv) {
        if (node.leaf)
            return;
        String varName = node.bestCandidate.testName;
        double score = Math.abs(node.bestCandidate.score);
        dv.increment(varName, score * node.density.sum());
        node.children.forEach(child -> collectGainVI(child, dv));
    }

    private void freqVICompute(Pair<ClassifierModel, VarInt> weak) {
        CTree weakTree = (CTree) weak._1;
        var scores = DensityVector.emptyByLabels(false, inputNames());
        collectFreqVI(weakTree.getRoot(), scores);
        for (int j = 0; j < inputNames().length; j++) {
            String varName = inputName(j);
            if (!freqVIMap.containsKey(varName)) {
                freqVIMap.put(varName, new ArrayList<>());
            }
            freqVIMap.get(varName).add(scores.get(varName));
        }
    }

    private void collectFreqVI(Node node, DensityVector<String> dv) {
        if (node.leaf)
            return;
        String varName = node.bestCandidate.testName;
        double score = Math.abs(node.bestCandidate.score);
        dv.increment(varName, node.density.sum());
        node.children.forEach(child -> collectFreqVI(child, dv));
    }

    private void oobCompute(Frame df, Pair<ClassifierModel, VarInt> weak) {
        double totalOobError;
        double totalOobInstances;
        VarInt oobIndexes = weak._2;
        Frame oobTest = df.mapRows(Mapping.wrap(oobIndexes));
        var fit = weak._1.predict(oobTest);
        for (int j = 0; j < oobTest.rowCount(); j++) {
            int fitIndex = fit.firstClasses().getInt(j);
            oobDensities.get(oobIndexes.getInt(j)).increment(fitIndex, 1.0);
        }
        oobFit.clearRows();
        totalOobError = 0.0;
        totalOobInstances = 0.0;
        for (Map.Entry<Integer, DensityVector<String>> e : oobDensities.entrySet()) {
            if (e.getValue().sum() > 0) {
                int bestIndex = e.getValue().findBestIndex();
                String bestLevel = firstTargetLevels().get(bestIndex);
                oobFit.setLabel(e.getKey(), bestLevel);
                if (!bestLevel.equals(oobTrueClass.getLabel(e.getKey()))) {
                    totalOobError++;
                }
                totalOobInstances++;
            }
        }
        oobError = (totalOobInstances > 0) ? totalOobError / totalOobInstances : 0.0;
    }

    private Pair<ClassifierModel, VarInt> buildWeakPredictor(Frame df, Var weights) {
        var weak = c.newInstance();

        Sample sample = rowSampler.get().nextSample(df, weights);

        Frame trainFrame = sample.df;
        Var trainWeights = sample.weights;

        weak.fit(trainFrame, trainWeights, firstTargetName());
        VarInt oobIndexes = VarInt.empty();
        if (oobComp) {
            Set<Integer> out = sample.mapping.stream().boxed().collect(toSet());
            oobIndexes = VarInt.wrap(IntStream.range(0, df.rowCount()).filter(row -> !out.contains(row)).toArray());
        }
        return Pair.from(weak, oobIndexes);
    }

    @Override
    protected ClassifierResult corePredict(Frame df, boolean withClasses, boolean withDensities) {
        ClassifierResult cp = ClassifierResult.build(this, df, true, true);
        List<ClassifierResult> treeFits = predictors.stream().parallel()
                .map(pred -> (ClassifierResult) pred.predict(df, baggingMode.needsClass(), baggingMode.needsDensity()))
                .collect(Collectors.toList());
        baggingMode.computeDensity(firstTargetLevels(), treeFits, cp.firstClasses(), cp.firstDensity());
        return cp;
    }

    @Override
    public String toSummary(Printer printer, POption<?>... options) {
        StringBuilder sb = new StringBuilder();
        sb.append("CForest model\n");
        sb.append("================\n\n");

        sb.append("Description:\n");
        sb.append(fullName().replaceAll(";", ";\n")).append("\n\n");

        sb.append("Capabilities:\n");
        sb.append(capabilities().toString()).append("\n");

        if (!hasLearned()) {
            return sb.toString();
        }

        sb.append("Learned model:\n");
        sb.append(inputVarsSummary(printer, options));
        sb.append(targetVarsSummary());

        // stuff specific to rf
        // todo


        return sb.toString();
    }
}
