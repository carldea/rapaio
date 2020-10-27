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

import rapaio.data.Var;
import rapaio.data.VarDouble;
import rapaio.graphics.opt.GOption;
import rapaio.graphics.plot.Artist;
import rapaio.graphics.plot.DataRange;
import rapaio.util.function.Double2DoubleFunction;

import java.awt.*;
import java.awt.geom.Line2D;

import static rapaio.graphics.Plotter.points;

/**
 * @author <a href="mailto:padreati@yahoo.com">Aurelian Tutuianu</a>
 */
public class FunctionLine extends Artist {

    private static final long serialVersionUID = 8388944194915495215L;
    private final Double2DoubleFunction f;

    public FunctionLine(Double2DoubleFunction f, GOption<?>... opts) {
        this.f = f;
        // apply default values for function line
        this.options.bind(points(1024 * 64));
        this.options.bind(opts);
    }

    @Override
    public void updateDataRange(DataRange range) {
    }

    @Override
    public void paint(Graphics2D g2d) {
        DataRange range = parent.getDataRange();
        Var x = VarDouble.fill(options.getPoints() + 1, 0);
        Var y = VarDouble.fill(options.getPoints() + 1, 0);
        double xstep = (range.xMax() - range.xMin()) / options.getPoints();
        for (int i = 0; i < x.rowCount(); i++) {
            x.setDouble(i, range.xMin() + i * xstep);
            y.setDouble(i, f.applyAsDouble(x.getDouble(i)));
        }

        Composite old = g2d.getComposite();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, options.getAlpha()));
        for (int i = 1; i < x.rowCount(); i++) {
            if (range.contains(x.getDouble(i - 1), y.getDouble(i - 1)) && range.contains(x.getDouble(i), y.getDouble(i))) {
                g2d.setColor(options.getColor(i));
                g2d.setStroke(new BasicStroke(options.getLwd()));
                g2d.draw(new Line2D.Double(
                        parent.xScale(x.getDouble(i - 1)),
                        parent.yScale(y.getDouble(i - 1)),
                        parent.xScale(x.getDouble(i)),
                        parent.yScale(y.getDouble(i))));

            }
        }
        g2d.setComposite(old);
    }
}
