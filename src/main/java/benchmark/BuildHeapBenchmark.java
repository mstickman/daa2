package benchmark;

import structures.MinHeap;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.Random;

public class BuildHeapBenchmark {

    static int[] makeInput(String kind, int n) {
        int[] a = new int[n];
        if (kind.equals("random")) {
            Random rnd = new Random(Benchmark.SEED);
            for (int i = 0; i < n; i++) {
                a[i] = rnd.nextInt(1_000_000);
            }
        } else {
            for (int i = 0; i < n; i++) {
                a[i] = n - i;
            }
        }
        return a;
    }

    public static void runAll(String csvPath) throws IOException {
        File file = new File(csvPath);
        if (file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }
        try (PrintWriter out = new PrintWriter(file)) {
            out.println("input,method,n,time_ms,steps,moves,comparisons");
            for (String kind : new String[]{"random", "descending"}) {
                for (int n : Benchmark.SIZES) {
                    final int[] arr = makeInput(kind, n);

                    Benchmark.Case insertCase = new Benchmark.Case() {
                        MinHeap heap;

                        void prepare() {
                            heap = new MinHeap();
                            metrics = heap.getMetrics();
                        }

                        void run() {
                            for (int v : arr) {
                                heap.insert(v);
                            }
                            Benchmark.sink += heap.size();
                        }
                    };

                    Benchmark.Case buildCase = new Benchmark.Case() {
                        void prepare() {
                        }

                        void run() {
                            MinHeap heap = MinHeap.buildHeap(arr);
                            metrics = heap.getMetrics();
                            Benchmark.sink += heap.size();
                        }
                    };

                    write(out, kind, "n_inserts", n, Benchmark.measure(insertCase));
                    write(out, kind, "buildHeap", n, Benchmark.measure(buildCase));
                }
            }
        }
        System.out.println("Saved " + csvPath);
    }

    static void write(PrintWriter out, String kind, String method, int n, Benchmark.Result r) {
        String line = kind + "," + method + "," + n + ","
                + String.format(Locale.US, "%.3f", r.timeMs) + ","
                + r.steps + "," + r.moves + "," + r.comparisons;
        out.println(line);
        System.out.println("buildheap " + line);
    }
}
