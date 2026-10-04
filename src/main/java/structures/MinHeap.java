package structures;

import metrics.Metrics;

public class MinHeap {
    private int[] data;
    private int size;
    private final Metrics metrics = new Metrics();

    public MinHeap() {
        data = new int[10];
    }

    private MinHeap(int capacity) {
        data = new int[Math.max(capacity, 10)];
    }

    private void grow() {
        int[] bigger = new int[data.length * 2];
        for (int i = 0; i < size; i++) {
            bigger[i] = data[i];
            metrics.moves++;
        }
        data = bigger;
    }

    private void swap(int a, int b) {
        int tmp = data[a];
        data[a] = data[b];
        data[b] = tmp;
        metrics.moves += 2;
    }

    public void insert(int x) {
        if (size == data.length) {
            grow();
        }
        data[size] = x;
        size++;
        bubbleUp(size - 1);
    }

    private void bubbleUp(int i) {
        while (i > 0) {
            int parent = (i - 1) / 2;
            metrics.steps++;
            metrics.comparisons++;
            if (data[parent] <= data[i]) {
                break;
            }
            swap(parent, i);
            i = parent;
        }
    }

    public int peekMin() {
        if (size == 0) {
            throw new IllegalStateException("heap is empty");
        }
        metrics.steps++;
        return data[0];
    }

    public int extractMin() {
        if (size == 0) {
            throw new IllegalStateException("heap is empty");
        }
        metrics.steps++;
        int min = data[0];
        size--;
        if (size > 0) {
            data[0] = data[size];
            metrics.moves++;
            bubbleDown(0);
        }
        return min;
    }

    private void bubbleDown(int i) {
        while (true) {
            int left = 2 * i + 1;
            if (left >= size) {
                break;
            }
            int smallest = left;
            metrics.steps++;
            int right = left + 1;
            if (right < size) {
                metrics.steps++;
                metrics.comparisons++;
                if (data[right] < data[left]) {
                    smallest = right;
                }
            }
            metrics.comparisons++;
            if (data[i] <= data[smallest]) {
                break;
            }
            swap(i, smallest);
            i = smallest;
        }
    }

    public static MinHeap buildHeap(int[] array) {
        MinHeap h = new MinHeap(array.length);
        for (int i = 0; i < array.length; i++) {
            h.data[i] = array[i];
        }
        h.size = array.length;
        for (int i = h.size / 2 - 1; i >= 0; i--) {
            h.bubbleDown(i);
        }
        return h;
    }

    public boolean isHeap() {
        for (int i = 1; i < size; i++) {
            if (data[(i - 1) / 2] > data[i]) {
                return false;
            }
        }
        return true;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public Metrics getMetrics() {
        return metrics;
    }
}
