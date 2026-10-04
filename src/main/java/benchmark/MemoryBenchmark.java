package benchmark;

import org.openjdk.jol.info.GraphLayout;
import structures.DynamicArray;
import structures.MinHeap;
import structures.MyLinkedList;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.Random;

public class MemoryBenchmark {

    public static void runAll(String csvPath) throws IOException {
        File file = new File(csvPath);
        if (file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }
        try (PrintWriter out = new PrintWriter(file)) {
            out.println("structure,n,bytes,mb,bytes_per_element");
            for (int n : Benchmark.SIZES) {
                Random rnd = new Random(Benchmark.SEED);
                int[] values = new int[n];
                for (int i = 0; i < n; i++) {
                    values[i] = rnd.nextInt(1_000_000);
                }

                DynamicArray array = new DynamicArray();
                MyLinkedList list = new MyLinkedList();
                MinHeap heap = new MinHeap();
                for (int v : values) {
                    array.add(v);
                    list.add(v);
                    heap.insert(v);
                }

                write(out, "DynamicArray", n, GraphLayout.parseInstance(array).totalSize());
                write(out, "MyLinkedList", n, GraphLayout.parseInstance(list).totalSize());
                write(out, "MinHeap", n, GraphLayout.parseInstance(heap).totalSize());
            }
        }
        System.out.println("Saved " + csvPath);
    }

    static void write(PrintWriter out, String name, int n, long bytes) {
        String line = name + "," + n + "," + bytes + ","
                + String.format(Locale.US, "%.4f", bytes / (1024.0 * 1024.0)) + ","
                + String.format(Locale.US, "%.2f", (double) bytes / n);
        out.println(line);
        System.out.println("memory " + line);
    }
}
