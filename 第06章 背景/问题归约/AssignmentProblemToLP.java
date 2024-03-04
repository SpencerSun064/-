public class AssignmentProblemToLP {
    private int n;              // number of rows and columns
    private double[][] weight;  // the n-by-n cost matrix
    private double[] px;        // px[i] = dual variable for row i
    private double[] py;        // py[j] = dual variable for col j
    private int[] xy;           // xy[i] = j means i-j is a match
    private int[] yx;           // yx[j] = i means i-j is a match


    public AssignmentProblemToLP(double[][] weight) {
        n = weight.length;
        this.weight = new double[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                this.weight[i][j] = weight[i][j];

        double[] c = new double[n*n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                c[i*n+j] = weight[i][j];   // or vice versa?
        double[] b = new double[2*n];
        for (int i = 0; i < 2*n; i++)
            b[i] = 1.0;

        // constraint matrix
        double[][] A = new double[2*n][n*n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                A[i][i*n+j] = 1.0;
            }
        }
        for (int j = 0; j < n; j++) {
            for (int i = 0; i < n; i++) {
                A[j+n][i*n+j] = 1.0;
            }
        }

        LinearProgramming lp = new LinearProgramming(A, b, c);

        // dual variables
        double[] y = lp.dual();
        px = new double[n];
        for (int i = 0; i < n; i++)
            px[i] = y[i];
        py = new double[n];
        for (int i = 0; i < n; i++)
            py[i] = y[i+n];

        // primal variables
        double[] x = lp.primal();
        xy = new int[n];
        yx = new int[n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (x[i*n+j] == 1.0) {
                    xy[i] = j;
                    yx[j] = i;
                }
            }
        }

        assert check();
    }

    // reduced cost of i-j
    private double reducedCost(int i, int j) {
        return weight[i][j] - px[i] - py[j];
    }

    // total weight of min weight perfect matching
    public double weight() {
        double total = 0.0;
        for (int i = 0; i < n; i++)
            total += weight[i][xy[i]];
        return total;
    }

    public int sol(int i) {
        return xy[i];
    }

    // check that dual variables are feasible
    private boolean isDualFeasible() {
        double EPSILON = 1E-10;
        // check that all edges have >= 0 reduced cost
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (reducedCost(i, j) > EPSILON) {
                    StdOut.println("Dual variables are not feasible");
                    return false;
                }
            }
        }
        return true;
    }

    // check that primal and dual variables are complementary slack
    private boolean isComplementarySlack() {
        double EPSILON = 1E-10;

        // check that all matched edges have 0-reduced cost
        for (int i = 0; i < n; i++) {
            if (reducedCost(i, xy[i]) < -EPSILON) {
                StdOut.println("Primal and dual variables are not complementary slack");
                StdOut.println("Reduced cost of " + i + "-" + xy[i] + " = " + reducedCost(i, xy[i]));
                return false;
            }
        }
        return true;
    }

    // check that primal variables are a perfect matching
    private boolean isPerfectMatching() {

        // check that xy[] is a perfect matching
        boolean[] perm = new boolean[n];
        for (int i = 0; i < n; i++) {
            if (perm[xy[i]]) {
                StdOut.println("Not a perfect matching");
                return false;
            }
            perm[xy[i]] = true;
        }

        // check that xy[] and yx[] are inverses
        for (int j = 0; j < n; j++) {
            if (xy[yx[j]] != j) {
                StdOut.println("xy[] and yx[] are not inverses");
                return false;
            }
        }
        for (int i = 0; i < n; i++) {
            if (yx[xy[i]] != i) {
                StdOut.println("xy[] and yx[] are not inverses");
                return false;
            }
        }

        return true;
    }


    // check optimality conditions
    private boolean check() {
        return isPerfectMatching() && isDualFeasible() && isComplementarySlack();
    }

    public static void main(String[] args) {

        int n = Integer.parseInt(args[0]);
        double[][] weight = new double[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                weight[i][j] = StdRandom.uniformDouble(0.0, 1.0);

        AssignmentProblemToLP assignment = new AssignmentProblemToLP(weight);
        StdOut.println("weight = " + assignment.weight());
        for (int i = 0; i < n; i++)
            StdOut.println(i + "-" + assignment.sol(i));
    }

}