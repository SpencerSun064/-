import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Ex11 {
    
    private static Comparable[] aux;
    private final static int CUTOFF = 8;//size
    
    public static void sort(Comparable[] input) {
        int N = input.length;
        aux = input.clone();//must be clone(the same as input)
        StdOut.println("input:" + input + " aux:" + aux);//for test
        sort(aux, input, 0, N-1);
    }
    
    //this level takes source as input(need to be sorted)
    //and destination as auxiliary, and put the result in destination
    private static void sort(Comparable[] source, Comparable[] destination, int lo, int hi) {//avoid copy
        
        if((lo+CUTOFF-1) >= hi) { //use insertion sort for tiny subarrays
            insertionsort(destination, lo, hi);//prepare destination for up level
            return;
        }
        
        int mid = lo + (hi-lo)/2;
        sort(destination, source, lo, mid);//down level switch the roles of the input array and auxiliary array
        sort(destination, source, mid+1, hi);
        
        if(!less(source[mid+1], source[mid])) {//ship merge
            System.arraycopy(source, lo, destination, lo, hi-lo+1);//prepare destination for up level
            StdOut.println("destination:" + destination);//for test
            StdOut.printf("skip merge(source, destination, %4d, %4d, %4d)", lo, mid, hi);//for test
            show(destination);//for test
            return;
        }
        
        merge(source, destination, lo, mid, hi);//merge sorted source to destination
        
    }
    
    private static void insertionsort(Comparable[] input, int lo, int hi) {
        for(int i = lo + 1; i <= hi; i++) {
            for(int j = i; j > lo && less(input[j], input[j-1]); j--) {
                exch(input, j, j-1);
            }
        }
        
        StdOut.println("destination:" + input);
        StdOut.printf("insertionsort(input, %4d, %4d)", lo, hi);//for test
        show(input);//for test
    }
    
    private static void exch(Comparable[] a, int i, int j) {
        
        Comparable t = a[i];
        a[i] = a[j];
        a[j] = t;
        
    }
    
    private static void merge(Comparable[] source, Comparable[] destination, int lo, int mid, int hi) {
        
        int i = lo;
        int j = mid + 1;
        for(int k = lo; k <= hi; k++) {
            if(i > mid)
                destination[k] = source[j++];
            else if(j > hi)
                destination[k] = source[i++];
            else if(less(source[j], source[i]))
                destination[k] = source[j++];
            else 
                destination[k] = source[i++];
        }
        StdOut.println("source:" + source + " destination:" + destination);//for test
        StdOut.printf("merge(source, destination, %4d, %4d, %4d)", lo, mid, hi);//for test
        show(destination);//for test
        
    }
    
    private static boolean less(Comparable v, Comparable w) {
        
        return v.compareTo(w) < 0;
        
    }
    
    private static void show(Comparable[] a) {
        
        //print the array, on a single line.
        for(int i = 0; i < a.length; i++) {
            StdOut.print(a[i] + " ");
        }
        StdOut.println();
        
    }
    
    public static boolean isSorted(Comparable[] a) {
        
        for(int i = 1; i < a.length; i++) {
            if(less(a[i], a[i-1]))
                return false;
        }
        
        return true;
        
    }
    
    public static void main(String[] args) {
        
        //Read strings from standard input, sort them, and print.
        String[] input = In.readStrings();
        show(input);//for test
        sort(input);
        assert isSorted(input);
        show(input);//for test
        
    }

}