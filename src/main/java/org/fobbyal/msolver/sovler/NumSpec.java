package org.fobbyal.msolver.sovler;

import org.fobbyal.msolver.sovler.tree.Comparison;
import org.fobbyal.msolver.sovler.tree.Constant;
import org.fobbyal.msolver.sovler.tree.NumberMSolverMember;
import org.fobbyal.msolver.sovler.tree.BooleanExpression;
import org.fobbyal.msolver.sovler.value.ResultException;

import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by fobbyal
 * Creation Time 8/14/16 12:41 AM
 * Project for jmath-solver.
 */
public interface NumSpec<N> {
    N E();

    N PI();

    N fromString(String strRepresentation);

    default Constant<N> toConstant(String text) {
        return Constant.of(fromString(text));
    }

    default Consumer<Integer> matchNumber(int number) {
        return a -> {
            if (a != number)
                throw new ResultException("Must have " + number + " args.");
        };
    }

    default Consumer<Integer> atLeast(int number) {
        return a -> {
            if (a < number)
                throw new ResultException("Must have at least " + number + " args.");
        };
    }

    BinaryOperator<N> getAddition();

    BinaryOperator<N> getSubtraction();

    BinaryOperator<N> getMultiplication();

    BinaryOperator<N> getDivision();

    BinaryOperator<N> getPow();

    Comparison<N> createComparison(NumberMSolverMember<N> left, BooleanExpression.Operator optr,
                                   NumberMSolverMember<N> right);

    Function<N, N> createNegate();

    Map<String, Function<List<NumberMSolverMember<N>>, NumberMSolverMember<N>>> getAvailableFunctions();
}
