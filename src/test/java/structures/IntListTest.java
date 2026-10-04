package structures;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

abstract class IntListTest {

    abstract IntList create();

    private IntList filled(int... values) {
        IntList list = create();
        for (int v : values) {
            list.add(v);
        }
        return list;
    }

    private void assertSame(ArrayList<Integer> expected, IntList actual) {
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals((int) expected.get(i), actual.get(i));
        }
    }


    @Test
    void randomOperationsMatchArrayList() {
        Random rnd = new Random(1);
        IntList list = create();
        ArrayList<Integer> expected = new ArrayList<>();
        for (int step = 0; step < 5000; step++) {
            int op = rnd.nextInt(3);
            if (op == 0) {
                int x = rnd.nextInt(100);
                list.add(x);
                expected.add(x);
            } else if (op == 1) {
                int index = rnd.nextInt(expected.size() + 1);
                int x = rnd.nextInt(100);
                list.add(index, x);
                expected.add(index, x);
            } else if (!expected.isEmpty()) {
                int index = rnd.nextInt(expected.size());
                assertEquals((int) expected.remove(index), list.remove(index));
            }
        }
        assertSame(expected, list);
    }

    @Test
    void containsMatchesArrayList() {
        Random rnd = new Random(2);
        IntList list = create();
        ArrayList<Integer> expected = new ArrayList<>();
        for (int i = 0; i < 500; i++) {
            int x = rnd.nextInt(1000);
            list.add(x);
            expected.add(x);
        }
        for (int q = -5; q < 1005; q++) {
            assertEquals(expected.contains(q), list.contains(q));
        }
    }

    @Test
    void manyElementsAfterResize() {
        IntList list = create();
        for (int i = 0; i < 10000; i++) {
            list.add(i);
        }
        assertEquals(10000, list.size());
        for (int i = 0; i < 10000; i++) {
            assertEquals(i, list.get(i));
        }
    }


    @Test
    void emptyStructure() {
        IntList list = create();
        assertEquals(0, list.size());
        assertFalse(list.contains(5));
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(0));
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(0));
    }

    @Test
    void oneElement() {
        IntList list = filled(7);
        assertEquals(1, list.size());
        assertEquals(7, list.get(0));
        assertTrue(list.contains(7));
        assertFalse(list.contains(8));
        assertEquals(7, list.remove(0));
        assertEquals(0, list.size());
        assertFalse(list.contains(7));
    }

    @Test
    void duplicateValues() {
        IntList list = filled(3, 3, 3, 3);
        assertEquals(4, list.size());
        assertTrue(list.contains(3));
        list.remove(1);
        assertEquals(3, list.size());
        assertTrue(list.contains(3));
        list.remove(0);
        list.remove(0);
        list.remove(0);
        assertFalse(list.contains(3));
    }

    @Test
    void firstAndLastIndex() {
        IntList list = filled(10, 20, 30);
        assertEquals(10, list.get(0));
        assertEquals(30, list.get(2));
        list.add(0, 5);
        list.add(4, 99);
        assertEquals(5, list.get(0));
        assertEquals(99, list.get(4));
        assertEquals(99, list.remove(4));
        assertEquals(5, list.remove(0));
        assertEquals(3, list.size());
        assertEquals(10, list.get(0));
        assertEquals(30, list.get(2));
        list.add(77);
        assertEquals(77, list.get(3));
    }

    @Test
    void invalidIndexThrows() {
        IntList list = filled(1, 2, 3);
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(3));
        assertThrows(IndexOutOfBoundsException.class, () -> list.add(-1, 5));
        assertThrows(IndexOutOfBoundsException.class, () -> list.add(4, 5));
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(3));
        assertEquals(3, list.size());
    }

    @Test
    void metricsAreCounted() {
        IntList list = filled(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        list.getMetrics().reset();
        list.add(5, 100);
        assertTrue(list.getMetrics().moves > 0);
        list.getMetrics().reset();
        list.contains(-1);
        assertEquals(11, list.getMetrics().comparisons);
    }
}
