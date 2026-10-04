package benchmark;

import metrics.Metrics;
import structures.DynamicArray;
import structures.IntList;
import structures.MinHeap;
import structures.MyLinkedList;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;
import java.util.function.Supplier;

public class Benchmark {

    static final int[] SIZES = {100, 1000, 10000, 100000};
    static final int WARMUP_RUNS = 3;
    static final int MEASURED_RUNS = 5;
    static final long SEED = 42;

    static long sink;

    static abstract class Case {
        Metrics metrics;
        abstract void prepare();
        abstract void run();
    }

    static class Result {
        double timeMs;
        long steps, moves, comparisons;
    }

    static class Data {
        int[] values;
        int[] indexes;
        int[] queries;
    }

    static Data makeData(int n) {
        Random rnd = new Random(SEED);
        Data d = new Data();
        d.values = new int[n];
        for (int i = 0; i < n; i++) {
            d.values[i] = rnd.nextInt(1_000_000);
        }
        d.indexes = new int[10_000];
        for (int i = 0; i < d.indexes.length; i++) {
            d.indexes[i] = rnd.nextInt(n);
        }
        d.queries = new int[1_000];
        for (int i = 0; i < d.queries.length; i++) {
            if (i % 2 == 0) {
                d.queries[i] = d.values[rnd.nextInt(n)];
            } else {
                d.queries[i] = -1 - rnd.nextInt(1000);
            }
        }
        return d;
    }

    static Result measure(Case c) {
        for (int i = 0; i < WARMUP_RUNS; i++) {
            c.prepare();
            c.run();
        }
        double[] times = new double[MEASURED_RUNS];
        for (int i = 0; i < MEASURED_RUNS; i++) {
            c.prepare();
            long start = System.nanoTime();
            c.run();
            long end = System.nanoTime();
            times[i] = (end - start) / 1000000.0;
        }
        Arrays.sort(times);
        Result r = new Result();
        r.timeMs = times[MEASURED_RUNS / 2];
        r.steps = c.metrics.steps;
        r.moves = c.metrics.moves;
        r.comparisons = c.metrics.comparisons;
        return r;
    }

    static IntList fill(Supplier<IntList> factory, int[] values) {
        IntList list = factory.get();
        for (int v : values) {
            list.add(v);
        }
        return list;
    }

    static Case w1(final Supplier<IntList> factory, final Data d) {
        return new Case() {
            IntList list;

            void prepare() {
                list = fill(factory, d.values);
                list.getMetrics().reset();
                metrics = list.getMetrics();
            }

            void run() {
                long sum = 0;
                for (int idx : d.indexes) {
                    sum += list.get(idx);
                }
                sink += sum;
            }
        };
    }

    static Case w2(final Supplier<IntList> factory, final Data d) {
        return new Case() {
            IntList list;

            void prepare() {
                list = fill(factory, d.values);
                list.getMetrics().reset();
                metrics = list.getMetrics();
            }

            void run() {
                int found = 0;
                for (int q : d.queries) {
                    if (list.contains(q)) {
                        found++;
                    }
                }
                sink += found;
            }
        };
    }

    static Case w3(final Supplier<IntList> factory, final Data d, final boolean head) {
        return new Case() {
            IntList list;

            void prepare() {
                list = fill(factory, d.values);
                list.getMetrics().reset();
                metrics = list.getMetrics();
            }

            void run() {
                int pos = head ? 0 : d.values.length / 2;
                for (int k = 0; k < 1000; k++) {
                    list.add(pos, k);
                }
                for (int k = 0; k < 1000; k++) {
                    list.remove(pos);
                }
                sink += list.size();
            }
        };
    }

    static Case w4(final Data d) {
        return new Case() {
            MinHeap heap;

            void prepare() {
                heap = new MinHeap();
                metrics = heap.getMetrics();
            }

            void run() {
                for (int v : d.values) {
                    heap.insert(v);
                }
                int prev = Integer.MIN_VALUE;
                for (int i = 0; i < d.values.length; i++) {
                    int m = heap.extractMin();
                    if (m < prev) {
                        throw new IllegalStateException("extractMin is not in non-decreasing order!");
                    }
                    prev = m;
                }
                sink += prev;
            }
        };
    }

    static void writeRow(PrintWriter out, String workload, String variant, String structure,
                         int n, Result r) {
        if (out == null) {
            return;
        }
        out.println(workload + "," + variant + "," + structure + "," + n + ","
                + String.format(Locale.US, "%.3f", r.timeMs) + ","
                + r.steps + "," + r.moves + "," + r.comparisons);
        System.out.println(workload + " " + variant + " " + structure + " n=" + n
                + " time=" + String.format(Locale.US, "%.3f", r.timeMs) + " ms, steps="
                + r.steps + ", moves=" + r.moves + ", comparisons=" + r.comparisons);
    }

    static void runSuite(PrintWriter out, int n) {
        Supplier<IntList> arrayFactory = () -> new DynamicArray();
        Supplier<IntList> listFactory = () -> new MyLinkedList();
        Data d = makeData(n);

        writeRow(out, "W1", "-", "DynamicArray", n, measure(w1(arrayFactory, d)));
        writeRow(out, "W1", "-", "MyLinkedList", n, measure(w1(listFactory, d)));

        writeRow(out, "W2", "-", "DynamicArray", n, measure(w2(arrayFactory, d)));
        writeRow(out, "W2", "-", "MyLinkedList", n, measure(w2(listFactory, d)));

        writeRow(out, "W3", "head", "DynamicArray", n, measure(w3(arrayFactory, d, true)));
        writeRow(out, "W3", "head", "MyLinkedList", n, measure(w3(listFactory, d, true)));
        writeRow(out, "W3", "middle", "DynamicArray", n, measure(w3(arrayFactory, d, false)));
        writeRow(out, "W3", "middle", "MyLinkedList", n, measure(w3(listFactory, d, false)));

        writeRow(out, "W4", "-", "MinHeap", n, measure(w4(d)));
    }

    public static void runAll(String csvPath) throws IOException {
        File file = new File(csvPath);
        if (file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }

        System.out.println("JVM warm-up...");
        for (int round = 0; round < 3; round++) {
            runSuite(null, 100);
            runSuite(null, 1000);
        }

        try (PrintWriter out = new PrintWriter(file)) {
            out.println("workload,variant,structure,n,time_ms,steps,moves,comparisons");
            for (int n : SIZES) {
                runSuite(out, n);
            }
        }
        System.out.println("Saved " + csvPath);
    }
}
