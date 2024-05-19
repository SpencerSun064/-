import java.io.IOException;
import qs.Problem;
import qs.QS;
import qs.QSException;
import qs.Reporter;

public class QSoptSolver {

    public static void main(String[] args) throws QSException, IOException {
        Problem problem = Problem.read(args[0], false);
        problem.opt_primal();
        if (problem.get_status() == QS.LP_OPTIMAL) {
            StdOut.println("Optimal " + problem.get_objname() + " = " + problem.get_objval());
            StdOut.println("Optimal primal solution (nonzeros only): ");
            problem.print_x(new Reporter(System.out), true, 6);
            StdOut.println("Optimal dual solution (nonzeros only): ");
            problem.print_pi(new Reporter(System.out), true, 6);
        }
        else {
            System.out.println("Problem maybe unbounded or infeasible.\n");
        }
    }
}