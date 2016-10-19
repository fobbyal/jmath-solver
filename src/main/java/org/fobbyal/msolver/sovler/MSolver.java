package org.fobbyal.msolver.sovler;

import org.fobbyal.msolver.sovler.tree.NumberMSolverMember;
import org.fobbyal.msolver.sovler.value.InvalidResult;
import org.fobbyal.msolver.sovler.value.MSolverResult;
import org.fobbyal.msolver.sovler.value.ValueCache;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by fobbyal
 * Creation Time 8/4/16 1:28 PM
 * Project for jmath-solver.
 */
public class MSolver<N> {

    private final Map<String, NumberMSolverMember<N>> formulaScope;
    private final String ident;


    public MSolver(String ident, Map<String, NumberMSolverMember<N>> formula) {
        Objects.requireNonNull(ident);
        this.ident = ident;
        this.formulaScope = formula;
    }

    public String getIdent() {
        return ident;
    }

    public Set<String> getVars() {
        return formulaScope.values().stream()
                .map(m -> m.getVarSet().stream())
                .reduce(Stream.empty(), Stream::concat)
                .filter(a -> !formulaScope.containsKey(a))
                .collect(Collectors.toSet());
    }

    private MSolverResult<N> solve(String name,
                                   Map<String, MSolverResult<N>> accumulatedResult,
                                   Map<String, N> inputValues) {
        return solve(name, accumulatedResult, inputValues, new TreeSet<>(String.CASE_INSENSITIVE_ORDER));
    }

    private MSolverResult<N> solve(String varName,
                                   Map<String, MSolverResult<N>> accumulatedResult,
                                   Map<String, N> inputValues,
                                   Set<String> forVars) {

        //check if there is cyclic dependencies
        if (forVars.contains(varName))
            return error(varName, "was cyclically referenced", accumulatedResult);
        //already solved
        if (accumulatedResult.containsKey(varName)) return accumulatedResult.get(varName);
        //a input value
        if (inputValues.containsKey(varName)) return MSolverResult.of(inputValues.get(varName));
        //not a member of the formula scope
        if (!formulaScope.containsKey(varName)) return error(varName, "is not defined in scope", accumulatedResult);

        //todo: may consider using immutable set to improve performance
        HashSet<String> newVars = new HashSet<>();
        newVars.addAll(forVars);
        newVars.add(varName);

        NumberMSolverMember<N> formula = formulaScope.get(varName);
        for (String var : formula.getVarSet()) {
            solve(var, accumulatedResult, inputValues, newVars);
        }

        return accumulatedResult.put(varName, formula.eval(
                createValueCache(accumulatedResult, inputValues)
        ));
    }

    private MSolverResult<N> error(String varName, String errorMessage, Map<String, MSolverResult<N>> results) {
        //noinspection unchecked
        MSolverResult<N> error = InvalidResult.of("[" + varName + "] " + errorMessage);
        results.put(varName, error);
        return error;
    }

    private ValueCache<N> createValueCache(Map<String, MSolverResult<N>> accumulatedResult, Map<String, N> inputValues) {
        return key -> {
            if (accumulatedResult.containsKey(key)) return accumulatedResult.get(key);
            if (inputValues.containsKey(key)) return MSolverResult.of(inputValues.get(key));
            return InvalidResult.of(key + " is not in scope");
        };
    }

    public CompletableFuture<Map<String, MSolverResult<N>>> solveAsync(Map<String, N> input, Executor executor) {
        return CompletableFuture.supplyAsync(() -> solve(input), executor);
    }

    public CompletableFuture<Map<String, MSolverResult<N>>> solveAsync(Map<String, N> input) {
        return CompletableFuture.supplyAsync(() -> solve(input));

    }

    public Map<String, MSolverResult<N>> solve(Map<String, N> input) {
        if (!(input instanceof TreeMap) || ((TreeMap) input).comparator() != String.CASE_INSENSITIVE_ORDER) {
            TreeMap<String, N> tmpMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
            tmpMap.putAll(input);
            input = tmpMap;
        }

        final Map<String, N> finalInput = input;

        Map<String, MSolverResult<N>> result = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        formulaScope.entrySet().stream().forEach(entry -> solve(entry.getKey(), result, finalInput));
        return result;
    }
}
