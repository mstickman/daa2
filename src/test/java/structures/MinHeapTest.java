package structures;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class MinHeapTest {

    @Test
    void emptyHeapThrows() {
        MinHeap h = new MinHeap();
        assertTrue(h.isEmpty());
        assertThrows(IllegalStateException.class, () -> h.peekMin());
        assertThrows(IllegalStateException.class, () -> h.extractMin());
    }

    @Test
    void oneElement() {
        MinHeap h = new MinHeap();
        h.insert(42);
        assertEquals(42, h.peekMin());
        assertEquals(1, h.size());
        assertEquals(42, h.extractMin());
        assertTrue(h.isEmpty());
        assertThrows(IllegalStateException.class, () -> h.extractMin());
    }

    @Test
    void duplicateValues() {
        MinHeap h = new MinHeap();
        for (int i = 0; i < 20; i++) {
            h.insert(5);
            assertTrue(h.isHeap());
        }
        for (int i = 0; i < 20; i++) {
            assertEquals(5, h.extractMin());
            assertTrue(h.isHeap());
        }
        assertTrue(h.isEmpty());
    }

    @Test
    void heapPropertyAfterEveryInsertAndExtract() {
        Random rnd = new Random(3);
        MinHeap h = new MinHeap();
        for (int i = 0; i < 2000; i++) {
            h.insert(rnd.nextInt(500));
            assertTrue(h.isHeap(), "broken after insert #" + i);
        }
        while (!h.isEmpty()) {
            h.extractMin();
            assertTrue(h.isHeap(), "broken after extractMin");
        }
    }

    @Test
    void peekMinIsSmallest() {
        Random rnd = new Random(4);
        MinHeap h = new MinHeap();
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < 1000; i++) {
            int x = rnd.nextInt(100000);
            h.insert(x);
            min = Math.min(min, x);
            assertEquals(min, h.peekMin());
        }
    }

    @Test
    void sortedOutputMatchesArraysSort() {
        Random rnd = new Random(5);
        int n = 5000;
        int[] values = new int[n];
        MinHeap h = new MinHeap();
        for (int i = 0; i < n; i++) {
            values[i] = rnd.nextInt(1000) - 500;
            h.insert(values[i]);
        }
        int[] expected = values.clone();
        Arrays.sort(expected);
        for (int i = 0; i < n; i++) {
            assertEquals(expected[i], h.extractMin());
        }
    }

    @Test
    void buildHeapWorks() {
        Random rnd = new Random(6);
        for (int n : new int[]{0, 1, 2, 3, 10, 1000}) {
            int[] values = new int[n];
            for (int i = 0; i < n; i++) {
                values[i] = rnd.nextInt(10000);
            }
            MinHeap h = MinHeap.buildHeap(values);
            assertTrue(h.isHeap());
            assertEquals(n, h.size());
            int[] expected = values.clone();
            Arrays.sort(expected);
            for (int i = 0; i < n; i++) {
                assertEquals(expected[i], h.extractMin());
            }
        }
    }

    @Test
    void buildHeapUsesFewerComparisonsThanInsertsOnDescendingInput() {
        int n = 10000;
        int[] values = new int[n];
        for (int i = 0; i < n; i++) {
            values[i] = n - i;
        }
        MinHeap inserted = new MinHeap();
        for (int v : values) {
            inserted.insert(v);
        }
        MinHeap built = MinHeap.buildHeap(values);
        assertTrue(built.getMetrics().comparisons < inserted.getMetrics().comparisons);
        assertTrue(built.getMetrics().comparisons <= 2L * n);
    }
}
