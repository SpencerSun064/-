import drasys.or.mp.Problem;
import drasys.or.mp.lp.DenseSimplex;

public class LPDemo {
    public static void main(String[] args) throws Exception {
//        final byte LTE = Constraint.LESS;
//        final byte GTE = Constraint.GREATER;
//        prob.newConstraint("c2").setType(LTE).setRightHandSide( 100.0);
//        prob.newConstraint("c3").setType(LTE).setRightHandSide(6000.0);

        Problem prob = new Problem(3, 2);
        prob.getMetadata().put("lp.isMaximize", "true");
        prob.newVariable("x1").setObjectiveCoefficient(13.0);
        prob.newVariable("x2").setObjectiveCoefficient(23.0);
        prob.newConstraint("corn").setRightHandSide(480.0);
        prob.newConstraint("hops").setRightHandSide(160.0);
        prob.newConstraint("malt").setRightHandSide(1190.0);

        prob.setCoefficientAt("corn", "x1",  5.0);
        prob.setCoefficientAt("corn", "x2", 15.0);
        prob.setCoefficientAt("hops", "x1",  4.0);
        prob.setCoefficientAt("hops", "x2",  4.0);
        prob.setCoefficientAt("malt", "x1", 35.0);
        prob.setCoefficientAt("malt", "x2", 20.0);

        StdOut.println(prob);
        DenseSimplex lp = new DenseSimplex(prob);
        StdOut.println(lp.solve());
        StdOut.println(lp.getSolution());
    }
}