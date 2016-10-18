package org.fobbyal.msolver.sovler.bd;

import org.fobbyal.msolver.MSolverMatches;
import org.fobbyal.msolver.sovler.MSolverTest;
import org.fobbyal.msolver.sovler.MSolverUtils;
import org.fobbyal.msolver.sovler.value.MSolverResult;
import org.hamcrest.Matcher;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: aliang
 * Date: 8/16/2016
 * Time: 4:35 PM
 */
public class BigDecimalTest extends MSolverTest<BigDecimal> {
    static BigDecimal bd(int value) {
        return new BigDecimal(value);

    }
    static BigDecimal bd(String value) {
        return new BigDecimal(value);
    }

    @Override
    protected MSolverUtils<BigDecimal> createSolverUtil() {
        return new MSolverUtils<>(new BigDecimalNumSpec());
    }

    @Override
    protected Matcher<Map<String, MSolverResult<BigDecimal>>> valueMatcher(BigDecimal expected) {
        return MSolverMatches.varEquals("a", expected);
    }

    void assertExpression(String formula, double expected) {
        assertExpression(formula, new BigDecimal(expected));

    }


}
