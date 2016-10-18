package org.fobbyal.msolver.sovler.dbl;

import org.fobbyal.msolver.MSolverMatches;
import org.fobbyal.msolver.sovler.MSolver;
import org.fobbyal.msolver.sovler.tree.NumberMSolverMember;
import org.fobbyal.msolver.sovler.value.MSolverResult;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.fobbyal.msolver.MSolverMatches.validMatcher;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Created by fobbyal
 * Creation Time 8/4/16 6:13 PM
 * Project for jmath-solver.
 */
@SuppressWarnings({"Duplicates", "StringBufferReplaceableByString"})
public class DoubleFormulaTest extends DoubleTest {

    @Test
    public void testCyclicDependency() {
        solve("a = 2*b\n b = 2*a").entrySet().forEach(
                e -> assertThat(e.getValue().toString(), containsString("cyclically"))
        );

    }

    @Test
    public void testGetVars() throws Exception {
        NumberMSolverMember<Double> vars = solverUtil.compile("a = b + d + g").get("a");
        assertThat(vars.getVarSet(), hasSize(3));
        assertThat(vars.getVarSet(), contains("b", "d", "g"));
    }

    @Test
    public void testSolveDivideByZero() {
        StringBuilder formula = new StringBuilder();
        formula.append("a = if(b==0,c,c/b)");

        HashMap<String, Double> input = new HashMap<>();
        input.put("b", 0d);
        input.put("c", 123d);

        MSolver<Double> solver = solverUtil.createSolver(formula.toString());

        assertThat(solver.solve(input), MSolverMatches.varCloseTo("a", 123d));

        input.put("b", 123d);
        input.put("c", 123d);

        assertThat(solver.solve(input), MSolverMatches.varCloseTo("a", 1d));
        //assertThat("Divide by zero should return invalid number", solverUtil.createSolver("a = 1/0").solve(new TreeMap<>()), MSolverMatches.is);

    }


    @Test
    public void testSolve1() throws Exception {
        StringBuilder formula = new StringBuilder();

        formula.append("a = b + (f + g) / f + if(b <15, f-523.6, g )\n");
        formula.append("b = 2 * c \n");
        formula.append("d = 1.2345 + f\n");

        HashMap<String, Double> input = new HashMap<>();
        input.put("c", 23.223);
        input.put("g", 22.123);
        input.put("f", 987.889);


        Map<String, MSolverResult<Double>> answer = solve(formula.toString(), input);


        double c = 23.223;
        double g = 22.123;
        double f = 987.889;

        double d = 1.2345 + f;
        double b = 2 * c;
        double a = b + (f + g) / f + (b < 15 ? (f - 523.6) : g);

        assertThat(answer, MSolverMatches.varCloseTo("d", d));
        assertThat(answer, MSolverMatches.varCloseTo("b", b));
        assertThat(answer, MSolverMatches.varCloseTo("a", a));
    }

    @Test
    public void testSolve2() throws Exception {
        StringBuilder formula = new StringBuilder();

        formula.append("a = b + (f + g) / f + if(b <15 || d<700, f-523.6, g )\n");
        formula.append("b = 2 * c \n");
        formula.append("d = 1.2345 + f\n");

        HashMap<String, Double> input = new HashMap<>();
        input.put("c", 23.223);
        input.put("g", 22.123);
        input.put("f", 987.889);


        Map<String, MSolverResult<Double>> answer = solve(formula.toString(), input);


        double c = 23.223;
        double g = 22.123;
        double f = 987.889;

        double d = 1.2345 + f;
        double b = 2 * c;
        double a = b + (f + g) / f + (b < 15 || d < 700 ? (f - 523.6) : g);

        assertThat(answer, MSolverMatches.varCloseTo("d", d));
        assertThat(answer, MSolverMatches.varCloseTo("b", b));
        assertThat(answer, MSolverMatches.varCloseTo("a", a));
    }

    @Test
    public void testSolve3() throws Exception {
        StringBuilder formula = new StringBuilder();

        formula.append("a = b + f + g\n");
        formula.append("b = 2 * c \n");
        formula.append("d = 1.2345 + f\n");

        HashMap<String, Double> input = new HashMap<>();
        input.put("c", 23.223);
        input.put("g", 22.123);
        input.put("f", 987.889);


        Map<String, MSolverResult<Double>> answer = solve(formula.toString(), input);


        double c = 23.223;
        double g = 22.123;
        double f = 987.889;

        double d = 1.2345 + f;
        double b = 2 * c;
        double a = b + f + g;

        assertThat(answer, MSolverMatches.varCloseTo("d", d));
        assertThat(answer, MSolverMatches.varCloseTo("b", b));
        assertThat(answer, MSolverMatches.varCloseTo("a", a));
    }

    @Test
    public void testInvalidFormula() throws Exception {
        StringBuilder formula = new StringBuilder();
        formula.append("b =2 + g * c + f / 700 \n");
        formula.append("d = 1.2345 + f\n");
        formula.append("a = b + (f * 12 + g^4.23) / (f*d) + if(b <15, f-523.6, g )\n");

        HashMap<String, Double> input = new HashMap<>();


        Map<String, MSolverResult<Double>> answer = solve(formula.toString(), input);


        assertThat(answer, not(validMatcher("d")));
        assertThat(answer, not(validMatcher("b")));
        assertThat(answer, not(validMatcher("a")));
    }

    @Test
    public void testSolve4() throws Exception {
        StringBuilder formula = new StringBuilder();

        formula.append("b =2 + g * c + f / 700 \n");
        formula.append("d = 1.2345 + f\n");
        formula.append("a = b + (f * 12 + g^4.23) / (f*d) + if(b <15, f-523.6, g )\n");

        HashMap<String, Double> input = new HashMap<>();
        input.put("c", 23.223);
        input.put("g", 22.123);
        input.put("f", 987.889);


        Map<String, MSolverResult<Double>> answer = solve(formula.toString(), input);


        double c = 23.223;
        double g = 22.123;
        double f = 987.889;

        double d = 1.2345 + f;
        double b = 2 + g * c + f / 700;
        double a = b + (f * 12 + Math.pow(g, 4.23)) / (f * d) + (b < 15 ? (f - 523.6) : g);

        assertThat(answer, MSolverMatches.varCloseTo("d", d));
        assertThat(answer, MSolverMatches.varCloseTo("b", b));
        assertThat(answer, MSolverMatches.varCloseTo("a", a));
    }

    @Test
    public void testNestedFunctions() throws Exception {
        StringBuilder formula = new StringBuilder();

        formula.append("b = 2 + sqrt(g * c) + f / sqrt(700) \n");
        formula.append("d = round(1.2345,2) + f\n");
        formula.append("a = b + (f * 12 + g^sin(4.23)) / (f*d) + if(b <15, f-523.6, g )\n");

        HashMap<String, Double> input = new HashMap<>();
        input.put("c", 23.223);
        input.put("g", 22.123);
        input.put("f", 987.889);


        Map<String, MSolverResult<Double>> answer = solve(formula.toString(), input);


        double c = 23.223;
        double g = 22.123;
        double f = 987.889;

        double d = Math.round(1.2345 * 100) / 100d + f;
        double b = 2 + Math.sqrt(g * c) + f / Math.sqrt(700);
        double a = b + (f * 12 + Math.pow(g, Math.sin(4.23))) / (f * d) + (b < 15 ? (f - 523.6) : g);

        assertThat(answer, MSolverMatches.varCloseTo("d", d));
        assertThat(answer, MSolverMatches.varCloseTo("b", b));
        assertThat(answer, MSolverMatches.varCloseTo("a", a));
    }

    @Test
    public void testCaseSensitivity4() throws Exception {
        StringBuilder formula = new StringBuilder();

        formula.append("b =2 + g * c + f / 700 \n");
        formula.append("d = 1.2345 + f\n");
        formula.append("a = b + (f * 12 + g^4.23) / (f*d) + if(b <15, f-523.6, g )\n");

        HashMap<String, Double> input = new HashMap<>();
        input.put("C", 23.223);
        input.put("G", 22.123);
        input.put("F", 987.889);


        Map<String, MSolverResult<Double>> answer = solve(formula.toString(), input);


        double c = 23.223;
        double g = 22.123;
        double f = 987.889;

        double d = 1.2345 + f;
        double b = 2 + g * c + f / 700;
        double a = b + (f * 12 + Math.pow(g, 4.23)) / (f * d) + (b < 15 ? (f - 523.6) : g);

        assertThat(answer, MSolverMatches.varCloseTo("d", d));
        assertThat(answer, MSolverMatches.varCloseTo("b", b));
        assertThat(answer, MSolverMatches.varCloseTo("a", a));
    }


}