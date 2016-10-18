package org.fobbyal.msolver.sovler.bd;

import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * Created by fobbyal
 * Creation Time 8/15/16 2:11 AM
 * Project for jmath-solver.
 */
public class BigDecimalExpressionTest extends BigDecimalTest {


    @Test
    public void basicAdditionTest() {
        String a = "12.5";
        String b = "35.234";
        String expr = String.format("%s+%s", a, b);
        BigDecimal expected = bd(a).add(bd(b));
        assertExpression(expr, expected);
    }


    @Test
    public void basicMultiplicationTest() {
        String a = "12.5";
        String b = "35.234";
        String expr = String.format("%s*%s", a, b);
        BigDecimal expected = bd(a).multiply(bd(b), MathContext.DECIMAL64);
        assertExpression(expr, expected);
    }

    @Test
    public void basicDivisionTest() {
        String a = "12.5";
        String b = "35.234";
        String expr = String.format("%s/%s", a, b);
        BigDecimal expected = bd(a).divide(bd(b), MathContext.DECIMAL64);
        assertExpression(expr, expected);
    }

    @Test
    public void basicSubtractionTest() {
        String a = "12.5";
        String b = "35.234";
        String expr = String.format("%s-%s", a, b);
        BigDecimal expected = bd(a).subtract(bd(b));
        assertExpression(expr, expected);
    }

    @Test
    public void basicNegationTest() {
        String b = "35.234";
        String expr = String.format("-%s", b);
        BigDecimal expected = bd(b).negate();
        assertExpression(expr, expected);
    }

    @Test
    public void divideByZeroTest() {
        String a = "12.5";
        String b = "0";
        String expr = String.format("%s/%s", a, b);
        assertInvalid(expr);
    }


    @SuppressWarnings("Duplicates")
    @Test
    public void comparisonIfTests() {

        assertExpression("if(2>1 ,1,0)", 1);
        assertExpression("if(1>2 ,1,0)", 0);

        assertExpression("if(2>=1 ,1,0)", 1);
        assertExpression("if(1>=2 ,1,0)", 0);
        assertExpression("if(2>=2 ,1,0)", 1);

        assertExpression("if(2<1 ,1,0)", 0);
        assertExpression("if(1<2 ,1,0)", 1);

        assertExpression("if(2<=1 ,1,0)", 0);
        assertExpression("if(2<=2 ,1,0)", 1);
        assertExpression("if(1<=2 ,1,0)", 1);

        assertExpression("if(2==1 ,1,0)", 0);
        assertExpression("if(1==1 ,1,0)", 1);

        assertExpression("if(1<>1 ,1,0)", 0);
        assertExpression("if(2<>1 ,1,0)", 1);
        assertExpression("if(1!=1 ,1,0)", 0);
        assertExpression("if(2!=1 ,1,0)", 1);
    }

}
