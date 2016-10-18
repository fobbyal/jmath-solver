package org.fobbyal.msolver.sovler.tree;

/**
 * Created with IntelliJ IDEA.
 * User: aliang
 * Date: 8/15/2016
 * Time: 2:13 PM
 */
public abstract class DynamicNumberMember<N> implements NumberMSolverMember<N> {

    private String[] vars;

    @Override
    public final String[] getVars() {
        return vars == null ? vars = findVars() : vars;
    }

    protected abstract String[] findVars();
}
