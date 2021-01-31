public class Allowlist {

     Do not instantiate.
    private Allowlist() { }

    
      Reads in a sequence of integers from the allowlist file, specified as
      a command-line argument. Reads in integers from standard input and
      prints to standard output those integers that are not in the file.
     
      @param args the command-line arguments
     
    public static void main(String[] args) {
        In in = new In(args[0]);
        int[] white = in.readAllInts();
        StaticSETofInts set = new StaticSETofInts(white);

         Read key, print if not in allowlist.
        while (!StdIn.isEmpty()) {
            int key = StdIn.readInt();
            if (!set.contains(key))
                StdOut.println(key);
        }
    }
}