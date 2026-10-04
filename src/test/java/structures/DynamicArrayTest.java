package structures;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DynamicArrayTest extends IntListTest {

    @Override
    IntList create() {
        return new DynamicArray();
    }

    @Test
    void getIsOneStep() {
        DynamicArray a = new DynamicArray();
        for (int i = 0; i < 100; i++) {
            a.add(i);
        }
        a.getMetrics().reset();
        a.get(77);
        assertEquals(1, a.getMetrics().steps);
    }

    @Test
    void removeFirstShiftsAllElements() {
        DynamicArray a = new DynamicArray();
        for (int i = 0; i < 50; i++) {
            a.add(i);
        }
        a.getMetrics().reset();
        a.remove(0);
        assertEquals(49, a.getMetrics().moves);
    }

    @Test
    void growsWithoutLosingData() {
        DynamicArray a = new DynamicArray(1);
        for (int i = 0; i < 100; i++) {
            a.add(i * 2);
        }
        for (int i = 0; i < 100; i++) {
            assertEquals(i * 2, a.get(i));
        }
    }
}
