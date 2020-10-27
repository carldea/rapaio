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
import rapaio.graphics.opt.GOption;
import rapaio.graphics.opt.PchPalette;
import rapaio.graphics.plot.Artist;
import rapaio.graphics.plot.Axes;
import rapaio.graphics.plot.DataRange;

import java.awt.*;

/**
 * Plot component which allows one to add points to a plot.
 *
 * @author <a href="mailto:padreati@yahoo.com">Aurelian Tutuianu</a>
 */
public class Points extends Artist {

    private static final long serialVersionUID = -4766079423843859315L;

    private final Var x;
    private final Var y;

    public Points(Var x, Var y, GOption<?>... opts) {
        this.x = x;
        this.y = y;
        this.options.bind(opts);
    }

    @Override
    public void bind(Axes parent) {
        super.bind(parent);
        parent.getPlot().xLab(x.name());
        parent.getPlot().yLab(y.name());
    }

    @Override
    public void updateDataRange(DataRange range) {
        if (x.rowCount() == 0) {
            return;
        }
        for (int i = 0; i < Math.min(x.rowCount(), y.rowCount()); i++) {
            if (x.isMissing(i) || y.isMissing(i)) {
                continue;
            }
            range.union(x.getDouble(i), y.getDouble(i));
        }
    }

    @Override
    public void paint(Graphics2D g2d) {

        int len = Math.min(x.rowCount(), y.rowCount());
        for (int i = 0; i < len; i++) {
            if (x.isMissing(i) || y.isMissing(i)) {
                continue;
            }

            double xx = x.getDouble(i);
            double yy = y.getDouble(i);

            if (!parent.getDataRange().contains(xx, yy)) continue;

            g2d.setColor(options.getColor(i));
            g2d.setStroke(new BasicStroke(options.getLwd()));
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, options.getAlpha()));

            PchPalette.STANDARD.draw(g2d,
                    parent.xScale(xx),
                    parent.yScale(yy),
                    options.getSz(i), options.getPch(i));
        }
    }
}
