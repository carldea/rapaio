/*
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 *
 * Copyright 2013 - 2021 Aurelian Tutuianu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package rapaio.ml.classifier.linear.binarylogistic;

import rapaio.math.linear.DMatrix;
import rapaio.math.linear.DVector;
import rapaio.math.linear.decomposition.CholeskyDecomposition;
import rapaio.math.linear.decomposition.QRDecomposition;
import rapaio.ml.common.ParamSet;
import rapaio.ml.common.ValueParam;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.exp;

/**
 * Created by <a href="mailto:padreati@yahoo.com">Aurelian Tutuianu</a> on 3/21/20.
 */
public class BinaryLogisticNewton extends ParamSet<BinaryLogisticNewton> {

    @Serial
    private static final long serialVersionUID = 4772367031535421893L;

    public final ValueParam<Double, BinaryLogisticNewton> eps = new ValueParam<>(this,
            1e-100, "eps", "eps");
    public final ValueParam<Integer, BinaryLogisticNewton> maxIter = new ValueParam<>(this,
            10, "maxIter", "maxIter");
    public final ValueParam<Double, BinaryLogisticNewton> lambda = new ValueParam<>(this,
            0.0, "lambda", "lambda");
    public final ValueParam<DMatrix, BinaryLogisticNewton> x = new ValueParam<>(this,
            null, "x", "x");
    public final ValueParam<DVector, BinaryLogisticNewton> y = new ValueParam<>(this,
            null, "y", "y");
    public final ValueParam<DVector, BinaryLogisticNewton> w0 = new ValueParam<>(this,
            null, "w0", "w0");

    public static record Result(DVector w, List<Double> nll, List<DVector> ws, boolean converged) {
    }

    public Result fit() {
        int it = 0;
        // current solution
        DVector w = w0.get().copy();
        ArrayList<DVector> ws = new ArrayList<>();
        ws.add(w);
        List<Double> nlls = new ArrayList<>();
        nlls.add(negativeLogLikelihood(x.get(), y.get(), w));

        while (it++ < maxIter.get()) {
            DVector wnew = iterate(w);
            double nll = negativeLogLikelihood(x.get(), y.get(), wnew);

            double nll_delta = nll - nlls.get(nlls.size() - 1);
            if (it > 1 && (Math.abs(nll_delta) <= eps.get() /*|| nll_delta > 0*/)) {
                return new Result(w, nlls, ws, true);
            }
            ws.add(wnew);
            nlls.add(nll);
            w = wnew;
        }
        return new Result(w, nlls, ws, false);
    }

    private double negativeLogLikelihood(DMatrix x, DVector y, DVector w) {
        return -x.dot(w)
                .apply((i, v) -> (y.get(i) == 1) ? (1. / (1. + exp(-v))) : (1 - 1. / (1. + exp(-v))))
                .apply(this::cut)
                .apply(Math::log)
                .nansum() / x.rowCount();
    }

    private double cut(double value) {
        return Math.min(1 - 1e-12, value);
    }

    private DVector iterate(DVector w) {

        // compute delta weights first as (X^t I_{p(1-p)} X)^{-1} X^t (y-p)

        // Xw dot product
        DVector xw = x.get().dot(w);

        // p diag = 1/(1+exp(-Xw))
        DVector p = xw.copy().apply(value -> cut(1. / (1. + exp(-value))));

        // p(1-p) diag from p diag
        DVector pvars = p.copy().apply(value -> value * (1 - value));

        // Xt(p(1-p)
        DMatrix xpvar = x.get().copy();
        for (int i = 0; i < xpvar.rowCount(); i++) {
            double pvar = pvars.get(i);
            for (int j = 0; j < xpvar.colCount(); j++) {
                xpvar.set(i, j, xpvar.get(i, j) * pvar);
            }
        }

        // X^t * I(p(1-p))^T * X
        DMatrix mA = xpvar.t().dot(x.get());

        DMatrix invA;
        CholeskyDecomposition chol = CholeskyDecomposition.from(mA);
        if (chol.isSPD()) {
            invA = chol.solve(DMatrix.identity(w.size()));
        } else {
            QRDecomposition qr = QRDecomposition.from(mA);
            invA = qr.solve(DMatrix.identity(w.size()));
        }

        // z = Wx - I(p(1-p))^{-1}(y-p)
        DVector delta = invA.dot(x.get().t().dot(y.get().copy().sub(p)));


        // otherwise we fall in QR decomposition
        return delta.add(w);
    }
}
