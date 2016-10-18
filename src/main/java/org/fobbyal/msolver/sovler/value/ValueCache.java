package org.fobbyal.msolver.sovler.value;

/**
 * Created by fobbyal
 * Creation Time 8/4/16 5:34 PM
 * Project for jmath-solver.
 */
@FunctionalInterface
public interface ValueCache<N> {
    MSolverResult<N> getValue(String varName);
}
