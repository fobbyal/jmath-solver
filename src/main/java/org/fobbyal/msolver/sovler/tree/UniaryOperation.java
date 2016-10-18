package org.fobbyal.msolver.sovler.tree;

import org.fobbyal.msolver.sovler.value.MSolverResult;
import org.fobbyal.msolver.sovler.value.ValueCache;

import java.util.function.Function;

/**
 * Created by fobbyal
 * Creation Time 8/5/16 10:45 PM
 * Project for jmath-solver.
 */
public class UniaryOperation<N> extends DynamicNumberMember<N> {

    private final NumberMSolverMember<N> value;
    private final Function<N, N> func;

    public UniaryOperation(NumberMSolverMember<N> value, Function<N, N> func) {
        this.value = value;
        this.func = func;
    }


    @Override
    public MSolverResult<N> eval(ValueCache<N> context) {
        return value.eval(context).flatMap(a -> MSolverResult.of(func.apply(a)));
    }

    @Override
    protected String[] findVars() {
        return value.getVars();
    }
}
