/*
 * Copyright 2013 Aurelian Tutuianu <padreati@yahoo.com>
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

package rapaio.graphics;

import rapaio.data.Vector;
import rapaio.graphics.plot.HistogramBars;

/**
 * @author tutuianu
 */
public class Histogram extends Plot {

    private HistogramBars hist;

    public Histogram(Vector v) {
        this(v, 20, false);
    }

    public Histogram(Vector v, int bins, boolean prob) {
        this(v, bins, prob, Double.NaN, Double.NaN);
    }

    public Histogram(Vector v, int bins, boolean prob, double from, double to) {
        hist = new HistogramBars(this, v, bins, prob, from, to);
        add(hist);
        this.setBottomLabel(v.getName());
    }

    public int getBins() {
        return hist.getBins();
    }

    public void setBins(int bins) {
        hist.setBins(bins);
        hist.setRebuild(true);
    }

    public boolean isProb() {
        return hist.isProb();
    }

    public void setProb(boolean prob) {
        hist.setProb(prob);
        hist.setRebuild(true);
    }
}