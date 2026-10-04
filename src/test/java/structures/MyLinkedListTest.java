package structures;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyLinkedListTest extends IntListTest {

    @Override
    IntList create() {
        return new MyLinkedList();
    }

    @Test
    void getCostsAboutIndexSteps() {
        MyLinkedList l = new MyLinkedList();
        for (int i = 0; i < 100; i++) {
            l.add(i);
        }
        l.getMetrics().reset();
        l.get(77);
        assertEquals(77, l.getMetrics().steps);
    }

    @Test
    void insertAtHeadIsConstant() {
        MyLinkedList l = new MyLinkedList();
        for (int i = 0; i < 1000; i++) {
            l.add(i);
        }
        l.getMetrics().reset();
        l.add(0, -1);
        assertEquals(0, l.getMetrics().steps);
        assertTrue(l.getMetrics().moves > 0);
    }

    @Test
    void middleInsertWalksToTheMiddle() {
        MyLinkedList l = new MyLinkedList();
        for (int i = 0; i < 100; i++) {
            l.add(i);
        }
        l.getMetrics().reset();
        l.add(50, -1);
        assertEquals(49, l.getMetrics().steps);
        assertEquals(-1, l.get(50));
    }

    @Test
    void removeLastUpdatesTail() {
        MyLinkedList l = new MyLinkedList();
        l.add(1);
        l.add(2);
        l.add(3);
        l.remove(2);
        l.add(4);
        assertEquals(3, l.size());
        assertEquals(4, l.get(2));
    }

    @Test
    void emptyAfterRemovingEverythingThenAddAgain() {
        MyLinkedList l = new MyLinkedList();
        l.add(1);
        l.remove(0);
        l.add(2);
        assertEquals(1, l.size());
        assertEquals(2, l.get(0));
    }
}
