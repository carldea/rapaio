package rapaio.data.filter.var;

import org.junit.Test;
import rapaio.core.distributions.*;
import rapaio.core.stat.*;
import rapaio.data.*;

import static org.junit.Assert.assertTrue;

/**
 * Created by <a href="mailto:padreati@yahoo.com">Aurelian Tutuianu</a> on 10/2/18.
 */
public class VStandardizeTest {

    @Test
    public void testDouble() {

        Distribution d = Gamma.of(0.5, 2);
        VarDouble x = VarDouble.from(1000, d::sampleNext);

        double mean = Mean.of(x).value();
        double sd = Variance.of(x).sdValue();

        Var m1 = x.copy().fapply(VStandardize.filter());
        Var m2 = x.copy().fapply(VStandardize.filter(mean));
        Var m3 = x.copy().fapply(VStandardize.filter(mean, sd));

        assertTrue(m1.deepEquals(m2));
        assertTrue(m2.deepEquals(m3));
    }

    @Test
    public void testConstant() {
        VarDouble x = VarDouble.fill(100, 10);
        Var sd = x.copy().fapply(VStandardize.filter());
        assertTrue(x.deepEquals(sd));
    }

    @Test
    public void testNonNumeric() {
        VarNominal x = VarNominal.copy("a", "b");
        Var sd = x.fapply(VStandardize.filter());
        assertTrue(x.deepEquals(sd));
    }
}
