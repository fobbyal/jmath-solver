package org.fobbyal.msolver.sovler.tree;

import org.fobbyal.msolver.sovler.ExceptionUtil;
import org.fobbyal.msolver.sovler.value.MSolverResult;
import org.fobbyal.msolver.sovler.value.ResultException;
import org.fobbyal.msolver.sovler.value.ValueCache;

import java.util.Comparator;

/**
 * Created by fobbyal
 * Creation Time 8/13/16 11:03 PM
 * Project for jmath-solver.
 */
public abstract class ComparisonPredicate<N> extends AbstractPredicate<N> {
    private final NumberMSolverMember<N> left;
    private final NumberMSolverMember<N> right;
    private final Operator op;

    public ComparisonPredicate(NumberMSolverMember<N> left, Operator op, NumberMSolverMember<N> right) {
        this.left = left;
        this.right = right;
        this.op = op;
    }

    protected boolean isGreaterThan(N left, N right) {
        return getComparator().compare(left, right) > 0;
    }

    protected boolean isGreaterOrEqTo(N left, N right) {
        return getComparator().compare(left, right) >= 0;
    }

    protected boolean isLessThan(N left, N right) {
        return getComparator().compare(left, right) < 0;
    }

    protected boolean isLessThanOrEqTo(N left, N right) {
        return getComparator().compare(left, right) <= 0;
    }

    protected boolean isEqual(N left, N right) {
        return getComparator().compare(left, right) == 0;
    }

    protected boolean isNotEqual(N left, N right) {
        return getComparator().compare(left, right) != 0;
    }


    protected abstract Comparator<N> getComparator();


    protected BinaryPredicate<N> getPredicate(Operator op) {
        switch (op) {
            case EQ:
                return this::isEqual;
            case NEQ:
                return this::isNotEqual;
            case GT:
                return this::isGreaterThan;
            case GE:
                return this::isGreaterOrEqTo;
            case LT:
                return this::isLessThan;
            case LE:
                return this::isLessThanOrEqTo;
            default:
                throw new ResultException("Operator " + op + " not implemented");
        }

    }


    @SuppressWarnings({"unchecked", "RedundantCast"})
    @Override
    public MSolverResult eval(ValueCache context) {
        return left.eval(context)
                .flatMap(l -> right.eval(context)
                        .flatMap(r -> ExceptionUtil.guard(() -> MSolverResult.of(getPredicate(op).test((N) l, (N) r)))));
    }

    @Override
    protected String[] findVars() {
        return concat(left.getVars(), right.getVars());
    }

/*    @Override
    public String toString() {
        return left.toString() + op + right.toString();
    }*/
}
