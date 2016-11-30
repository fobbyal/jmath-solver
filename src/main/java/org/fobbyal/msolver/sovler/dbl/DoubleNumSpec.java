package org.fobbyal.msolver.sovler.dbl;

import org.fobbyal.msolver.sovler.NumSpec;
import org.fobbyal.msolver.sovler.tree.BooleanExpression;
import org.fobbyal.msolver.sovler.tree.Comparison;
import org.fobbyal.msolver.sovler.tree.MathFunction;
import org.fobbyal.msolver.sovler.tree.NumberMSolverMember;
import org.fobbyal.msolver.sovler.value.ResultException;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.BinaryOperator;
import java.util.function.Function;

/**
 * Created by fobbyal
 * Creation Time 8/14/16 12:49 AM
 * Project for jmath-solver.
 */
public class DoubleNumSpec implements NumSpec<Double> {
    private static final BinaryOperator<Double> ADDITION = (a, b) -> a + b;
    private static final BinaryOperator<Double> SUBTRACTION = (a, b) -> a - b;
    private static final BinaryOperator<Double> MULTIPLICATION = (a, b) -> a * b;
    private static final Function<Double, Double> NEGATE = a -> -1 * a;
    private static final BinaryOperator<Double> DIVISION = (a, b) -> {
        if (b == 0) throw new ResultException("Cannot divide " + a + " by 0");
        return a / b;
    };
    private static final BinaryOperator<Double> POW = Math::pow;

    private final Map<String, Function<List<NumberMSolverMember<Double>>, NumberMSolverMember<Double>>> functionMap;

    public DoubleNumSpec(Map<String, Function<List<NumberMSolverMember<Double>>, NumberMSolverMember<Double>>>
                                 customFunction) {
        functionMap = new TreeMap<>(String
                .CASE_INSENSITIVE_ORDER);
        functionMap.put("random", list -> new MathFunction<>(list, createRandomFunction(), matchNumber(0)));
        functionMap.put("min", list -> new MathFunction<>(list, createMinFunction(), atLeast(1)));
        functionMap.put("max", list -> new MathFunction<>(list, createMaxFunction(), atLeast(1)));
        functionMap.put("avg", list -> new MathFunction<>(list, createAvgFunction(), atLeast(1)));
        functionMap.put("abs", list -> new MathFunction<>(list, createAbsFunction(), matchNumber(1)));
        functionMap.put("round", list -> new MathFunction<>(list, createRoundFunction(), matchNumber(2)));
        functionMap.put("floor", list -> new MathFunction<>(list, createFloorFunction(), matchNumber(1)));
        functionMap.put("ceiling", list -> new MathFunction<>(list, createCeilingFunction(), matchNumber(1)));
        functionMap.put("log", list -> new MathFunction<>(list, createLogFunction(), matchNumber(1)));
        functionMap.put("log10", list -> new MathFunction<>(list, createLog10Function(), matchNumber(1)));
        functionMap.put("sqrt", list -> new MathFunction<>(list, createSqrtFunction(), matchNumber(1)));
        functionMap.put("sin", list -> new MathFunction<>(list, createSinFunction(), matchNumber(1)));
        functionMap.put("cos", list -> new MathFunction<>(list, createCosFunction(), matchNumber(1)));
        functionMap.put("tan", list -> new MathFunction<>(list, createTanFunction(), matchNumber(1)));
        functionMap.put("asin", list -> new MathFunction<>(list, createASinFunction(), matchNumber(1)));
        functionMap.put("acos", list -> new MathFunction<>(list, createACosFunction(), matchNumber(1)));
        functionMap.put("atan", list -> new MathFunction<>(list, createAtanFunction(), matchNumber(1)));
        functionMap.put("sinh", list -> new MathFunction<>(list, createSinHFunction(), matchNumber(1)));
        functionMap.put("cosh", list -> new MathFunction<>(list, createCosHFunction(), matchNumber(1)));
        functionMap.put("tanh", list -> new MathFunction<>(list, createTanHFunction(), matchNumber(1)));
        functionMap.put("rad", list -> new MathFunction<>(list, createRadFunction(), matchNumber(1)));
        functionMap.put("deg", list -> new MathFunction<>(list, createDegFunction(), matchNumber(1)));
        if (customFunction != null) {
            functionMap.putAll(customFunction);
        }
    }

    public DoubleNumSpec() {
        this(null);
    }

    protected Function<List<Double>, Double> createSinFunction() {
        return l -> Math.sin(l.get(0));
    }

    protected Function<List<Double>, Double> createCosFunction() {
        return l -> Math.cos(l.get(0));
    }

    protected Function<List<Double>, Double> createTanFunction() {
        return l -> Math.tan(l.get(0));

    }

    protected Function<List<Double>, Double> createASinFunction() {
        return l -> Math.asin(l.get(0));
    }

    protected Function<List<Double>, Double> createACosFunction() {
        return l -> Math.acos(l.get(0));

    }

    protected Function<List<Double>, Double> createAtanFunction() {
        return l -> Math.atan(l.get(0));
    }

    protected Function<List<Double>, Double> createSinHFunction() {

        return l -> Math.sinh(l.get(0));
    }

    protected Function<List<Double>, Double> createCosHFunction() {

        return l -> Math.cosh(l.get(0));
    }

    protected Function<List<Double>, Double> createTanHFunction() {

        return l -> Math.tanh(l.get(0));
    }

    protected Function<List<Double>, Double> createRadFunction() {

        return l -> Math.toRadians(l.get(0));
    }

    protected Function<List<Double>, Double> createDegFunction() {

        return l -> Math.toDegrees(l.get(0));
    }

    protected Function<List<Double>, Double> createRandomFunction() {

        return l -> Math.random();
    }

    protected Function<List<Double>, Double> createMinFunction() {

        return l -> l.stream().reduce(l.get(0), (a, b) -> a < b ? a : b);
    }

    protected Function<List<Double>, Double> createMaxFunction() {

        return l -> l.stream().reduce(l.get(0), (a, b) -> a > b ? a : b);
    }

    protected Function<List<Double>, Double> createAbsFunction() {

        return l -> Math.abs(l.get(0));
    }

    protected Function<List<Double>, Double> createRoundFunction() {
        return args -> {
            int digits = args.get(1).intValue();
            double multiplier = Math.pow(10, digits);

            return Math.round(args.get(0) * multiplier) / multiplier;
        };
    }

    public Function<List<Double>, Double> createAvgFunction() {
        return l -> l.stream().mapToDouble(d -> d).average().orElseGet(() -> Double.NaN);

        //return l -> l.stream().reduce(0d, (a, b) -> a + b) / (double)l.size();
    }

    protected Function<List<Double>, Double> createFloorFunction() {
        return l -> Math.floor(l.get(0));
    }

    protected Function<List<Double>, Double> createCeilingFunction() {
        return l -> Math.ceil(l.get(0));
    }

    protected Function<List<Double>, Double> createLogFunction() {
        return l -> Math.log(l.get(0));
    }

    protected Function<List<Double>, Double> createLog10Function() {
        return l -> Math.log10(l.get(0));
    }

    protected Function<List<Double>, Double> createSqrtFunction() {
        return l -> Math.sqrt(l.get(0));
    }

    @Override
    public Double E() {
        return Math.E;
    }

    @Override
    public Double PI() {
        return Math.PI;
    }

    @Override
    public Double fromString(String strRepresentation) {
        return Double.valueOf(strRepresentation);
    }

    @Override
    public Map<String, Function<List<NumberMSolverMember<Double>>, NumberMSolverMember<Double>>> getAvailableFunctions() {
        return functionMap;
    }

    @Override
    public BinaryOperator<Double> getAddition() {
        return ADDITION;
    }

    @Override
    public BinaryOperator<Double> getSubtraction() {
        return SUBTRACTION;
    }

    @Override
    public BinaryOperator<Double> getMultiplication() {
        return MULTIPLICATION;
    }

    @Override
    public BinaryOperator<Double> getDivision() {
        return DIVISION;
    }

    @Override
    public BinaryOperator<Double> getPow() {
        return POW;
    }

    @Override
    public Comparison<Double> createComparison(NumberMSolverMember<Double> left, BooleanExpression.Operator optr,
                                               NumberMSolverMember<Double> right) {
        return new DoubleComparison(left, optr, right);
    }


    @Override
    public Function<Double, Double> createNegate() {
        return NEGATE;
    }
}
