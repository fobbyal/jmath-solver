package org.fobbyal.msolver.sovler.value;

import org.fobbyal.msolver.sovler.ExceptionUtil;

import java.util.Optional;
import java.util.function.Function;

/**
 * Created by fobbyal
 * Creation Time 8/2/16 9:00 PM
 * Project for jmath-solver.
 */
public class MSolverResult<N> {

    final N value;

    protected MSolverResult(N value) {
        this.value = value;
    }

    public static MSolverResult<Boolean> of(boolean bool) {
        return new MSolverResult<>(bool);
    }

    public static <V> MSolverResult<V> of(V value) {
        if (value == null) {
            //noinspection unchecked
            return InvalidResult.of();
        }
        return new MSolverResult<>(value);
    }

    public N unwrap() {
        return value;
    }

    public boolean isValid() {
        return true;
    }

    @SuppressWarnings("unchecked")
    public <O> MSolverResult<O> flatMap(Function<N, MSolverResult<O>> function) {
        if (!isValid()) {
            return (MSolverResult<O>) this;
        }
        return ExceptionUtil.guard(() -> function.apply(value));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MSolverResult<?> that = (MSolverResult<?>) o;

        return value.equals(that.value);

    }

    public Optional<N> toOptional() {
        if (isValid())
            return Optional.of(value);
        return Optional.empty();
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return "[Solved]:{" + value + "}";
    }
}
