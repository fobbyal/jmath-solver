package org.fobbyal.msolver.sovler.dbl;


import org.fobbyal.msolver.sovler.tree.ComparisonPredicate;
import org.fobbyal.msolver.sovler.tree.NumberMSolverMember;

import java.util.Comparator;


/**
 * Created by fobbyal
 * Creation Time 8/10/16 12:48 AM
 * Project for jmath-solver.
 */
public class DoubleComparison extends ComparisonPredicate<Double> {
    private static final Comparator<Double> DOUBLE_COMPARATOR = Double::compareTo;

    public DoubleComparison(NumberMSolverMember<Double> left, Operator op, NumberMSolverMember<Double> right) {
        super(left, op, right);
    }

    @Override
    protected Comparator<Double> getComparator() {
        return DOUBLE_COMPARATOR;
    }

}
