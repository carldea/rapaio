package rapaio.performance;

import org.junit.Test;
import rapaio.core.stat.Mean;
import rapaio.core.stat.Variance;
import rapaio.data.Numeric;
import rapaio.graphics.Plot;
import rapaio.graphics.plot.Lines;
import rapaio.printer.LocalPrinter;

import static rapaio.workspace.Workspace.draw;
import static rapaio.workspace.Workspace.setPrinter;

/**
 * User: Aurelian Tutuianu <paderati@yahoo.com>
 */
public class PerformanceTests {

    @Test
    public void testNumericVector() {

        setPrinter(new LocalPrinter());

        final int TESTS = 500;
        final int LEN = 100_000;

        Numeric index = new Numeric();
        Numeric time1 = new Numeric();
        Numeric time2 = new Numeric();
        Numeric delta = new Numeric();

        for (int i = 0; i < TESTS; i++) {

            long start = System.currentTimeMillis();
            double[] values = new double[LEN];
            for (int j = 0; j < LEN; j++) {
                values[j] += j * Math.sin(j);
            }
            time1.addValue(System.currentTimeMillis() - start);

            start = System.currentTimeMillis();
            Numeric numeric = new Numeric(LEN);
            for (int j = 0; j < LEN; j++) {
                numeric.addValue(j * Math.sin(j));
            }
            time2.addValue(System.currentTimeMillis() - start);
            index.addIndex(i);
            delta.addValue(time1.value(i) - time2.value(i));
        }

//        draw(new Plot()
//                .add(new Lines(index, time1).setColorIndex(1))
//                .add(new Lines(index, time2).setColorIndex(2))
//                .add(new Lines(index, delta).setColorIndex(3))
//        );

        draw(new Plot()
                .add(new Lines(index, time1).setColorIndex(1))
                .add(new Lines(index, time2).setColorIndex(2))
                .setBottomLabel("array")
                .setLeftLabel("NumVector")
        );

//        draw(new Plot()
//                .add(new Histogram(delta).setBins(30))
//        );

        new Mean(delta).summary();
        new Variance(delta).summary();
    }
}