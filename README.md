DAA Assignment 2 - Data Structures (In-Memory Workload Engine)

WHAT IS IN THE PROJECT

Own implementations of three data structures. No java.util collections are used inside them.
DynamicArray: stores int[], grows 2x when it is full.
MyLinkedList: singly linked list with head and tail pointers.
MinHeap: array-based binary min-heap, plus bonus Floyd buildHeap.
All of them store primitive int (no generics, no Integer).

HOW TO BUILD AND RUN THE TESTS

Command: mvn test

Command: mvn -q compile exec:java

WHAT THE COUNTERS MEAN

Format of the lines: structure-counter-what it counts

DynamicArray-steps-one read of an array cell (get, contains)
DynamicArray-moves-one element shifted or copied (shift in add/remove, copy while growing)
DynamicArray-comparisons-one comparison of two elements
MyLinkedList-steps-one move to the next node (cur = cur.next)
MyLinkedList-moves-one pointer update (next, head, tail)
MyLinkedList-comparisons-one comparison of two elements
MinHeap-steps-one read of a parent or child cell
MinHeap-moves-one element moved (a swap is 2 moves, a copy while growing is 1 move)
MinHeap-comparisons-one comparison of two elements
<img width="1801" height="546" alt="image" src="https://github.com/user-attachments/assets/8466a466-d507-4439-a3a7-c945aee6617c" />

<img width="910" height="585" alt="W4_time" src="https://github.com/user-attachments/assets/7167aa24-a220-43aa-a9c6-962c22eaf68a" />
<img width="728" height="585" alt="W4_ops" src="https://github.com/user-attachments/assets/72db36b3-1f1c-4250-bfe9-8014faa5c429" />
<img width="910" height="585" alt="W3_time" src="https://github.com/user-attachments/assets/5cfcb456-f090-4a68-ac36-1b186baff179" />
<img width="1456" height="585" alt="W3_ops" src="https://github.com/user-attachments/assets/12ad5847-fd57-45e2-a939-5e6290d2ab2b" />
<img width="910" height="585" alt="W2_time" src="https://github.com/user-attachments/assets/6d920ef9-f72d-4a0b-a81a-28a35eb448ca" />
<img width="728" height="585" alt="W2_ops" src="https://github.com/user-attachments/assets/1c90addc-02f7-4f67-935d-b1994cecacd2" />
<img width="910" height="585" alt="W1_time" src="https://github.com/user-attachments/assets/1c7f713e-1589-4b18-aefb-866313bfb3d4" />
<img width="728" height="585" alt="W1_ops" src="https://github.com/user-attachments/assets/80067a23-d5ef-47e6-8da0-554b22c05f92" />
<img width="1430" height="585" alt="bonusB_buildheap" src="https://github.com/user-attachments/assets/00991164-96ce-47da-bffc-c83eda2ec766" />
<img width="910" height="585" alt="bonusA_memory" src="https://github.com/user-attachments/assets/f635b57a-c48a-4ea6-847a-44837f97d789" />
