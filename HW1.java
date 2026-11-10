package com.mycompany.h.w1;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

public class HW1 {

    static final int N = 100000; 

    // 1 Selection Sort
    public static void selectionSort(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (arr[j] < arr[minIndex]) {
                    minIndex = j;
                }
            }
            int temp = arr[minIndex];
            arr[minIndex] = arr[i];
            arr[i] = temp;
        }
    }

    // 2 Insertion Sort
    public static void insertionSort(int[] arr) {
        int n = arr.length;
        for (int i = 1; i < n; i++) {
            int key = arr[i];
            int j = i - 1;
            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
    }

    // 3 Bubble Sort 
    public static void bubbleSort(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    swapped = true;
                }
            }
            if (!swapped) break;
        }
    }

    // 4 Merge Sort 
    private static void merge(int[] arr, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;
        int[] leftArr = new int[n1];
        int[] rightArr = new int[n2];

        for (int i = 0; i < n1; i++) leftArr[i] = arr[left + i];
        for (int j = 0; j < n2; j++) rightArr[j] = arr[mid + 1 + j];

        int i = 0, j = 0, k = left;
        while (i < n1 && j < n2) {
            if (leftArr[i] <= rightArr[j]) {
                arr[k++] = leftArr[i++];
            } else {
                arr[k++] = rightArr[j++];
            }
        }
        while (i < n1) arr[k++] = leftArr[i++];
        while (j < n2) arr[k++] = rightArr[j++];
    }

    public static void mergeSort(int[] arr, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            mergeSort(arr, left, mid);
            mergeSort(arr, mid + 1, right);
            merge(arr, left, mid, right);
        }
    }

    // 5 quick Sort 
    private static int partition(int[] arr, int low, int high) {
        int pivot = arr[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (arr[j] < pivot) {
                i++;
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }
        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;
        return i + 1;
    }

    public static void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            int pi = partition(arr, low, high);
            quickSort(arr, low, pi - 1);
            quickSort(arr, pi + 1, high);
        }
    }

    // غير مرتبه
    static int[] createRandom(Random rnd) {
        int[] a = new int[N];
        for (int i = 0; i < N; i++) a[i] = rnd.nextInt(1_000_000);
        return a;
    }

    // مرتبه عكسيا
    static int[] createReversed(Random rnd) {
        int[] a = createRandom(rnd);
        Arrays.sort(a);
        for (int i = 0; i < N / 2; i++) {
            int temp = a[i];
            a[i] = a[N - 1 - i];
            a[N - 1 - i] = temp;
        }
        return a;
    }

    // مرتبه جزئيا
    static int[] createPartiallySorted(Random rnd) {
        int[] a = createRandom(rnd);
        Arrays.sort(a);
        for (int k = 0; k < N / 10; k++) {
            int i = rnd.nextInt(N);
            int j = rnd.nextInt(N);
            int temp = a[i];
            a[i] = a[j];
            a[j] = temp;
        }
        return a;
    }

    // مرتبه
    static int[] createSorted(Random rnd) {
        int[] a = createRandom(rnd);
        Arrays.sort(a);
        return a;
    }

    static long measure(int[] original, Consumer<int[]> sorter) {
        int[] copy = Arrays.copyOf(original, original.length); 
        long start = System.nanoTime();
        sorter.accept(copy);                         
        long end = System.nanoTime();
        return (end - start) / 1000;                     
    }

    static void run() {
        Random rnd = new Random(42); 

        // بيانات الاختبار الأربع
        int[][] data = {
            createRandom(rnd),
            createReversed(rnd),
            createPartiallySorted(rnd),
            createSorted(rnd)
        };

        String[] names = {"Selection Sort", "Insertion Sort", "Bubble Sort", "Merge Sort", "Quick Sort"};
        
        List<Consumer<int[]>> sorters = new ArrayList<>();
        sorters.add(arr -> selectionSort(arr));
        sorters.add(arr -> insertionSort(arr));
        sorters.add(arr -> bubbleSort(arr));
        sorters.add(arr -> mergeSort(arr, 0, arr.length - 1));
        sorters.add(arr -> quickSort(arr, 0, arr.length - 1));

        System.out.println("Array size N = " + N + " (time in microseconds)\n");
       
        for (int i = 0; i < names.length; i++) {
            long randomTime = measure(data[0], sorters.get(i));
            long reversedTime = measure(data[1], sorters.get(i));
            long partialTime  = measure(data[2], sorters.get(i));
            long sortedTime   = measure(data[3], sorters.get(i));

            System.out.println("Algorithm: " + names[i]);
            System.out.println("  - Random:            " + randomTime + " us");
            System.out.println("  - Reversed:          " + reversedTime + " us");
            System.out.println("  - Partially Sorted:  " + partialTime + " us");
            System.out.println("  - Fully Sorted:      " + sortedTime + " us");
            System.out.println("----------------------------------------");
        }
    }

    public static void main(String[] args) {
        run();
    }
}
