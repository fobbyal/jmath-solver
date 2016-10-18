package org.fobbyal.msolver.sovler.tree;

import org.fobbyal.msolver.sovler.value.MSolverResult;
import org.fobbyal.msolver.sovler.value.ValueCache;

/**
 * Created by fobbyal
 * Creation Time 7/28/16 1:14 AM
 * Project for jmath-solver.
 */
public class Variable<N> extends DynamicNumberMember<N> {

    private final String variableName;

    public Variable(String variableName) {
        this.variableName = variableName;
    }

    @Override
    protected String[] findVars() {
        return new String[]{variableName};
    }

    @Override
    public MSolverResult<N> eval(ValueCache<N> context) {
        return context.getValue(variableName);
    }
}
