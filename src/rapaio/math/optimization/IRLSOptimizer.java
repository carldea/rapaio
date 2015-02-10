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

package rapaio.math.optimization;

import rapaio.data.Numeric;
import rapaio.data.Var;
import rapaio.math.linear.LA;
import rapaio.math.linear.LUDecomposition;
import rapaio.math.linear.M;
import rapaio.math.linear.V;

import java.util.List;
import java.util.function.Function;

/**
 * Iteratively Re-weighted Least Squares optimization algorithm.
 * <p>
 * Created by <a href="mailto:padreati@yahoo.com">Aurelian Tutuianu</a> at 2/3/15.
 */
public class IRLSOptimizer {

    /**
     * The hessian matrix
     */
    private M hessian;
    /**
     * Contains the values of the coefficients for each data point
     */
    private M coef;
    private V derivatives;
    private V err;
    private V grad;

    /**
     * Performs optimization on the given inputs to find the minima of the function.
     *
     * @param eps            the desired accuracy of the result.
     * @param iterationLimit the maximum number of iteration steps to allow
     * @param f              the function to optimize
     * @param fd             the derivative of the function to optimize
     * @param vars           contains the initial estimate of the minima. The length should be equal to the number of variables being solved for. This value may be altered.
     * @param inputs         a list of input data point values to learn from
     * @param outputs        a vector containing the true values for each data point in <tt>inputs</tt>
     * @return the compute value for the optimization.
     */
    public Numeric optimize(double eps, int iterationLimit, Function<Var, Double> f,
                            Function<Var, Double> fd, Numeric vars, List<Var> inputs, Numeric outputs) {

        hessian = LA.newMEmpty(vars.rowCount(), vars.rowCount());
        coef = LA.newMEmpty(inputs.size(), vars.rowCount());
        for (int i = 0; i < inputs.size(); i++) {
            Var x_i = inputs.get(i);
            coef.set(i, 0, 1.0);
            for (int j = 1; j < vars.rowCount(); j++)
                coef.set(i, j, x_i.value(j - 1));
        }

        derivatives = LA.newVEmpty(inputs.size());
        err = LA.newVEmpty(outputs.rowCount());
        grad = LA.newVEmpty(vars.rowCount());

        double maxChange = Double.MAX_VALUE;
        while (!Double.isNaN(maxChange) && maxChange > eps && iterationLimit-- > 0) {
            maxChange = iterationStep(f, fd, vars, inputs, outputs);
        }
        return vars;
    }

    private double iterationStep(Function<Var, Double> f, Function<Var, Double> fd, Numeric vars, List<Var> inputs, Numeric outputs) {
        for (int i = 0; i < inputs.size(); i++) {
            Var x_i = inputs.get(i);
            double y = f.apply(x_i);
            double error = y - outputs.value(i);
            err.set(i, error);
            derivatives.set(i, fd.apply(x_i));
        }

        for (int j = 0; j < hessian.rowCount(); j++) {
            double gradTmp = 0;
            for (int k = 0; k < coef.rowCount(); k++) {
                double coefficient_kj = coef.get(k, j);
                gradTmp += coefficient_kj * err.get(k);

                double factor = derivatives.get(k) * coefficient_kj;

                for (int i = 0; i < hessian.rowCount(); i++)
                    hessian.increment(j, i, coef.get(k, i) * factor);
            }
            grad.set(j, gradTmp);
        }

        LUDecomposition lu = new LUDecomposition(hessian);

        //We sent a clone of the hessian b/c we make incremental updates every iteration
        if (Math.abs(lu.det()) < 1e-14) {
            //TODO use a pesudo inverse instead of giving up
            return Double.NaN;//Indicate that we need to stop
        }
        V delta = lu.solve(grad).mapCol(0);

        for (int i = 0; i < vars.rowCount(); i++)
            vars.setValue(i, vars.value(i) - delta.get(i, 0));

        double max = Math.abs(delta.get(0));
        for (int i = 1; i < delta.rowCount(); i++) {
            max = Math.max(max, Math.abs(delta.get(i)));
        }
        return max;
    }
}
