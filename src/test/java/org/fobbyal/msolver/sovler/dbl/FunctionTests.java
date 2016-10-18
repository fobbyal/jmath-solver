package org.fobbyal.msolver.sovler.dbl;

import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by fobbyal
 * Creation Time 8/12/16 1:23 AM
 * Project for jmath-solver.
 */
public class FunctionTests extends DoubleTest {

    @Test
    public void testRandom() {
        Double value = solve("a=random()").get("a").unwrap();
        assert (Double.isFinite(value));
    }

    @Test
    public void testMin() {
        assertExpression("min(1,2,34+55,-334,6,7,0,-3883)", -3883);
    }

    @Test
    public void testMax() {
        assertExpression("max(1,2,344,1-334,6,7,0,-3883)", 344);
    }

    @Test
    public void absTest() {
        assertExpression("abs(-3883)", 3883);
        assertExpression("abs(1-3883)", 3882);
    }

    @Test
    public void avgTest() {
        Random rand = new Random();
        List<Double> values =
                IntStream.range(10, 1500).mapToObj(
                        i -> Math.random() * Math.pow(10, rand.nextInt(5)))
                        .map(a -> Math.round(a * 100000) / 100000d)
                        .collect(Collectors.toList());
        double avg = values.stream().map(Object::toString).map(Double::valueOf).reduce(0d, (a, b) -> a + b) /
                (double) values.size();
        String vars = values.stream().map(a -> a + "").reduce("", (a, b) -> (Objects.equals(a, "") ? "" : a + ",") + b);
        Double result = solve("a = avg(" + vars + ")").get("a").unwrap();
        assertThat(" Avg of " + values.size() + " should varCloseTo raw call " + new DoubleNumSpec().createAvgFunction()
                        .apply(values) + "\navg(" + vars + ")",
                result, Matchers.closeTo(avg, avg * 0.001));
        //assertExpression("avg(" + vars + ")", avg);
    }

    @Test
    public void roundTest() {
        assertExpression("round(3883.2341351,0)", 3883);
        assertExpression("round(3883.7341351,0)", 3884);
        assertExpression("round(3883.2341351,5)", 3883.23414);
        assertExpression("round(3883.7341351,6)", 3883.734135);
    }

    @Test
    public void floorTest() {
        assertExpression("floor(3883.2341351)", 3883);
        assertExpression("floor(3883.7341351)", 3883);
    }

    @Test
    public void ceilingTest() {
        assertExpression("ceiling(3883.2341351)", 3884);
        assertExpression("ceiling(3883.7341351)", 3884);
    }

    @Test
    public void logTest() {
        assertExpression("log(234.2341351)", Math.log(234.2341351));
        assertExpression("log(83.7341351)", Math.log(83.7341351));
    }


    @Test
    public void log10Test() {
        assertExpression("log10(234.2341351)", Math.log10(234.2341351));
        assertExpression("log10(83.7341351)", Math.log10(83.7341351));
    }

    @Test
    public void sqrtTest() {
        assertExpression("sqrt(234.2341351)", Math.sqrt(234.2341351));
        assertExpression("sqrt(83.7341351)", Math.sqrt(83.7341351));
    }


    @Test
    public void sinTest() {
        double value = Math.random();
        assertExpression("sin(" + value + ")", Math.sin(value));
    }

    @Test
    public void cosTest() {
        double value = Math.random();
        assertExpression("cos(" + value + ")", Math.cos(value));
    }


    @Test
    public void tanTest() {
        double value = Math.random();
        assertExpression("tan(" + value + ")", Math.tan(value));
    }

    @Test
    public void asinTest() {
        double value = Math.random();
        assertExpression("asin(" + value + ")", Math.asin(value));
    }

    @Test
    public void acosTest() {
        double value = Math.random();
        assertExpression("acos(" + value + ")", Math.acos(value));
    }

    @Test
    public void atanTest() {
        double value = Math.random();
        assertExpression("atan(" + value + ")", Math.atan(value));
    }

    @Test
    public void sinhTest() {
        double value = Math.random();
        assertExpression("sinh(" + value + ")", Math.sinh(value));
    }

    @Test
    public void coshTest() {
        double value = Math.random();
        assertExpression("cosh(" + value + ")", Math.cosh(value));
    }

    @Test
    public void tanhTest() {
        double value = Math.random();
        assertExpression("tanh(" + value + ")", Math.tanh(value));
    }

    @Test
    public void radTest() {
        double value = Math.random();
        assertExpression("rad(" + value + ")", Math.toRadians(value));
    }

    @Test
    public void degTest() {
        double value = Math.random();
        assertExpression("deg(" + value + ")", Math.toDegrees(value));
    }
}
