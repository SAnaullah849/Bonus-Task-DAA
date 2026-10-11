import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Main {

    static class BST {
        private static class Node {
            int value;
            Node left;
            Node right;

            Node(int value) {
                this.value = value;
            }
        }

        private Node root;

        public void insert(int value) {
            root = insertNode(root, value);
        }

        private Node insertNode(Node current, int value) {
            if (current == null) {
                return new Node(value);
            }

            if (value < current.value) {
                current.left = insertNode(current.left, value);
            } else if (value > current.value) {
                current.right = insertNode(current.right, value);
            }

            return current;
        }

        public boolean search(int value) {
            Node current = root;

            while (current != null) {
                if (value == current.value) {
                    return true;
                }

                if (value < current.value) {
                    current = current.left;
                } else {
                    current = current.right;
                }
            }
            return false;
        }

        public void delete(int value) {
            root = deleteNode(root, value);
        }

        private Node deleteNode(Node current, int value) {
            if (current == null) {
                return null;
            }

            if (value < current.value) {
                current.left = deleteNode(current.left, value);
            } else if (value > current.value) {
                current.right = deleteNode(current.right, value);
            } else {
                if (current.left == null) {
                    return current.right;
                }
                if (current.right == null) {
                    return current.left;
                }

                current.value = findSmallest(current.right);
                current.right = deleteNode(current.right, current.value);
            }

            return current;
        }

        private int findSmallest(Node current) {
            while (current.left != null) {
                current = current.left;
            }
            return current.value;
        }
    }

    static class SimpleHashTable {
        private final List<Integer>[] buckets;
        private int size = 0;

        @SuppressWarnings("unchecked")
        public SimpleHashTable(int capacity) {
            buckets = (List<Integer>[]) new List[capacity];
            for (int i = 0; i < capacity; i++) {
                buckets[i] = new LinkedList<>();
            }
        }

        private int getIndex(int value) {
            return Math.floorMod(Integer.hashCode(value), buckets.length);
        }

        public void insert(int value) {
            List<Integer> bucket = buckets[getIndex(value)];
            if (!bucket.contains(value)) {
                bucket.add(value);
                size++;
            }
        }

        public boolean search(int value) {
            return buckets[getIndex(value)].contains(value);
        }

        public void delete(int value) {
            List<Integer> bucket = buckets[getIndex(value)];
            if (bucket.remove(Integer.valueOf(value))) {
                size--;
            }
        }
    }

    interface Task {
        void run();
    }

    private static double measureMilliseconds(Task task) {
        long start = System.nanoTime();
        task.run();
        long end = System.nanoTime();
        return (end - start) / 1_000_000.0;
    }

    private static double average(List<Double> times) {
        double sum = 0;
        for (double time : times) {
            sum += time;
        }
        return sum / times.size();
    }

    private static void benchmark(int n, int trials, List<String> csvRows) {
        List<Double> bstInsert = new ArrayList<>();
        List<Double> hashInsert = new ArrayList<>();
        List<Double> bstSearch = new ArrayList<>();
        List<Double> hashSearch = new ArrayList<>();
        List<Double> bstDelete = new ArrayList<>();
        List<Double> hashDelete = new ArrayList<>();

        for (int trial = 1; trial <= trials; trial++) {
            List<Integer> values = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                values.add(i);
            }
            Collections.shuffle(values, new Random(1000L + n * 17L + trial));

            List<Integer> searchOrder = new ArrayList<>(values);
            Collections.shuffle(searchOrder, new Random(2000L + n * 19L + trial));

            BST bst = new BST();
            SimpleHashTable hash = new SimpleHashTable(Math.max(8, n * 2));

            double time = measureMilliseconds(() -> {
                for (int value : values) {
                    bst.insert(value);
                }
            });
            bstInsert.add(time);
            csvRows.add(csvRow(n, trial, "BST", "Insert", time));

            time = measureMilliseconds(() -> {
                for (int value : values) {
                    hash.insert(value);
                }
            });
            hashInsert.add(time);
            csvRows.add(csvRow(n, trial, "Hash Table", "Insert", time));

            time = measureMilliseconds(() -> {
                for (int value : searchOrder) {
                    bst.search(value);
                }
            });
            bstSearch.add(time);
            csvRows.add(csvRow(n, trial, "BST", "Search", time));

            time = measureMilliseconds(() -> {
                for (int value : searchOrder) {
                    hash.search(value);
                }
            });
            hashSearch.add(time);
            csvRows.add(csvRow(n, trial, "Hash Table", "Search", time));

            List<Integer> deleteOrder = new ArrayList<>(values);
            Collections.shuffle(deleteOrder, new Random(3000L + n * 23L + trial));

            time = measureMilliseconds(() -> {
                for (int value : deleteOrder) {
                    bst.delete(value);
                }
            });
            bstDelete.add(time);
            csvRows.add(csvRow(n, trial, "BST", "Delete", time));

            time = measureMilliseconds(() -> {
                for (int value : deleteOrder) {
                    hash.delete(value);
                }
            });
            hashDelete.add(time);
            csvRows.add(csvRow(n, trial, "Hash Table", "Delete", time));
        }

        System.out.printf("%-10d %-10s %12.3f %14.3f%n", n, "Insert",
                average(bstInsert), average(hashInsert));
        System.out.printf("%-10d %-10s %12.3f %14.3f%n", n, "Search",
                average(bstSearch), average(hashSearch));
        System.out.printf("%-10d %-10s %12.3f %14.3f%n", n, "Delete",
                average(bstDelete), average(hashDelete));
    }

    private static String csvRow(int n, int trial, String structure,
                                 String operation, double time) {
        return n + "," + trial + "," + structure + "," + operation + ","
                + String.format(java.util.Locale.US, "%.6f", time);
    }

    private static void writeCsv(List<String> rows) {
        try (java.io.PrintWriter writer = new java.io.PrintWriter("results.csv")) {
            writer.println("elements,trial,structure,operation,time_ms");
            for (String row : rows) {
                writer.println(row);
            }
            System.out.println("\nDetailed measurements saved to results.csv");
        } catch (java.io.FileNotFoundException e) {
            System.out.println("Could not write results.csv: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        int[] sizes = {100, 1000, 10000};
        int trials = 5;
        List<String> csvRows = new ArrayList<>();

        System.out.println("BST vs Hash Table - average batch time in milliseconds");
        System.out.println("Each result is the average of " + trials + " trials.");
        System.out.printf("%-10s %-10s %12s %14s%n",
                "Elements", "Operation", "BST (ms)", "Hash Table (ms)");

        for (int n : sizes) {
            benchmark(n, trials, csvRows);
        }

        writeCsv(csvRows);
    }
}