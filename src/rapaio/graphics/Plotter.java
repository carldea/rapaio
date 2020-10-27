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

package rapaio.graphics;

import rapaio.core.distributions.Distribution;
import rapaio.core.distributions.empirical.KFunc;
import rapaio.data.Frame;
import rapaio.data.Var;
import rapaio.data.VarDouble;
import rapaio.data.VarInt;
import rapaio.experiment.ml.clustering.DistanceMatrix;
import rapaio.graphics.opt.ColorPalette;
import rapaio.graphics.opt.GOption;
import rapaio.graphics.opt.GOptionAlpha;
import rapaio.graphics.opt.GOptionBins;
import rapaio.graphics.opt.GOptionColor;
import rapaio.graphics.opt.GOptionLabels;
import rapaio.graphics.opt.GOptionLwd;
import rapaio.graphics.opt.GOptionPalette;
import rapaio.graphics.opt.GOptionPch;
import rapaio.graphics.opt.GOptionPoints;
import rapaio.graphics.opt.GOptionProb;
import rapaio.graphics.opt.GOptionSz;
import rapaio.graphics.plot.BarChart;
import rapaio.graphics.plot.BoxPlot;
import rapaio.graphics.plot.GridLayer;
import rapaio.graphics.plot.Plot;
import rapaio.graphics.plot.QQPlot;
import rapaio.graphics.plot.artist.ABLine;
import rapaio.graphics.plot.artist.CorrGram;
import rapaio.graphics.plot.artist.DensityLine;
import rapaio.graphics.plot.artist.FunctionLine;
import rapaio.graphics.plot.artist.Histogram;
import rapaio.graphics.plot.artist.Histogram2D;
import rapaio.graphics.plot.artist.Lines;
import rapaio.graphics.plot.artist.Points;
import rapaio.graphics.plot.artist.ROCCurve;
import rapaio.ml.eval.metric.ROC;
import rapaio.util.function.Double2DoubleFunction;

import java.awt.*;

public final class Plotter {

    public static Plot plot(GOption<?>... opts) {
        return new Plot(opts);
    }

    public static GridLayer gridLayer(int rows, int cols) {
        return new GridLayer(rows, cols);
    }

    public static QQPlot qqplot(Var points, Distribution dist, GOption<?>... opts) {
        return new QQPlot(points, dist, opts);
    }

    public static BoxPlot boxPlot(Var x, Var factor, GOption<?>... opts) {
        return new BoxPlot(x, factor, opts);
    }

    public static BoxPlot boxPlot(Var x, GOption<?>... opts) {
        return new BoxPlot(x, opts);
    }

    public static BoxPlot boxPlot(Var[] vars, GOption<?>... opts) {
        return new BoxPlot(vars, opts);
    }

    public static BoxPlot boxPlot(Frame df, GOption<?>... opts) {
        return new BoxPlot(df, opts);
    }

    public static Plot hist(Var v, GOption<?>... opts) {
        return plot().add(new Histogram(v, opts));
    }

    public static Plot hist(Var v, double minValue, double maxValue, GOption<?>... opts) {
        return plot().add(new Histogram(v, minValue, maxValue, opts));
    }

    public static Plot hist2d(Var x, Var y, GOption<?>... opts) {
        return plot().add(new Histogram2D(x, y, opts));
    }

    public static Plot densityLine(Var var, GOption<?>... opts) {
        return plot().add(new DensityLine(var, opts));
    }

    public static Plot densityLine(Var var, double bandwidth, GOption<?>... opts) {
        return plot().add(new DensityLine(var, bandwidth, opts));
    }

    public static Plot densityLine(Var var, KFunc kfunc, GOption<?>... opts) {
        return plot().add(new DensityLine(var, kfunc, opts));
    }

    public static Plot densityLine(Var var, KFunc kfunc, double bandwidth, GOption<?>... opts) {
        return plot().add(new DensityLine(var, kfunc, bandwidth, opts));
    }

    public static Plot funLine(Double2DoubleFunction f, GOption<?>... opts) {
        return plot().add(new FunctionLine(f, opts));
    }

    public static Plot lines(Var x, Var y, GOption<?>... opts) {
        return plot().add(new Lines(x, y, opts));
    }

    public static Plot lines(Var y, GOption<?>... opts) {
        return plot().add(new Lines(y, opts));
    }

    public static Plot points(Var x, Var y, GOption<?>... opts) {
        return plot().add(new Points(x, y, opts));
    }

    public static Plot points(Var y, GOption<?>... opts) {
        return plot().add(new Points(VarInt.seq(y.rowCount()).withName("pos"), y, opts));
    }

    public static Plot rocCurve(ROC roc, GOption<?>... opts) {
        return plot().add(new ROCCurve(roc, opts));
    }

    public static BarChart barChart(Var category, GOption<?>... opts) {
        return new BarChart(category, opts);
    }

    public static BarChart barChart(Var category, Var cond, GOption<?>... opts) {
        return new BarChart(category, cond, opts);
    }

    public static BarChart barChart(Var category, Var cond, Var numeric, GOption<?>... opts) {
        return new BarChart(category, cond, numeric, opts);
    }

    public static Plot hLine(double a, GOption<?>... opts) {
        return plot().add(new ABLine(true, a, opts));
    }

    public static Plot vLine(double a, GOption<?>... opts) {
        return plot().add(new ABLine(false, a, opts));
    }

    public static Plot abLine(double a, double b, GOption<?>... opts) {
        return plot().add(new ABLine(a, b, opts));
    }

    public static Plot corrGram(DistanceMatrix d, GOption<?>... opts) {
        return corrGram(d, true, true, opts);
    }

    public static Plot corrGram(DistanceMatrix d, boolean labels, boolean grid, GOption<?>... opts) {
        return plot().add(new CorrGram(d, labels, grid, opts));
    }

    // GRAPHICAL OPTIONS

    public static GOptionPalette palette(ColorPalette colorPalette) {
        return new GOptionPalette(colorPalette);
    }

    public static GOptionColor color(int... index) {
        return new GOptionColor(index);
    }

    public static GOptionColor color(Color color) {
        return new GOptionColor(color);
    }

    public static GOptionColor color(Color[] colors) {
        return new GOptionColor(colors);
    }

    public static GOptionColor color(Var color) {
        return new GOptionColor(color);
    }

    public static GOptionLwd lwd(float lwd) {
        return new GOptionLwd(lwd);
    }

    public static GOptionSz sz(Var sizeIndex) {
        return sz(sizeIndex, 1);
    }

    public static GOptionSz sz(Var sizeIndex, double factor) {
        return sz(sizeIndex, factor, 0);
    }

    public static GOptionSz sz(Var sizeIndex, double factor, double offset) {
        VarDouble size = sizeIndex
                .stream()
                .mapToDouble()
                .map(x -> x * factor + offset)
                .boxed()
                .collect(VarDouble.collector());
        return new GOptionSz(size);
    }

    public static GOptionSz sz(double size) {
        return new GOptionSz(VarDouble.scalar(size));
    }

    public static GOptionPch pch(Var pchIndex, int... mapping) {
        VarInt pch = VarInt.from(pchIndex.rowCount(), row -> {
            int i = pchIndex.getInt(row);
            if (i >= 0 && i < mapping.length) {
                return mapping[i];
            }
            return mapping[0];
        });
        return new GOptionPch(pch);
    }

    public static GOptionPch pch(int pch) {
        return new GOptionPch(VarInt.scalar(pch));
    }

    public static GOptionAlpha alpha(float alpha) {
        return new GOptionAlpha(alpha);
    }

    public static GOptionBins bins(int bins) {
        return new GOptionBins(bins);
    }

    public static GOptionProb prob(boolean prob) {
        return new GOptionProb(prob);
    }

    public static GOptionPoints points(int points) {
        return new GOptionPoints(points);
    }

    public static GOptionLabels labels(String... labels) {
        return new GOptionLabels(labels);
    }
}
