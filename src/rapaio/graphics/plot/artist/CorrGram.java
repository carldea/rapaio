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

package rapaio.graphics.plot.artist;

import rapaio.core.correlation.CorrPearson;
import rapaio.core.correlation.CorrSpearman;
import rapaio.experiment.ml.clustering.DistanceMatrix;
import rapaio.graphics.opt.ColorGradient;
import rapaio.graphics.opt.GOption;
import rapaio.graphics.opt.GOptionColor;
import rapaio.graphics.plot.Artist;
import rapaio.graphics.plot.Axes;
import rapaio.graphics.plot.DataRange;
import rapaio.math.MTools;
import rapaio.printer.Format;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.stream.DoubleStream;

/**
 * Correlograms are artists which helps visualize data in correlation matrices.
 * A correlation matrix is a {@link DistanceMatrix} which has to have values normalized in
 * range [-1,1]. If this is not the case the values will be cut off in those limits.
 * <p>
 * In order to give some example one can use {@link CorrPearson#matrix()} to obtain
 * the Pearson linear correlation matrix. In a similar fashion one can use
 * {@link CorrSpearman#matrix()}.
 * <p>
 * By default, adding this artist will turn all markers and tickers for the given axes.
 * If one wants a different behaviour it must turn them on explicitly.
 * <p>
 * The colors used to display values comes from {@link ColorGradient#newHueGradient(int, int, double[])}
 * with start=0, end=240 and an array of 101 percentages. One can changes this behaviour by setting
 * the {@link rapaio.graphics.Plotter#color(Color[])} graphical option to an array of 101 color elements.
 * <p>
 * Created by <a href="mailto:padreati@yahoo.com">Aurelian Tutuianu</a> on 2/19/16.
 */
public class CorrGram extends Artist {

    private static final long serialVersionUID = 7529398214880633755L;

    private final boolean grid;
    private final boolean labels;
    private final DistanceMatrix d;

    public CorrGram(DistanceMatrix d, boolean labels, boolean grid, GOption<?>... opts) {
        this.labels = labels;
        this.grid = grid;
        this.d = d;

        this.options.setColor(new GOptionColor(ColorGradient.newHueGradient(0, 240,
                DoubleStream.iterate(0, x -> x + 0.01).limit(101).toArray()).getColors()
        ));
        this.options.bind(opts);
    }

    @Override
    public void bind(Axes parent) {
        super.bind(parent);
        parent.getPlot().bottomMarkers(false);
        parent.getPlot().bottomThick(false);
        parent.getPlot().leftMarkers(false);
        parent.getPlot().leftThick(false);
    }

    private int computeIndex(int i, int j) {
        double value = d.get(i, j);
        value = Math.min(value, 1);
        value = Math.max(value, -1);
        return (int) MTools.floor((1 + value) * 50);
    }

    @Override
    public void updateDataRange(DataRange range) {
        range.union(0, 0);
        range.union(d.length(), d.length());
    }

    @Override
    public void paint(Graphics2D g2d) {

        double xstep = Math.abs(xScale(1) - xScale(0));
        double ystep = Math.abs(yScale(1) - yScale(0));
        for (int i = 0; i < d.length(); i++) {
            for (int j = 0; j < d.length(); j++) {
                if (i != j) {
                    g2d.setColor(options.getColor(computeIndex(i, j)));
                    g2d.fill(new Rectangle2D.Double(
                            xScale(j),
                            yScale(d.length() - i),
                            xstep,
                            ystep));
                }
                if (labels) {
                    String label = Format.floatFlexShort(d.get(i, j));
                    if (i == j) {
                        label = d.name(i);
                    }
                    Rectangle2D bounds = g2d.getFontMetrics().getStringBounds(label, g2d);
                    double width = bounds.getWidth();
                    double height = bounds.getHeight();

                    g2d.setColor(Color.BLACK);
                    g2d.drawString(label,
                            (int) (xScale(j) + xstep / 2 - width / 2),
                            (int) (yScale(d.length() - i) + ystep / 2 + height / 2));
                }
            }
        }

        if (grid) {
            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(options.getLwd()));

            g2d.draw(new Line2D.Double(xScale(0), yScale(0), xScale(d.length()), yScale(0)));
            g2d.draw(new Line2D.Double(xScale(0), yScale(d.length()), xScale(0), yScale(0)));
            for (int i = 0; i <= d.length(); i++) {
                for (int j = 0; j <= d.length(); j++) {
                    g2d.draw(new Line2D.Double(xScale(j), yScale(i), xScale(d.length()), yScale(i)));
                    g2d.draw(new Line2D.Double(xScale(j), yScale(d.length()), xScale(j), yScale(i)));
                }
            }
        }
    }
}
