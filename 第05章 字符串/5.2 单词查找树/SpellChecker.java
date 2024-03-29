public class SpellChecker {  

    public static void main(String[] args) {
        StringSET dictionary = new StringSET();

        // read in dictionary of words
        In dict = new In(args[0]);
        while (!dict.isEmpty()) {
            String word = dict.readString();
            dictionary.add(word);
        }
        StdOut.println("Done reading dictionary");

        // read strings from standard input and print out if not in dictionary
        StdOut.println("Enter words, and I'll print out the misspelled ones");
        In corpus = new In();
        while (!corpus.isEmpty()) {
            String word = corpus.readString();
            if (!dictionary.contains(word)) StdOut.println(word);
        }
    }
}
