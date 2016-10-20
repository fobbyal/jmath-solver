package org.fobbyal.msolver.sovler.bd;

import org.fobbyal.msolver.sovler.tree.Comparison;
import org.fobbyal.msolver.sovler.tree.NumberMSolverMember;

import java.math.BigDecimal;
import java.util.Comparator;

/**
 * Created by fobbyal
 * Creation Time 8/15/16 2:07 AM
 * Project for jmath-solver.
 */
public class BigDecimalComparison extends Comparison<BigDecimal> {
    private static final Comparator<BigDecimal> COMPARATOR = BigDecimal::compareTo;

    public BigDecimalComparison(NumberMSolverMember<BigDecimal> left, Operator op, NumberMSolverMember<BigDecimal> right) {
        super(left, op, right);
    }

    @Override
    protected Comparator<BigDecimal> getComparator() {
        return COMPARATOR;
    }
}
