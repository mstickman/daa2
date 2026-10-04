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
