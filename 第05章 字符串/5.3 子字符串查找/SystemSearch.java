public class SystemSearch {


    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        String text  = "a";
        String query = "a";
        for (int i = 0; i < n; i++) {
            text  = text  + text;
            query = query + query;
        }
        text = text + text;
        query = query + "b";
        StdOut.println(text.indexOf(query));
    }
}
