package rapaio.math.linear;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import rapaio.core.RandomSource;
import rapaio.math.linear.dense.SolidDVector;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by <a href="mailto:padreati@yahoo.com">Aurelian Tutuianu</a> on 1/14/20.
 */
public abstract class StandardDMatrixTest {

    protected static final double TOL = 1e-15;

    @BeforeEach
    void beforeEach() {
        RandomSource.setSeed(123);
    }

    protected abstract DMatrix generateSequential(int n, int m);

    protected abstract DMatrix generateIdentity(int n);

    protected abstract DMatrix generateFill(int n, int m, double fill);

    protected abstract DMatrix generateWrap(double[][] values);

    protected abstract String className();

    @Test
    void testMapRow() {
        DMatrix m = generateSequential(10, 11);
        DVector vector1 = m.mapRow(3);
        DVector vector2 = m.mapRowCopy(3);
        for (int i = 0; i < vector1.size(); i++) {
            assertEquals(33.0 + i, vector1.get(i), TOL);
            assertEquals(33.0 + i, vector2.get(i), TOL);
        }
    }

    @Test
    void testMapRows() {
        DMatrix m = generateSequential(10, 11);

        DMatrix view = m.mapRows(3, 4);
        DMatrix copy = m.mapRowsCopy(3, 4);

        for (int i = 0; i < view.rowCount(); i++) {
            for (int j = 0; j < view.colCount(); j++) {
                assertEquals(view.get(i, j), copy.get(i, j), TOL);
            }
        }

        view.set(0, 0, 100);
        copy.set(0, 0, 200);

        assertEquals(m.get(3, 0), view.get(0, 0), TOL);
        assertEquals(100, m.get(3, 0), TOL);
        assertEquals(200, copy.get(0, 0), TOL);
    }

    @Test
    void testRangeRows() {
        DMatrix m = generateSequential(10, 11);

        DMatrix view = m.rangeRows(3, 5);
        DMatrix copy = m.rangeRowsCopy(3, 5);

        for (int i = 0; i < view.rowCount(); i++) {
            for (int j = 0; j < view.colCount(); j++) {
                assertEquals(view.get(i, j), copy.get(i, j), TOL);
            }
        }

        view.set(0, 0, 100);
        copy.set(0, 0, 200);

        assertEquals(m.get(3, 0), view.get(0, 0), TOL);
        assertEquals(100, m.get(3, 0), TOL);
        assertEquals(200, copy.get(0, 0), TOL);
    }

    @Test
    void testRemoveRows() {
        DMatrix m = generateSequential(10, 11);

        DMatrix view = m.removeRows(0, 1, 2, 4, 6, 7, 8, 9, 10);
        DMatrix copy = m.removeRowsCopy(0, 1, 2, 4, 6, 7, 8, 9, 10);

        for (int i = 0; i < view.rowCount(); i++) {
            for (int j = 0; j < view.colCount(); j++) {
                assertEquals(view.get(i, j), copy.get(i, j), TOL);
            }
        }

        view.set(0, 0, 100);
        copy.set(0, 0, 200);

        assertEquals(m.get(3, 0), view.get(0, 0), TOL);
        assertEquals(100, m.get(3, 0), TOL);
        assertEquals(200, copy.get(0, 0), TOL);
    }

    @Test
    void testMapCol() {
        DMatrix m = generateSequential(10, 11);
        DVector vector1 = m.mapCol(3);
        DVector vector2 = m.mapColCopy(3);
        for (int i = 0; i < vector1.size(); i++) {
            assertEquals(3.0 + i * 11, vector1.get(i), TOL);
            assertEquals(3.0 + i * 11, vector2.get(i), TOL);
        }
    }

    @Test
    void testMapCols() {
        DMatrix m = generateSequential(10, 11);

        DMatrix view = m.mapCols(3, 4);
        DMatrix copy = m.mapColsCopy(3, 4);

        for (int i = 0; i < view.rowCount(); i++) {
            for (int j = 0; j < view.colCount(); j++) {
                assertEquals(view.get(i, j), copy.get(i, j), TOL);
            }
        }

        view.set(0, 0, 100);
        copy.set(0, 0, 200);

        assertEquals(m.get(0, 3), view.get(0, 0), TOL);
        assertEquals(100, m.get(0, 3), TOL);
        assertEquals(200, copy.get(0, 0), TOL);
    }

    @Test
    void testRangeCols() {
        DMatrix m = generateSequential(10, 11);

        DMatrix view = m.rangeCols(3, 5);
        DMatrix copy = m.rangeColsCopy(3, 5);

        for (int i = 0; i < view.rowCount(); i++) {
            for (int j = 0; j < view.colCount(); j++) {
                assertEquals(view.get(i, j), copy.get(i, j), TOL);
            }
        }

        view.set(0, 0, 100);
        copy.set(0, 0, 200);

        assertEquals(m.get(0, 3), view.get(0, 0), TOL);
        assertEquals(100, m.get(0, 3), TOL);
        assertEquals(200, copy.get(0, 0), TOL);
    }

    @Test
    void testRemoveCols() {
        DMatrix m = generateSequential(10, 11);

        DMatrix view = m.removeCols(0, 1, 2, 4, 6, 7, 8, 9, 10);
        DMatrix copy = m.removeColsCopy(0, 1, 2, 4, 6, 7, 8, 9, 10);

        for (int i = 0; i < view.rowCount(); i++) {
            for (int j = 0; j < view.colCount(); j++) {
                assertEquals(view.get(i, j), copy.get(i, j), TOL);
            }
        }

        view.set(0, 0, 100);
        copy.set(0, 0, 200);

        assertEquals(m.get(0, 3), view.get(0, 0), TOL);
        assertEquals(100, m.get(0, 3), TOL);
        assertEquals(200, copy.get(0, 0), TOL);
    }

    @Test
    void testPlus() {
        DMatrix m1 = generateSequential(20, 10);
        DMatrix m2 = generateSequential(20, 10);

        DMatrix t1 = m1.copy().plus(m2);
        DMatrix t2 = m1.copy().plus(1);
        DMatrix t3 = m1.plus(1).plus(m2);

        assertEquals(20, t1.rowCount());
        assertEquals(10, t1.colCount());
        assertEquals(20, t2.rowCount());
        assertEquals(10, t2.colCount());
        assertEquals(20, t3.rowCount());
        assertEquals(10, t3.colCount());

        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 10; j++) {
                assertEquals(2 * (i * 10 + j), t1.get(i, j), TOL);
                assertEquals(i * 10 + j + 1, t2.get(i, j), TOL);
                assertEquals(2 * (i * 10 + j) + 1, t3.get(i, j));
            }
        }

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> generateSequential(10, 10).plus(generateSequential(11, 11)));
        assertEquals("Matrices are not conform with this operation.", ex.getMessage());
    }

    @Test
    void testMinus() {
        DMatrix m1 = generateSequential(20, 10);
        DMatrix m2 = generateSequential(20, 10);

        DMatrix t1 = m1.copy().minus(m2);
        DMatrix t2 = m1.copy().minus(1);
        DMatrix t3 = m1.minus(1).minus(m2);

        assertEquals(20, t1.rowCount());
        assertEquals(10, t1.colCount());
        assertEquals(20, t2.rowCount());
        assertEquals(10, t2.colCount());
        assertEquals(20, t3.rowCount());
        assertEquals(10, t3.colCount());

        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 10; j++) {
                assertEquals(0, t1.get(i, j), TOL);
                assertEquals(i * 10 + j - 1, t2.get(i, j), TOL);
                assertEquals(-1, t3.get(i, j));
            }
        }

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> generateSequential(10, 10).minus(generateSequential(9, 10)));
        assertEquals("Matrices are not conform with this operation.", ex.getMessage());
    }

    @Test
    void testTimes() {
        DMatrix m1 = generateSequential(20, 10);
        DMatrix m2 = generateSequential(20, 10);

        DMatrix t1 = m1.copy().times(m2);
        DMatrix t2 = m1.copy().times(2);
        DMatrix t3 = m1.times(2).times(m2);

        assertEquals(20, t1.rowCount());
        assertEquals(10, t1.colCount());
        assertEquals(20, t2.rowCount());
        assertEquals(10, t2.colCount());
        assertEquals(20, t3.rowCount());
        assertEquals(10, t3.colCount());

        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 10; j++) {
                assertEquals(Math.pow(i * 10 + j, 2), t1.get(i, j), TOL);
                assertEquals(2 * (i * 10 + j), t2.get(i, j), TOL);
                assertEquals(2 * Math.pow(i * 10 + j, 2), t3.get(i, j));
            }
        }

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> generateSequential(10, 10).times(generateSequential(10, 9)));
        assertEquals("Matrices are not conform with this operation.", ex.getMessage());

    }

    @Test
    void testDiv() {
        DMatrix m1 = generateSequential(20, 10);
        DMatrix m2 = generateSequential(20, 10);

        DMatrix t1 = m1.copy().div(m2);
        DMatrix t2 = m1.copy().div(2);
        DMatrix t3 = m1.div(2).div(m2);

        assertEquals(20, t1.rowCount());
        assertEquals(10, t1.colCount());
        assertEquals(20, t2.rowCount());
        assertEquals(10, t2.colCount());
        assertEquals(20, t3.rowCount());
        assertEquals(10, t3.colCount());

        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 10; j++) {
                if (i == 0 && j == 0) {
                    assertEquals(Double.NaN, t1.get(i, j), TOL);
                    assertEquals(0, t2.get(i, j), TOL);
                    assertEquals(Double.NaN, t3.get(i, j));
                } else {
                    assertEquals(1, t1.get(i, j), TOL);
                    assertEquals((i * 10. + j) / 2, t2.get(i, j), TOL);
                    assertEquals(1 / 2., t3.get(i, j));
                }
            }
        }

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> generateSequential(10, 10).div(generateSequential(9, 9)));
        assertEquals("Matrices are not conform with this operation.", ex.getMessage());
    }

    @Test
    void testDot() {

        DVector v = SolidDVector.ones(10);
        DMatrix m = generateSequential(10, 10);

        var m1 = m.dot(v);
        for (int i = 0; i < m1.size(); i++) {
            assertEquals(45 + 100 * i, m1.get(i), TOL);
        }

        DMatrix n1 = generateSequential(10, 20);
        DMatrix n2 = generateSequential(20, 30);

        var m2 = n1.dot(n2);
        assertEquals(10, m2.rowCount());
        assertEquals(30, m2.colCount());

        for (int i = 0; i < m2.rowCount(); i++) {
            for (int j = 0; j < m2.colCount(); j++) {
                assertEquals(n1.mapRow(i).dot(n2.mapCol(j)), m2.get(i, j), TOL);
            }
        }
    }

    @Test
    void testTrace() {
        for (int i = 1; i < 10; i++) {
            int len = i + 1;
            var m = generateSequential(len, len);
            assertEquals((len + 1) * (len) * (len - 1) / 2., m.trace(), TOL);
        }

        var ex = assertThrows(IllegalArgumentException.class, () -> generateSequential(2, 3).trace());
        assertEquals("Matrix is not squared, trace of the matrix is not defined.", ex.getMessage());
    }

    @Test
    void testDiag() {
        var m = generateSequential(10, 10);
        var d = m.diag();
        for (int i = 1; i < 10; i++) {
            assertEquals(i * 11, d.get(i), TOL);
        }
    }

    @Test
    void testScatter() {
        for (int i = 1; i < 10; i++) {
            var x = generateSequential(i, i);
            var s = x.scatter();

            var c = generateIdentity(i).minus(generateFill(i, i, 1.0 / i));
            var s2 = x.t().dot(c).dot(x);

            assertTrue(s2.isEqual(s, 1e-11));
        }
    }

    @Test
    void testRowMaxValues() {
        for (int i = 1; i < 10; i++) {
            var m = generateSequential(i, i);
            var max = m.rowMaxValues();
            for (int j = 0; j < max.size(); j++) {
                assertEquals((j + 1) * i - 1, max.get(j), TOL);
            }
        }
    }

    @Test
    void testIsEqual() {
        var m1 = generateWrap(new double[][]{
                {1, 1, 2},
                {1, 2, 4}
        });
        var m2 = generateWrap(new double[][]{
                {1d + 0x1.0p-47, 1, 2},
                {1, 2, 4}
        });

        System.out.println(String.format("%.100f", m1.get(0, 0)));
        System.out.println(String.format("%.100f", m2.get(0, 0)));

        assertTrue(m1.isEqual(m2));
        assertTrue(m2.isEqual(m1));
        assertTrue(m1.isEqual(m2, 1e-14));
        assertFalse(m1.isEqual(m2, 1e-30));

        assertFalse(m1.isEqual(generateSequential(2, 2)));
        assertFalse(m1.isEqual(generateSequential(3, 3)));
    }

    @Test
    void testPrintable() {

        var id3 = generateIdentity(3).plus(0x.1p-22);

        assertEquals(className() + "{rowCount:3, colCount:3, values:[" +
                "[1.000000014901,0.000000014901,0.000000014901]," +
                "[0.000000014901,1.000000014901,0.000000014901]," +
                "[0.000000014901,0.000000014901,1.000000014901]}", id3.toString());

        assertEquals("               [0]            [1]            [2] \n" +
                "[0] 1.000000014901 0.000000014901 0.000000014901 \n" +
                "[1] 0.000000014901 1.000000014901 0.000000014901 \n" +
                "[2] 0.000000014901 0.000000014901 1.000000014901 \n", id3.toSummary());

        assertEquals("               [0]            [1]            [2] \n" +
                "[0] 1.000000014901 0.000000014901 0.000000014901 \n" +
                "[1] 0.000000014901 1.000000014901 0.000000014901 \n" +
                "[2] 0.000000014901 0.000000014901 1.000000014901 \n", id3.toContent());

        assertEquals("               [0]            [1]            [2] \n" +
                "[0] 1.000000014901 0.000000014901 0.000000014901 \n" +
                "[1] 0.000000014901 1.000000014901 0.000000014901 \n" +
                "[2] 0.000000014901 0.000000014901 1.000000014901 \n", id3.toFullContent());


        var id25 = generateIdentity(25);

        assertEquals(className() + "{rowCount:25, colCount:25, values:[" +
                "[1,0,0,0,0,0,0,0,0,0,...]," +
                "[0,1,0,0,0,0,0,0,0,0,...]," +
                "[0,0,1,0,0,0,0,0,0,0,...]," +
                "[0,0,0,1,0,0,0,0,0,0,...]," +
                "[0,0,0,0,1,0,0,0,0,0,...]," +
                "[0,0,0,0,0,1,0,0,0,0,...]," +
                "[0,0,0,0,0,0,1,0,0,0,...]," +
                "[0,0,0,0,0,0,0,1,0,0,...]," +
                "[0,0,0,0,0,0,0,0,1,0,...]," +
                "[0,0,0,0,0,0,0,0,0,1,...]," +
                "...}", id25.toString());

        assertEquals("     [0] [1] [2] [3] [4] [5] [6] [7] [8] [9] [10] [11] [12] [13] [14] [15] [16] [17] [18] [19] ... [23] [24] \n" +
                        " [0]  1   0   0   0   0   0   0   0   0   0   0    0    0    0    0    0    0    0    0    0   ...  0    0   \n" +
                        " [1]  0   1   0   0   0   0   0   0   0   0   0    0    0    0    0    0    0    0    0    0   ...  0    0   \n" +
                        " [2]  0   0   1   0   0   0   0   0   0   0   0    0    0    0    0    0    0    0    0    0   ...  0    0   \n" +
                        " [3]  0   0   0   1   0   0   0   0   0   0   0    0    0    0    0    0    0    0    0    0   ...  0    0   \n" +
                        " [4]  0   0   0   0   1   0   0   0   0   0   0    0    0    0    0    0    0    0    0    0   ...  0    0   \n" +
                        " [5]  0   0   0   0   0   1   0   0   0   0   0    0    0    0    0    0    0    0    0    0   ...  0    0   \n" +
                        " [6]  0   0   0   0   0   0   1   0   0   0   0    0    0    0    0    0    0    0    0    0   ...  0    0   \n" +
                        " [7]  0   0   0   0   0   0   0   1   0   0   0    0    0    0    0    0    0    0    0    0   ...  0    0   \n" +
                        " [8]  0   0   0   0   0   0   0   0   1   0   0    0    0    0    0    0    0    0    0    0   ...  0    0   \n" +
                        " [9]  0   0   0   0   0   0   0   0   0   1   0    0    0    0    0    0    0    0    0    0   ...  0    0   \n" +
                        "[10]  0   0   0   0   0   0   0   0   0   0   1    0    0    0    0    0    0    0    0    0   ...  0    0   \n" +
                        "[11]  0   0   0   0   0   0   0   0   0   0   0    1    0    0    0    0    0    0    0    0   ...  0    0   \n" +
                        "[12]  0   0   0   0   0   0   0   0   0   0   0    0    1    0    0    0    0    0    0    0   ...  0    0   \n" +
                        "[13]  0   0   0   0   0   0   0   0   0   0   0    0    0    1    0    0    0    0    0    0   ...  0    0   \n" +
                        "[14]  0   0   0   0   0   0   0   0   0   0   0    0    0    0    1    0    0    0    0    0   ...  0    0   \n" +
                        "[15]  0   0   0   0   0   0   0   0   0   0   0    0    0    0    0    1    0    0    0    0   ...  0    0   \n" +
                        "[16]  0   0   0   0   0   0   0   0   0   0   0    0    0    0    0    0    1    0    0    0   ...  0    0   \n" +
                        "[17]  0   0   0   0   0   0   0   0   0   0   0    0    0    0    0    0    0    1    0    0   ...  0    0   \n" +
                        "[18]  0   0   0   0   0   0   0   0   0   0   0    0    0    0    0    0    0    0    1    0   ...  0    0   \n" +
                        "[19]  0   0   0   0   0   0   0   0   0   0   0    0    0    0    0    0    0    0    0    1   ...  0    0   \n" +
                        "...  ... ... ... ... ... ... ... ... ... ... ...  ...  ...  ...  ...  ...  ...  ...  ...  ...  ... ...  ...  \n" +
                        "[23]  0   0   0   0   0   0   0   0   0   0   0    0    0    0    0    0    0    0    0    0   ...  1    0   \n" +
                        "[24]  0   0   0   0   0   0   0   0   0   0   0    0    0    0    0    0    0    0    0    0   ...  0    1   \n",
                id25.toSummary());

        assertEquals("     [0] [1] [2] [3] [4] [5] [6] [7] [8] [9] [10] [11] [12] [13] [14] [15] [16] [17] [18] [19] ... [23] [24] \n" +
                        " [0]  1   0   0   0   0   0   0   0   0   0   0    0    0    0    0    0    0    0    0    0   ...  0    0   \n" +
                        " [1]  0   1   0   0   0   0   0   0   0   0   0    0    0    0    0    0    0    0    0    0   ...  0    0   \n" +
                        " [2]  0   0   1   0   0   0   0   0   0   0   0    0    0    0    0    0    0    0    0    0   ...  0    0   \n" +
                        " [3]  0   0   0   1   0   0   0   0   0   0   0    0    0    0    0    0    0    0    0    0   ...  0    0   \n" +
                        " [4]  0   0   0   0   1   0   0   0   0   0   0    0    0    0    0    0    0    0    0    0   ...  0    0   \n" +
                        " [5]  0   0   0   0   0   1   0   0   0   0   0    0    0    0    0    0    0    0    0    0   ...  0    0   \n" +
                        " [6]  0   0   0   0   0   0   1   0   0   0   0    0    0    0    0    0    0    0    0    0   ...  0    0   \n" +
                        " [7]  0   0   0   0   0   0   0   1   0   0   0    0    0    0    0    0    0    0    0    0   ...  0    0   \n" +
                        " [8]  0   0   0   0   0   0   0   0   1   0   0    0    0    0    0    0    0    0    0    0   ...  0    0   \n" +
                        " [9]  0   0   0   0   0   0   0   0   0   1   0    0    0    0    0    0    0    0    0    0   ...  0    0   \n" +
                        "[10]  0   0   0   0   0   0   0   0   0   0   1    0    0    0    0    0    0    0    0    0   ...  0    0   \n" +
                        "[11]  0   0   0   0   0   0   0   0   0   0   0    1    0    0    0    0    0    0    0    0   ...  0    0   \n" +
                        "[12]  0   0   0   0   0   0   0   0   0   0   0    0    1    0    0    0    0    0    0    0   ...  0    0   \n" +
                        "[13]  0   0   0   0   0   0   0   0   0   0   0    0    0    1    0    0    0    0    0    0   ...  0    0   \n" +
                        "[14]  0   0   0   0   0   0   0   0   0   0   0    0    0    0    1    0    0    0    0    0   ...  0    0   \n" +
                        "[15]  0   0   0   0   0   0   0   0   0   0   0    0    0    0    0    1    0    0    0    0   ...  0    0   \n" +
                        "[16]  0   0   0   0   0   0   0   0   0   0   0    0    0    0    0    0    1    0    0    0   ...  0    0   \n" +
                        "[17]  0   0   0   0   0   0   0   0   0   0   0    0    0    0    0    0    0    1    0    0   ...  0    0   \n" +
                        "[18]  0   0   0   0   0   0   0   0   0   0   0    0    0    0    0    0    0    0    1    0   ...  0    0   \n" +
                        "[19]  0   0   0   0   0   0   0   0   0   0   0    0    0    0    0    0    0    0    0    1   ...  0    0   \n" +
                        "...  ... ... ... ... ... ... ... ... ... ... ...  ...  ...  ...  ...  ...  ...  ...  ...  ...  ... ...  ...  \n" +
                        "[23]  0   0   0   0   0   0   0   0   0   0   0    0    0    0    0    0    0    0    0    0   ...  1    0   \n" +
                        "[24]  0   0   0   0   0   0   0   0   0   0   0    0    0    0    0    0    0    0    0    0   ...  0    1   \n",
                id25.toContent());

        assertEquals("     [0] [1] [2] [3] [4] [5] [6] [7] [8] [9] [10] [11] [12] [13] [14] [15] [16] [17] [18] [19] [20] [21] [22] [23] [24] \n" +
                " [0]  1   0   0   0   0   0   0   0   0   0   0    0    0    0    0    0    0    0    0    0    0    0    0    0    0   \n" +
                " [1]  0   1   0   0   0   0   0   0   0   0   0    0    0    0    0    0    0    0    0    0    0    0    0    0    0   \n" +
                " [2]  0   0   1   0   0   0   0   0   0   0   0    0    0    0    0    0    0    0    0    0    0    0    0    0    0   \n" +
                " [3]  0   0   0   1   0   0   0   0   0   0   0    0    0    0    0    0    0    0    0    0    0    0    0    0    0   \n" +
                " [4]  0   0   0   0   1   0   0   0   0   0   0    0    0    0    0    0    0    0    0    0    0    0    0    0    0   \n" +
                " [5]  0   0   0   0   0   1   0   0   0   0   0    0    0    0    0    0    0    0    0    0    0    0    0    0    0   \n" +
                " [6]  0   0   0   0   0   0   1   0   0   0   0    0    0    0    0    0    0    0    0    0    0    0    0    0    0   \n" +
                " [7]  0   0   0   0   0   0   0   1   0   0   0    0    0    0    0    0    0    0    0    0    0    0    0    0    0   \n" +
                " [8]  0   0   0   0   0   0   0   0   1   0   0    0    0    0    0    0    0    0    0    0    0    0    0    0    0   \n" +
                " [9]  0   0   0   0   0   0   0   0   0   1   0    0    0    0    0    0    0    0    0    0    0    0    0    0    0   \n" +
                "[10]  0   0   0   0   0   0   0   0   0   0   1    0    0    0    0    0    0    0    0    0    0    0    0    0    0   \n" +
                "[11]  0   0   0   0   0   0   0   0   0   0   0    1    0    0    0    0    0    0    0    0    0    0    0    0    0   \n" +
                "[12]  0   0   0   0   0   0   0   0   0   0   0    0    1    0    0    0    0    0    0    0    0    0    0    0    0   \n" +
                "[13]  0   0   0   0   0   0   0   0   0   0   0    0    0    1    0    0    0    0    0    0    0    0    0    0    0   \n" +
                "[14]  0   0   0   0   0   0   0   0   0   0   0    0    0    0    1    0    0    0    0    0    0    0    0    0    0   \n" +
                "[15]  0   0   0   0   0   0   0   0   0   0   0    0    0    0    0    1    0    0    0    0    0    0    0    0    0   \n" +
                "[16]  0   0   0   0   0   0   0   0   0   0   0    0    0    0    0    0    1    0    0    0    0    0    0    0    0   \n" +
                "[17]  0   0   0   0   0   0   0   0   0   0   0    0    0    0    0    0    0    1    0    0    0    0    0    0    0   \n" +
                "[18]  0   0   0   0   0   0   0   0   0   0   0    0    0    0    0    0    0    0    1    0    0    0    0    0    0   \n" +
                "[19]  0   0   0   0   0   0   0   0   0   0   0    0    0    0    0    0    0    0    0    1    0    0    0    0    0   \n" +
                "[20]  0   0   0   0   0   0   0   0   0   0   0    0    0    0    0    0    0    0    0    0    1    0    0    0    0   \n" +
                "[21]  0   0   0   0   0   0   0   0   0   0   0    0    0    0    0    0    0    0    0    0    0    1    0    0    0   \n" +
                "[22]  0   0   0   0   0   0   0   0   0   0   0    0    0    0    0    0    0    0    0    0    0    0    1    0    0   \n" +
                "[23]  0   0   0   0   0   0   0   0   0   0   0    0    0    0    0    0    0    0    0    0    0    0    0    1    0   \n" +
                "[24]  0   0   0   0   0   0   0   0   0   0   0    0    0    0    0    0    0    0    0    0    0    0    0    0    1   \n",
                id25.toFullContent());


    }
}
