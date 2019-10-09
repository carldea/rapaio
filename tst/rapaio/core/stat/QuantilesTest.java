package rapaio.core.stat;

import org.junit.Before;
import org.junit.Test;
import rapaio.core.*;
import rapaio.core.distributions.*;
import rapaio.data.*;

import static org.junit.Assert.assertEquals;

/**
 * Created by <a href="mailto:padreati@yahoo.com">Aurelian Tutuianu</a> on 10/10/18.
 */
public class QuantilesTest {

    private static final double TOL = 1e-12;

    @Before
    public void setUp() {
        RandomSource.setSeed(123);
    }

    @Test
    public void testDoubleR7() {

        Normal normal = Normal.std();
        VarDouble x = VarDouble.from(1_000_000, normal::sampleNext);

        Quantiles q = Quantiles.of(x, 0, 0.025, 0.5, 0.975, 1);
        double[] qq = q.values();

        assertEquals(Minimum.of(x).value(), qq[0], TOL);
        assertEquals(Maximum.of(x).value(), qq[4], TOL);

        // aprox -1.96
        assertEquals(-1.9562361490537725, qq[1], TOL);

        // aprox 0
        assertEquals(0.0011999109845885958, qq[2], TOL);

        // aprox 1.96
        assertEquals(1.9615631844255106, qq[3], TOL);
    }

    @Test
    public void testDoubleR8() {

        Normal normal = Normal.std();
        VarDouble x = VarDouble.from(1_000_000, normal::sampleNext);

        Quantiles q = Quantiles.of(x, Quantiles.Type.R8, 0, 0.025, 0.5, 0.975, 1);
        double[] qq = q.values();

        assertEquals(Minimum.of(x).value(), qq[0], TOL);
        assertEquals(Maximum.of(x).value(), qq[4], TOL);

        // aprox -1.96
        assertEquals(-1.9562507056994485, qq[1], TOL);

        // aprox 0
        assertEquals(0.0011999109845885958, qq[2], TOL);

        // aprox 1.96
        assertEquals(1.9615708871881077, qq[3], TOL);
    }
}
