package org.fobbyal.msolver.sovler.tree;

/**
 * Created by fobbyal
 * Creation Time 8/10/16 11:01 PM
 * Project for jmath-solver.
 */
@FunctionalInterface
public interface BooleanExpression<N> extends MSolverMember<Boolean, N> {
    enum Operator {
        GT, GE, LT, LE, EQ, NEQ, AND, OR
    }

    @FunctionalInterface
    interface BinaryPredicate<N> {
        boolean test(N left, N right);
    }
}
