package org.fobbyal.msolver.sovler.tree;


import org.fobbyal.msolver.sovler.value.MSolverResult;
import org.fobbyal.msolver.sovler.value.ValueCache;

import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

/**
 * Created by fobbyal
 * Creation Time 7/20/16 12:49 AM
 * Project for jmath-solver.
 */
@FunctionalInterface
public interface MSolverMember<N, I> {

    String[] NO_VARS = new String[0];

    MSolverResult<N> eval(ValueCache<I> context);

    default String[] getVars() {
        return NO_VARS;
    }

    default Set<String> getVarSet() {
        return Stream.of(getVars()).collect(toSet());
    }

    default String[] concat(String[] leftVars, String[] rightVars) {
        if (leftVars.length == 0 && rightVars.length == 0) {
            return NO_VARS;
        }
        if (leftVars.length == 0) {
            return rightVars;
        }
        if (rightVars.length == 0) {
            return leftVars;
        }
        String[] result = new String[leftVars.length + rightVars.length];
        System.arraycopy(leftVars, 0, result, 0, leftVars.length);
        System.arraycopy(rightVars, 0, result, leftVars.length, rightVars.length);
        return result;
    }

}
