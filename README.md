# BST vs Hash Table 

## 1. What is this project?

This project compares two ways to store numbers in a computer:

- **BST (Binary Search Tree):** Smaller numbers go to the left. Bigger numbers go to the right.
- **Hash Table:** A formula chooses a bucket for each number. If two numbers use the same bucket, they share a small list.

The program tests three actions: **insert** (add), **search** (find), and **delete** (remove).

## 2. Files

- `Main.java` — all Java code, including both data structures and the test.
- `results.csv` — detailed timing results from five test runs.
- `handwritten_report.txt` — simple report to read and rewrite by hand.



The program tests 100, 1,000, and 10,000 numbers. It prints average times in milliseconds and creates `results.csv` with the individual results from five trials.

## 4. Time complexity

`n` means the number of items. `O(1)` means the work stays roughly the same as the list grows. `O(log n)` means the work grows slowly. `O(n)` means the work may grow with the number of items.
![Screenshot 2026-10-11 181335.png](Screenshot%202026-10-11%20181335.png)
### Why?

- A BST follows a path from its top node. A balanced tree has short paths. But if numbers are added in sorted order, this simple BST can become like a long chain, making operations slow.
- A hash table uses a formula to choose a bucket. It is usually fast when numbers are spread across buckets.
- Sometimes different numbers land in the same bucket. This is called a **collision**. Too many collisions can make a hash table slower.
- This Java BST is **not self-balancing**, so `O(log n)` is not guaranteed.
- The hash table uses **separate chaining**, meaning each bucket stores a list of numbers.

## 5. Experimental results

The program measures the time needed to process all `n` numbers for each action. Each printed result is an average of five trials. Exact timings depend on the computer, Java version, and other programs running at the same time.
![Handwritten Report Page 1.jpeg](Handwritten%20Report%20Page%201.jpeg)
![Handwritten Report Page 2.jpeg](Handwritten%20Report%20Page%202.jpeg)
After running `java Main`, copy the values printed in your terminal into the table below. The program also saves all individual measurements in `results.csv`.
## 6. Conclusion

A hash table is usually faster for adding, finding, and removing one exact number because it can go directly to a bucket. This is expected `O(1)` time when collisions are limited. A BST can also be fast, but its speed depends on its shape. A balanced BST has `O(log n)` operations, while a badly shaped BST can take `O(n)` time. The BST keeps numbers in order, which is useful when we need sorted data. A basic hash table does not keep numbers sorted.
