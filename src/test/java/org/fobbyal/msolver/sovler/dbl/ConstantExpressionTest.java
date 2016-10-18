package org.fobbyal.msolver.sovler.dbl;

import org.testng.annotations.Test;

/**
 * Created by fobbyal
 * Creation Time 8/4/16 12:51 AM
 * Project for jmath-solver.
 */
public class ConstantExpressionTest extends DoubleTest {


    @Test
    public void basicAdditionTest() {
        assertExpression("10+5+5", 20d);
    }


    @Test
    public void basicMultiplicationTest() {
        assertExpression("2*5", 10d);
    }

    @Test
    public void basicDivisionTest() {
        assertExpression("5/2", 2.5);
    }

    @Test
    public void basicSubtractionTest() {
        assertExpression("5-2", 3d);
    }

    @Test
    public void basicNegationTest() {
        assertExpression("(-2)", -2d);
    }

    @Test
    public void divideByZeroTest() {
        //noinspection unchecked
        assertInvalid("1/0");
    }

    @Test
    public void piTest() {
        assertExpression("pi", Math.PI);
    }

    @Test
    public void eTest() {
        assertExpression("e", Math.E);
    }


    @Test
    public void arithmeticOrderTest() {
        assertExpression("1+15*2-22/3", (1d + 15d * 2d - 22d / 3d));
        assertExpression("1+15*2-22/-3", (1d + 15d * 2d - 22d / -3d));
    }

    @Test
    public void arithmeticGroupedOrderTest() {
        assertExpression("(1+15)*2-22/3", ((1d + 15d) * 2d - 22d / 3d));
        assertExpression("1+15*(2-22)/-3", (1d + 15d * (2d - 22d) / -3d));
    }

    @Test
    public void divisionMultiplicationOrderTest() {
        //racing divide and multiply
        assertExpression("73.0 / (6.0 - 72.0) * 25.0", 73.0 / (6.0 - 72.0) * 25.0);
    }

    @Test
    public void additionTest() {
        assertExpression("(45.0 + 63.0)", (45.0 + 63.0));
    }

    @Test
    public void SubtractionTest() {
        assertExpression("45.0 - 75.0", 45.0 - 75.0);
    }

    @Test
    public void multiplicationTest() {
        assertExpression("75.0 * 90.0", 75.0 * 90.0);
    }

    @Test
    public void divisionTest() {
        assertExpression("75.0 / 90.0", 75.0 / 90.0);
    }

    @Test
    public void powTest() {
        assertExpression("33^12", Math.pow(33, 12));
    }

    @Test
    public void additionSubtractionTest() {
        assertExpression("(45.0 - 75.0 / 90.0 + 63.0)", (45.0 - 75.0 / 90.0 + 63.0));
    }


    @Test
    public void powerTest() {
        assertExpression("(45.0 - 75.0^2 / 90.0^3 + 63.0)", (45.0 - Math.pow(75.0, 2d) / Math.pow(90.0, 3) + 63.0));
    }

    @Test
    public void simpleIfTest() {
        assertExpression("if(true,1,0)", 1);
        assertExpression("if(false,1,0)", 0);
        assertExpression("if(!true,1,0)", 0);
        assertExpression("if(!false,1,0)", 1);
        assertExpression("if(1!=1,1,0)", 0);
        assertExpression("if(1==1,1,0)", 1);

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

    @Test
    public void compositePredicateIfTests() {
        assertExpression("if(1==1 && 2==2 ,1,0)", 1);
        assertExpression("if(1==1 && 1==2 ,1,0)", 0);
        assertExpression("if(1==1 || 1==2 ,1,0)", 1);
        assertExpression("if(1==3 || 1==4 ,1,0)", 0);

        assertExpression("if(1==1 and 2==2 ,1,0)", 1);
        assertExpression("if(1==1 and 1==2 ,1,0)", 0);
        assertExpression("if(1==1 or 1==2 ,1,0)", 1);
        assertExpression("if(1==3 or 1==4 ,1,0)", 0);
    }

    @Test
    public void complexComparisonIfTests() {
        assertExpression("if(1==2 || 2==2 && 3==3 ,1,0)", 1);
        assertExpression("if(2==2 && 3==3 || 1==2 ,1,0)", 1);

        assertExpression("if(2==2 || 2==1 && 3==2 ,1,0)", 1);
        assertExpression("if((2==2 || 2==1) && 3==2 ,1,0)", 0);

        assertExpression("if(2==3 && 3==2 || 1==1 ,1,0)", 1);
        assertExpression("if(2==3 &&(3==2 || 1==1),1,0)", 0);

        assertExpression("if(1==2 or 2==2 and 3==3 ,1,0)", 1);
        assertExpression("if(2==2 and 3==3 or 1==2 ,1,0)", 1);

        assertExpression("if(2==2 or 2==1 and 3==2 ,1,0)", 1);
        assertExpression("if((2==2 or 2==1) and 3==2 ,1,0)", 0);

        assertExpression("if(2==3 and 3==2 or 1==1 ,1,0)", 1);
        assertExpression("if(2==3 and(3==2 or 1==1),1,0)", 0);
    }


}
