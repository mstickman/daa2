package structures;

import metrics.Metrics;

public class MyLinkedList implements IntList {

    private static class Node {
        int value;
        Node next;

        Node(int value) {
            this.value = value;
        }
    }

    private Node head;
    private Node tail;
    private int size;
    private final Metrics metrics = new Metrics();

    @Override
    public void add(int x) {
        Node n = new Node(x);
        if (head == null) {
            head = n;
            tail = n;
        } else {
            tail.next = n;
            tail = n;
        }
        metrics.moves += 2;
        size++;
    }

    @Override
    public void add(int index, int x) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("index = " + index + ", size = " + size);
        }
        if (index == size) {
            add(x);
            return;
        }
        Node n = new Node(x);
        if (index == 0) {
            n.next = head;
            head = n;
            metrics.moves += 2;
            size++;
            return;
        }
        Node prev = head;
        for (int i = 0; i < index - 1; i++) {
            prev = prev.next;
            metrics.steps++;
        }
        n.next = prev.next;
        prev.next = n;
        metrics.moves += 2;
        size++;
    }

    @Override
    public int remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("index = " + index + ", size = " + size);
        }
        int removed;
        if (index == 0) {
            removed = head.value;
            head = head.next;
            metrics.moves++;
            if (head == null) {
                tail = null;
                metrics.moves++;
            }
        } else {
            Node prev = head;
            for (int i = 0; i < index - 1; i++) {
                prev = prev.next;
                metrics.steps++;
            }
            Node target = prev.next;
            removed = target.value;
            prev.next = target.next;
            metrics.moves++;
            if (target == tail) {
                tail = prev;
                metrics.moves++;
            }
        }
        size--;
        return removed;
    }

    @Override
    public int get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("index = " + index + ", size = " + size);
        }
        Node cur = head;
        for (int i = 0; i < index; i++) {
            cur = cur.next;
            metrics.steps++;
        }
        return cur.value;
    }

    @Override
    public boolean contains(int x) {
        Node cur = head;
        while (cur != null) {
            metrics.comparisons++;
            if (cur.value == x) {
                return true;
            }
            cur = cur.next;
            metrics.steps++;
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Metrics getMetrics() {
        return metrics;
    }
}
