package rapaio.data.filter;

import org.junit.Test;
import rapaio.data.Var;
import rapaio.data.VarDouble;
import rapaio.data.filter.VCumSum;

import static org.junit.Assert.assertEquals;

/**
 * Created by <a href="mailto:padreati@yahoo.com">Aurelian Tutuianu</a> on 10/1/18.
 */
public class VCumSumTest {

    private static final double TOL = 1e-20;

    @Test
    public void testDouble() {

        Var fill = VarDouble.fill(100, 1);
        Var cs = fill.fapply(VCumSum.filter());

        for (int i = 0; i < fill.rowCount(); i++) {
            assertEquals(i+1, fill.getDouble(i), TOL);
        }
    }
}