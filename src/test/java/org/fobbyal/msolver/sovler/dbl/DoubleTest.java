package org.fobbyal.msolver.sovler.dbl;

import org.fobbyal.msolver.sovler.MSolverTest;
import org.fobbyal.msolver.sovler.MSolverUtils;
import org.fobbyal.msolver.sovler.value.MSolverResult;
import org.hamcrest.Matcher;
import parsii.eval.Parser;
import parsii.tokenizer.ParseException;

import java.util.Map;

import static org.fobbyal.msolver.MSolverMatches.varCloseTo;

/**
 * Created by fobbyal
 * Creation Time 8/6/16 7:57 AM
 * Project for jmath-solver.
 */
class DoubleTest extends MSolverTest<Double> {

    public DoubleTest() {
    }

    @Override
    protected MSolverUtils<Double> createSolverUtil() {
        return new MSolverUtils<>(new DoubleNumSpec());
    }

    void assertExpression(String formula, double value) {
        assertExpression(formula, (Double) value);
    }

    void assertExpression(String formula) throws ParseException {
        parsii.eval.Expression expr = Parser.parse(formula);
        assertExpression(formula, expr.evaluate());
    }

    @Override
    protected Matcher<Map<String, MSolverResult<Double>>> valueMatcher(Double expected) {
        return varCloseTo("a", expected);
    }





}
