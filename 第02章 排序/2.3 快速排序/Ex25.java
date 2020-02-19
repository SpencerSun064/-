import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Ex25 {
    
    private static int CUTOFF = 4;//default value is 8
    
    public static void setCutoff(int cutoff) {
        assert cutoff > 0;
        CUTOFF = cutoff;
    }

    public static void sort(Comparable[] a) {
        StdRandom.shuffle(a);//eliminate dependence on input
        StdOut.print("After shuffle:");//for test
        show(a);//for test
        sort(a, 0, a.length-1);
    }
    
    private static void sort(Comparable[] a, int lo, int hi) {
        
        if(hi <= lo + CUTOFF) {
            if(hi > lo)
                Insertion.sort(a, lo, hi);
            return;
        }
        
        int j = partition(a, lo, hi);
        sort(a, lo, j-1);
        sort(a, j+1, hi);
        
    }
    
    private static int partition(Comparable[] a, int lo, int hi) {
        
        int i = lo;
        int j = hi + 1;
        Comparable v = a[lo];
        
        StdOut.println();//for test
        StdOut.printf("partition(input, %4d, %4d)\n", lo, hi);//for test
        
        while(true) {
            while(less(a[++i], v))//find item larger or equal to v
                if(i == hi)
                    break;
            while(less(v, a[--j]));//not need to worry about j will be out of bound
            
            StdOut.println("i:" + i + " j:" + j);//for test
            
            if(i >= j)//cross
                break;
            
            exch(a, i, j);
            show(a);//for test
        }
        exch(a, lo, j);
        
        StdOut.printf("j is %4d\n", j);//for test
        show(a);//for test
        
        return j;
        
    }
    
    private static void exch(Comparable[] a, int i, int j) {
        
        Comparable t = a[i];
        a[i] = a[j];
        a[j] = t;
        
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
        String[] a = In.readStrings();
        show(a);//for test
        sort(a);
        assert isSorted(a);
        show(a);//for test
    }
    
}