public class BipartiteMatchingToMaxflow {
    private static final int UNMATCHED = -1;

    private final int V;                 // number of vertices in the graph
    private final int cardinality;       // cardinality of max matching / min vertex cover
    private int[] mate;                  // mate[v] =  w if v-w is an edge in max matching
                                         //         = -1 if v is not in max matching
    private boolean[] inMinVertexCover;  // inMinVertexCover[v] = true iff v is in min vertex cover

    /**
     * Determines a maximum matching (and a minimum vertex cover)
     * in a bipartite graph.
     *
     * @param  G the bipartite graph
     * @throws IllegalArgumentException if {@code G} is not bipartite
     */
    public BipartiteMatchingToMaxflow(Graph G) {
        BipartiteX bipartite = new BipartiteX(G);
        if (!bipartite.isBipartite()) {
            throw new IllegalArgumentException("graph is not bipartite");
        }

        // reduction to maxflow problem
        this.V = G.V();
        int s = V;
        int t = V + 1;
        FlowNetwork H = new FlowNetwork(V + 2);
        for (int v = 0; v < V; v++) {
            for (int w : G.adj(v)) {
                if (bipartite.color(v))
                    H.addEdge(new FlowEdge(v, w, Double.POSITIVE_INFINITY));
                else
                    H.addEdge(new FlowEdge(w, v, Double.POSITIVE_INFINITY));
            }
        }
        for (int v = 0; v < V; v++) {
            if (bipartite.color(v))
                H.addEdge(new FlowEdge(s, v, 1.0));
            else
                H.addEdge(new FlowEdge(v, t, 1.0));
        }

        // solve the maximum flow problem
        FordFulkerson maxflow = new FordFulkerson(H, s, t);

        // get the cardinality of the maximum matching (guaranteed to be an integer)
        cardinality = (int) maxflow.value();

        // extract the edges in the maximum matching
        mate = new int[V];
        for (int v = 0; v < V; v++)
            mate[v] = UNMATCHED;
        for (int v = 0; v < V; v++) {
            for (FlowEdge e : H.adj(v)) {
                if (e.from() == v && e.flow() > 0 && e.to() != t) {
                    int w = e.to();
                    mate[v] = w;
                    mate[w] = v;
                }
            }
        }

        // also find a min vertex cover
        inMinVertexCover = new boolean[V];
        for (int v = 0; v < V; v++) {
            if (bipartite.color(v) && !maxflow.inCut(v)) inMinVertexCover[v] = true;
            if (!bipartite.color(v) && maxflow.inCut(v)) inMinVertexCover[v] = true;
        }

        assert certifySolution(G);
    }

    /**
     * Returns the vertex to which the specified vertex is matched in
     * the maximum matching.
     *
     * @param  v the vertex
     * @return the vertex to which vertex {@code v} is matched in the
     *         maximum matching; {@code -1} if the vertex is not matched
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     *
     */
    public int mate(int v) {
        validate(v);
        return mate[v];
    }

    /**
     * Returns true if the specified vertex is matched in the maximum matching.
     *
     * @param  v the vertex
     * @return {@code true} if vertex {@code v} is matched in maximum matching;
     *         {@code false} otherwise
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     *
     */
    public boolean isMatched(int v) {
        validate(v);
        return mate[v] != UNMATCHED;
    }

    /**
     * Returns the number of edges in a maximum matching.
     *
     * @return the number of edges in a maximum matching
     */
    public int size() {
        return cardinality;
    }

    /**
     * Returns true if the graph contains a perfect matching.
     * That is, the number of edges in the maximum matching is equal to one half
     * of the number of vertices in the graph (so that every vertex is matched).
     *
     * @return {@code true} if the graph contains a perfect matching;
     *         {@code false} otherwise
     */
    public boolean isPerfect() {
        return cardinality * 2 == V;
    }

    /**
     * Returns true if the specified vertex is in the minimum vertex cover.
     *
     * @param  v the vertex
     * @return {@code true} if vertex {@code v} is in the minimum vertex cover;
     *         {@code false} otherwise
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public boolean inMinVertexCover(int v) {
        validate(v);
        return inMinVertexCover[v];
    }

    private void validate(int v) {
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
    }


    /**************************************************************************
     *
     *  The code below is solely for testing correctness of the data type.
     *
     **************************************************************************/

    // check that mate[] and inVertexCover[] define a max matching and min vertex cover, respectively
    private boolean certifySolution(Graph G) {

        // check that mate(v) = w iff mate(w) = v
        for (int v = 0; v < V; v++) {
            if (mate(v) == UNMATCHED) continue;
            if (mate(mate(v)) != v) return false;
        }

        // check that size() is consistent with mate()
        int matchedVertices = 0;
        for (int v = 0; v < V; v++) {
            if (mate(v) != UNMATCHED) matchedVertices++;
        }
        if (2*size() != matchedVertices) return false;

        // check that size() is consistent with minVertexCover()
        int sizeOfMinVertexCover = 0;
        for (int v = 0; v < V; v++)
            if (inMinVertexCover(v)) sizeOfMinVertexCover++;
        if (size() != sizeOfMinVertexCover) return false;

        // check that mate() uses each vertex at most once
        boolean[] isMatched = new boolean[V];
        for (int v = 0; v < V; v++) {
            int w = mate[v];
            if (w == UNMATCHED) continue;
            if (v == w) return false;
            if (v >= w) continue;
            if (isMatched[v] || isMatched[w]) return false;
            isMatched[v] = true;
            isMatched[w] = true;
        }

        // check that mate() uses only edges that appear in the graph
        for (int v = 0; v < V; v++) {
            if (mate(v) == UNMATCHED) continue;
            boolean isEdge = false;
            for (int w : G.adj(v)) {
                if (mate(v) == w) isEdge = true;
            }
            if (!isEdge) return false;
        }

        // check that inMinVertexCover() is a vertex cover
        for (int v = 0; v < V; v++)
            for (int w : G.adj(v))
                if (!inMinVertexCover(v) && !inMinVertexCover(w)) return false;

        return true;
    }

    /**
     * Unit tests the {@code HopcroftKarp} data type.
     * Takes three command-line arguments {@code V1}, {@code V2}, and {@code E};
     * creates a random bipartite graph with {@code V1} + {@code V2} vertices
     * and {@code E} edges; computes a maximum matching and minimum vertex cover;
     * and prints the results.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        int V1 = Integer.parseInt(args[0]);
        int V2 = Integer.parseInt(args[1]);
        int E  = Integer.parseInt(args[2]);
        Graph G = GraphGenerator.bipartite(V1, V2, E);
        StdOut.println(G);

        BipartiteMatchingToMaxflow matching = new BipartiteMatchingToMaxflow(G);

        // print maximum matching
        StdOut.printf("Number of edges in max matching        = %d\n", matching.size());
        StdOut.printf("Number of vertices in min vertex cover = %d\n", matching.size());
        StdOut.printf("Graph has a perfect matching           = %b\n", matching.isPerfect());
        StdOut.println();

        if (G.V() >= 1000) return;

        StdOut.print("Max matching: ");
        for (int v = 0; v < G.V(); v++) {
            int w = matching.mate(v);
            if (matching.isMatched(v) && v < w)  // print each edge only once
                StdOut.print(v + "-" + w + " ");
        }
        StdOut.println();

        // print minimum vertex cover
        StdOut.print("Min vertex cover: ");
        for (int v = 0; v < G.V(); v++)
            if (matching.inMinVertexCover(v))
                StdOut.print(v + " ");
        StdOut.println();
    }

}