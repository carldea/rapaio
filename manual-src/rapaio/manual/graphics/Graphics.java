/*
 * Apache License
 * Version 2.0, January 2004
 * http://www.apache.org/licenses/
 *
 *    Copyright 2013 Aurelian Tutuianu
 *    Copyright 2014 Aurelian Tutuianu
 *    Copyright 2015 Aurelian Tutuianu
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

package rapaio.manual.graphics;

import rapaio.data.Frame;
import rapaio.datasets.Datasets;
import rapaio.graphics.base.Figure;
import rapaio.graphics.base.ImageUtility;
import rapaio.printer.IdeaPrinter;
import rapaio.sys.WS;

import java.io.IOException;
import java.net.URISyntaxException;

import static rapaio.graphics.Plotter.boxPlot;

public class Graphics {

    public static void main(String[] args) throws IOException, URISyntaxException {

        WS.setPrinter(new IdeaPrinter().withTextWidth(90));

        Frame iris = Datasets.loadIrisDataset();
        iris.printSummary();


        Figure fig = boxPlot(iris.mapVars("0~3"));
        WS.draw(fig);
        ImageUtility.saveImage(fig, 600, 400, "/home/ati/work/rapaio/manual-img/graphics-boxplot-iris.png");
    }
}