package org.fobbyal.msolver.sovler.tree;

import org.fobbyal.msolver.sovler.ExceptionUtil;
import org.fobbyal.msolver.sovler.value.MSolverResult;
import org.fobbyal.msolver.sovler.value.ValueCache;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by fobbyal
 * Creation Time 8/11/16 9:55 PM
 * Project for jmath-solver.
 */
public class MathFunction<N> extends DynamicNumberMember<N> {

    final private List<NumberMSolverMember<N>> args;

    final private Function<List<N>, N> func;

    final private Consumer<Integer> argSizeValidator;


    public MathFunction(List<NumberMSolverMember<N>> args, Function<List<N>, N> func) {
        this(args, func, null);
    }

    public MathFunction(List<NumberMSolverMember<N>> args, Function<List<N>, N> func, Consumer<Integer> argSizeValidator) {
        this.args = args;
        this.func = func;
        this.argSizeValidator = argSizeValidator;
    }


    @SuppressWarnings("ToArrayCallWithZeroLengthArrayArgument")
    @Override
    protected String[] findVars() {
        return args == null || args.size() == 0 ?
                new String[0] :
                args.stream().flatMap(a -> Stream.of(a.getVars())).collect(Collectors.toSet()).toArray(new String[0]);
    }

    @SuppressWarnings("unchecked")
    @Override
    public MSolverResult<N> eval(ValueCache<N> context) {
        return ExceptionUtil.guard(() -> {
            if (argSizeValidator != null) argSizeValidator.accept(args.size());
            ArrayList<N> argValues = new ArrayList<>();
            for (NumberMSolverMember<N> member : args) {
                MSolverResult<N> arg = member.eval(context);
                if (arg.isValid()) {
                    argValues.add(arg.unwrap());
                } else {
                    return arg;
                }
            }
            return MSolverResult.of(func.apply(argValues));
        });
    }

/*    @Override
    public String toString() {
        return
    }*/
}
