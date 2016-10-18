package org.fobbyal.msolver.sovler.bd;

import org.fobbyal.msolver.MSolverMatches;
import org.fobbyal.msolver.sovler.MSolver;
import org.fobbyal.msolver.sovler.tree.NumberMSolverMember;
import org.fobbyal.msolver.sovler.value.MSolverResult;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;
import java.util.Map;

import static org.fobbyal.msolver.MSolverMatches.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Created by fobbyal
 * Creation Time 8/16/16 11:33 PM
 * Project for jmath-solver.
 */
@SuppressWarnings({"StringBufferReplaceableByString", "Duplicates"})
public class BigDecimalFormulaTest extends BigDecimalTest {
    @Test
    public void testCyclicDependency() {
        solve("a = 2*b\n b = 2*a").entrySet().forEach(
                e -> assertThat(e.getValue().toString(), containsString("cyclically"))
        );

    }

    @Test
    public void testGetVars() throws Exception {
        NumberMSolverMember<BigDecimal> vars = solverUtil.compile("a = b + d + g").get("a");
        assertThat(vars.getVarSet(), hasSize(3));
        assertThat(vars.getVarSet(), contains("b", "d", "g"));
    }

    @Test
    public void testSolveDivideByZero() {
        StringBuilder formula = new StringBuilder();
        formula.append("a = if(b==0,c,c/b)");

        HashMap<String, BigDecimal> input = new HashMap<>();
        input.put("b", bd("0"));
        input.put("c", bd("123"));

        MSolver<BigDecimal> solver = solverUtil.createSolver(formula.toString());

        assertThat(solver.solve(input), varEquals("a", bd("123")));

        input.put("b", bd("123"));
        input.put("c", bd("123"));

        assertThat(solver.solve(input), MSolverMatches.varEquals("a", bd("1")));

    }


    @Test
    public void testSolve1() throws Exception {
        StringBuilder formula = new StringBuilder();

        formula.append("a = b + (f + g) / f + if(b <15, f-523.6, g )\n");
        formula.append("b = 2 * c \n");
        formula.append("d = 1.2345 + f\n");

        HashMap<String, BigDecimal> input = new HashMap<>();
        input.put("c", bd("23.223"));
        input.put("g", bd("22.123"));
        input.put("f", bd("987.889"));


        Map<String, MSolverResult<BigDecimal>> answer = solve(formula.toString(), input);


        BigDecimal c = new BigDecimal("23.223");
        BigDecimal g = new BigDecimal("22.123");
        BigDecimal f = new BigDecimal("987.889");

        BigDecimal d = bd("1.2345").add(f);
        BigDecimal b = bd(2).multiply(c);
        BigDecimal a = b.add(f.add(g).divide(f, MathContext.DECIMAL64)).add((b.compareTo(bd(15)) < 0 ? f.subtract(bd
                ("523.6")) : g));

        assertThat(answer, varEquals("d", d));
        assertThat(answer, varEquals("b", b));
        assertThat(answer, varCloseTo("a", a));
    }

    @Test
    public void testSolve2() throws Exception {
        StringBuilder formula = new StringBuilder();

        formula.append("a = b + (f + g) / f + if(b <15 || d<700, f-523.6, g )\n");
        formula.append("b = 2 * c \n");
        formula.append("d = 1.2345 + f\n");

        HashMap<String, BigDecimal> input = new HashMap<>();
        input.put("c", bd("23.223"));
        input.put("g", bd("22.123"));
        input.put("f", bd("987.889"));


        Map<String, MSolverResult<BigDecimal>> answer = solve(formula.toString(), input);


        BigDecimal c = new BigDecimal("23.223");
        BigDecimal g = new BigDecimal("22.123");
        BigDecimal f = new BigDecimal("987.889");

        BigDecimal d = new BigDecimal("1.2345").add(f);
        BigDecimal b = c.multiply(new BigDecimal("2"));
        BigDecimal a = b.add(f.add(g).divide(f, MathContext.DECIMAL64)).add(
                b.compareTo(bd(15)) < 0 || d.compareTo(bd(700)) < 0 ? f.subtract(bd("523.6")) :
                        g);

        assertThat(answer, varEquals("d", d));
        assertThat(answer, varEquals("b", b));
        assertThat(answer, varCloseTo("a", a));
    }

    @Test
    public void testSolve3() throws Exception {
        StringBuilder formula = new StringBuilder();

        formula.append("a = b + f + g\n");
        formula.append("b = 2 * c \n");
        formula.append("d = 1.2345 + f\n");

        HashMap<String, BigDecimal> input = new HashMap<>();
        input.put("c", bd("23.223"));
        input.put("g", bd("22.123"));
        input.put("f", bd("987.889"));


        Map<String, MSolverResult<BigDecimal>> answer = solve(formula.toString(), input);


        BigDecimal c = new BigDecimal("23.223");
        BigDecimal g = new BigDecimal("22.123");
        BigDecimal f = new BigDecimal("987.889");

        BigDecimal d = bd("1.2345").add(f);
        BigDecimal b = bd(2).multiply(c);
        BigDecimal a = b.add(f).add(g);

        assertThat(answer, varEquals("d", d));
        assertThat(answer, varEquals("b", b));
        assertThat(answer, varEquals("a", a));
    }

    @Test
    public void testInvalidFormula() throws Exception {
        StringBuilder formula = new StringBuilder();
        formula.append("b =2 + g * c + f / 700 \n");
        formula.append("d = 1.2345 + f\n");
        formula.append("a = b + (f * 12 + g^4) / (f*d) + if(b <15, f-523.6, g )\n");

        HashMap<String, BigDecimal> input = new HashMap<>();


        Map<String, MSolverResult<BigDecimal>> answer = solve(formula.toString(), input);


        assertThat(answer, not(validMatcher("d")));
        assertThat(answer, not(validMatcher("b")));
        assertThat(answer, not(validMatcher("a")));
    }

    @Test
    public void testSolve4() throws Exception {
        StringBuilder formula = new StringBuilder();

        formula.append("b =2 + g * c + f / 700 \n");
        formula.append("d = 1.2345 + f\n");
        formula.append("a = b + (f * 12 + g^4) / (f*d) + if(b <15, f-523.6, g )\n");

        HashMap<String, BigDecimal> input = new HashMap<>();
        input.put("c", bd("23.223"));
        input.put("g", bd("22.123"));
        input.put("f", bd("987.889"));


        Map<String, MSolverResult<BigDecimal>> answer = solve(formula.toString(), input);


        BigDecimal c = new BigDecimal("23.223");
        BigDecimal g = new BigDecimal("22.123");
        BigDecimal f = new BigDecimal("987.889");

        BigDecimal d = bd("1.2345").add(f);
        BigDecimal b = bd(2).add(g.multiply(c)).add(f.divide(bd(700), MathContext.DECIMAL64));
        BigDecimal a = b.add(f.multiply(bd(12)).add(g.pow(4)).divide(f.multiply(d), MathContext.DECIMAL64))
                .add(b.compareTo(bd(15)) < 0 ? f.subtract(bd("523.6")) : g);

        assertThat(answer, varEquals("d", d));
        assertThat(answer, varEquals("b", b));
        assertThat(answer, varCloseTo("a", a));
    }

    //todo: test after functions are done for big decimal
//    @Test
//    public void testNestedFunctions() throws Exception {
//        StringBuilder formula = new StringBuilder();
//
//        formula.append("b = 2 + sqrt(g * c) + f / sqrt(700) \n");
//        formula.append("d = 1.2345 + f\n");
//        formula.append("a = b + (f * 12 + g^sin(4.23)) / (f*d) + if(b <15, f-523.6, g )\n");
//
//        HashMap<String, BigDecimal> input = new HashMap<>();
//        input.put("c", bd("23.223"));
//        input.put("g", bd("22.123"));
//        input.put("f", bd("987.889"));
//
//
//        Map<String, MSolverResult<BigDecimal>> answer = solve(formula.toString(), input);
//
//
//        BigDecimal c = new BigDecimal("23.223");
//        BigDecimal g = new BigDecimal("22.123");
//        BigDecimal f = new BigDecimal("987.889");
//
//        BigDecimal d = 1.2345 * 100 / 100d + f;
//        BigDecimal b = 2 + Math.sqrt(g * c) + f / Math.sqrt(700);
//        BigDecimal a = b + (f * 12 + Math.pow(g, Math.sin(4.23))) / (f * d) + (b < 15 ? (f - 523.6) : g);
//
//        assertThat(answer, varEquals("d", d));
//        assertThat(answer, varEquals("b", b));
//        assertThat(answer, varEquals("a", a));
//    }

    @Test
    public void testCaseSensitivity4() throws Exception {
        StringBuilder formula = new StringBuilder();

        formula.append("b =2 + g * c + f / 700 \n");
        formula.append("d = 1.2345 + f\n");
        formula.append("a = b + (f * 12 + g^4) / (f*d) + if(b <15, f-523.6, g )");
        //formula.append("a = b + (f * 12 + g) / (f*d)");
        //formula.append("a = b + (f * 12 + g^4) / (f*d)");
        //formula.append("a = g^4");

        HashMap<String, BigDecimal> input = new HashMap<>();
        input.put("C", bd("23.223"));
        input.put("G", bd("22.123"));
        input.put("F", bd("987.889"));


        Map<String, MSolverResult<BigDecimal>> answer = solve(formula.toString(), input);


        BigDecimal c = new BigDecimal("23.223");
        BigDecimal g = new BigDecimal("22.123");
        BigDecimal f = new BigDecimal("987.889");

        BigDecimal d = bd("1.2345").add(f);
        BigDecimal b = bd(2).add(g.multiply(c)).add(f.divide(bd(700), MathContext.DECIMAL64));
        BigDecimal a = b.add(f.multiply(bd(12)).add(g.pow(4)).divide(f.multiply(d), MathContext.DECIMAL64))
                .add(b.compareTo(bd(15)) < 0 ? f.subtract(bd("523.6")) : g);


        assertThat(answer, varEquals("d", d));
        assertThat(answer, varEquals("b", b));
        assertThat(answer, varCloseTo("a", a));
    }
}
