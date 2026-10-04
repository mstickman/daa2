Assignment 2


1. COMPLEXITY TABLE


| Structure | Operation | Best | Average | Worst | Aux. space | Justification |
|---|---|---|---|---|---|---|
| DynamicArray | add(x) | Θ(1) | Θ(1) amortized | O(n) one call, Ω(1) | O(1) (O(n) while growing) | write to `data[size]`; a full array is copied once, copies cost 1+2+4+...+n < 2n in total |
| DynamicArray | add(index, x) | Θ(1) (index = size) | Θ(n) | Θ(n) (index = 0) | O(1) | shifts size - index elements, on average n/2 |
| DynamicArray | remove(index) | Θ(1) (last index) | Θ(n) | Θ(n) (index = 0) | O(1) | shifts size - index - 1 elements to the left |
| DynamicArray | get(index) | Θ(1) | Θ(1) | Θ(1) | O(1) | direct access `data[index]` |
| DynamicArray | contains(x) | Θ(1) (x is first) | Θ(n) | Θ(n) (x absent) | O(1) | linear scan; found: about n/2 reads, absent: n reads |
| MyLinkedList | add(x) | Θ(1) | Θ(1) | Θ(1) | O(1) | `tail` pointer, 2 pointer updates |
| MyLinkedList | add(index, x) | Θ(1) (index = 0 or size) | Θ(n) | Θ(n) (index = size - 1) | O(1) | walk index - 1 nodes, then 2 pointer updates |
| MyLinkedList | remove(index) | Θ(1) (index = 0) | Θ(n) | Θ(n) (last node) | O(1) | walk index - 1 nodes, then 1-2 pointer updates |
| MyLinkedList | get(index) | Θ(1) (index = 0) | Θ(n) | Θ(n) (last index) | O(1) | walk `index` nodes from head |
| MyLinkedList | contains(x) | Θ(1) (x is head) | Θ(n) | Θ(n) (x absent) | O(1) | linear walk |
| MinHeap | insert(x) | Θ(1) (x >= parent) | Θ(1) on random data | Θ(log n) (new minimum) | O(1) (O(n) while growing) | bubble-up goes at most the height of the tree = ⌊log n⌋; a random element goes up O(1) levels on average |
| MinHeap | peekMin() | Θ(1) | Θ(1) | Θ(1) | O(1) | read `data[0]` |
| MinHeap | extractMin() | Ω(1) (all values equal) | Θ(log n) | Θ(log n) | O(1) | the last element comes to the root and usually goes down to the bottom; height is ⌊log n⌋ |
| MinHeap | buildHeap(array) (bonus) | Θ(n) | Θ(n) | Θ(n) | O(1) extra (copy of input O(n)) | Floyd: sum over levels of (nodes at level) x (height) = O(n); every node is checked at least once |



2. LOOP INVARIANT PROOFS

PROOF 1: DynamicArray.contains(x)

Code of the loop:

    for (int i = 0; i < size; i++) {
        if (data[i] == x) return true;
    }
    return false;

return true happens only when we really saw data[i] == x. Return false happens only after the invariant has shown that x is not in the whole array. So contains is correct for any input, including the empty array (the loop does not run and the result is false).


PROOF 2: MinHeap.bubbleDown(i), used by extractMin and buildHeap

Code of the loop:

    while (true) {
        int left = 2*i + 1;
        if (left >= size) break;
        int smallest = left;
        if (left + 1 < size && data[left + 1] < data[left]) smallest = left + 1;
        if (data[i] <= data[smallest]) break;
        swap(i, smallest);
        i = smallest;
    }

in both break cases the pairs (i, children of i) are correct, and by invariant (a) all other pairs are correct, so the whole array is a min-heap again. Together with the fact that extractMin returns the old data[0] (the minimum), extractMin is correct.


3. EXPERIMENT SETUP

| Workload | DynamicArray | MyLinkedList |
|---|---|---|
| W1 get x 10 000 | 0.025 ms, 10 000 steps | 1076 ms, 504 930 938 steps |
| W2 contains x 1 000 | 24.9 ms, 73 682 044 steps | 122.3 ms, 73 681 544 steps |
| W3 head (1000 ins + 1000 rem) | 49.5 ms, 200 999 000 moves | 0.009 ms, 3 000 moves |
| W3 middle (n / 2) | 22.6 ms, 100 999 000 moves | 172.7 ms, 99 998 000 steps + 3 000 moves |
| W4 MinHeap (n insert + n extractMin) | 12.4 ms, 3 059 125 comparisons | - |



4. PLOTS

All charts are in the folder results/plots/ (PNG files). Every chart has axis labels, units and a legend.

5. DISCUSSION

1. In W1 the array makes exactly 1 step per get(i) (10000 steps in total), and the list makes about i steps (about n/2 on average), so for n = 100000 the list makes about 50000 times more steps; the measured time ratio is about 40000 times.
2. The array is fast because its int values lie one after another in memory; the CPU loads a cache line of 64 bytes (16 int values) at once, so the next 15 reads are already in the L1 cache (spatial locality), and the hardware prefetcher can guess the next addresses.
3. For simple iteration the array is also better: W2 contains makes almost the same number of steps and comparisons in both structures (73.68 million at n = 100000), but the array is about 5 times faster (24.9 ms against 122.3 ms).
4. The reason is pointer chasing: to read the next node the CPU must first finish reading the pointer in the current node, so the reads depend on each other and cannot be executed in parallel or prefetched well, while in the array the addresses are known in advance.
5. Every list node is an object: it has an object header (12 bytes with compressed pointers), a 4-byte int and a 4-byte reference, which gives 24 bytes per element in our JOL measurement against 4 bytes of payload in the array; so one cache line holds less than 3 nodes but 16 array cells.
6. Nodes are created one by one by new, and after many insertions, removals and garbage collections they are not neighbours in memory any more (in our benchmark they are allocated one after another, so the real result for a long-living list can be even worse), and the garbage collector has to trace and clean every small node object separately, while the array is one big object.
7. W3 middle shows the same thing very clearly: both structures do about 100 million operations at n = 100000 (array: moves, list: steps), the Big-O is the same Θ(n), but the array needs 22.6 ms and the list 172.7 ms, which is about 7.6 times more, because the array shifts a continuous block and the list jumps over nodes.
8. Equal Big-O hides the constant factor: the cost of one operation is different (a sequential move in the cache against a dependent memory load), and the operation counters alone cannot show this, so we need both counters and real time.
9. The list is the better choice when insertions and removals happen at the head: in W3 head the list needs only 3000 pointer updates (0.009 ms) while the array shifts 201 million elements (49.5 ms) at n = 100000; it is also better when we keep references to nodes and need to remove them in O(1), and when we do not want a big resize copy.
10. The list is also useful when the size is unpredictable and a pause for copying the whole array (the worst case O(n) add) is not acceptable; the price is extra memory (about 3.7 times more at n = 100000: 2.29 MB against 0.63 MB).


6. BONUS TASKS

TASK A: memory footprint with JOL

| n | DynamicArray, MB | MyLinkedList, MB | MinHeap, MB |
|---|---|---|---|
| 100 | 0.0007 | 0.0024 | 0.0007 |
| 1 000 | 0.0050 | 0.0230 | 0.0050 |
| 10 000 | 0.0391 | 0.2290 | 0.0391 |
| 100 000 | 0.6251 | 2.2889 | 0.6251 |

