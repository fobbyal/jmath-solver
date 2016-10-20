package org.fobbyal.msolver.sovler.bd;

import org.fobbyal.msolver.sovler.NumSpec;
import org.fobbyal.msolver.sovler.tree.BooleanExpression;
import org.fobbyal.msolver.sovler.tree.Comparison;
import org.fobbyal.msolver.sovler.tree.MathFunction;
import org.fobbyal.msolver.sovler.tree.NumberMSolverMember;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.BinaryOperator;
import java.util.function.Function;

/**
 * Created by fobbyal
 * Creation Time 8/15/16 12:38 AM
 * Project for jmath-solver.
 */
public class BigDecimalNumSpec implements NumSpec<BigDecimal> {

    private static final BigDecimal PI = new BigDecimal("3.1415926535897932384626433832795028841971693993751058209749445923078164062862089986280348253421170679");
    private static final BigDecimal E = new BigDecimal("2.71828182845904523536028747135266249775724709369995957496696762772407663");

    private final BinaryOperator<BigDecimal> addition;
    private final BinaryOperator<BigDecimal> subtraction;
    private final BinaryOperator<BigDecimal> multiplication;
    private final BinaryOperator<BigDecimal> division;
    private final BinaryOperator<BigDecimal> pow;
    private final Map<String, Function<List<NumberMSolverMember<BigDecimal>>, NumberMSolverMember<BigDecimal>>> functionMap;
    private final MathContext context;
    private final Function<BigDecimal, BigDecimal> negate;


    public BigDecimalNumSpec(MathContext context, Map<String,
            Function<List<NumberMSolverMember<BigDecimal>>, NumberMSolverMember<BigDecimal>>> functionMap) {
        this.context = context == null ? MathContext.DECIMAL64 : context;
        this.functionMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        //todo: add functions
        this.functionMap.put("min", list -> new MathFunction<>(list, BigDecimalNumSpec::min, atLeast(1)));
        this.functionMap.put("max", list -> new MathFunction<>(list, BigDecimalNumSpec::max, atLeast(1)));

        if (functionMap != null)
            this.functionMap.putAll(functionMap);
        //context is used here to provide performance gain
        addition = (a, b) -> a.add(b, this.context);
        subtraction = (a, b) -> a.subtract(b, this.context);
        multiplication = (a, b) -> a.multiply(b, this.context);
        division = (a, b) -> a.divide(b, this.context);

        BinaryOperator<BigDecimal> floatPower = (v1, v2) -> {
            int signOf2 = v2.signum();
            double dn1 = v1.doubleValue();
            v2 = v2.multiply(new BigDecimal(signOf2)); // n2 is now positive
            BigDecimal remainderOf2 = v2.remainder(BigDecimal.ONE);
            BigDecimal n2IntPart = v2.subtract(remainderOf2);
            BigDecimal intPow = v1.pow(n2IntPart.intValueExact(), this.context);
            BigDecimal doublePow = new BigDecimal(Math.pow(dn1,
                    remainderOf2.doubleValue()));

            BigDecimal result = intPow.multiply(doublePow, this.context);
            if (signOf2 == -1) {
                result = BigDecimal.ONE.divide(result, this.context.getPrecision(),
                        RoundingMode.HALF_UP);
            }
            return result;

        };

        pow = (v1, v2) -> {
            if (v2.stripTrailingZeros().scale() <= 0)
                return v1.pow(v2.intValue());
            return floatPower.apply(v1, v2);
        };
        negate = bd -> bd.negate(this.context);

    }

    public BigDecimalNumSpec(Map<String, Function<List<NumberMSolverMember<BigDecimal>>, NumberMSolverMember<BigDecimal>>> functionMap) {
        this(null, functionMap);
    }


    public BigDecimalNumSpec() {
        this(null, null);
    }

    public BigDecimalNumSpec(MathContext context) {
        this(context, null);
    }

    public static BigDecimal min(List<BigDecimal> args) {
        if (args.size() == 0) return args.get(0);
        return args.stream().min(BigDecimal::compareTo).orElseGet(() -> args.get(0));
    }

    public static BigDecimal max(List<BigDecimal> args) {
        if (args.size() == 0) return args.get(0);
        return args.stream().max(BigDecimal::compareTo).orElseGet(() -> args.get(0));
    }

    @Override
    public BigDecimal E() {
        return E;
    }

    @Override
    public BigDecimal PI() {
        return PI;
    }

    @Override
    public BigDecimal fromString(String strRepresentation) {
        return new BigDecimal(strRepresentation);
    }

    @Override
    public BinaryOperator<BigDecimal> getAddition() {
        return addition;
    }

    @Override
    public BinaryOperator<BigDecimal> getSubtraction() {
        return subtraction;
    }

    @Override
    public BinaryOperator<BigDecimal> getMultiplication() {
        return multiplication;
    }

    @Override
    public BinaryOperator<BigDecimal> getDivision() {
        return division;
    }

    @Override
    public BinaryOperator<BigDecimal> getPow() {
        return pow;
    }

    @Override
    public Comparison<BigDecimal> createComparison(NumberMSolverMember<BigDecimal> left, BooleanExpression.Operator optr, NumberMSolverMember<BigDecimal> right) {
        return new BigDecimalComparison(left, optr, right);
    }

    @Override
    public Function<BigDecimal, BigDecimal> createNegate() {
        return negate;
    }

    @Override
    public Map<String, Function<List<NumberMSolverMember<BigDecimal>>, NumberMSolverMember<BigDecimal>>> getAvailableFunctions() {
        return functionMap;
    }
}
