package org.fobbyal.msolver.sovler.tree;

import org.fobbyal.msolver.sovler.value.InvalidResult;
import org.fobbyal.msolver.sovler.value.MSolverResult;
import org.fobbyal.msolver.sovler.value.ValueCache;

/**
 * Created by fobbyal
 * Creation Time 8/9/16 1:18 AM
 * Project for jmath-solver.
 */
public class CompositePredicate extends AbstractPredicate {

    private final Predicate left;
    private final Operator op;
    private final Predicate right;

    public CompositePredicate(Predicate left, Operator op, Predicate right) {
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
                return InvalidResult.of("found invalid operator [" + op + "] for CompositePredicate");
        }


    }


    @Override
    protected String[] findVars() {
        return concat(left.getVars(), right.getVars());
    }
}
