package benchmark;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Benchmark.runAll("results/results.csv");
        BuildHeapBenchmark.runAll("results/buildheap.csv");
        MemoryBenchmark.runAll("results/memory.csv");
    }
}
