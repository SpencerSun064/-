public class Genome {

    // Do not instantiate.
    private Genome() { }

    /**
     * Reads a sequence of 8-bit extended ASCII characters over the alphabet
     * { A, C, T, G } from standard input; compresses them using two bits per
     * character; and writes the results to standard output.
     */
    public static void compress() { 
        Alphabet DNA = Alphabet.DNA;
        String s = BinaryStdIn.readString();
        int n = s.length();
        BinaryStdOut.write(n);

        // Write two-bit code for char. 
        for (int i = 0; i < n; i++) {
            int d = DNA.toIndex(s.charAt(i));
            BinaryStdOut.write(d, 2);
        }
        BinaryStdOut.close();
    } 

    /**
     * Reads a binary sequence from standard input; converts each two bits
     * to an 8-bit extended ASCII character over the alphabet { A, C, T, G };
     * and writes the results to standard output.
     */
    public static void expand() {
        Alphabet DNA = Alphabet.DNA;
        int n = BinaryStdIn.readInt();
        // Read two bits; write char. 
        for (int i = 0; i < n; i++) {
            char c = BinaryStdIn.readChar(2);
            BinaryStdOut.write(DNA.toChar(c), 8);
        }
        BinaryStdOut.close();
    }


    /**
     * Sample client that calls {@code compress()} if the command-line
     * argument is "-" an {@code expand()} if it is "+".
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        if      (args[0].equals("-")) compress();
        else if (args[0].equals("+")) expand();
        else throw new IllegalArgumentException("Illegal command line argument");
    }

}