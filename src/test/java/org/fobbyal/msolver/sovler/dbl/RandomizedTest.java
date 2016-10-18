package org.fobbyal.msolver.sovler.dbl;

import org.fobbyal.msolver.MathExpressionGenerator;
import org.fobbyal.msolver.MathExpressionGenerator.Expression;
import org.testng.annotations.Test;
import parsii.tokenizer.ParseException;

/**
 * Created by fobbyal
 * Creation Time 8/6/16 7:06 AM
 * Project for jmath-solver.
 */
public class RandomizedTest extends DoubleTest {


    @Test
    public void testBasicMathExpression() throws Exception {
        MathExpressionGenerator generator = new MathExpressionGenerator();
        for (Expression expression : generator.generate(100000)) {
            assertExpression(expression);
        }

    }

    private void assertExpression(Expression expression) throws ParseException {
        assertExpression(expression.toString());
    }

}
