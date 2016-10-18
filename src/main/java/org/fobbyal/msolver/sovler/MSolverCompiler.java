package org.fobbyal.msolver.sovler;

import org.antlr.v4.runtime.tree.ParseTree;
import org.fobbyal.msolver.antl.MSolverParser;
import org.fobbyal.msolver.antl.MSolverParser.*;
import org.fobbyal.msolver.sovler.tree.*;
import org.fobbyal.msolver.sovler.tree.Predicate.Operator;
import org.fobbyal.msolver.sovler.value.InvalidResult;
import org.fobbyal.msolver.sovler.value.MSolverResult;

import java.util.*;

import static org.fobbyal.msolver.sovler.tree.Predicate.Operator.AND;
import static org.fobbyal.msolver.sovler.tree.Predicate.Operator.EQ;
import static org.fobbyal.msolver.sovler.tree.Predicate.Operator.GE;
import static org.fobbyal.msolver.sovler.tree.Predicate.Operator.GT;
import static org.fobbyal.msolver.sovler.tree.Predicate.Operator.LE;
import static org.fobbyal.msolver.sovler.tree.Predicate.Operator.LT;
import static org.fobbyal.msolver.sovler.tree.Predicate.Operator.NEQ;
import static org.fobbyal.msolver.sovler.tree.Predicate.Operator.OR;

/**
 * Created by fobbyal
 * Creation Time 8/4/16 1:40 AM
 * Project for jmath-solver.
 */
public class MSolverCompiler<N> {

    private static final Predicate FALSE = context -> MSolverResult.of(false);
    private static final Predicate TRUE = context -> MSolverResult.of(true);

    private static final Map<String, Operator> operatorMap = new HashMap<>();

    static {
        operatorMap.put(">", GT);
        operatorMap.put(">=", GE);
        operatorMap.put("<", LT);
        operatorMap.put("<=", LE);
        operatorMap.put("==", EQ);
        operatorMap.put("!=", NEQ);
        operatorMap.put("<>", NEQ);
        operatorMap.put("!", NEQ);
    }


    final NumSpec<N> numSpec;

    public MSolverCompiler(NumSpec<N> numSpec) {
        this.numSpec = numSpec;
    }


    public Map<String, NumberMSolverMember<N>> compile(MSolverContext node) {

        Map<String, NumberMSolverMember<N>> values = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        for (int i = 0; i < node.getChildCount(); i++) {
            ParseTree child = node.getChild(i);
            if (child instanceof MSolverParser.DeclarationContext) {
                MSolverParser.DeclarationContext declaration = (MSolverParser.DeclarationContext) child;
                values.put(extractVarName(declaration), compile(extractAssignedExpression(declaration)));
            }
        }
        return values;
    }

    private String extractVarName(MSolverParser.DeclarationContext declare) {
        return declare.getChild(0).getText();
    }

    private MSolverParser.Value_exprContext extractAssignedExpression(DeclarationContext declare) {
        return (Value_exprContext) declare.getChild(2);
    }

    private NumberMSolverMember<N> compile(IfContext ifContext) {
        If_exprContext node = (If_exprContext) ifContext.getChild(0);
        Predicate_exprContext predicateExpression = (Predicate_exprContext) node.getChild(2);
        Value_exprContext thenValue = (Value_exprContext) node.getChild(4);
        Value_exprContext elseValue = (Value_exprContext) node.getChild(6);
        return new IfOperation<>(compile(predicateExpression), compile(thenValue), compile(elseValue));
    }

    protected Predicate compile(Comparison_predicateContext node) {
        Value_exprContext left = (Value_exprContext) node.getChild(0);
        Operator optr = toOperator(node.getChild(1));
        Value_exprContext right = (Value_exprContext) node.getChild(2);
        return numSpec.createComparison(compile(left), optr, compile(right));
    }

    protected Predicate compile(GroupedPredicateContext groupedPredicateContext) {
        return compile((Predicate_exprContext) groupedPredicateContext.getChild(1));
    }

    protected Predicate compile(AndContext andContext) {
        return new CompositePredicate(
                compile((Predicate_exprContext) andContext.getChild(0)),
                AND,
                compile((Predicate_exprContext) andContext.getChild(2)));
    }

    protected Operator toOperator(ParseTree node) {
        return operatorMap.get(node.getText());

    }

    protected Predicate compile(OrContext orContext) {
        return new CompositePredicate(
                compile((Predicate_exprContext) orContext.getChild(0)),
                OR,
                compile((Predicate_exprContext) orContext.getChild(2))
        );
    }

    @SuppressWarnings("unchecked")
    private Predicate<N> compile(TrueOrFalseContext trueOrFalseContext) {
        return Boolean.valueOf(trueOrFalseContext.getText()) ? TRUE : FALSE;
    }

    protected Predicate<N> compile(NotPredicateContext notPredicateContext) {
        Predicate<N> predicate = compile((Predicate_exprContext) notPredicateContext.getChild(1));
        return context -> predicate.eval(context).flatMap(b -> MSolverResult.of(!b));
    }


    @SuppressWarnings("unchecked")
    protected Predicate<N> compile(Predicate_exprContext predicate_exprContext) {
        if (predicate_exprContext.getChild(0) instanceof Comparison_predicateContext) {
            return compile((Comparison_predicateContext) predicate_exprContext.getChild(0));
        }
        if (predicate_exprContext instanceof GroupedPredicateContext) {
            return compile((GroupedPredicateContext) predicate_exprContext);
        }
        if (predicate_exprContext instanceof AndContext) {
            return compile((AndContext) predicate_exprContext);
        }
        if (predicate_exprContext instanceof OrContext) {
            return compile((OrContext) predicate_exprContext);
        }
        if (predicate_exprContext instanceof TrueOrFalseContext) {
            return compile((TrueOrFalseContext) predicate_exprContext);
        }
        if (predicate_exprContext instanceof NotPredicateContext) {
            return compile((NotPredicateContext) predicate_exprContext);
        }
        throw new RuntimeException("Not yet implemented");
    }

    private NumberMSolverMember<N> compile(FunctionContext node) {
        Func_exprContext func = (Func_exprContext) node.getChild(0);
        String funcName = func.getChild(0).getText();
        List<NumberMSolverMember<N>> args = new ArrayList<>();
        if (func.getChildCount() > 3) {
            for (int i = 2; i < func.getChildCount() - 1; i++) {
                Param_valueContext param = (Param_valueContext) func.getChild(i);
                args.add(compile((Value_exprContext) param.getChild(0)));
            }
        }
        return composeFunction(funcName, args);
    }

    @SuppressWarnings("unchecked")
    private NumberMSolverMember<N> composeFunction(String funcName, List<NumberMSolverMember<N>> args) {
        return numSpec.getAvailableFunctions().getOrDefault(
                funcName,
                (a -> b -> (MSolverResult<N>) InvalidResult.of("Function [" + funcName + "] is not defined"))).apply(args);
    }


    private NumberMSolverMember<N> compile(PowerContext node) {
        NumberMSolverMember<N> left = compile((Value_exprContext) node.getChild(0));
        NumberMSolverMember<N> right = compile((Value_exprContext) node.getChild(2));
        return new BinaryOperation<>(left, right, numSpec.getPow(), "^");
    }

    private NumberMSolverMember<N> compile(MultiplicationContext node) {
        NumberMSolverMember<N> left = compile((Value_exprContext) node.getChild(0));
        NumberMSolverMember<N> right = compile((Value_exprContext) node.getChild(2));
        return new BinaryOperation<>(left, right, numSpec.getMultiplication(), "*");
    }

    private NumberMSolverMember<N> compile(DivisionContext node) {
        NumberMSolverMember<N> left = compile((Value_exprContext) node.getChild(0));
        NumberMSolverMember<N> right = compile((Value_exprContext) node.getChild(2));
        return new BinaryOperation<>(left, right, numSpec.getDivision(), "/");
    }

    private NumberMSolverMember<N> compile(AdditionContext node) {
        NumberMSolverMember<N> left = compile((Value_exprContext) node.getChild(0));
        NumberMSolverMember<N> right = compile((Value_exprContext) node.getChild(2));
        return new BinaryOperation<>(left, right, numSpec.getAddition(), "+");
    }

    private NumberMSolverMember<N> compile(SubtractionContext node) {
        NumberMSolverMember<N> left = compile((Value_exprContext) node.getChild(0));
        NumberMSolverMember<N> right = compile((Value_exprContext) node.getChild(2));
        return new BinaryOperation<>(left, right, numSpec.getSubtraction(), "-");
    }

    private NumberMSolverMember<N> compile(GroupedArithmeticContext node) {
        return compile((Value_exprContext) node.getChild(1));

    }

    private NumberMSolverMember<N> compile(NegationContext node) {
        return new UniaryOperation<>(compile((Value_exprContext) node.getChild(1)), numSpec.createNegate());
    }


    private NumberMSolverMember<N> compile(ConstantContext node) {
        if (node.getChild(0) instanceof NumericConstContext) {
            return compile((NumericConstContext) node.getChild(0));
        }
        if (node.getChild(0) instanceof NumericVariableContext) {
            return compile((NumericVariableContext) node.getChild(0));
        }
        if (node.getChild(0) instanceof EValueContext) {
            return a -> MSolverResult.of(numSpec.E());
        }
        if (node.getChild(0) instanceof PiValueContext) {
            return a -> MSolverResult.of(numSpec.PI());

        }
        throw new RuntimeException("Expected constant value");
    }


    private NumberMSolverMember<N> compile(NumericConstContext node) {
        return numSpec.toConstant(node.getText());
    }

    private NumberMSolverMember<N> compile(NumericVariableContext node) {
        return new Variable<>(node.getText());
    }

    private NumberMSolverMember<N> compile(Value_exprContext node) {

        if (node instanceof IfContext) {
            return compile((IfContext) node);
        }
        if (node instanceof FunctionContext) {
            return compile((FunctionContext) node);
        }
        if (node instanceof PowerContext) {
            return compile((PowerContext) node);
        }
        if (node instanceof MultiplicationContext) {
            return compile((MultiplicationContext) node);
        }
        if (node instanceof DivisionContext) {
            return compile((DivisionContext) node);
        }
        if (node instanceof AdditionContext) {
            return compile((AdditionContext) node);
        }
        if (node instanceof SubtractionContext) {
            return compile((SubtractionContext) node);
        }
        if (node instanceof GroupedArithmeticContext) {
            return compile((GroupedArithmeticContext) node);
        }
        if (node instanceof NegationContext) {
            return compile((NegationContext) node);
        }
        if (node instanceof ConstantContext) {
            return compile((ConstantContext) node);
        }

        throw new RuntimeException("Not Implemented");
    }

}
