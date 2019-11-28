package rapaio.data;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import rapaio.core.RandomSource;

import java.util.Arrays;
import java.util.Iterator;

import static org.junit.Assert.*;

/**
 * Created by <a href="mailto:padreati@yahoo.com">Aurelian Tutuianu</a> on 9/19/18.
 */
public class VarStringTest {

    private String[] largeValues;
    private String[] shortValues = new String[]{"Ana", "are", "mere"};

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() {
        RandomSource.setSeed(123);
        largeValues = new String[100];
        for (int i = 0; i < 100; i++) {
            StringBuilder sb = new StringBuilder();
            int len = RandomSource.nextInt(20) + 1;
            for (int j = 0; j < len; j++) {
                sb.append((char) ('a' + RandomSource.nextInt(26)));
            }
            largeValues[i] = sb.toString();
        }
    }

    @Test
    public void testBuilders() {

        VarString empty1 = VarString.empty();
        assertEquals(0, empty1.rowCount());

        VarString empty2 = VarString.empty(10);
        assertEquals(10, empty2.rowCount());
        for (int i = 0; i < 10; i++) {
            assertNull(empty2.getLabel(i));
        }

        VarString empty3 = empty2.newInstance(empty2.rowCount());
        assertTrue(empty2.deepEquals(empty3));

        VarString copy1 = VarString.copy(largeValues);
        assertTrue(copy1.deepEquals(VarString.copy(Arrays.asList(largeValues))));
        assertTrue(copy1.deepEquals(VarString.wrap(Arrays.asList(largeValues))));

        Iterator<String> it = Arrays.asList(largeValues).iterator();
        assertTrue(copy1.deepEquals(VarString.from(largeValues.length, it::next)));

        VarString copy2 = VarString.copy(largeValues).withName("copy");
        assertEquals("copy", copy2.name());

        VarString copy3 = copy2.copy();
        assertTrue(copy2.deepEquals(copy3));

        assertEquals("VarText [name:\"copy\", rowCount:100, values: omt, hyhvnlwuznrcbaqk, iyedusfwdkelqbxete, ovascfqio, maajxky, rnlrytgkbgic, ahcbrqdsxv, hpfqgtmdypsbzxvf, oeygjbumaa, k, ..., ldif, tciudeieeo]", copy2.toString());
    }

    @Test
    public void testAddRemoveClear() {
        VarString text = VarString.copy(shortValues);
        text.addRows(3);

        assertEquals(6, text.rowCount());
        for (int i = 0; i < 3; i++) {
            assertNull(text.getLabel(3 + i));
        }

        text.removeRow(2);
        text.removeRow(2);
        assertEquals("Ana", text.getLabel(0));
        assertEquals("are", text.getLabel(1));
        assertNull(text.getLabel(2));
        assertNull(text.getLabel(3));

        text.clearRows();
        assertEquals(0, text.rowCount());
    }

    @Test
    public void testGetDouble() {
        expectedException.expect(OperationNotAvailableException.class);
        VarString.empty(1).getDouble(0);
    }

    @Test
    public void testSetDouble() {
        expectedException.expect(OperationNotAvailableException.class);
        VarString.empty(1).setDouble(0, 0);
    }

    @Test
    public void testAddDouble() {
        expectedException.expect(OperationNotAvailableException.class);
        VarString.empty(1).addDouble(10.f);
    }

    @Test
    public void testGetInt() {
        expectedException.expect(OperationNotAvailableException.class);
        VarString.empty(1).getInt(0);
    }

    @Test
    public void testSetInt() {
        expectedException.expect(OperationNotAvailableException.class);
        VarString.empty(1).setInt(0, 0);
    }

    @Test
    public void testAddInt() {
        expectedException.expect(OperationNotAvailableException.class);
        VarString.empty(1).addInt(10);
    }

    @Test
    public void testGetLong() {
        expectedException.expect(OperationNotAvailableException.class);
        VarString.empty(1).getLong(0);
    }

    @Test
    public void testSetLong() {
        expectedException.expect(OperationNotAvailableException.class);
        VarString.empty(1).setLong(0, 0);
    }

    @Test
    public void testAddLong() {
        expectedException.expect(OperationNotAvailableException.class);
        VarString.empty(1).addLong(10);
    }

    @Test
    public void testSetLevels() {
        expectedException.expect(OperationNotAvailableException.class);
        VarString.empty(1).setLevels(new String[]{});
    }

    @Test
    public void testLevels() {
        expectedException.expect(OperationNotAvailableException.class);
        VarString.empty(1).levels();
    }

    @Test
    public void testAddSetLevel() {
        VarString x = VarString.copy("Ana");
        x.setLabel(0, "Maria");
        x.addLabel("John");

        assertEquals(2, x.rowCount());
        assertEquals("Maria", x.getLabel(0));
        assertEquals("John", x.getLabel(1));
    }

    @Test
    public void testMissingOperations() {
        VarString x = VarString.empty(1);
        assertEquals(1, x.rowCount());
        assertNull(x.getLabel(0));

        x.setLabel(0, "l1");
        x.addLabel("l2");

        assertEquals("l1", x.getLabel(0));
        assertEquals("l2", x.getLabel(1));

        x.addLabel("l3");
        x.setMissing(1);
        x.addMissing();

        assertEquals("l1", x.getLabel(0));
        assertTrue(x.isMissing(1));
        assertEquals("l3", x.getLabel(2));
        assertTrue(x.isMissing(3));
    }
}