package org.fobbyal.msolver.sovler.tree;

import org.fobbyal.msolver.sovler.value.MSolverResult;
import org.fobbyal.msolver.sovler.value.ValueCache;

/**
 * Created by fobbyal
 * Creation Time 8/3/16 11:58 PM
 * Project for jmath-solver.
 */
public class Constant<N> implements NumberMSolverMember<N> {

    private final MSolverResult<N> value;

    private Constant(MSolverResult<N> value) {
        this.value = value;
    }

    public static <N> Constant<N> of(N value) {
        return new Constant<>(MSolverResult.of(value));
    }


    @Override
    public MSolverResult<N> eval(ValueCache<N> context) {
        return value;
    }

}
