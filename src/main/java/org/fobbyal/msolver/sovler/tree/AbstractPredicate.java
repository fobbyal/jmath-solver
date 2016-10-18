package org.fobbyal.msolver.sovler.tree;

/**
 * Created with IntelliJ IDEA.
 * User: aliang
 * Date: 8/15/2016
 * Time: 2:05 PM
 */
public abstract class AbstractPredicate<N> implements Predicate<N> {

    private String[] vars;

    @Override
    public final String[] getVars() {
        return vars == null ? vars = findVars() : vars;
    }

    protected abstract String[] findVars();
}
