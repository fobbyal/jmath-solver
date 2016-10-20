package org.fobbyal.msolver.sovler.tree;

import org.fobbyal.msolver.sovler.value.InvalidResult;
import org.fobbyal.msolver.sovler.value.MSolverResult;
import org.fobbyal.msolver.sovler.value.ValueCache;

/**
 * Created by fobbyal
 * Creation Time 8/9/16 1:18 AM
 * Project for jmath-solver.
 */
public class CompositeBoolExpression extends AbstractBooleanExpression {

    private final BooleanExpression left;
    private final Operator op;
    private final BooleanExpression right;

    public CompositeBoolExpression(BooleanExpression left, Operator op, BooleanExpression right) {
        this.left = left;
        this.op = op;
        this.right = right;
    }

    @SuppressWarnings("unchecked")
    @Override
    public MSolverResult eval(ValueCache context) {
        switch (op) {
            case AND:
                return left.eval(context)
                        .flatMap(l -> right.eval(context)
                                .flatMap(r -> MSolverResult.of((Boolean) l && (Boolean) r)));
            case OR:
                return left.eval(context)
                        .flatMap(l -> right.eval(context)
                                .flatMap(r -> MSolverResult.of((Boolean) l || (Boolean) r)));
            default:
                return InvalidResult.of("found invalid operator [" + op + "] for CompositeBoolExpression");
        }


    }


    @Override
    protected String[] findVars() {
        return concat(left.getVars(), right.getVars());
    }
}
