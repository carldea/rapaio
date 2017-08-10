package rapaio.core.distributions;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by <a href="mailto:padreati@yahoo.com">Aurelian Tutuianu</a> on 8/10/17.
 */
public class FischerTest {

    private static final double TOL = 1e-12;

    @Test
    public void testBasic() {

        Fischer f = new Fischer(4, 7);

        assertEquals("Fischer(4,7)", f.name());
        assertEquals(0, f.min(), TOL);
        assertEquals(Double.POSITIVE_INFINITY, f.max(), TOL);
        assertEquals(1.4, f.mean(), TOL);
        assertEquals(.7, f.mode(), TOL);
        assertEquals(2.94, f.var(), TOL);
        assertEquals(Double.NaN, f.skewness(), TOL);
    }

    @Test
    public void testWithR() {

        Fischer f1_1 = new Fischer(1, 1);
        assertEquals(0.13207992798466983153, f1_1.pdf(1.2), TOL);
        assertEquals(0.52897726983585657834, f1_1.cdf(1.2), TOL);

        assertEquals(0.025085630936916514244, f1_1.quantile(0.1), TOL);
        assertEquals(0.52786404500042105603, f1_1.quantile(0.4), TOL);
        assertEquals(1, f1_1.quantile(0.5), TOL);
        assertEquals(1.8944271909999135239, f1_1.quantile(0.6), TOL);
        assertEquals(39.86345818906141858, f1_1.quantile(0.9), TOL);

        Fischer f3 = new Fischer(3, 3);

        assertEquals(0.26197671666380800692, f3.pdf(1.2), TOL);
        assertEquals(0.55779470866887481684, f3.cdf(1.2), TOL);

        assertEquals(0.18550214375530638122, f3.quantile(0.1), TOL);
        assertEquals(0.72750926395022452731, f3.quantile(0.4), TOL);
        assertEquals(1.0000000000000008882, f3.quantile(0.5), TOL);
        assertEquals(1.3745529432439211881, f3.quantile(0.6), TOL);
        assertEquals(5.3907732803297836455, f3.quantile(0.9), TOL);
    }
}
