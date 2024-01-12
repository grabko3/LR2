package org.example; 
import java.util.ArrayList; 
import java.util.Random; 

enum SortingType { 
    BUBBLE, SHELL, MERGE, QUICK 
} 

interface Sorter { 
    ArrayList<Integer> sort(ArrayList<Integer> input); 
} 

class BubbleSorter implements Sorter { 
    public ArrayList<Integer> sort(ArrayList<Integer> input) { 
        int n = input.size(); 
        for (int i = 0; i < n-1; i++) 
            for (int j = 0; j < n-i-1; j++) 
                if (input.get(j) > input.get(j+1)) {
                    int temp = input.get(j); 
                    input.set(j, input.get(j+1)); 
                    input.set(j+1, temp); 
                } 
        return input; 
    } 
} 

class ShellSorter implements Sorter { 
    public ArrayList<Integer> sort(ArrayList<Integer> input) { 
        int n = input.size(); 
        for (int gap = n/2; gap > 0; gap /= 2) 
            for (int i = gap; i < n; i += 1) { 
                int temp = input.get(i); 
                int j; 
                for (j = i; j >= gap && input.get(j - gap) > temp; j -= gap) 
                    input.set(j, input.get(j - gap)); 
                input.set(j, temp); 
            } 
        return input; 
    } 
} 

class MergeSorter implements Sorter { 
    public ArrayList<Integer> sort(ArrayList<Integer> input) { 
        if (input.size() <= 1) 
            return input; 
 
        ArrayList<Integer> left = new ArrayList<>(); 
        ArrayList<Integer> right = new ArrayList<>(); 
        boolean logicalSwitch = false; 
        while (!input.isEmpty()) { 
            if (logicalSwitch) 
                left.add(input.remove(0)); 
            else 
                right.add(input.remove(0)); 
            logicalSwitch = !logicalSwitch; 
        } 
 
        return merge(sort(left), sort(right)); 
    } 
 
    private ArrayList<Integer> merge(ArrayList<Integer> left, ArrayList<Integer> right) { 
        ArrayList<Integer> merged = new ArrayList<>(); 
        while (!left.isEmpty() && !right.isEmpty()) { 
            if (left.get(0) <= right.get(0)) 
                merged.add(left.remove(0)); 
            else 
                merged.add(right.remove(0)); 
        } 
 
        while (!left.isEmpty()) 
            merged.add(left.remove(0)); 
 
        while (!right.isEmpty()) 
            merged.add(right.remove(0)); 
 
        return merged; 
    } 
} 

class QuickSorter implements Sorter { 
    public ArrayList<Integer> sort(ArrayList<Integer> input) { 
        if (input.size() <= 1) 
            return input; 
 
        int pivot = input.get(input.size()/2); 
        ArrayList<Integer> less = new ArrayList<>(); 
        ArrayList<Integer> pivotList = new ArrayList<>(); 
        ArrayList<Integer> more = new ArrayList<>(); 
 
        for (int i : input) { 
            if (i < pivot) 
                less.add(i); 
            else if (i > pivot) 
                more.add(i); 
            else 
                pivotList.add(i); 
        } 
 
        less = sort(less); 
        more = sort(more); 
 
        less.addAll(pivotList); 
        less.addAll(more); 
 
        return less; 
    } 
} 

class SorterFactory { 
    public static Sorter getSorter(SortingType type) { 
        switch (type) { 
            case BUBBLE: 
                return new BubbleSorter(); 
            case SHELL: 
                return new ShellSorter(); 
            case MERGE: 
                return new MergeSorter(); 
            case QUICK: 
                return new QuickSorter(); 
            default: 
                throw new IllegalArgumentException("Invalid sorting type"); 
        } 
    } 
} 
 
public class Main { 
    public static void main(String[] args) { 
        int[] sizes = {10, 1000, 10000, 1000000}; 
        SortingType[] types = SortingType.values();
        for (int size : sizes) { 
            System.out.println("Кількість елементів: " + size); 
            ArrayList<Integer> array = generateArray(size); 
            printArray(array); 

            for (SortingType type : types) { 
                System.out.println("Тип сортування: " + type); 
                Sorter sorter = SorterFactory.getSorter(type); 
                ArrayList<Integer> sortedArray = measureSortingTime(sorter, array); 
                printArray(sortedArray); 
            } 
        } 
    } 

    private static ArrayList<Integer> generateArray(int size) { 
        ArrayList<Integer> array = new ArrayList<>(); 
        Random random = new Random(); 
        for (int i = 0; i < size; i++) { 
            array.add(random.nextInt(size)); 
        } 
 
        return array; 
    } 

    private static void printArray(ArrayList<Integer> array) { 
        int limit = Math.min(50, array.size()); 
        for (int i = 0; i < limit; i++) { 
            System.out.print(array.get(i) + ", "); 
        } 
        System.out.println(); 
    } 
 
    private static ArrayList<Integer> measureSortingTime(Sorter sorter, ArrayList<Integer> array) { 
        long startTime = System.currentTimeMillis(); 
        ArrayList<Integer> sortedArray = sorter.sort(new ArrayList<>(array)); 
        long endTime = System.currentTimeMillis(); 
        System.out.println("Час сортування: " + (endTime - startTime) + " мс"); 
 
        return sortedArray; 
    } 
}
