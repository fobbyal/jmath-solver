package org.fobbyal.msolver.sovler;

import org.fobbyal.msolver.MSolverMatches;
import org.fobbyal.msolver.sovler.value.MSolverResult;
import org.hamcrest.Matcher;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;

/**
 * Created with IntelliJ IDEA.
 * User: aliang
 * Date: 8/16/2016
 * Time: 4:22 PM
 */
public abstract class MSolverTest<N> {
    protected MSolverUtils<N> solverUtil;

    public MSolverTest() {
        this.solverUtil = createSolverUtil();
    }

    protected abstract MSolverUtils<N> createSolverUtil();

    protected void assertExpression(String formula, N expected) {
        assertThat(solve("a = " + formula), valueMatcher(expected));
    }


    protected abstract Matcher<Map<String, MSolverResult<N>>> valueMatcher(N expected);

    protected void assertInvalid(String formula) {
        assertThat(solve("a =" + formula), not(validMatcher()));
    }

    private Matcher<Map<String, MSolverResult<N>>> validMatcher() {
        return MSolverMatches.validMatcher("a");
    }

    private MSolver<N> createSolver(String formula) {
        return solverUtil.createSolver(formula);
    }

    protected Map<String, MSolverResult<N>> solve(String formula, Map<String, N> input) {
        return createSolver(formula).solve(input);
    }

    protected Map<String, MSolverResult<N>> solve(String formula) {
        return createSolver(formula).solve(new HashMap<>());
    }
}
