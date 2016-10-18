package org.fobbyal.msolver.sovler.tree;

import org.fobbyal.msolver.sovler.ExceptionUtil;
import org.fobbyal.msolver.sovler.value.MSolverResult;
import org.fobbyal.msolver.sovler.value.ValueCache;

import java.util.function.BinaryOperator;

/**
 * Created by fobbyal
 * Creation Time 7/20/16 1:01 AM
 * Project for jmath-solver.
 */
public class BinaryOperation<N> extends DynamicNumberMember<N> {

    private final NumberMSolverMember<N> left;
    private final NumberMSolverMember<N> right;
    private final BinaryOperator<N> binaryFunction;
    @SuppressWarnings({"unused", "FieldCanBeLocal"})
    private final String operator;

    public BinaryOperation(NumberMSolverMember<N> left, NumberMSolverMember<N> right, BinaryOperator<N> binaryFunction, String operator) {
        this.left = left;
        this.right = right;
        this.binaryFunction = binaryFunction;
        this.operator = operator;
    }

    @Override
    protected String[] findVars() {
        String[] leftVars = left.getVars();
        String[] rightVars = right.getVars();
        return concat(leftVars, rightVars);
    }


    public MSolverResult<N> eval(
            NumberMSolverMember<N> left,
            NumberMSolverMember<N> right,
            ValueCache<N> context) {
        return left.eval(context)
                .flatMap(lVal -> right.eval(context)
                        .flatMap(rVal -> ExceptionUtil.guard(
                                () -> MSolverResult.of(binaryFunction.apply(lVal, rVal)))
                        )
                );
    }

    @Override
    public MSolverResult<N> eval(ValueCache<N> context) {
        return eval(left, right, context);
    }

/*    @Override
    public String toString() {
        return left.toString() + " " + operator + " " + right;
    }*/
}
